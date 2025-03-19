package Enemies;
import java.util.Random;

/**
 * Default Enemy class
 */
public class Enemy {
    protected float[] color = new float[] { 255, 255, 255 };
    protected Vector sizeRange = new Vector(9, 120);
    protected Vector shadeRange = new Vector(100, 220);
    protected float sizeFromEating = 2;
    protected float scoreFromEating = 1;
    protected float size;
    protected Vector position;
    protected float speed;
    protected float shade;
    protected Vector movementDirection;
    protected float lifeTime;
    protected static Random rand = new Random();
    protected boolean offScreenFromSpawn = true;
    protected boolean offScreenFromMovement = false;

    /**
     * Constructor for the Enemy class
     * 
     * @param position          Position of the enemy
     * @param speed             Speed of the enemy
     * @param shade             Shade of the enemies display
     * @param movementDirection Direction the enemy is moving
     */
    public Enemy(Vector position, float speed, Vector movementDirection) {
        size = rand.nextFloat(sizeRange.getY() - sizeRange.getX()) + sizeRange.getX();
        this.position = position;
        this.speed = speed;
        shade = rand.nextFloat(shadeRange.getY() - shadeRange.getX()) + shadeRange.getX();
        this.movementDirection = movementDirection.normalize();
    }

    /**
     * Updates the enemy based on the time since the last frame and moves the enemy
     * 
     * @param deltaTime
     */
    public void UpdateEnemy(float deltaTime) {
        lifeTime += deltaTime;
        SpecialAbility(deltaTime);
        moveEnemy(deltaTime);
    }

    /**
     * Moves the enemy based on its speed and direction
     * 
     * @param deltaTime Time since last frame
     */
    protected void moveEnemy(float deltaTime) {
        position.setX(position.getX() + movementDirection.getX() * speed * deltaTime / 1000);
        position.setY(position.getY() + movementDirection.getY() * speed * deltaTime / 1000);

        boolean isOffscreen = position.getX() < 0 - size / 2 || position.getX() > 1920 + size / 2 ||
                position.getY() < 0 - size / 2 || position.getY() > 1080 + size / 2;
        if (isOffscreen) {
            if(!offScreenFromSpawn) {
                offScreenFromMovement = true;
            }
        } else {
            offScreenFromSpawn = false;
        }
    }

    /**
     * Special ability for the enemy - to be overridden by subclasses
     */
    protected void SpecialAbility(float deltaTime) {
    }

    /**
     * @return The time since the enemy was created
     */
    public float getLifeTime() {
        return lifeTime;
    }

    /**
     * @return the speed of the enemy
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @return the position of the enemy
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * @return The size of the enemy
     */
    public float getSize() {
        return size;
    }

    /**
     * @return the color of the enemy in RGB format
     */
    public float[] getColor() {
        return new float[] { color[0] * shade / 255, color[1] * shade / 255, color[2] * shade / 255 };
    }

    public Vector getSizeRange() {
        return sizeRange;
    }

    public float getSizeFromEating() {
        return sizeFromEating;
    }

    public float getScoreFromEating() {
        return scoreFromEating;
    }
    public boolean isOffscreen() {
        boolean alwaysBeenOffscreen = offScreenFromSpawn && lifeTime > 30000;
        return offScreenFromMovement || alwaysBeenOffscreen;
    }
}
