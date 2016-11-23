package ca.tru.comp3160.galacticoverlord.GalacticOverlord;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import ca.tru.comp3160.galacticoverlord.GalacticOverlord.audio.AudioHandler;
import ca.tru.comp3160.galacticoverlord.GalacticOverlord.graphics.GameBoard;
import ca.tru.comp3160.galacticoverlord.GalacticOverlord.model.Asteroid;
import ca.tru.comp3160.galacticoverlord.GalacticOverlord.model.GameModel;
import ca.tru.comp3160.galacticoverlord.GalacticOverlord.model.LaserBlast;
import ca.tru.comp3160.galacticoverlord.GalacticOverlord.model.MovableItem;

/*
 * PlayGameActivity
 *
 * This controller has several responsibilities:
 *  - initializing the view
 *  - main game loop
 *  - handling user input, and translating it into model updates
 *  - passing data from the model to the view
 */
public class PlayGameActivity extends Activity  implements SensorEventListener, View.OnTouchListener {
    public static final float TILT_THRESHOLD = 1.5f;

    private GameModel model;

    private GameBoard view;

    private AudioHandler audioHandler;

    private long redrawTime = 0L;

    private boolean paused = false;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        float worldWidth = 800f;
        float worldHeight = 1280f;

        // initialize the model
        this.model = new GameModel(worldWidth, worldHeight);

        // initialize the graphical view
        this.view = (GameBoard)findViewById(R.id.gameBoard);
        this.view.setShipPosition(convertToScreenCoords(this.model.getShipLocation()));
        this.view.setOnTouchListener(this);

        // initialize the audio view
        this.audioHandler = new AudioHandler(this);

        // tell Android that we want to receive events from the accelerometer
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
            Log.d("GO", "No accelerometer installed");
        } else {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            if (!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)) {
                Log.d("GO", "Could not register sensor listener");
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        this.audioHandler.pause(isFinishing());

        view.setOnTouchListener(null);
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.audioHandler.resume();

        view.setOnTouchListener(this);
        paused = false;
    }

    private PointF convertToScreenCoords(PointF position) {
        return new PointF(position.x, position.y); // no conversion for now
    }

    private void fire() {
        PointF blastPosition = model.fire();
        view.addLaserBlast(blastPosition);
    }

    private void update() {
        long now = System.currentTimeMillis();
        if (this.redrawTime > 0) {

            // tell the model to update
            boolean kaboom = model.timeElapsed(now - this.redrawTime);
            if (kaboom) {
                this.audioHandler.playExplosionSound();
            }
            int score = model.getScore();
            view.setScore(score);
            if (model.isGameOver()) {
                clearSprites();
                view.gameOver();
                this.audioHandler.playGameOverSound();
            } else {
                updateSpriteLocations();
            }
        }

        view.redraw();

        this.redrawTime = now;
    }

    private void updateSpriteLocations() {
        this.view.clearLaserBlasts();
        this.view.clearAsteroids();
        for (MovableItem item: this.model.getMovableItems()) {
            if (item instanceof LaserBlast) {
                view.addLaserBlast(item.getLocation());
            } else if (item instanceof Asteroid) {
                view.addAsteroid(item.getLocation());
            }
        }
    }

    private void clearSprites() {
        this.view.clearLaserBlasts();
        this.view.clearAsteroids();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!model.isGameOver() && !paused) {
            // +ve x is tilted left, -ve x is tilted right
            float x = event.values[0];
            if (x >= TILT_THRESHOLD) {
                model.moveLeft();
            } else if (x <= -TILT_THRESHOLD) {
                model.moveRight();
            }
            view.setShipPosition(convertToScreenCoords(model.getShipLocation()));
            update();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public boolean onTouch(View v, MotionEvent e){
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                fire();
                this.audioHandler.playLaserBlastSound();
                break;
        }

        return true;
    }
}