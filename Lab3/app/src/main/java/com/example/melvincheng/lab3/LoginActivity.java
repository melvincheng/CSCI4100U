package com.example.melvincheng.lab3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void attemptLogin(View v) {
        EditText username = (EditText)findViewById(R.id.edit_username);
        EditText password = (EditText)findViewById(R.id.edit_password);
        Intent intent = new Intent();

        if (username.getText().toString().equals("100526486") && password.getText().toString().equals("100526486")) {
            intent.putExtra("loginStatus", true);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        } else if (username.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), R.string.login_blankUsername, Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), R.string.login_blankPassword, Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra("loginStatus", false);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }
    }
}
