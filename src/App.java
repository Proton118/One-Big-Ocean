import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Enemies.ClassicEnemy;
import Enemies.DoomEnemy;
import Enemies.Enemy;
import Enemies.EvilEnemy;
import Enemies.GoldenEnemy;
import Enemies.GrowEnemy;
import Enemies.ShrinkEnemy;
import Enemies.Vector;
import processing.core.*;

public class App extends PApplet {
    public final float screenWidth = displayWidth;
    public final float screenHeight = displayHeight;
    static final Random rand = new Random();
    int backgroundShade = 234;
    float startPlayerSize = 20;
    float playerSize = startPlayerSize;
    final float maxPlayerSize = 100;
    final ArrayList<Enemy> enemies = new ArrayList<>();
    float enemyDelayMillis = 200;
    float enemyDelayArcadeMillis = 400;
    float enemyTimer = 0;
    float goldenDelayMillis = 20000;
    float goldenTimer = 0;
    float doomDelayMillis = 30000;
    float doomTimer = 0;
    float lastTime = 0;
    float deltaTime = 0;
    boolean gameOver = true;
    boolean titleScreen = true;
    GameMode gameMode = GameMode.Classic;
    PFont scoreFont;
    PFont gameOverFont;
    int score = 0;
    int arcadeClears = 0;
    int arcadeDifficultyBar = 0;
    boolean newScreenCutscene = false;
    PImage backArrow;
    boolean mouseWasPressed = false;
    int[] highScores = new int[GameMode.values().length];
    boolean isHighScore = false;
    String highScoreFileName = "src/highScores.dat";
    byte[] AESkey = new byte[]{ 0x4A, 0x6F, 0x68, 0x6E, 0x20, 0x44, 0x6F, 0x65, 0x20, 0x57, 0x61, 0x6C, 0x6B, 0x65, 0x72, 0x21 };

    private enum EnemyType {
        Enemy, ShrinkEnemy, GrowEnemy, EvilEnemy, GoldenEnemy, DoomEnemy, ClassicEnemy
    }

    public enum GameMode {
        Classic, Arcade, // Campaign,
    }

    public static void main(String[] args) {
        PApplet.main("App");
    }

    public void settings() {
        fullScreen();
        pixelDensity(2);
        smooth(16);
    }

    public void setup() {
        background(backgroundShade);
        noCursor();
        scoreFont = createFont("src/Fonts/LondrinaShadow-Regular.ttf", 100);
        gameOverFont = createFont("src/Fonts/ImperialScript-Regular.ttf", 100);
        backArrow = loadImage("Images/BackArrow.png");
        loadHighScores();
    }

    public void draw() {
        background(backgroundShade);
        deltaTime = millis() - lastTime;
        lastTime = millis();

        if (!gameOver) {
            drawPlayer();
        }

        if (!DetectNewScreen()) {
            HandleEnemySpawns(gameMode);
        }
        DrawEnemies();
        ClearOldEnemies();
        if (!gameOver) {
            DetectCollisions();
        } else {
            if (titleScreen) {
                drawTitleScreen();
            } else {
                DrawGameOver();
            }
        }

        DrawScore();
    }

    /**
     * Draws the player to the screen
     */
    public void drawPlayer() {
        noCursor();
        noStroke();
        fill(255);
        circle(mouseX, mouseY, playerSize);
    }

    /**
     * Spawns enemies if the time since the last enemy spawn is greater than the
     * enemy delay
     */
    public void HandleEnemySpawns(GameMode mode) {
        enemyTimer += deltaTime;
        goldenTimer += deltaTime;
        doomTimer += deltaTime;
        switch (mode) {
            case Classic:
                HandleEnemySpawnsClassic();
                break;
            case Arcade:
                HandleEnemySpawnsArcade();
                break;
        }
    }

    /**
     * Spawns classic enemies if the enemyTimer is up;
     */
    public void HandleEnemySpawnsClassic() {
        if (enemyTimer > enemyDelayMillis) {
            enemyTimer = enemyTimer % enemyDelayMillis;
            for (int i = 0; i < 1; i++) {
                InstantiateEnemyType(EnemyType.ClassicEnemy);
            }
        }
    }

