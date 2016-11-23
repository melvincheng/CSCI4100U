package ca.tru.comp3160.galacticoverlord.GalacticOverlord.model;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public abstract class MovableItem {
    private PointF location;
    private float speed;
    private boolean up;
    private boolean destroyed;

    public PointF getLocation() {
        return location;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public abstract float getWidth();
    public abstract float getHeight();

    /*
    public float getDiagonalDistance() {
        float deltaX = getWidth();
        float deltaY = getHeight();

        return (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    */

    public RectF getBoundingRect() {
        float halfWidth = getWidth() / 2;
        float halfHeight = getHeight() / 2;
        PointF location = getLocation();

        return new RectF(location.x - halfWidth, location.y - halfHeight, location.x + halfWidth, location.y + halfHeight);
    }


    /**
     * Extremely inefficient O(n^2) rectangular collision detection algorithm.
     *
     * Important special case for rectangular collision detection:
     *
     *               ***********        *****
     *               *         *        *   *
     *           *********     *    ************
     *           *   *   *     *    *   *   *  *
     *           *   *   *     *    *   *****  *
     *           *********     *    *          *
     *               *         *    ************
     *               ***********
     *
     * Another special case (note that no corners are within the other rectangle):
     *
     *             ******
     *             *    *
     *          ************
     *          *  *    *  *
     *          *  *    *  *
     *          ************
     *             *    *
     *             ******
     *
     * @param anotherItem Another object
     *
     * @return Whether or not the other object collides with this object
     */
    /*
    public boolean doesIntersectWith2(MovableItem anotherItem) {
        RectF item1_rect = this.getBoundingRect();
        PointF item1_tl = new PointF(item1_rect.left,  item1_rect.top);
        PointF item1_bl = new PointF(item1_rect.left,  item1_rect.bottom);
        PointF item1_tr = new PointF(item1_rect.right, item1_rect.top);
        PointF item1_br = new PointF(item1_rect.right, item1_rect.bottom);

        RectF item2_rect = anotherItem.getBoundingRect();
        PointF item2_tl = new PointF(item2_rect.left,  item2_rect.top);
        PointF item2_bl = new PointF(item2_rect.left,  item2_rect.bottom);
        PointF item2_tr = new PointF(item2_rect.right, item2_rect.top);
        PointF item2_br = new PointF(item2_rect.right, item2_rect.bottom);

        // check for horizontal alignment

        if (isInside(item1_tl, item2_rect) ||
            isInside(item1_tr, item2_rect) ||
            isInside(item1_bl, item2_rect) ||
            isInside(item1_br, item2_rect)) {
            return true;
        }

        if (isInside(item2_tl, item1_rect) ||
            isInside(item2_tr, item1_rect) ||
            isInside(item2_bl, item1_rect) ||
            isInside(item2_br, item1_rect)) {
            return true;
        }

        return false;
    }
    */

    /**
     * Efficient circular intersection algorithm.
     *
     * @param anotherItem The other object with which to compare.
     *
     * @return Whether or not the other object collides with this object.
     */
    public boolean doesIntersectWith(MovableItem anotherItem) {
        float otherRadius = anotherItem.getRadius();
        PointF otherCentre = anotherItem.getLocation();
        float thisRadius = this.getRadius();

        float centreDistance = this.distanceTo(otherCentre);
        //Log.i("GO", "centreDistance: " + centreDistance + ", otherRadius: " + otherRadius + ", thisRadius: " + thisRadius);
        Log.i("GO", "this.getBoundingRect(): " + this.getBoundingRect() + ", anotherItem.getBoundingRect(): " + anotherItem.getBoundingRect());

        return (centreDistance <= (otherRadius + thisRadius));
    }

    private float getRadius() {
        RectF item1_rect = this.getBoundingRect();
        return item1_rect.width() / 2;
    }

    private float distanceTo(PointF aPoint) {
        PointF centre = getLocation();

        float deltaX = Math.abs(aPoint.x - centre.x);
        float deltaY = Math.abs(aPoint.y - centre.y);

        return (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }


    /**
     * Used by rectangular collision detection algorithm
     *
     * @param point Some point
     * @param rect Some rectangle
     *
     * @return Whether or not the point is inside the rectangle
     */
    private boolean isInside(PointF point, RectF rect) {
        if (!between(point.x, rect.left, rect.right)) {
            return false;
        } else if (!between(point.y, rect.top, rect.bottom)) {
            return false;
        }
        return true;
    }

    private boolean between(float value, float lower, float upper) {
        if (value < lower) {
            return false;
        } else if (value > upper) {
            return false;
        }
        return true;
    }
}
