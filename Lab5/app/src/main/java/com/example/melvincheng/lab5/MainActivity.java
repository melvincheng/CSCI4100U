package com.example.melvincheng.lab5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.about_us_lbl);
    }

    public void show_license(View v){
        Intent intent = new Intent(this, ShowLicense.class);
        this.startActivity(intent);
    }
}