    /**
     * Spawns arcade enemies + golden enemy spawns if the timer is up
     */
    public void HandleEnemySpawnsArcade() {
        arcadeDifficultyBar += deltaTime / 1000;
        if (enemyTimer > enemyDelayArcadeMillis * Math.pow(0.97, (arcadeClears + arcadeDifficultyBar / 30))) {
            enemyTimer = enemyTimer % enemyDelayMillis;
            for (int i = 0; i < 1; i++) {
                InstantiateEnemyArcade();
            }
        }
        if (goldenTimer > goldenDelayMillis) {
            goldenTimer = goldenTimer % goldenDelayMillis;
            InstantiateEnemyType(EnemyType.GoldenEnemy);
        }
        if (arcadeClears >= 4) {
            if (doomTimer > doomDelayMillis * Math.pow(0.97, arcadeClears - 4) && !gameOver) {
                doomTimer = doomTimer % doomDelayMillis;
                InstantiateEnemyType(EnemyType.DoomEnemy);
            }
        }
    }

    /**
     * Instantiates a new enemy with a random size, speed, shade, position, and
     * movement direction
     */
    public void InstantiateEnemyArcade() {
        EnemyType enemyType = EnemyType.values()[rand.nextInt(3)];
        if (arcadeClears >= 2) {
            float randomNumber = rand.nextFloat();
            float odds = (float) Math.pow(Math.log(arcadeClears - 2) / Math.log(99), 2);
            if (randomNumber > odds) {
                enemyType = EnemyType.values()[rand.nextInt(4)];
            }
        }
        InstantiateEnemyType(enemyType);
    }

    /**
     * Instantiates a new enemy with a random position of the type specified
     * 
     * @param enemyType 0: Enemy, 1: ShrinkEnemy, 2: GrowEnemy, 3: GoldenEnemy, 4:
     *                  DoomEnemy, ClassicEnemy, EvilEnemy
     */
    public void InstantiateEnemyType(EnemyType enemyType) {
        Vector[] positionDirection = RandomEnemyPositionDirection();
        float speed = random(50, 200);
        switch (enemyType) {
            case Enemy:
                enemies.add(new Enemy(positionDirection[0], speed, positionDirection[1]));
                break;
            case ShrinkEnemy:
                enemies.add(new ShrinkEnemy(positionDirection[0], speed, positionDirection[1]));
                break;
            case GrowEnemy:
                enemies.add(new GrowEnemy(positionDirection[0], speed, positionDirection[1]));
                break;
            case GoldenEnemy:
                enemies.add(new GoldenEnemy(positionDirection[0], positionDirection[1], playerSize,
                        new Vector(width, height)));
                break;
            case DoomEnemy:
                enemies.add(new DoomEnemy(positionDirection[0], positionDirection[1]));
                break;
            case ClassicEnemy:
                enemies.add(new ClassicEnemy(positionDirection[0], speed, positionDirection[1], playerSize));
                break;
            case EvilEnemy:
                enemies.add(new EvilEnemy(positionDirection[0], positionDirection[1]));
                break;
        }
    }

    /**
     * Generates a random position and movement direction for an enemy
     * 
     * @return A vector array with the position (index 0) and movement direction
     *         (index 1)
     */
    public Vector[] RandomEnemyPositionDirection() {
        float directionalDisplacement = 0.5f;
        Vector positionDirection = (new Vector(random(-1, 1), random(-1, 1))).normalize();
        Vector movementDirection = new Vector(
                positionDirection.getX() * -1 + random(-directionalDisplacement, directionalDisplacement),
                positionDirection.getY() * -1 + random(-directionalDisplacement, directionalDisplacement));
        float outerEdgeX = positionDirection.getX() * (width + 300);
        float outerEdgeY = positionDirection.getY() * (width + 300);
        float centeredX = outerEdgeX / 2 + width / 2;
        float centeredY = outerEdgeY / 2 + height / 2;
        Vector position = new Vector(centeredX, centeredY);
        return new Vector[] { position, movementDirection };
    }

