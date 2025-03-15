package Enemies;
/**
 * Vector class to represent a 2D vector
 */
public class Vector {
    private float x;
    private float y;

    /**
     * Constructor for the Vector class
     * @param x X position
     * @param y Y position
     */
    public Vector(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return The x of the vector
     */
    public float getX(){
        return x;
    }

    /**
     * @return The y of the vector
     */
    public float getY(){
        return y;
    }

    /**
     * Sets the x of the vector
     * @param x the x to set
     */
    public void setX(float x){
        this.x = x;
    }
    /**
     * Sets the y of the vector
     * @param y the y to set
     */
    public void setY(float y){
        this.y = y;
    }
    /**
     * Normalizes the vector so it has a magnitude of 1 - becomes a direction vector
     * @return The normalized vector
     */
    public Vector normalize(){
        float magnitude = (float)Math.sqrt(x * x + y * y);
        x /= magnitude;
        y /= magnitude;
        return this;
    }
    public Vector negate(){
        x = -x;
        y = -y;
        return this;
    }
    /**
     * Calculates the direction the vector is pointing
     * @return the direction of the vector in radians
     */
    public float direction(){
        return (float)Math.atan2(y, x);
    }
    /**
     * Calculates the direction from the center of the screen
     * @param screenSize The size of the screen
     * @return the direction of the vector in radians
     */
    public float directionFromCenter(Vector screenSize){
        return (float)Math.atan2(y - screenSize.getY() / 2, x - screenSize.getX() / 2);
    }
    /**
     * @return a string representation of the vector "(x, y)"
     */
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
