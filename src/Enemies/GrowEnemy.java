package Enemies;
public class GrowEnemy extends Enemy {
    public static final float[] color = new float[] {255, 105, 97};
    public static final Vector sizeRange = new Vector(5, 30);
    public static final Vector shadeRange = new Vector(175, 255);
    public static final float sizeFromEating = 4;
    public GrowEnemy(Vector position, float speed, Vector movementDirection) {
        super(position, speed, movementDirection);
        super.color = color;
        super.sizeFromEating = sizeFromEating;
        size = rand.nextFloat(sizeRange.getY() - sizeRange.getX()) + sizeRange.getX();
        shade = rand.nextFloat(shadeRange.getY() - shadeRange.getX()) + shadeRange.getX();
    }
    @Override
    protected void SpecialAbility(float deltaTime) {
        size *= 1 + (0.25 * deltaTime / 1000);
        size = Math.min(size, 250);
    }
}
