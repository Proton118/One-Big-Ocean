package Enemies;
public class GoldenEnemy extends Enemy {
    public static final float[] color = new float[] { 255, 238, 140 };
    public static final Vector shadeRange = new Vector(220, 255);
    public static final Vector speedRange = new Vector(15f, 20f);
    public static final float sizeFromEating = 0;
    private static final float waveFrequency = 0.02f;
    private static final float waveAmplitude = 50f;
    private final Vector startingPosition;
    private float direction;

    public GoldenEnemy(Vector position, Vector movementDirection, float playerSize, Vector screenSize) {
        super(position, 0, movementDirection);
        super.color = color;
        super.sizeFromEating = sizeFromEating;
        size = playerSize + 3;
        scoreFromEating = 10;
        shade = rand.nextFloat(shadeRange.getY() - shadeRange.getX()) + shadeRange.getX();
        startingPosition = new Vector(position.getX(), position.getY());
        speed = rand.nextFloat(speedRange.getY() - speedRange.getX()) + speedRange.getX();
        direction = startingPosition.directionFromCenter(screenSize) + (float)Math.PI;
    }

    @Override
    protected void moveEnemy(float deltaTime) {
        float distanceToTravel = speed * lifeTime / 100;
        Vector newPosition = pointOnAngledSinWave(distanceToTravel, direction);
        position.setX(newPosition.getX() + startingPosition.getX());
        position.setY(newPosition.getY() + startingPosition.getY());
    }

    private Vector pointOnAngledSinWave(float t, float direction){
        return new Vector((float) (t * Math.cos(direction) - waveAmplitude * Math.sin(waveFrequency * t) * Math.sin(direction)),
                (float) (t * Math.sin(direction) + waveAmplitude * Math.sin(waveFrequency * t) * Math.cos(direction)));
    }
}
