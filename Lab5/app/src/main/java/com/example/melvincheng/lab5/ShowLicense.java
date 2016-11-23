package com.example.melvincheng.lab5;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ShowLicense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_license);
        setTitle(R.string.show_license_lbl);
        PageDownload pageDownload = new PageDownload();
        pageDownload.execute();
    }

    public void finish(View v) {
        this.finish();
    }

    private class PageDownload extends AsyncTask<String, Void, String>{
        private Exception exception = null;
        StringBuilder response;
        @Override
        protected String doInBackground(String... Params) {
            int result;
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("https://www.gnu.org/licenses/gpl.txt");
                conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                conn.getResponseCode();
                result = conn.getResponseCode();
                if (result == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        protected void onPostExecute(String result) {
            if (exception != null) {
                exception.printStackTrace();
            }
            TextView text = (TextView)findViewById(R.id.license_text);
            text.setText(result);
        }
    }
}
