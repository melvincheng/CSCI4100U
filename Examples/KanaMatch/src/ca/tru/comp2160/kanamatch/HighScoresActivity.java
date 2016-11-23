package ca.tru.comp2160.kanamatch;

import java.util.ArrayList;

import ca.tru.comp2160.kanamatch.model.MatchGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HighScoresActivity extends Activity {
	public final static String PREFERENCES_NAME = "KanaMatch.Highscores";
	public final static String NAME_KEY = "Player.Name";
	public final static int NUM_HIGH_SCORES = 10;
	
	private ArrayList<HighScore> scores;
	//private String name;
	private HighScore newScore;

	/*
    private String getName() { return this.name; }
    private void setName(String name) {
    	this.name = name;
    }
    */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.high_scores);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        Log.i("KanaMatch", "Loading High Scores...");
        loadHighScores();
        
        // add the high scores to the list
        ListView lstScores = (ListView)findViewById(R.id.lstHighScores);
        HighScoreAdapter adapter = new HighScoreAdapter(this, R.layout.high_score_item, scores);
        View header = (View)getLayoutInflater().inflate(R.layout.high_score_header, null);
        lstScores.addHeaderView(header);
        lstScores.setAdapter(adapter);
                
        // get the score and other information from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("score")) {
        	String name = intent.getStringExtra("name");
        	int score = intent.getIntExtra("score", -1);
        	int difficulty = intent.getIntExtra("difficulty", MatchGame.EASY);
        	if (score > 0) {
        		newScore = new HighScore(name, difficulty, score);
        		addHighScore(newScore);
        	}
        }
    }
    
    private void addHighScore(HighScore newScore) {
    	for (int i = 0; i < scores.size(); i++) {
    		HighScore currentScore = scores.get(i);
    		if (newScore.getScore() > currentScore.getScore()) {
    			scores.add(i, newScore);
    			break;
    		}
    	}
    	saveHighScores();
    }
    
    private String getNameKey(int index) {
    	return "highscore_" + index + "_name";
    }
    
    private String getDifficultyKey(int index) {
    	return "highscore_" + index + "_difficulty";
    }
    
    private String getScoreKey(int index) {
    	return "highscore_" + index + "_score";
    }
    
	private void loadHighScores() {
    	this.scores = new ArrayList<HighScore>();
    	
    	//SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	for (int i = 0; i < NUM_HIGH_SCORES; i++) {
    		if (preferences.contains(getNameKey(i))) {
    			String name = preferences.getString(getNameKey(i), "");
    			int difficulty = preferences.getInt(getDifficultyKey(i), MatchGame.EASY);
    			int scoreVal = preferences.getInt(getScoreKey(i), -1);
    			HighScore score = new HighScore(name, difficulty, scoreVal);
    			scores.add(score);
    		}
    	}
    	
    	// if there are no scores in the preferences, use defaults
    	if (scores.size() == 0) {
    		// default scores
            scores.add(new HighScore("Randy", MatchGame.HARD, 1000));
            scores.add(new HighScore("Martin", MatchGame.HARD, 900));
            scores.add(new HighScore("Salman", MatchGame.MEDIUM, 800));
            scores.add(new HighScore("Richard", MatchGame.EASY, 700));
            scores.add(new HighScore("Khalid", MatchGame.MEDIUM, 600));
            scores.add(new HighScore("Katrina", MatchGame.EASY, 500));
            scores.add(new HighScore("Jin", MatchGame.MEDIUM, 400));
            scores.add(new HighScore("Stephanie", MatchGame.MEDIUM, 300));
            scores.add(new HighScore("Harpreet", MatchGame.HARD, 200));
            scores.add(new HighScore("Joey", MatchGame.EASY, 100));
    	}
    }
    
    private void saveHighScores() {
    	//SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	SharedPreferences.Editor editor = preferences.edit();
    	
    	// ensure that we don't save more scores than the maximum
    	int count = scores.size();
    	if (scores.size() > NUM_HIGH_SCORES) {
    		count = NUM_HIGH_SCORES;
    	}
    	
    	// save all the scores
    	for (int i = 0; i < count; i++) {
    		HighScore score = scores.get(i);
    		editor.putString(getNameKey(i), score.getName());
    		editor.putInt(getDifficultyKey(i), score.getIntDifficulty());
    		editor.putInt(getScoreKey(i), score.getScore());
    	}
    	editor.commit();
    	
    	// if there are any extra scores, drop them :(
    	if (count < scores.size()) {
    		scores.subList(count, scores.size()).clear();
    	}
    }
    
    class HighScore {		
		private String name;
    	private int difficulty;
    	private int score;
    	
    	public HighScore(String name, int difficulty, int score) {
    		this.name = name;
    		this.difficulty = difficulty;
    		this.score = score;
    	}
    	
    	public String getName() { return this.name; }
    	public void setName(String name) { this.name = name; }
    	
    	public int getIntDifficulty() { return this.difficulty; }
    	public String getDifficulty() {
    		switch (this.difficulty) {
				case MatchGame.EASY:
					return "Easy";
				case MatchGame.MEDIUM:
					return "Medium";
				case MatchGame.HARD:
					return "Hard";
    		}
    		return "Easy";
    	}
    	
    	public int getScore() { return this.score; }
    }
    
    class HighScoreAdapter extends ArrayAdapter<HighScore> {

        private ArrayList<HighScore> items;

        public HighScoreAdapter(Context context, int textViewResourceId, ArrayList<HighScore> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.high_score_item, null);
                }
                HighScore score = items.get(position);
                if (score != null) {
                        TextView lblName = (TextView)view.findViewById(R.id.lblHighScoreName);
                        TextView lblDifficulty = (TextView)view.findViewById(R.id.lblHighScoreDifficulty);
                        TextView lblScore = (TextView)view.findViewById(R.id.lblHighScoreScore);
                        
                        if (lblName != null) {
                            lblName.setText(score.getName());
                        }
                        
                        if (lblDifficulty != null) {
                        	lblDifficulty.setText(score.getDifficulty());
                        }
                        
                        if (lblScore != null){
                            lblScore.setText("" + score.getScore());
                        }
                }
                return view;
        }
    }
}
