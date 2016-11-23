package ca.tru.comp3160.galacticoverlord.GalacticOverlord.model;

import android.graphics.PointF;

import java.util.ArrayList;

public class GameModel {
    public static final float SHIP_DISTANCE_FROM_BOTTOM = 280f;
    public static final float SIDE_MOVEMENT = 10f;
    public static final float LASER_BLAST_SPEED = 1f;
    public static final float ASTEROID_SPEED = .25f;
    public static final float ASTEROID_PROBABILITY = 0.1f;
    public static final float INITIAL_ASTEROID_Y_MIN = -500f;
    public static final float INITIAL_ASTEROID_Y_MAX = -50f;
    public static final int INITIAL_NUMBER_OF_ASTEROIDS = 3;
    public static final int AMMUNITION_SIZE = 3;
    public static final int ASTEROID_SCORE = 100;

    private float boardWidth;
    private float boardHeight;

    private ArrayList<MovableItem> movableItems;
    private ArrayList<LaserBlast> ammunition;

    private YourShip yourShip;

    private boolean gameOver = false;
    private int score = 0;


    public GameModel(float boardWidth, float boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // create the initial set of asteroids
        this.movableItems = new ArrayList<MovableItem>();
        for (int i = 0; i < INITIAL_NUMBER_OF_ASTEROIDS; i++) {
            this.movableItems.add(newAsteroid());
        }

        // create the laser blasts, to be reused
        this.ammunition = new ArrayList<LaserBlast>();
        for (int i = 0; i < AMMUNITION_SIZE; i++) {
            this.ammunition.add(newLaserBlast());
        }

        // place the ship in the middle, at the bottom of the screen
        PointF shipLocation = new PointF(this.boardWidth / 2, this.boardHeight - SHIP_DISTANCE_FROM_BOTTOM);
        this.yourShip = new YourShip();
        this.yourShip.setLocation(shipLocation);
        this.yourShip.setUp(false);
        this.yourShip.setSpeed(0f);

        this.gameOver = false;
        this.score = 0;
    }

    private void increaseScore(int amount) {
        this.score += amount;
    }

    public int getScore() {
        return this.score;
    }

