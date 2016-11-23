package ca.uoit.csci4100.samples.userinterfacessample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void back(View v) {
        setContentView(R.layout.activity_main);
    }

    public void linearLayout(View v) {
        setContentView(R.layout.linear);

        populateSpinner((Spinner) findViewById(R.id.lstSpinnerL), R.array.choices);
    }

    public void relativeLayout(View v) {
        setContentView(R.layout.relative);

        populateSpinner((Spinner)findViewById(R.id.lstSpinnerR), R.array.choices);
    }

    public void listView(View v) {
        setContentView(R.layout.list);

        populateList((ListView) findViewById(R.id.listView), R.array.choices);
    }

    public void gridView(View v) {
        setContentView(R.layout.grid);

        populateGrid((GridView) findViewById(R.id.gridView), R.array.choices);
    }

    private void populateSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void populateList(ListView listView, int arrayId) {
        String data[] = getResources().getStringArray(arrayId);
        listView.setAdapter(new LabelAdapter(this, data));
    }

    private void populateGrid(GridView gridView, int arrayId) {
        String data[] = getResources().getStringArray(arrayId);
        gridView.setAdapter(new LabelAdapter(this, data));
    }
}
