package ca.uoit.csci4100.samples.databasedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_FILENAME = "contacts.db";

    private static final String CREATE_STATEMENT = "" +
            "create table contacts(" +
            "  _id integer primary key autoincrement," +
            "  firstName text not null," +
            "  lastName text not null," +
            "  email text null," +
            "  phone text null)";

    private static final String DROP_STATEMENT = "" +
            "drop table contacts";

    public ContactHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STATEMENT);
        //db.execSQL("alter table contacts add column address text");
        //db.execSQL("update contacts set address = ''");
        db.execSQL(CREATE_STATEMENT);
    }

    public void deleteAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts", "", new String[] {});
        /*
        db.delete("contacts", "lastName = ?", new String[] {searchFor});

        // WARNING:  Do not use this method (SQL injection)
        db.delete("contacts", "phone = '"+searchFor+"'", new String[] {});
        */
    }


    public void deleteContactById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts", "_id = ?", new String[] { ""+id });
    }

    public Contact addNewContact(String firstName,
                              String lastName,
                              String email,
                              String phone) {
        // insert the contact data into the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("email", email);
        values.put("phone", phone);
        long id = db.insert("contacts", null, values);

        // create a new contact object
        Contact contact = new Contact(id, firstName, lastName, email, phone);
        return contact;
    }

    public List<Contact> getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Contact> results = new ArrayList<>();

        String[] columns = new String[] {"_id",
                                         "firstName",
                                         "lastName",
                                         "email",
                                         "phone"};
        String where = "";  // all contacts
        String[] whereArgs = new String[] {};
        String groupBy = "";  // no grouping
        String groupArgs = "";
        String orderBy = "lastName";

        Cursor cursor = db.query("contacts", columns, where, whereArgs,
                                 groupBy, groupArgs, orderBy);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String email = cursor.getString(3);
            String phone = cursor.getString(4);

            results.add(new Contact(id, firstName, lastName, email, phone));

            cursor.moveToNext();
        }

        return results;
    }

    public boolean updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("firstName", contact.getFirstName());
        values.put("lastName", contact.getLastName());
        values.put("email", contact.getEmail());
        values.put("phone", contact.getPhone());

        int numRows = db.update("contacts",
                                values,
                                "_id = ?",
                                new String[] {""+contact.getId()});
        return (numRows == 1);
    }

}
