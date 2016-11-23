package ca.tru.comp3160.galacticoverlord.GalacticOverlord.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import ca.tru.comp3160.galacticoverlord.GalacticOverlord.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends View {
    public static final int SCORE_OFFSET_X = 50;
    public static final int SCORE_OFFSET_Y = 50;

    public static final int GAME_OVER_OFFSET_X = 120;
    public static final int GAME_OVER_OFFSET_Y = 600;

    private RectF shipPosition;
    private ArrayList<RectF> laserBlastPositions;
    private ArrayList<RectF> asteroidPositions;

    private Bitmap shipBitmap;
    private Bitmap blastBitmap;
    private Bitmap asteroidBitmap;

    private Typeface typeface;

    private int score;
    private boolean gameOver = false;

    public GameBoard(Context context) {
        super(context);
        init();
    }

    public GameBoard(Context context, AttributeSet attribs) {
        super(context, attribs);
        init();
    }

    private void init() {
        AssetManager assetManager = getContext().getAssets();
        try {
            InputStream inputStream = assetManager.open("TwinHullShip.png");
            this.shipBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            inputStream = assetManager.open("LaserBlast.png");
            this.blastBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            inputStream = assetManager.open("Asteroid.png");
            this.asteroidBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            this.typeface = Typeface.createFromAsset(getContext().getAssets(), "SpaceAge.ttf");

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.laserBlastPositions = new ArrayList<RectF>();
        this.asteroidPositions = new ArrayList<RectF>();
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawRGB(0, 0, 0);

        if (!gameOver) {
            // draw everything at its current location

            // laser blasts
            for (RectF laserBlastPosition: this.laserBlastPositions) {
                canvas.drawBitmap(blastBitmap, null, laserBlastPosition, null);
            }

            // asteroids
            for (RectF asteroidPosition: this.asteroidPositions) {
                canvas.drawBitmap(asteroidBitmap, null, asteroidPosition, null);
            }

            // our ship
            canvas.drawBitmap(shipBitmap, null, getShipPosition(), null);
        } else {
            // game over
            String gameOver = getContext().getResources().getString(R.string.game_over);
            Paint largeFont = new Paint();
            largeFont.setTypeface(typeface);
            largeFont.setTextSize(72f);
            largeFont.setColor(0xff505050);
            canvas.drawText(gameOver, GAME_OVER_OFFSET_X, GAME_OVER_OFFSET_Y, largeFont);
        }

        // the score
        String score = getContext().getResources().getString(R.string.score_label) + " " + this.getScore();
        Paint font = new Paint();
        font.setTypeface(typeface);
        font.setTextSize(32f);
        font.setColor(0xff505050);
        canvas.drawText(score, SCORE_OFFSET_X, SCORE_OFFSET_Y, font);
    }

    public RectF getShipPosition() {
        return shipPosition;
    }

    /*
     * Creates a rectangle centred on the ship's position.
     */
    public void setShipPosition(PointF position) {
        float x = position.x;
        float y = position.y;
        int diffX = shipBitmap.getWidth() / 2;
        int diffY = shipBitmap.getHeight() / 2;
        this.shipPosition = new RectF(x - diffX, y - diffY, x + diffX, y + diffY);
    }

    public void clearLaserBlasts() {
        this.laserBlastPositions = new ArrayList<RectF>();
    }

    public void addLaserBlast(PointF position) {
        float x = position.x;
        float y = position.y;
        int diffX = blastBitmap.getWidth() / 2;
        int diffY = blastBitmap.getHeight() / 2;
        RectF blastPosition = new RectF(x - diffX, y - diffY, x + diffX, y + diffY);
        this.laserBlastPositions.add(blastPosition);
    }

    public void clearAsteroids() {
        this.asteroidPositions = new ArrayList<RectF>();
    }

    public void addAsteroid(PointF position) {
        float x = position.x;
        float y = position.y;
        int diffX = asteroidBitmap.getWidth() / 2;
        int diffY = asteroidBitmap.getHeight() / 2;
        RectF asteroidPosition = new RectF(x - diffX, y - diffY, x + diffX, y + diffY);
        this.asteroidPositions.add(asteroidPosition);
    }

    public void redraw() {
        invalidate();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void gameOver() {
        this.gameOver = true;
    }
}
