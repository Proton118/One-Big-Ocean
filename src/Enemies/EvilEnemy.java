package Enemies;
public class EvilEnemy extends Enemy {
    public static final float[] color = new float[] {246, 141, 93};
    public static final Vector shadeRange = new Vector(225, 255);
    public static final Vector speedRange = new Vector(30f, 100f);
    public static final Vector sizeRange = new Vector(5, 60);
    public static final float scoreFromEating = -2;

    public EvilEnemy(Vector position, Vector movementDirection) {
        super(position, 0, movementDirection);
        super.color = color;
        super.scoreFromEating = scoreFromEating;
        size = rand.nextFloat(sizeRange.getY() - sizeRange.getX()) + sizeRange.getX();
        shade = rand.nextFloat(shadeRange.getY() - shadeRange.getX()) + shadeRange.getX();
        speed = rand.nextFloat(speedRange.getY() - speedRange.getX()) + speedRange.getX();
    }
}
