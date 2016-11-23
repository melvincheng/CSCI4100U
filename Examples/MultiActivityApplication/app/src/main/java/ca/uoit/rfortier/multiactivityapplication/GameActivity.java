package ca.uoit.rfortier.multiactivityapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class GameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent callingIntent = getIntent();
        String level = callingIntent.getStringExtra("level");
        Log.i("Multi-Intent", "level: " + level);
    }

    public void gameOver(View view) {
        Intent resultIntent = new Intent();
        EditText scoreField = (EditText)findViewById(R.id.txtScore);
        String strScore = scoreField.getText().toString();
        int score = Integer.parseInt(strScore);
        resultIntent.putExtra("score", score);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
