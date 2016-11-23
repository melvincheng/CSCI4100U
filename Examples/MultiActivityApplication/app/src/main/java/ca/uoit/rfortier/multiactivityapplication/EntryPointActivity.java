package ca.uoit.rfortier.multiactivityapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class EntryPointActivity extends AppCompatActivity {
    public static final int REQUEST_PLAY_GAME = 41001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point);
    }

    public void playGame(View view) {
        Intent playGameIntent = new Intent(this, GameActivity.class);
        playGameIntent.putExtra("level", "3-2");
        startActivityForResult(playGameIntent, REQUEST_PLAY_GAME);
    }

    public void highScores(View view) {
        Intent highScoresGIntent = new Intent(this, HighScoresActivity.class);
        highScoresGIntent.putExtra("level", "1-2");
        startActivity(highScoresGIntent);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int responseCode,
                                 Intent result) {
        if ((requestCode == REQUEST_PLAY_GAME) &&
                (responseCode == Activity.RESULT_OK)) {
            int score = result.getIntExtra("score", 0);
            Log.i("MultiActivity", "score: " + score);
        }
    }
}
