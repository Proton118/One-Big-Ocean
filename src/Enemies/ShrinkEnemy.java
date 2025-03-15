package Enemies;
public class ShrinkEnemy extends Enemy {
    public static final float[] color = new float[] {162, 191, 255};
    public static final Vector sizeRange = new Vector(110, 220);
    public static final Vector shadeRange = new Vector(190, 255);
    public static final float sizeFromEating = -4;
    public ShrinkEnemy(Vector position, float speed, Vector movementDirection) {
        super(position, speed, movementDirection);
        super.color = color;
        super.sizeFromEating = sizeFromEating;
        size = rand.nextFloat(sizeRange.getY() - sizeRange.getX()) + sizeRange.getX();
        shade = rand.nextFloat((int)shadeRange.getY() - (int)shadeRange.getX()) + (int)shadeRange.getX();
    }
    @Override
    protected void SpecialAbility(float deltaTime) {
        size /= 1 + (0.15 * deltaTime / 1000);
        size = Math.max(size, 5);
    }
}
