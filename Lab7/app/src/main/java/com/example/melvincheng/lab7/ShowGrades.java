package com.example.melvincheng.lab7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShowGrades extends AppCompatActivity {

    static public List<Grade> gradeList;
    static public ArrayAdapter<Grade> arrayAdapter;
    static public GradesDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDb();
        display();
    }

    public void startAdd(View v) {
        Intent intent = new Intent(this, AddGrade.class);
        this.startActivityForResult(intent, 0);
    }

    public void startDelete(View v) {
        Intent intent = new Intent(this, DeleteGrade.class);
        this.startActivityForResult(intent, 0);
    }

    private void loadDb() {
        dbHelper = new GradesDBHelper(this);
        gradeList = dbHelper.getAllGrades();
    }

    private void display() {
        ListView listView = (ListView)findViewById(R.id.grade_list);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.grade, gradeList);
        listView.setAdapter(arrayAdapter);
    }
}
