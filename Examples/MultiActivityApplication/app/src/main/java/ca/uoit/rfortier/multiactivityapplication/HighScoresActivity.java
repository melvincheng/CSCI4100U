package ca.uoit.rfortier.multiactivityapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class HighScoresActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        Intent callingIntent = getIntent();
        String level = callingIntent.getStringExtra("level");
        Log.i("Multi-Intent", "level: " + level);
    }

}
