package ca.tru.comp2160.kanamatch.model;

//import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Collections;

import ca.tru.comp2160.kanamatch.R;

import android.content.res.Resources;
import android.util.Log;

public class MatchGame {
	public final static int EASY = 4;
	public final static int MEDIUM = 6;
	public final static int HARD = 8;
	public final static int EASY_SIZE = 4;
	public final static int MEDIUM_SIZE = 6;
	public final static int HARD_SIZE = 8;
	public final static int MATCH_SCORE_FACTOR = 1000000;
	public final static int KANA_KANA_MATCH = 301;
	public final static int KANA_ROMAJI_MATCH = 302;
	
	private long gameStart;
	private int difficulty;
	private int gameType;
	private int numMatches;
	private Card[][] cards;
	private long lastMatchTime = 0l;
	private int currentScore = 0;
	private ArrayList<Coord> selections;
	
	
	public void setGameStart(long gameStart) { this.gameStart = gameStart; }
	public long getGameStart() { return this.gameStart; }
	public long getElapsedGameTime() { return now() - this.gameStart; }

	//private void resetScore() { this.currentScore = 0; }
	private void addScore(int newScore) {
		this.currentScore += newScore;
	}
	public int getScore() { return this.currentScore; }
	
	private void resetLastMatchTime() {
		this.lastMatchTime = getGameStart();
	}
	private void setLastMatchTime(long time) { this.lastMatchTime = time; }
	//private long getLastMatchTime() { return this.lastMatchTime; }
	
	public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
	public int getDifficulty() { return this.difficulty; }
	
	public void setGameType(int gameType) { this.gameType = gameType; }
	public int getGameType() { return this.gameType; }
	
	public void setNumMatches(int numMatches) { this.numMatches = numMatches; }
	public int getNumMatches() { return this.numMatches; }

//	private Card getCell(int row, int col) { return this.cards[row][col]; }
	public String getCellLabel(int row, int col) { return this.cards[col][row].getLabel(); }
	public String getCellValue(int row, int col) { return this.cards[col][row].getValue(); }
	public boolean isCellSelected(int row, int col) { return this.cards[col][row].isSelected(); }
	
	private long now() { return System.currentTimeMillis(); }
	
	public void startNewGame(Resources resources) {
		setNumMatches(0);
		
		setGameStart(now());
		resetLastMatchTime();
		clearSelection();
		
		if (this.gameType == KANA_KANA_MATCH) {
			createBoardWithHiragana(resources);
		} else {
			createBoardWithHiraganaAndRomaji(resources);
		}
	}
	
	public int getBoardSize() {
		if (getDifficulty() == EASY) {
			return EASY_SIZE;
		} else if (getDifficulty() == MEDIUM) {
			return MEDIUM_SIZE;
		} else if (getDifficulty() == HARD) {
			return HARD_SIZE;
		}
		return EASY_SIZE; // default
	}
	
