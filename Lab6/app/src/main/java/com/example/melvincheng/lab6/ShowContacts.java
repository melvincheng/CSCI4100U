package com.example.melvincheng.lab6;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class ShowContacts extends AppCompatActivity {

    private ArrayList<Contact> contactArray;
    static public ArrayAdapter<Contact> arrayAdapter;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactArray = new ArrayList<>();
//        writeFile();
    }

    public void startAdd(View v) {
        Intent intent = new Intent(this, AddContact.class);
        this.startActivityForResult(intent, 0);
    }

    public void startDelete(View v) {
        Intent intent = new Intent(this, DeleteContact.class);
        this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeFile();
    }


    @Override
    protected void onStart() {
        super.onStart();
        readFile();
        display();
    }
    public void onActivityResult(int reqCode, int resCode, Intent result) {
        try {
            if (result.getBooleanExtra("add",false)) {
                Contact newContact = new Contact(
                        ++ID,
                        result.getStringExtra("firstName"),
                        result.getStringExtra("lastName"),
                        result.getStringExtra("phone"));
                contactArray.add(newContact);
            } else {
                int item = result.getIntExtra("item",-1);
                contactArray.remove(item);
            }
        } catch (NullPointerException e) {

        }
        writeFile();
    }
    private void writeFile() {
        try {
            File file = getFileStreamPath("contactData.txt");
            FileOutputStream raw = new FileOutputStream(file);
            OutputStreamWriter out = new OutputStreamWriter(raw);
            for (int i = 0; i < contactArray.size(); i++) {
                Contact tempContact = contactArray.get(i);
                out.write("\n" + String.valueOf(tempContact.id) + " ");
                out.write(tempContact.firstName + " ");
                out.write(tempContact.lastName + " ");
                out.write(tempContact.phone);
            }
            out.flush();
            out.close();
            raw.close();
        } catch (IOException e) {

        }
    }

    private void readFile() {
        contactArray.clear();
        File file = getFileStreamPath("contactData.txt");
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                int currentId = scanner.nextInt();
                ID = Math.max(currentId,ID);
                Contact contact = new Contact(currentId, scanner.next(), scanner.next(), scanner.next());
                contactArray.add(contact);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void display() {
        ListView listView = (ListView)findViewById(R.id.contact_list);
        arrayAdapter = new ArrayAdapter<Contact>(this, R.layout.contact,contactArray);
        listView.setAdapter(arrayAdapter);
    }
}
