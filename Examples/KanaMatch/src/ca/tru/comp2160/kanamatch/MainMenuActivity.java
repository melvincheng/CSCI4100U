package ca.tru.comp2160.kanamatch;

import ca.tru.comp2160.kanamatch.HighScoresActivity.HighScore;
import ca.tru.comp2160.kanamatch.model.MatchGame;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class MainMenuActivity extends Activity {
	public static int PLAY_GAME_REQUEST = 123001;
	public static int SHOW_HIGH_SCORES_REQUEST = 123002;
	public static int SUBMIT_HIGH_SCORE_REQUEST = 123003;
	
    public static int PLAYED_GAME_RESULT = 123011;
    public static int SHOWED_HIGH_SCORES_RESULT = 123012;
    public static int SUBMITTED_HIGH_SCORE_RESULT = 123013;
    	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_menu);
    }
    
    public void startGame(View v) {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String difficultyKey = getResources().getString(R.string.prefDifficultyKey);
    	String audioKey = getResources().getString(R.string.prefAudioKey);
    	String gameTypeKey = getResources().getString(R.string.prefGameTypeKey);
    	
    	int difficulty = Integer.parseInt(preferences.getString(difficultyKey, ""+MatchGame.EASY));
    	int gameType = Integer.parseInt(preferences.getString(gameTypeKey, ""+MatchGame.KANA_KANA_MATCH));
    	boolean playAudio = preferences.getBoolean(audioKey, false);
    	
    	startGame(difficulty, gameType, playAudio);
    }

    private void startGame(int difficulty, int gameType, boolean playAudio) {
    	Intent playGameIntent = new Intent(this, MatchGameActivity.class);
    	playGameIntent.putExtra("difficulty", difficulty);
    	playGameIntent.putExtra("gameType", gameType);
        playGameIntent.putExtra("playAudio", playAudio);
    	startActivityForResult(playGameIntent, PLAY_GAME_REQUEST);
    }
    
    public void showPreferences(View v) {
    	Intent showPrefsIntent = new Intent(this, OptionsActivity.class);
    	startActivity(showPrefsIntent);
    }
    
    public void showHighScores(View v) {
    	Intent showHighScoresIntent = new Intent(this, HighScoresActivity.class);
    	startActivity(showHighScoresIntent);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == PLAYED_GAME_RESULT) {
    		// collect the results from the game
    		String name = data.getStringExtra("name");
	    	int score = data.getIntExtra("score", -1);
	    	int difficulty = data.getIntExtra("difficulty", MatchGame.EASY);
	    	
	    	// send the results from the game to the high scores activity
	    	Intent addHighScoreIntent = new Intent(this, HighScoresActivity.class);
	    	addHighScoreIntent.putExtra("name", name);
	    	addHighScoreIntent.putExtra("score", score);
	    	addHighScoreIntent.putExtra("difficulty", difficulty);
	    	startActivityForResult(addHighScoreIntent, SUBMIT_HIGH_SCORE_REQUEST);
    	}
    }
}
