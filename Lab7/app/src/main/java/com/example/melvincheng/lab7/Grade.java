package com.example.melvincheng.lab7;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by melvincheng on 07/11/16.
 */
public class Grade {
    private int studentID;
    private String courseComponent;
    private float mark;

    public Grade(int studentID, String courseComponent, float mark) {
        this.studentID = studentID;
        this.courseComponent = courseComponent;
        this.mark = mark;
    }

    public int getId() {
        return studentID;
    }

    public String getCourseComponent() {
        return courseComponent;
    }

    public float getMark() {
        return mark;
    }

    public String toString() {
        return studentID + " " + courseComponent + " " + mark;
    }
}
