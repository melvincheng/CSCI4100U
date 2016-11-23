package ca.uoit.csci4100.samples.housing;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadHousingTask extends AsyncTask<String, Void, List<HousingProject>> {
    private Exception exception = null;
    private HousingDownloadListener listener = null;

    public DownloadHousingTask(HousingDownloadListener listener) {
        this.listener = listener;
    }

    private List<String> loadCSVLines(InputStream inStream) throws IOException {
        List<String> lines = new ArrayList<>();

        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

        String line = null;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

    @Override
    protected List<HousingProject> doInBackground(String... params) {
        List<HousingProject> housingProjects = new ArrayList<>();

        String[] values = null;
        try {
            // download the CSV data
            URL url = new URL(params[0]);
            InputStream inStream = url.openStream();
            List<String> lines = loadCSVLines(inStream);
            inStream.close();

            // extract the data, making HousingProject objects
            for (int i = 1; i < lines.size(); i++) {
                values = lines.get(i).split(",");
                float latitude = Float.parseFloat(values[0]);
                float longitude = Float.parseFloat(values[1]);
                String address = values[5];
                String municipality = values[6];
                int numUnits = Integer.parseInt(values[9]);

                HousingProject project = new HousingProject(longitude, latitude, address, municipality, numUnits);

                housingProjects.add(project);
            }
        } catch (IOException e) {
            Log.i("HOUSING", "exception:  " + e);
            this.exception = e;
        }

        return housingProjects;
    }

    @Override
    protected void onPostExecute(List<HousingProject> housingProjects) {
        if (this.exception != null) {
            this.exception.printStackTrace();
        } else {
            this.listener.housingDataDownloaded(housingProjects);
        }
    }
}
