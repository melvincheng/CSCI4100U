package com.example.melvincheng.lab3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickAbout(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        this.startActivity(intent);
    }

    public void clickLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivityForResult(intent, 0);
    }

    public void onActivityResult(int reqCode, int resCode, Intent result) {
        super.onActivityResult(reqCode, resCode, result);
        Boolean loginStatus = false;
        if(resCode == Activity.RESULT_OK) {
            loginStatus = result.getBooleanExtra("loginStatus",false);
        }
        if (loginStatus) {
            Toast.makeText(getBaseContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
        }
    }
}
