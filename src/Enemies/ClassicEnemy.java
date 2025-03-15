package Enemies;
import java.util.ArrayList;
import java.util.Arrays;

public class ClassicEnemy extends Enemy {
    private final ArrayList<float[]> classicColors = new ArrayList<>(Arrays.asList(
            new float[] { 255, 255, 255 },
            new float[] { 255, 255, 255 },
            new float[] { 255, 238, 140 },
            new float[] { 255, 105, 97 },
            new float[] { 162, 191, 255 },
            new float[] {246, 141, 93}));
    private final ArrayList<Vector> classicShadeRanges = new ArrayList<>(Arrays.asList(
            new Vector(100, 220),
            new Vector(100, 220),
            new Vector(220, 255),
            new Vector(175, 255),
            new Vector(190, 255),
            new Vector(225, 255)));

    public ClassicEnemy(Vector position, float speed, Vector movementDirection) {
        super(position, speed, movementDirection);
        super.color = color;
        int index = rand.nextInt(classicColors.size());
        color = classicColors.get(index);
        shadeRange = classicShadeRanges.get(index);
        shade = rand.nextFloat((int) shadeRange.getY() - (int) shadeRange.getX()) + (int) shadeRange.getX();
    }
}
