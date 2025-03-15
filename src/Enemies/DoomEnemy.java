package Enemies;
public class DoomEnemy extends Enemy {
    public static final float[] color = new float[] { 0, 0, 0 };
    public static final Vector sizeRange = new Vector(2, 4);
    public static final Vector speedRange = new Vector(10f, 18f);
    private boolean isShrinking = false;

    public DoomEnemy(Vector position, Vector movementDirection) {
        super(position, 0, movementDirection);
        super.color = color;
        speed = rand.nextFloat(speedRange.getY() - speedRange.getX()) + speedRange.getX();
        size = rand.nextFloat(sizeRange.getY() - sizeRange.getX()) + sizeRange.getX();
    }

    @Override
    protected void SpecialAbility(float deltaTime) {
        sizeFromEating = size / 2;

        if(offScreenFromSpawn){
            return;
        }
        if (size < 3000 && !isShrinking) {
            size *= 1 + (0.15 * deltaTime / 1000);
        } else {
            isShrinking = true;
            size *= 1 - (0.5 * deltaTime / 1000);
        }
    }
}