    public boolean timeElapsed(long duration) {
        boolean kaboom = false;

        // update the item locations, due to speed
        for (int i = 0; i < this.getMovableItems().size(); i++) {
            MovableItem item = this.getMovableItems().get(i);

            float speed = item.getSpeed();
            PointF location = item.getLocation();
            float motion = (float)duration * speed; // d = v * t

            if (item.isUp()) {
                location.y -= motion;
            } else {
                location.y += motion;
            }

            this.movableItems.set(i, item);
        }

        // have there been any collisions?
        for (int i = 0; i < this.getMovableItems().size(); i++) {
            MovableItem item1 = this.getMovableItems().get(i);

            // first, check if the object collides with our ship
            if ((item1 instanceof Asteroid) && item1.doesIntersectWith(this.yourShip)) {
                setGameOver(true);
            }

            // second, check if the object collides with any moving object
            for (int j = 1; j < this.getMovableItems().size(); j++) {
                MovableItem item2 = this.getMovableItems().get(j);
                if (i != j) {
                    if (item1.doesIntersectWith(item2)) {
                        increaseScore(ASTEROID_SCORE);
                        kaboom = true;

                        item1.setDestroyed(true);
                        item2.setDestroyed(true);
                    }
                }
            }
        }

        // clean up any items that may be out of play
        ArrayList<MovableItem> newItems = new ArrayList<MovableItem>();
        for (int i = 0; i < this.getMovableItems().size(); i++) {
            MovableItem item = this.getMovableItems().get(i);
            PointF location = item.getLocation();

            if (item instanceof Asteroid) {
                if (item.isDestroyed()) {
                    // asteroid was destroyed
                    // move any out-of-play asteroids back to the top of the screen
                    // at a random horizontal position
                    location = randomAsteroidLocation();

                    item.setLocation(location);
                    item.setDestroyed(false);
                    newItems.add(item);

                    // possibly, add an extra random asteroid at the top of the screen
                    // this increases the difficulty of the game as time goes on
                    double diceRoll = Math.random();
                    if (diceRoll < ASTEROID_PROBABILITY) {
                        Asteroid newAsteroid = newAsteroid();
                        this.getMovableItems().add(newAsteroid);
                    }

                } else if (isOutsideWorld(location, false)) {
                    // asteroid has gone past
                    // move any out-of-play asteroids back to the top of the screen
                    // at a random horizontal position
                    location = randomAsteroidLocation();

                    item.setLocation(location);
                    item.setDestroyed(false);
                    newItems.add(item);
                } else {
                    // asteroid is still on the board
                    newItems.add(item);
                }
            } else if (item instanceof LaserBlast) {

                if (isOutsideWorld(location, true)) {
                    // the laser blast left the screen, reuse it
                    item.setSpeed(0f);
                    this.ammunition.add((LaserBlast)item);

                } else if (item.isDestroyed()) {
                    // the last blast was destroyed, reuse it
                    item.setLocation(defaultLaserBlastLocation());
                    item.setSpeed(0f);
                    this.ammunition.add((LaserBlast)item);

                } else {
                    // the laser blast is still on the board
                    newItems.add(item);
                }
            }
        }
        this.movableItems = null;
        this.movableItems = newItems;

        return kaboom;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    private Asteroid newAsteroid() {
        PointF location = randomAsteroidLocation();

        Asteroid newAsteroid = new Asteroid();
        newAsteroid.setLocation(location);
        newAsteroid.setUp(false);
        newAsteroid.setSpeed(ASTEROID_SPEED);

        return newAsteroid;
    }

    private PointF randomAsteroidLocation() {
        float yOffset = (int)(Math.random() * Math.abs(INITIAL_ASTEROID_Y_MAX - INITIAL_ASTEROID_Y_MIN));
        float y = yOffset + (int)INITIAL_ASTEROID_Y_MIN;
        float x = (int)(Math.random() * this.boardWidth);

        return new PointF(x, y);
    }

    private PointF defaultLaserBlastLocation() {
        return new PointF(0f, 0f);
    }

    private boolean isOutsideWorld(PointF location, boolean up) {
        if (location.x < 0) {
            return true;
        } else if (!up && location.y < INITIAL_ASTEROID_Y_MIN) {
            return true;
        } else if (up && location.y < 0) {
            return true;
        } else if (location.x > this.boardWidth) {
            return true;
        } else if (location.y > this.boardHeight) {
            return true;
        }
        return false;
    }

    public PointF getShipLocation() {
        return this.yourShip.getLocation();
    }

    public void setShipLocation(PointF shipLocation) {
        this.yourShip.setLocation(shipLocation);
    }

    public void moveLeft() {
        this.yourShip.getLocation().x -= SIDE_MOVEMENT;

        if (this.yourShip.getLocation().x < 0) {
            this.yourShip.getLocation().x = 0;
        }
    }

    public void moveRight() {
        this.yourShip.getLocation().x += SIDE_MOVEMENT;

        if (this.yourShip.getLocation().x > (this.boardWidth - 1)) {
            this.yourShip.getLocation().x = this.boardWidth - 1;
        }
    }

    private LaserBlast laserBlastInstance() {
        if (this.ammunition.size() < 1) {
            return null;
        }

        LaserBlast laserBlast = this.ammunition.get(0);
        this.ammunition.remove(0);

        return laserBlast;

    }

    private LaserBlast newLaserBlast() {
        LaserBlast newLaserBlast = new LaserBlast();
        newLaserBlast.setLocation(defaultLaserBlastLocation());

        newLaserBlast.setUp(true);
        newLaserBlast.setSpeed(LASER_BLAST_SPEED);

        return newLaserBlast;
    }

    public PointF fire() {
        LaserBlast newLaserBlast = laserBlastInstance();
        if (newLaserBlast != null) {
            PointF shipLocation = getShipLocation();
            PointF location = new PointF(shipLocation.x, shipLocation.y);
            newLaserBlast.setLocation(location);
            newLaserBlast.setSpeed(LASER_BLAST_SPEED);
            newLaserBlast.setUp(true);
            newLaserBlast.setDestroyed(false);

            this.getMovableItems().add(newLaserBlast);
        } else {
            // no ammunition available
        }

        return yourShip.getLocation();
    }

    public ArrayList<MovableItem> getMovableItems() {
        return movableItems;
    }
}
