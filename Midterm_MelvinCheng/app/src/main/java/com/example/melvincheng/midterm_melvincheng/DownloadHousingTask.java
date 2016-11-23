package com.example.melvincheng.midterm_melvincheng;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
TODO:

1. Set the type parameters for this class.  This AsyncTask will not use progress.
   Its input will be a URL string, and its result will be a list of HousingProject
   objects.  The important fields in the CSV file are as follows:
   a. latitude (column 0, header says 'X')
   b. longitude (column 1, header says 'Y')
   c. address (column 5, header says 'PROJ_ADDRESS')
   d. municipality (column 6, header says 'MUNICIPALITY')
   e. numUnits (column 9, header says 'NUM_UNITS')
2. Implement the doInBackground() method to download and process the CSV data
   into a list of HousingProject objects.
3. Implement the onPostExecute() method to handle any exceptions and pass the
   list of HousingProjects back to the listener.
*/
public class DownloadHousingTask extends AsyncTask<String, Void, List<HousingProject>> {
    private Exception exception = null;
    private HousingDownloadListener listener = null;

    public DownloadHousingTask(HousingDownloadListener listener) {
        this.listener = listener;
    }

	/**
	 * loadCSVLines()
	 *	
	 * @arg inStream The input stream from which to read the CSV data
	 *
	 * @return A list of strings, each of which will be one line of CSV data
	 *
	 * This function is included to help you process the CSV file.  This function
	 * downloads all of the data from the provided InputStream, and returns a list of
	 * lines.  Since we are downloading a CSV file, these lines will consist of 
	 * comma-separated data (like the example given in Listing 1).
	**/
    private List<String> loadCSVLines(InputStream inStream) throws IOException {
        List<String> lines = new ArrayList<>();

        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

        String line = null;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }


    protected ArrayList<HousingProject> doInBackground(String... Param) {
        Intent intent = new Intent();
        String stringUrl = intent.getStringExtra("url");
        ArrayList<HousingProject> houseList = new ArrayList<>();
        String[] response;
        try {
            URL url = new URL(stringUrl);
            int result;
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            conn.getResponseCode();
            result = conn.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                List<String> lines = loadCSVLines(conn.getInputStream());
                for (int i = 0; i < lines.size(); i++) {
                    response = lines.get(i).split(",");
                    HousingProject tempHouse = new HousingProject(
                            Float.valueOf(response[0]),
                            Float.valueOf(response[1]),
                            response[5],
                            response[6],
                            Integer.valueOf(response[9]));
                    houseList.add(tempHouse);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return houseList;
    }

    protected void onPostExectue(List<HousingProject> list) {
        if (exception != null) {
            exception.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtra("houseList", (Parcelable) list);
    }
	// TODO:  Implement the doInBackGround() method
	//        This method will download the CSV data from the URL
	//        parameter (params[0]), extract the relevant data from
	//        the file, creating a list of HousingProject objects.
	//        The HousingProject list will be the result.

    // TODO:  Implement the onPostExecute() method
    //        This method will handle exceptions, and pass the result data
    //        back to the listener
}
