package ca.uoit.csci4100.samples.housing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HousingDownloadListener, AdapterView.OnItemSelectedListener {
    private static final String URL = "http://csundergrad.science.uoit.ca/csci3230u/data/Affordable_Housing.csv";
    private List<HousingProject> housingProjects = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner list = (Spinner)findViewById(R.id.lstHousingProjects);
        list.setOnItemSelectedListener(this);

        DownloadHousingTask task = new DownloadHousingTask(this);
        task.execute(URL);
    }

    @Override
    public void housingDataDownloaded(List<HousingProject> housingProjects) {
        this.housingProjects = housingProjects;

        ArrayAdapter<HousingProject> adapter = new ArrayAdapter<HousingProject>(this, android.R.layout.simple_spinner_item, housingProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner list = (Spinner)findViewById(R.id.lstHousingProjects);
        list.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HousingProject housingProject = housingProjects.get(position);

        setTextFieldValue(R.id.txtAddress, housingProject.getAddress());
        setTextFieldValue(R.id.txtMunicipality, housingProject.getMunicipality());
        setTextFieldValue(R.id.txtNumUnits, "" + housingProject.getNumUnits());
        setTextFieldValue(R.id.txtLatitude, "" + housingProject.getLatitude());
        setTextFieldValue(R.id.txtLongitude, "" + housingProject.getLongitude());
    }

    private void setTextFieldValue(int id, String value) {
        EditText field = (EditText)findViewById(id);
        field.setText(value);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
