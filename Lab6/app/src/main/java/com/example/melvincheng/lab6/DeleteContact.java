package com.example.melvincheng.lab6;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class DeleteContact extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    int item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        spinner = (Spinner)findViewById(R.id.contact_spinner);
        spinner.setAdapter(ShowContacts.arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }
    public void delete(View v) {
        Intent intent = new Intent();
        intent.putExtra("item", item);
        intent.putExtra("add",false);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
