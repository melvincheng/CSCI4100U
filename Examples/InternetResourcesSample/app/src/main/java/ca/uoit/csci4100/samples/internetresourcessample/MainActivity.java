package ca.uoit.csci4100.samples.internetresourcessample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends Activity {
    public static int GET_NAME_REQUEST = 4100001;

    private String name = "android"; // for now
    private String baseURL = "http://services.aonaware.com/DictService/DictService.asmx/Define?word=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getOrigin(View v) {
        EditText txtName = (EditText)findViewById(R.id.txtName);
        String url = baseURL + txtName.getText().toString().toLowerCase();
        DownloadOriginTask downloadOriginTask = new DownloadOriginTask();
        Log.d("InternetResourcesSample", "running task: " + url);
        downloadOriginTask.execute(url);
    }

    public void showDefinition(String definition) {
        EditText txtDefinition = (EditText)findViewById(R.id.txtDefinition);
        txtDefinition.setText(definition);
    }

    class DownloadOriginTask extends AsyncTask<String, Void, String> {
        private Exception exception = null;
        private String definition = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                // parse out the data
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
                Log.d("InternetResourcesSample", "url: " + params[0]);
                URL url = new URL(params[0]);
                Document document = docBuilder.parse(url.openStream());
                document.getDocumentElement().normalize();

                // look for <WordDefinition> tags
                NodeList main = document.getElementsByTagName("Definitions");
                if ((main.getLength() > 0) && (main.item(0).getNodeType() == Node.ELEMENT_NODE)) {
                    Element definitions = (Element)main.item(0);
                    NodeList defTags = definitions.getElementsByTagName("WordDefinition");
                    definition = "";
                    for (int i = 0; i < defTags.getLength(); i++) {
                        Node def = defTags.item(i);
                        definition += def.getTextContent() + "\n";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            }

            return definition;
        }

        private String stripTags(String code) {
            return code; // for now
        }

        @Override
        protected void onPostExecute(String result) {
            if (exception != null) {
                exception.printStackTrace();
                return;
            }

            Log.d("InternetResourcesSample", "setting definition: " + definition);
            showDefinition(definition);
        }
    }
}
