package com.example.melvincheng.a1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Iterator;

public class Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Bundle extras = getIntent().getExtras();
        int yesCount = extras.getInt("yesCount");
        int noCount = extras.getInt("noCount");

        TextView yes_num_lbl = (TextView)findViewById(R.id.yes_num_lbl);
        TextView no_num_lbl = (TextView)findViewById(R.id.no_num_lbl);

        yes_num_lbl.setText(Integer.toString(yesCount));
        no_num_lbl.setText(Integer.toString(noCount));
    }
    public void finish(View v) {
        this.finish();
    }
}