	private void createBoardWithHiragana(Resources resources) {
        String[] hiragana = resources.getStringArray(R.array.hiragana);
        int boardSize = this.getBoardSize();
        ArrayList<String> cardValues = this.getCardsToUse(resources, (boardSize * boardSize) / 2, false);
        
		// initialize the game board in a grid of the appropriate size
		int cardIndex = 0;
		cards = new Card[boardSize][boardSize];
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				String label = cardValues.get(cardIndex);
				cards[y][x] = new Card(label);
				String romaji = getRomajiForHiragana(resources, label);
				cards[y][x].setValue(romaji);
				cardIndex++;
			}
		}
	}
	
	private String getRomajiForHiragana(Resources resources, String hiraganaChar) {
        String[] hiragana = resources.getStringArray(R.array.hiragana);
        String[] romaji = resources.getStringArray(R.array.romaji);
        
        for (int i = 0; i < hiragana.length; i++) {
        	if (hiragana[i].equals(hiraganaChar)) {
        		return romaji[i];
        	}
        }
        
		return "";
	}
	
	public static boolean isHiragana(String str) {
		if ((str.charAt(0) >= 'a') && (str.charAt(0) <= 'z'))
			return false;
		return true;
	}
	
	private void createBoardWithHiraganaAndRomaji(Resources resources) {
        String[] hiragana = resources.getStringArray(R.array.hiragana);
        int boardSize = this.getBoardSize();
        ArrayList<String> cardValues = this.getCardsToUse(resources, (boardSize * boardSize) / 2, true);
        
		// initialize the game board in a grid of the appropriate size
		int cardIndex = 0;
		cards = new Card[boardSize][boardSize];
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				String label = cardValues.get(cardIndex);
				cards[y][x] = new Card(label);
				String romaji = "";
				if (isHiragana(label)) {
					romaji = getRomajiForHiragana(resources, label);
				} else {
					romaji = label;
				}
				cards[y][x].setValue(romaji);
				cardIndex++;
			}
		}
	}
	
	private ArrayList<String> getCardsToUse(Resources resources, int numberOfCards, boolean useRomaji) {
        String[] hiragana = resources.getStringArray(R.array.hiragana);
        String[] romaji = resources.getStringArray(R.array.romaji);

		// create an array list with all of the available values
		ArrayList<String> allCardValues = new ArrayList<String>();
		for (int i = 0; i < hiragana.length; i++) {
			allCardValues.add(hiragana[i]);
		}
		
		// shuffle the list of cards, so that we can choose any cards
		Collections.shuffle(allCardValues);
		
		Log.i("KanaMatch/debug", "allCardValues.size = " + allCardValues.size());
		Log.i("KanaMatch/debug", "numberOfCards = " + numberOfCards);
		
		allCardValues.set(0, "\u3041"); // FOR NOW
		
		// take the first numberOfCards values
		ArrayList<String> deck1 = copyN(allCardValues, numberOfCards);

		Log.i("KanaMatch/debug", "deck1.size = " + deck1.size());

		// append a copy of the same card list to itself, since we want two of each card
		ArrayList<String> cards = null;
		if (useRomaji) {
			cards = new ArrayList<String>();
			for (int i = 0; i < deck1.size(); i++) {
				String hiraganaChar = deck1.get(i);
				cards.add(getRomajiForHiragana(resources, hiraganaChar));
			}
		} else {
			cards = copy(deck1);
		}
		cards.addAll(deck1);
		
		Log.i("KanaMatch/debug", "cards (double deck1).size = " + cards.size());

		// shuffle the cards again, so they are randomized
		Collections.shuffle(cards);
		
		Log.i("KanaMatch/debug", "cards (shuffled).size = " + cards.size());

		return cards;
	}
	
	private ArrayList<String> copy(ArrayList<String> original) {
		ArrayList<String> copy = new ArrayList<String>(original.size());

		for (String value: original) {
			copy.add(value);
		}
		
		return copy;
	}
	
	private ArrayList<String> copyN(ArrayList<String> original, int numElements) {
		// protect against users sending us bad arguments
		if ((numElements > original.size()) || (numElements < 0)) {
			numElements = original.size();
		}
		
		ArrayList<String> copy = new ArrayList<String>(original.size());

		for (int i = 0; i < numElements; i++) {
			copy.add(original.get(i));
		}
		
		return copy;
	}
	
	public boolean isMatch(int x1, int y1) {
		// just to be cautious, ensure that selections has an element
		if (selections.size() < 1) {
			return false;
		}
		
		// TODO: Add check for hiragana/romaji match
		// 
		
		Coord selection = selections.get(0);
		int x2 = selection.getX();
		int y2 = selection.getY();

		// obviously, the same card clicked twice is not a match
		if ((x1 == x2) && (y1 == y2)) {
			return false;
		}
		
		// if two cards have the same text, they are a match
		String label1 = cards[y1][x1].getValue();
		String label2 = cards[y2][x2].getValue();
		return label1.equals(label2);
	}
	
	public void handleMatch(int x1, int y1) {
		// just to be cautious, ensure that selections has an element
		if (selections.size() < 1) {
			return;
		}
		
		Coord selection = selections.get(0);
		int x2 = selection.getX();
		int y2 = selection.getY();
		
		// blank those cards
		cards[y1][x1] = new Card("");
		cards[y2][x2] = new Card("");
		numMatches++;
		
		// Score for this match is inversely proportional to how long it took
		if (this.lastMatchTime == 0) {
			this.lastMatchTime = this.getGameStart();
		}
		long now = now();
		int elapsedTimeSinceLastMatch = (int)(now - this.lastMatchTime);
		int matchScore = MATCH_SCORE_FACTOR / elapsedTimeSinceLastMatch;
		this.addScore(matchScore);
				
		// record the match time for the next time
		this.setLastMatchTime(now);
	}
	
	public boolean isGameOver() {
		for (int row = 0; row < cards.length; row++) {
			for (int col = 0; col < cards[row].length; col++) {
				if (!cards[row][col].getLabel().equals("")) {
					return false;
				}
			}
		}
		return true;
	}

	public void select(int x, int y) {
		if (selections.size() >= 2) {
			clearSelection();
		}
		
		if (selections.size() == 0) {
			// this is the first card to be selected
			// remember its coordinates
			
			// if the card has not already been matched, and is not already selected, select it
			selections.add(new Coord(x, y));

			cards[y][x].setSelected(true);
		} else if (selections.size() == 1) {
			// the user has already selected a card previously, check if it is a match
			
			if (isMatch(x, y)) {
				// the user found a match, award her/him some points
				handleMatch(x, y);
				
				// remove any existing selection
				clearSelection();
			} else {
				// the user did not find a match, let them try again
				selectCard(x, y); // two selections until the next card is selected
			}
		}
	}
	
	public void selectCard(int x, int y) {
		cards[y][x].setSelected(true);
		selections.add(new Coord(x, y));
	}
	
	public void clearSelection() {
		if (selections == null) {
			selections = new ArrayList<Coord>();
		} else {
			for (Coord coord: selections) {
				cards[coord.getY()][coord.getX()].setSelected(false);
			}
			selections.clear();
		}
	}
}
