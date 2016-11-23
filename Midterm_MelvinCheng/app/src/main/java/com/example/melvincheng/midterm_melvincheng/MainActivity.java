package com.example.melvincheng.midterm_melvincheng;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/*
TODO:

1. Add this activity as a selection listener to the dropdown
2. Make the activity class implement the right interface to be a selection listener
3. Implement the selection listener event to populate the text fields with the selected
   housing project
4. Make this activity class implement the HousingDownloadListener interface provided
5. Make this activity class implement the housingDataDownloaded() method.  This method will
   populate the spinner with housing data (using the toString() method of the HousingData
   class).
*/
public class MainActivity extends AppCompatActivity
        implements HousingDownloadListener, AdapterView.OnItemSelectedListener

                          /* TODO: Add HousingDownloadListener interface */
                          /* TODO: Add selection listener interface */ {
    private static final String URL = "http://csundergrad.science.uoit.ca/csci3230u/data/Affordable_Housing.csv";

    private List<HousingProject> housingProjects = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO:  Add this activity as a listener for the spinner



        // TODO:  Create an AsyncTask instance, and start it
        Intent intent = new Intent(this, DownloadHousingTask.class);
        intent.putExtra("url", URL);
        this.startActivity(intent);
    }

    // TODO:  Implement the handler method for the HousingDownloadListener interface
    //         - Populate the spinner with the downloaded data
    //        Hint:  Use an ArrayAdapter for this purpose

    @Override
    public void housingDataDownloaded(List<HousingProject> housingProjects) {
        Spinner spinner = (Spinner)findViewById(R.id.lstHousingProjects);
        ArrayAdapter<HousingProject> arrayAdapter = new ArrayAdapter<HousingProject>(this, R.layout.list_items_housing_project,housingProjects);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }


    // TODO:  Implement the item selection method to put the data from the selected
    //        housing project into the text fields of our UI

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        EditText addressEdit = (EditText)findViewById(R.id.address_edit);
        EditText municipalityEdit = (EditText)findViewById(R.id.municipality_edit);
        EditText latitudeEdit = (EditText)findViewById(R.id.latitude_edit);
        EditText longitudeEdit = (EditText)findViewById(R.id.longitude_edit);
        EditText numUnitsEdit = (EditText)findViewById(R.id.numUnits_edit);
        addressEdit.setText(housingProjects.get(position).getAddress());
        municipalityEdit.setText(housingProjects.get(position).getMunicipality());
        latitudeEdit.setText(Float.toString(housingProjects.get(position).getLatitude()));
        longitudeEdit.setText(Float.toString(housingProjects.get(position).getLongitude()));
        numUnitsEdit.setText(housingProjects.get(position).getNumUnits());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}