    /**
     * Draws all enemies in the enemies array after moving them based on their speed
     * and direction
     */
    public void DrawEnemies() {
        for (Enemy enemy : enemies) {
            enemy.UpdateEnemy(deltaTime);
            drawEnemy(enemy);
        }
    }

    /**
     * Detects collisions and either grows the player or ends the game
     */
    public void DetectCollisions() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);

            float distanceToEnemy = sqrt(pow(enemy.getPosition().getX() - mouseX, 2)
                    + pow(enemy.getPosition().getY() - mouseY, 2));
            boolean collision = distanceToEnemy < playerSize / 2 + enemy.getSize() / 2;
            if (collision) {
                if (enemy.getSize() < playerSize) {
                    enemies.remove(enemy);
                    playerSize += enemy.getSizeFromEating();
                    playerSize = Math.max(playerSize, 5);
                    score += enemy.getScoreFromEating();
                } else {
                    gameOver = true;
                }
            }
        }
    }

    /**
     * Detects if the player has grown to max size so it can zoom out after all
     * enemies are gone
     * 
     * @return if the player has grown to max size
     */
    public boolean DetectNewScreen() {
        if ((playerSize > maxPlayerSize || newScreenCutscene) && !gameOver) {
            newScreenCutscene = true;
            return HandleShrinking();
        }
        return false;
    }

    /**
     * Handles shrinking the player on the screen reset
     * 
     * @return true if the player is still shrinking, false if the player is fully
     *         shrunk
     */
    public boolean HandleShrinking() {
        if (enemies.size() == 0) {
            playerSize -= 33 * deltaTime / 1000;
            if (playerSize < startPlayerSize) {
                playerSize = startPlayerSize;
                newScreenCutscene = false;
                if (gameMode == GameMode.Arcade) {
                    score *= 2;
                    arcadeClears++;
                    arcadeDifficultyBar = 0;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Removes enemies that are likely off screen
     */
    public void ClearOldEnemies() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.isOffscreen()) {
                enemies.remove(enemy);
            }
        }
    }

    /**
     * Draws an enemy to the screen
     * 
     * @param enemy The enemy to draw
     */
    public void drawEnemy(Enemy enemy) {
        noStroke();
        fill(enemy.getColor()[0], enemy.getColor()[1], enemy.getColor()[2]);
        circle(enemy.getPosition().getX(), enemy.getPosition().getY(), enemy.getSize());
    }

    /**
     * Draws the score to the top left corner of the screen
     */
    public void DrawScore() {
        fill(0);
        textSize(100);
        textFont(scoreFont);
        textAlign(LEFT, TOP);
        text((score != 0 ? String.valueOf(score) : ""), 25, 10);
    }

    /**
     * Draws the game over screen
     */
    public void DrawGameOver() {
        if (!isHighScore) {
            isHighScore = updateHighScores(score, gameMode);
            saveHighScores();
        }

        Vector toMenuTopLeft = new Vector(width - backArrow.width, 20);
        Vector toMenuDimensions = new Vector(backArrow.width + 20, backArrow.height + 20);
        newScreenCutscene = false;
        cursor();
        fill(0);
        textSize(100);
        textFont(gameOverFont);
        textAlign(CENTER, CENTER);
        text("Game Over", width / 2, height / 2);
        if (isHighScore) {
            textSize(50);
            text("High Score", width / 2, height / 2 + 100);
        }
        float imgRatio = (float) backArrow.width / backArrow.height;
        doomTimer = 0;
        backArrow.resize(width / 15, (int) (width / 15f / imgRatio));
        image(backArrow, width - backArrow.width - 20, 20);
        boolean toMenuClicked = isInRect(new Vector(mouseX, mouseY), toMenuTopLeft, toMenuDimensions);
        if (toMenuClicked && mouseJustPressed()) {
            titleScreen = true;
        } else if (mouseJustPressed()) {
            gameStart();
        }

    }

    /**
     * Draws the title screen
     */
    public void drawTitleScreen() {
        score = highScores[gameMode.ordinal()];
        Vector modeSwitcherTopLeft = new Vector(width / 2 - 150, height / 2 + 75);
        Vector modeSwitcherDimensions = new Vector(300, 80);
        cursor();
        fill(0);
        textSize(100);
        textFont(gameOverFont);
        textAlign(CENTER, CENTER);
        text("One Big Ocean", width / 2, height / 2);
        textSize(50);
        text(gameMode.toString(), width / 2, height / 2 + 100);

        boolean modeSwitcherClicked = isInRect(new Vector(mouseX, mouseY), modeSwitcherTopLeft, modeSwitcherDimensions);
        if (modeSwitcherClicked && mouseJustPressed()) {
            gameMode = GameMode.values()[(gameMode.ordinal() + 1) % GameMode.values().length];
            enemies.clear();
        } else if (mouseJustPressed()) {
            gameStart();
        }
    }

    /**
     * Starts the game
     */
    public void gameStart() {
        titleScreen = false;
        gameOver = false;
        isHighScore = false;
        enemies.clear();
        playerSize = startPlayerSize;
        score = 0;
        doomTimer = 0;
        goldenTimer = 0;
        enemyTimer = 0;
        arcadeClears = 0;
    }

    /**
     * Checks if a position is in a rectangle
     * 
     * @param position   the position to check
     * @param topLeft    the top left coordinate of the rectangle
     * @param dimensions the dimensions of the rectangle (width, height)
     * @return if the position is in the rectangle
     */
    public boolean isInRect(Vector position, Vector topLeft, Vector dimensions) {
        return position.getX() > topLeft.getX() && position.getX() < topLeft.getX() + dimensions.getX() &&
                position.getY() > topLeft.getY() && position.getY() < topLeft.getY() + dimensions.getY();
    }

    /**
     * Checks if the mouse was just pressed
     * 
     * @return if the mouse was just pressed
     */
    public boolean mouseJustPressed() {
        if (mousePressed && !mouseWasPressed) {
            mouseWasPressed = true;
            return true;
        } else if (!mousePressed) {
            mouseWasPressed = false;
        }
        return false;
    }

    /**
     * Updates the high scores for a given mode
     * 
     * @param score the score of the run
     * @param mode  the mode the score was in
     * @return true if the score was a high score
     */
    public boolean updateHighScores(int score, GameMode mode) {
        if (score > highScores[mode.ordinal()]) {
            highScores[mode.ordinal()] = score;
            return true;
        }
        return false;
    }

    public void saveHighScores() {
        String[] highScoresStrings = new String[highScores.length];
        for (int i = 0; i < highScores.length; i++) {
            highScoresStrings[i] = str(highScores[i]);
        }
        byte[] encryptedHighScores = encryptStrings(join(highScoresStrings, "\n").getBytes());
        saveBytes(highScoreFileName, encryptedHighScores);
    }

    public void loadHighScores() {
        try {
            byte[] highScoresBytes = loadBytes(highScoreFileName);
            byte[] decodedBytes = decryptBytes(highScoresBytes);
            String[] highScoresStrings = new String(decodedBytes).split("\n");
            for (int i = 0; i < highScoresStrings.length; i++) {
                highScores[i] = Integer.valueOf(highScoresStrings[i]);
            }
        } catch (Exception e) {
            highScores = new int[GameMode.values().length];
        }
    }

    public byte[] encryptStrings(byte[] bytes) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AESkey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] decryptBytes(byte[] bytes) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AESkey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    private String getMacAddress() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                byte[] mac = networks.nextElement().getHardwareAddress();
                if (mac != null) {
                    StringBuilder addressString = new StringBuilder();
                    for (byte b : mac) {
                        addressString.append(String.format("%02X", b));
                    }
                    return addressString.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "000000000000";
    }

    private byte[] getAESKeyFromMAC() {
        try {
            String address = getMacAddress();
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] key = sha.digest(address.getBytes("UTF-8"));
            return Arrays.copyOf(key, 16);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[16];
        }
    }

    public void dispose() {
        updateHighScores(score, gameMode);
        saveHighScores();
    }
}