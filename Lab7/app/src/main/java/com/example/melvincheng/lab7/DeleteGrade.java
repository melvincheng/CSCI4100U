package com.example.melvincheng.lab7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class DeleteGrade extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinner;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_grade);

        spinner = (Spinner)findViewById(R.id.grade_spinner);
        spinner.setAdapter(ShowGrades.arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void delete(View v) {
        ShowGrades.dbHelper.deleteGradeById(ShowGrades.gradeList.get(pos).getId());
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
