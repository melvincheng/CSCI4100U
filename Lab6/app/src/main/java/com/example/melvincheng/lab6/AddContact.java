package com.example.melvincheng.lab6;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
    }

    public void add(View v) {
        EditText firstName_edit = (EditText)findViewById(R.id.firstName_edit);
        EditText lastName_edit = (EditText)findViewById(R.id.lastName_edit);
        EditText phone_edit = (EditText)findViewById(R.id.phone_edit);

        String firstName = firstName_edit.getText().toString();
        String lastName = lastName_edit.getText().toString();
        String phone = phone_edit.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("add", true);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("phone", phone);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }
}
