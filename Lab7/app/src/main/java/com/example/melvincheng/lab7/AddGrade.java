package com.example.melvincheng.lab7;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddGrade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);
    }

    public void add(View v) {
        EditText studentId_edit = (EditText)findViewById(R.id.studentId_edit);
        EditText courseComponent_edit = (EditText)findViewById(R.id.courseComponent_edit);
        EditText mark_edit = (EditText)findViewById(R.id.mark_edit);

        int id = Integer.valueOf(studentId_edit.getText().toString());
        String courseComponent = courseComponent_edit.getText().toString();
        float mark = Float.valueOf(mark_edit.getText().toString());

        ShowGrades.dbHelper.addNewGrade(id, courseComponent, mark);
        this.finish();
    }
}
