package com.example.melvincheng.lab7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GradesDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION  = 1;
    private static final String DATABASE_FILENAME = "grades.db";

    private static final String CREATE_STATEMENT = "" +
            "create table grades(" +
            " studentId integer primary key," +
            " courseComponent varchar(100) not null," +
            " mark decimal not null)";

    private  static final String DROP_STATEMENT = "" +
            "drop table grades";

    public GradesDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STATEMENT);
        db.execSQL(CREATE_STATEMENT);
    }

    public void deleteAllGrades() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("grades", "", new String[] {});
    }

    public void deleteGradeById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("grades", "studentId = ?", new String[] { ""+id });
    }

    public Grade addNewGrade(int id, String courseComponent, float mark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("studentId", id);
        values.put("courseComponent", courseComponent);
        values.put("mark", mark);
        db.insert("grades", null, values);

        // create a new contact object
        Grade grade = new Grade(id, courseComponent, mark);
        return grade;
    }

    public List<Grade> getAllGrades() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Grade> results = new ArrayList<>();

        String[] columns = new String[] {"studentId",
                "courseComponent",
                "mark"};
        String where = "";  // all contacts
        String[] whereArgs = new String[] {};
        String groupBy = "";  // no grouping
        String groupArgs = "";
        String orderBy = "studentId";

        Cursor cursor = db.query("grades", columns, where, whereArgs,
                groupBy, groupArgs, orderBy);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int sid = cursor.getInt(0);
            String courseComponent = cursor.getString(1);
            float mark = cursor.getFloat(2);

            results.add(new Grade(sid, courseComponent, mark));

            cursor.moveToNext();
        }

        return results;
    }
}
