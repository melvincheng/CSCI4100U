package ca.uoit.csci4100.samples.envtestapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity
                          implements DefinitionListener {
    private final String DEFINITION_LOOKUP_URL = "http://services.aonaware.com/DictService/DictService.asmx/Define?word=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void lookItUp(View view) {
        // obtain the word to be defined
        EditText wordField = (EditText)findViewById(R.id.txtWord);
        String word = wordField.getText().toString().toLowerCase();

        // create the async task to look up the definition
        DownloadDefinitionTask task = new DownloadDefinitionTask(this);
        task.execute(DEFINITION_LOOKUP_URL + word);
        Log.i("AsyncDemo", "looking up " + word);
    }

    public void handleDefinition(String def) {
        Log.i("AsyncDemo", "got the def: " + def);
        // update the UI
        TextView label = (TextView)findViewById(R.id.lblDefinition);
        label.setText(def);
    }

    class DownloadDefinitionTask extends AsyncTask<String, Void, String> {
        private String definition = null;
        private Exception exception = null;
        private DefinitionListener listener = null;

        public DownloadDefinitionTask(DefinitionListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // download the XML data from the service
                URL url = new URL(params[0]);

                // parse the definition out of the XML
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(url.openStream());
                doc.getDocumentElement().normalize();

                NodeList wordDefs = doc.getElementsByTagName("WordDefinition");
                if (wordDefs.getLength() > 0) {
                    Node def = wordDefs.item(0);
                    definition = def.getTextContent();
                } else {
                    definition = "";
                }

                // remember the data
                return definition;
            } catch (Exception e) {
                exception = e;
                definition = "";
            } finally {
                return definition;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // handle any error
            if (exception != null) {
                exception.printStackTrace();
                return;
            }

            // show the data
            listener.handleDefinition(result);
        }
    }
}
