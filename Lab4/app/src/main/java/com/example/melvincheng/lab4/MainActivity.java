package com.example.melvincheng.lab4;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    BatteryStatus receiver = new BatteryStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(receiver, filter);
    }
}
