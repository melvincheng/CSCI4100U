package ca.uoit.csci4100.samples.safdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final int BROWSE_FOR_FILE = 41002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void browse(View view) {
        Intent openDoc = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openDoc.addCategory(Intent.CATEGORY_OPENABLE);
        openDoc.setType("text/*");
        startActivityForResult(openDoc, BROWSE_FOR_FILE);
    }

    private void showContent(String text) {
        // put it into the label
        TextView label = (TextView)findViewById(R.id.lblContent);
        label.setText(text);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int responseCode,
                                 Intent resultIntent) {
        if (requestCode == BROWSE_FOR_FILE) {
            if ((responseCode == RESULT_OK) && (resultIntent != null)) {
                try {
                    Uri uri = resultIntent.getData();
                    InputStream inStream = getContentResolver().openInputStream(uri);
                    BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
                    String line = in.readLine();
                    StringBuffer buffer = new StringBuffer();
                    while (line != null) {
                        buffer.append(line);
                        line = in.readLine();
                    }
                    inStream.close();
                    showContent(buffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
