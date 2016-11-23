package ca.tru.comp2160.kanamatch;

import ca.tru.comp2160.kanamatch.model.Coord;
import ca.tru.comp2160.kanamatch.model.MatchGame;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class MatchGameActivity extends Activity implements OnClickListener {
    public static int CELL_ID_BASE = 10000;
    
	private MatchGame game = null;
    private ArrayList<ArrayList<TextView>> cells;
	private String name;
	
	private boolean playAudio = false;
	private SoundPool soundPool = null;
	private SparseBooleanArray soundLoadingStatuses;
	private SparseIntArray sampleIds;
		
    private int[] soundIds = new int[] {R.raw.sound_a, R.raw.sound_i, R.raw.sound_u, R.raw.sound_e, R.raw.sound_o,
            R.raw.sound_ka, R.raw.sound_ki, R.raw.sound_ku, R.raw.sound_ke, R.raw.sound_ko,
            R.raw.sound_sa, R.raw.sound_shi, R.raw.sound_su, R.raw.sound_se, R.raw.sound_so,
            R.raw.sound_ta, R.raw.sound_chi, R.raw.sound_tsu, R.raw.sound_te, R.raw.sound_to,
            R.raw.sound_na, R.raw.sound_ni, R.raw.sound_nu, R.raw.sound_ne, R.raw.sound_no,
            R.raw.sound_ha, R.raw.sound_hi, R.raw.sound_fu, R.raw.sound_he, R.raw.sound_ho,
            R.raw.sound_ma, R.raw.sound_mi, R.raw.sound_mu, R.raw.sound_me, R.raw.sound_mo,
            R.raw.sound_ya, R.raw.sound_yu, R.raw.sound_yo,
            R.raw.sound_ra, R.raw.sound_ri, R.raw.sound_ru, R.raw.sound_re, R.raw.sound_ro,
            R.raw.sound_wa, R.raw.sound_wi, R.raw.sound_we, R.raw.sound_wo,
            R.raw.sound_n};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.game_board);
        
        // determine the game's difficulty level
        Intent playGameIntent = getIntent();
        int difficultyLevel = playGameIntent.getIntExtra("difficulty", MatchGame.EASY);
        int gameType = playGameIntent.getIntExtra("gameType", MatchGame.KANA_KANA_MATCH);
        playAudio = playGameIntent.getBooleanExtra("playAudio", false);

        // create a new model, and start a new game
        this.game = new MatchGame();
        this.game.setDifficulty(difficultyLevel);
        Log.i("KanaMatch", "boardSize = " + game.getBoardSize());
        this.game.setGameType(gameType);
        this.game.startNewGame(getResources());
        
        // create a clickable game board
        createGameBoard();
        updateScore();
        
        // pre-load all of the hiragana sounds
        preloadSoundData();
    }
    
    private void preloadSoundData() {
    	this.soundLoadingStatuses = new SparseBooleanArray(50);
    	this.sampleIds = new SparseIntArray(50);
    	
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
          public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        	  Log.i("KanaMatch", " done loading soundId = " + sampleId);
        	  soundLoadingStatuses.put(sampleId, true);
          }
        });
        
        for (int i = 0; i < soundIds.length; i++) {
        	Log.i("KanaMatch", "Loading sound #" + i + ": " + soundIds[i]);
        	int sampleId = soundPool.load(this, soundIds[i], 1);
        	Log.i("KanaMatch", "  sampleId = " + sampleId);
        	soundLoadingStatuses.put(sampleId, false);
        	sampleIds.put(soundIds[i], sampleId);
        }
    }

    private int getIndexOfRomaji(String romajiValue) {
        String[] romaji = getResources().getStringArray(R.array.romaji);
        for (int i = 0; i < romaji.length; i++) {
        	if (romajiValue.equals(romaji[i])) {
        		return i;
        	}
        }
        return -1;
    }
    
    private void playSound(String romaji) {
    	int index = getIndexOfRomaji(romaji);
    	
    	Log.i("KanaMatch", "playing sound for romaji: " + romaji);
    	
    	if (index >= 0) {
	        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
	        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        float volume = actualVolume / maxVolume;
	
	        if (index < soundIds.length){ 
	        	int sampleId = this.sampleIds.get(soundIds[index]);
		        if ((soundLoadingStatuses != null) && soundLoadingStatuses.get(sampleId)) {
		        	soundPool.play(sampleId, volume, volume, 1, 0, 1f);
		          	Log.e("KanaMatch", "Played sound");
		        }
	        } else {
	        	Log.i("KanaMatch", "Unable to play sound");
	        	Log.i("KanaMatch", "  soundLoadingStatuses = " + soundLoadingStatuses);
	        	Log.i("KanaMatch", "  index = " + index);
	        	Log.i("KanaMatch", "  soundIds.length = " + soundIds.length);
	        	if (index < soundIds.length) {
	        		Log.i("KanaMatch", "  soundIds[index] = " + soundIds[index]);
		        	int sampleId = this.sampleIds.get(soundIds[index]);
	        		Log.i("KanaMatch", "  sampleId = " + sampleId);
	        		Log.i("KanaMatch", "  status = " + soundLoadingStatuses.get(sampleId));
	        	}
	        }
    	}
    }
    
    private void createGameBoard() {
    	int gameBoardSize = this.game.getBoardSize();
    	
    	// this structure is the view group for the cells
    	TableLayout gameBoardGrid = (TableLayout)this.findViewById(R.id.lytGameBoardGrid);
    	gameBoardGrid.setStretchAllColumns(true);
    	    	
    	// this structure is to store the cells, so that we can determine which cell was clicked
    	cells = new ArrayList<ArrayList<TextView>>();
    	
    	// figure out how large to make each cell
    	int numColumns = game.getBoardSize();
    	int numRows = game.getBoardSize();
    	int cellWidth = getCellWidth(numColumns);
    	int cellHeight = getCellHeight(numRows);

    	for (int row = 0; row < gameBoardSize; row++) {
    		TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            
            ArrayList<TextView> cellRow = new ArrayList<TextView>();
            
        	for (int col = 0; col < gameBoardSize; col++) {
        		String label = this.game.getCellLabel(row, col);
        		int index = (row * gameBoardSize) + col;

        		TextView cell = createCell(label, index, cellWidth, cellHeight);
        		
        		// add the cell to the table layout
        		tableRow.addView(cell);
        		
        		// also, add the cell to our local data structure
        		cellRow.add(cell);
        	}
        	// add the table row to the view group
        	//gameBoardGrid.addView(tableRow, new TableLayout.LayoutParams(cellWidth, cellHeight));
        	//gameBoardGrid.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	gameBoardGrid.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        	
        	// add the row to the cell data structure
        	cells.add(cellRow);
    	}
    	
    	// adjust the height and width of each cell, after being added to the component
    	// TODO: this
    }

    private void updateView() {    	
    	int gameBoardSize = this.game.getBoardSize();
    	for (int row = 0; row < gameBoardSize; row++) {
        	for (int col = 0; col < gameBoardSize; col++) {
        		String label = game.getCellLabel(row, col);
        		boolean selected = game.isCellSelected(row, col);
        		TextView cell = this.getCell(row, col);
        		if (selected) {
        			// show the text of selected cells
        			//cell.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        			cell.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
        		} else if (!label.equals("")) {
        			// hide the text of unselected cells
        			cell.setText(label);
    		    	cell.setBackgroundResource(R.drawable.button_background);
        			cell.setTextColor(getResources().getColor(android.R.color.black));
        		} else {
        			// hide the text and card border for removed cells
        			cell.setText(label);
    		    	cell.setBackgroundResource(R.drawable.button_blank_background);
        			cell.setTextColor(getResources().getColor(android.R.color.black));
        		}
        	}
    	}
    	
    	// update the score
    	updateScore();
    }
    
    private void updateScore() {
    	int score = this.game.getScore();
    	String scorePrefix = getResources().getString(R.string.score_label);
    	TextView scoreLabel = (TextView)findViewById(R.id.lblScore);
    	scoreLabel.setText(scorePrefix + score);
    }
    
    private Coord findCellCoordinates(View cell) {
    	for (int row = 0; row < cells.size(); row++) {
    		ArrayList<TextView> cellRow = cells.get(row);
    		for (int col = 0; col < cellRow.size(); col++) {
    			if (cell == cellRow.get(col)) {
    				return new Coord(row, col);
    			}
    		}
    	}
    	return null;
    }
    
    private TextView getCell(int row, int col) {
    	ArrayList<TextView> cellRow = cells.get(row);
    	return cellRow.get(col);
    }
    
    private TextView createCell(String label, int index, int cellWidth, int cellHeight) {
    	TextView cell = new TextView(this);
    	
    	// set the component style
    	cell.setClickable(true);
    	cell.setOnClickListener(this);
    	cell.setBackgroundResource(R.drawable.button_background);
    	cell.setGravity(Gravity.CENTER);
    	cell.setId(CELL_ID_BASE + index);
    	cell.setText(label);
    	if (MatchGame.isHiragana(label)) {
    		cell.setTextAppearance(this, R.style.CardStyle);
    	} else {
        	cell.setTextAppearance(this, R.style.RomajiCardStyle);
    	}
    	
    	
    	// set the component dimensions
    	TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    	cell.setLayoutParams(layoutParams);
    	
    	return cell;
  	}
    
    private int getCellWidth(int numColumns) {
    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	int width = size.x;
    	Log.i("KanaMatch", "numColumns = " + numColumns);
    	return width / numColumns;
    }

    private int getCellHeight(int numRows) {
    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	int height = size.y;
    	Log.i("KanaMatch", "numRows = " + numRows);
    	return height / numRows;
    }
    
	public void onClick(View v) {
		if (!this.game.isGameOver()) {
			// figure out which cell was clicked
			Coord position = findCellCoordinates(v);
		
			// handle the click on the server side
			this.game.select(position.getX(), position.getY());
			this.updateView();
			
			// play the appropriate sound, if enabled
			if (playAudio) {
				String romaji = this.game.getCellValue(position.getX(), position.getY());
				playSound(romaji);
			}
		}

		// if the game is over, go back to the main menu, passing along our new score
		if (this.game.isGameOver()) {
			Intent getNameIntent = new Intent(this, GetNameActivity.class);
			startActivityForResult(getNameIntent, GetNameActivity.GET_NAME_REQUEST);
		}
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == GetNameActivity.GET_NAME_RESULT) {
    		// we are now ready to add the new high score
	    	name = data.getStringExtra("name");
	    	
	    	// activate the high scores activity
			Intent intent = this.getIntent();
			intent.putExtra("score", this.game.getScore());
			intent.putExtra("difficulty", this.game.getDifficulty());
			intent.putExtra("name", name);
			this.setResult(MainMenuActivity.PLAYED_GAME_RESULT, intent);
			Log.i("KanaMatch", "1end game - score " + this.game.getScore());
			finish();

    	}
    }

	class UpdateTask extends TimerTask {
		private MatchGameActivity activity;
		
		public UpdateTask(MatchGameActivity activity) {
			this.activity = activity;
		}
		
	    public void run() {
	    	activity.updateView();
	    }
	}			
}


