package breakout;

import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.text.Text;

import static javafx.scene.input.KeyCode.*;


/**
 * Feel free to completely change this code or delete it entirely.
 *
 * @author Divyansh Jain
 */

public class Main extends Application {
    // useful names for constant values used

    public Text rulesText;
    public Text startText;
    public Text livesText;
    public Text levelText;
    public int lives_left = 5;
    public static final String TITLE = "Example JavaFX Animation";
    public static final Color DUKE_BLUE = new Color(0, 0.188, 0.529, 1);
    public static final int SIZE = 400;
    public static double BLOCK_WIDTH = 40;
    public static double BLOCK_HEIGHT = 30;

    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 400;
    public static int PADDLE_WIDTH = 60;

    public static final double MAX_BOUNCE_ANGLE = Math.PI / 4.0;
    public static final int PADDLE_HEIGHT = 10;
    public static final int BALL_RADIUS = 3;

    public static final int NUM_BLOCKS = 10;

    public static double ballX = WINDOW_WIDTH / 2;
    public static double ballY = WINDOW_HEIGHT / 2;
    public static double ballSpeedX = 1;
    public static double ballSpeedY = 1.3;
    public static int remainingBlocks = 20;

    public static Paddle paddle;

    public static Circle ball;

    public static boolean ballCaught = false;

    public int currentLevel = 1;

    public Timeline timeline;
    public Scene scene;

    public Group root;

    /**
     * Initialize what will be displayed.
     */

    public void start(Stage stage) {
        root = new Group();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.BLACK);

        stage.setTitle("Breakout Game");
        stage.setScene(scene);
        stage.show();

        showHomeScreen(root, stage);
    }

    public void showHomeScreen(Group root, Stage stage) {
        // Clear the currentRoot group
        root.getChildren().clear();

        // Initialize Text nodes for rules and start message
        rulesText = new Text(
                "Game Rules:\n\n" +
                        "1. Press LEFT and RIGHT arrow keys to move the paddle.\n" +
                        "2. Use the paddle to bounce the ball and break the blocks.\n" +
                        "3. Catch power-ups to gain advantages:\n" +
                        "   - Increase speed\n" +
                        "   - Decrease ball speed\n" +
                        "   - Increase paddle width\n" +
                        "   - Decrease paddle width\n" +
                        "4. Avoid losing all lives.\n" +
                        "5. Different types of blocks:\n" +
                        "   - Blue blocks: Returns ball to paddle\n" +
                        "   - Silver blocks: Drop power-ups to be caught\n" +
                        "   - Lightsalmon blocks: Require 2 hits to be destroyed\n" +
                        "   - Dark sea green blocks: Require 4 hits to be destroyed\n" +
                        "   - Papayawhip blocks: Normal blocks"
        );

        rulesText.setFill(Color.WHITE);
        rulesText.setTranslateX(10);
        rulesText.setTranslateY(50);

        startText = new Text("Press SPACE to start Level 1");
        startText.setFill(Color.WHITE);
        startText.setTranslateX(10);
        startText.setTranslateY(350);

        root.getChildren().addAll(rulesText, startText);

        // Event handler for SPACE key press
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == SPACE) {
                startLevel(root, stage);
            }
        });
    }


    public void startLevel(Group root, Stage stage) {
//        timeline = new Timeline();

        // Create ball
        Circle ball = new Circle(BALL_RADIUS, Color.BLUE);
        ball.setTranslateX(WINDOW_WIDTH / 2 );
        ball.setTranslateY(WINDOW_HEIGHT-PADDLE_HEIGHT-BALL_RADIUS);
        Main.ball = ball;
        // Create paddle
        Main.paddle = new Paddle(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2, WINDOW_HEIGHT - PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
        // Create blocks

        Group currentRoot = new Group();

        // Parse configuration file and create blocks
        List<Block> blocks = ConfigParser.parseConfigFile("C:\\Users\\divya\\IdeaProjects\\projects\\breakout_dj200\\src\\main\\java\\breakout\\level1");
        currentRoot.getChildren().addAll(blocks);

        livesText = new Text("Lives: " + lives_left);
        livesText.setFill(Color.WHITE);
        livesText.setTranslateX(WINDOW_WIDTH-75);
        livesText.setTranslateY(20);

        levelText = new Text("Level: " + currentLevel);
        levelText.setFill(Color.WHITE);
        levelText.setTranslateX(WINDOW_WIDTH-75);
        levelText.setTranslateY(40);



        // Add elements to the current root
        currentRoot.getChildren().addAll(ball, paddle, livesText, levelText);
        updateLivesText();

        // Create a new Scene using the current root
        Scene currentScene = new Scene(currentRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Flag to check if the game has started

        AtomicBoolean gameStarted = new AtomicBoolean(false);

        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {

            if (!gameStarted.get()) {
                return;  // Do nothing if the game hasn't started
            }
            // Update ball position
            if (!Main.ballCaught) {
                ballX += ballSpeedX;
                ballY += ballSpeedY;
                ball.setTranslateX(ballX);
                ball.setTranslateY(ballY);
            }

            // Ball-paddle collision
            paddle.ball_paddle_collision(ball, paddle);

            // Ball-block collision

            for (Node node : currentRoot.getChildren()) {
                if (node instanceof Block && ball.getBoundsInParent().intersects(node.getBoundsInParent())) {
                    Block block = (Block) node;
                    if (block instanceof PowerUpBlock) {
                        // Handle power-up block collision
                        block.takeHit();
                    }
                    else if (block instanceof SpecialBlock) {
                        // Pass the paddle object to takeHit method for SpecialBlock
                        ((SpecialBlock) block).takeHit(paddle);
                    }
                    else{
                        block.takeHit(); // Remove block on collision
                        // Reverse Y direction
                        ballSpeedY = -ballSpeedY;
                    }

                    // Check if all blocks are destroyed
                    if (remainingBlocks == 0) {
                        loadNextLevel(currentRoot, stage);
                        stopGame(stage, "Congratulations");
                    }
                    break; // Break out of loop to avoid concurrent modification exception
                }
            }

            // Bounce off window edges
            if (ballX < 0 || ballX > WINDOW_WIDTH) {
                ballSpeedX = -ballSpeedX; // Reverse X direction
            }

            if (ballY < 0) {
                ballSpeedY = -ballSpeedY; // Reverse Y direction
            }

            // Check if ball hits the bottom of the window
            if (ballY >= WINDOW_HEIGHT) {
//                stopGame(stage, "Game Over. Better Luck Next time.");
                // Check if there are remaining lives
                if (lives_left > 0) {
                    lives_left -= 1;
                    if (lives_left==0){
                        stopGame(stage,"Game over");
                        return;
                    }
                    updateLivesText();

                    // Pause the game temporarily to display a message or perform any other actions
                    timeline.pause();
                    gameStarted.set(false);

                    // Display a message or perform any other actions to indicate life lost

                    // Reset the ball position to the initial position

                    // Resume the game
                } else {
                    // No remaining lives, stop the game
                    stopGame(stage, "Game over");
                    return;
                }
            }

            // Update power-up positions
            List<Node> toRemove = new ArrayList<>();
            for (Node node : currentRoot.getChildren()) {
                if (node instanceof PowerUp) {
                    PowerUp powerUp = (PowerUp) node;
                    powerUp.updatePosition();

                    // Check if power-up intersects with the paddle
                    if (powerUp.intersectsPaddle(paddle)) {

                        powerUp.handlePowerUpCollection(powerUp); // Call the method on the PowerUp instance
                        toRemove.add(powerUp); // Mark for removal
                    }

                    // Check if power-up reaches the bottom
                    if (powerUp.getTranslateY() > WINDOW_HEIGHT) {
                        toRemove.add(powerUp); // Mark for removal
                    }
                }
            }

            // Remove collected and out-of-bounds power-ups
            currentRoot.getChildren().removeAll(toRemove);
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Handle paddle movement
        currentScene.setFill(Color.BLACK);
        currentScene.setOnKeyPressed(event -> {
            KeyCode keyPressed = event.getCode();

            // Idea #1 - Increment life total when L key is pressed
            if (keyPressed == L) {
                lives_left++;
                updateLivesText();
            }

            // Idea #2 - Reset ball and paddle positions when R key is pressed
            else if (keyPressed == R) {
                ballX = WINDOW_WIDTH / 2;
                ballY = WINDOW_HEIGHT - PADDLE_HEIGHT - BALL_RADIUS;
                ball.setTranslateX(ballX);
                ball.setTranslateY(ballY);

                // Create paddle
                Main.paddle.setTranslateX(0);
            }

            else if (keyPressed == F) {
                Main.ballSpeedX *= 1.2;
                Main.ballSpeedY *= 1.2;
            }

            // Idea #3 - Load specific level when number keys (1-9) are pressed
            else if (keyPressed.isDigitKey() && Integer.parseInt(keyPressed.getName()) <= 9) {
                int requestedLevel = Integer.parseInt(keyPressed.getName());
                loadRequestedLevel(requestedLevel, currentRoot, stage);
            }

            // Idea #4 - Reset the game to splash screen when S key is pressed
            else if (keyPressed == S) {
                showHomeScreen(currentRoot, stage);
            }

            else if (keyPressed == P) {
                Main.ballSpeedX *= 0.8;
                Main.ballSpeedY *= 0.8;
            }

            if (event.getCode() == LEFT) {
                paddle.setTranslateX(paddle.getTranslateX() - 20);
            }
            else if (event.getCode() == RIGHT) {
                paddle.setTranslateX(paddle.getTranslateX() + 20);
            }

            if (paddle.getTranslateX() < 0 - (WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2)-PADDLE_WIDTH) {
                paddle.setTranslateX(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2); // Move to the right side
            } else if (paddle.getTranslateX() > (WINDOW_WIDTH/2 - PADDLE_WIDTH/2 + PADDLE_WIDTH)) {
                paddle.setTranslateX(0-(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2)); // Move to the left side
            }

            if (event.getCode() == SPACE) {
                if (!gameStarted.get()) {

                    gameStarted.set(true);  // Set gameStarted to true when space bar is pressed

                    // Set initial position and speed when starting the game
                    ballX = paddle.paddle_getX() + paddle.getTranslateX() + PADDLE_WIDTH / 2;
                    ballY = WINDOW_HEIGHT - PADDLE_HEIGHT - BALL_RADIUS;
                    Main.ball.setTranslateX(ballX);
                    Main.ball.setTranslateY(ballY);

                    // Start the game loop
                    timeline.play();
                } else {
                    // Reset the flag to false when space bar is pressed again
                    Main.ballCaught = false;
                    ballX = paddle.paddle_getX() + paddle.getTranslateX() + PADDLE_WIDTH / 2;
                    ballY = WINDOW_HEIGHT - PADDLE_HEIGHT - BALL_RADIUS;
                    Main.ball.setTranslateX(ballX);
                    Main.ball.setTranslateY(ballY);
                    timeline.play();
                }
            }

        });




        stage.setTitle("Bouncing Ball Game");
        stage.setScene(currentScene);
        stage.show();
    }

    public void updateLivesText() {
        livesText.setText("Lives: " + lives_left);
    }

    private void loadRequestedLevel(int requestedLevel, Group currentRoot, Stage stage) {

        // Example: Load level 1
        if (requestedLevel == 1) {
            // Reset game state and load level 1
            currentLevel = 0;
            Main.paddle.setTranslateX(0);
            resetGame();
            loadNextLevel(currentRoot, stage);
        }

        else if (requestedLevel == 2) {
            // Reset game state and load level 1
            currentLevel = 1;
            Main.paddle.setTranslateX(0);
            resetGame();
            loadNextLevel(currentRoot, stage);
        }

        else {
            currentLevel = 2;
            Main.paddle.setTranslateX(0);
            resetGame();
            loadNextLevel(currentRoot, stage);
        }

    }

    public void displayGameOverScreen(Stage stage, String message) {
        Group gameOverRoot = new Group();
        Text gameOverText = new Text(message);
        gameOverText.setFill(Color.WHITE);
        gameOverText.setTranslateX(50);
        gameOverText.setTranslateY(200);
        gameOverRoot.getChildren().add(gameOverText);

        Scene gameOverScene = new Scene(gameOverRoot, WINDOW_WIDTH, WINDOW_HEIGHT, Color.BLACK);
        stage.setScene(gameOverScene);
        stage.show();

    }

    public void displayCongratulationsScreen(Stage stage, String message) {
        Group Congrats = new Group();
        Text congrats_text = new Text(message);
        congrats_text.setFill(Color.WHITE);
        congrats_text.setTranslateX(50);
        congrats_text.setTranslateY(200);
        Congrats.getChildren().add(congrats_text);

        Scene Congratsscene = new Scene(Congrats, WINDOW_WIDTH, WINDOW_HEIGHT, Color.BLACK);
        stage.setScene(Congratsscene);
        stage.show();

    }

    public void stopGame(Stage primaryStage, String message) {
        // Stop the game and display the message
            if (message.contains("Game over")) {
                displayGameOverScreen(primaryStage, message);
            }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void loadNextLevel(Group currentRoot, Stage stage) {
        // Increment the current level
        currentLevel++;
        if (currentLevel==4){
            displayCongratulationsScreen(stage, "Congratulations");
            return;
        }

        levelText.setText("Level: " + currentLevel);

        resetGame();

        timeline.stop();


            // Try to load the configuration file for the next level
            List<Block> newBlocks = ConfigParser.parseConfigFile("C:\\Users\\divya\\IdeaProjects\\projects\\breakout_dj200\\src\\main\\java\\breakout\\level" + String.valueOf(currentLevel));

        // Update remainingBlocks based on the total number of blocks
            remainingBlocks = newBlocks.size();

        // Clear the root group
            currentRoot.getChildren().clear();

            // Add the new blocks to the root group
            currentRoot.getChildren().addAll(newBlocks);

            // Add the paddle and ball back to the root group
            currentRoot.getChildren().addAll(Main.ball, Main.paddle);

            // Reset other game state variables if needed
            // Implement a method to reset other game state variables

            currentRoot.getChildren().addAll(livesText, levelText);

    }

    private void resetGame() {
        // Reset game state variables to initial values
        Main.ballCaught = false;
        Main.ballX = WINDOW_WIDTH / 2;
        Main.ballY = WINDOW_HEIGHT - PADDLE_HEIGHT - BALL_RADIUS;
        Main.ball.setTranslateX(Main.ballX);
        Main.ball.setTranslateY(Main.ballY);
        Main.ballSpeedX = 1;
        Main.ballSpeedY = 1.3;
        Main.remainingBlocks = 40; // configuring for first level; automatic for all other levels
    }
}

class ConfigParser {
    public static List<Block> parseConfigFile(String filePath) {
        List<Block> blocks = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.split(" ");

                for (int col = 0; col < tokens.length; col++) {
                    int blockType = Integer.parseInt(tokens[col]);
                    Block block = createBlock(blockType);
                    block.place_block(block, col * Main.BLOCK_WIDTH, row * Main.BLOCK_HEIGHT);
                    blocks.add(block);
                }

                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return blocks;
    }

    private static Block createBlock(int blockType) {
        switch (blockType) {
            case 0:
                return new NormalBlock();
            case 1:
                return new MultiHitBlock(2);
            case 2:
                return new MultiHitBlock(4);
            case 3:
                return new SpecialBlock();
            case 4:
                return new PowerUpBlock();
            default:
                return new NormalBlock(); // Default to NormalBlock if type is unknown
        }
    }
}


class Block extends Group {
    private int hitsRequired;
    private int health;
    private boolean isSpecial;
    private boolean isPowerUp;

    private Color color;

    public Block(int hitsRequired, boolean isSpecial, boolean isPowerUp, Color color) {
        this.hitsRequired = hitsRequired;
        this.health = hitsRequired;
        this.isSpecial = isSpecial;
        this.isPowerUp = isPowerUp;
        this.color = color;

        Rectangle block = new Rectangle(Main.BLOCK_WIDTH, Main.BLOCK_HEIGHT, color);
        block.setStroke(Color.GREEN);
        block.setStrokeWidth(5);
        getChildren().add(block);
    }

    public void place_block(Block block, double x_coordinate, double y_coordinate) {
        block.setTranslateX(x_coordinate);
        block.setTranslateY(y_coordinate);
    }

    public void takeHit() {
        health--;

        if (health <= 0) {
            getChildren().clear();
            // Additional logic for multi-hit block destruction
            Main.remainingBlocks -= 1;
        }
    }
}

class Paddle extends Group {
    public Rectangle paddle;

    public Paddle() {
        paddle = new Rectangle(50, 50, Color.GREEN);
        getChildren().add(paddle);
    }

    public Paddle(double x_coordinate, double y_coordinate, double width, double height) {
        paddle = new Rectangle(width, height, Color.GREEN);
        paddle.setX(x_coordinate);
        paddle.setY(y_coordinate);
        getChildren().add(paddle);
    }

    public double paddle_getX() {
        return paddle.getX();
    }

    public double paddle_getY() {
        return paddle.getY();
    }

    public void check_side_switch() {
        if (paddle.getTranslateX() < 0 - (Main.WINDOW_WIDTH / 2 - Main.PADDLE_WIDTH / 2) - Main.PADDLE_WIDTH) {
            paddle.setTranslateX(Main.WINDOW_WIDTH / 2 - Main.PADDLE_WIDTH / 2); // Move to the right side
        } else if (paddle.getTranslateX() > (Main.WINDOW_WIDTH / 2 - Main.PADDLE_WIDTH / 2 + Main.PADDLE_WIDTH)) {
            paddle.setTranslateX(0 - (Main.WINDOW_WIDTH / 2 - Main.PADDLE_WIDTH / 2)); // Move to the left side
        }
    }

    public void ball_paddle_collision(Circle ball, Paddle paddle) {
        if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
            double paddleThird = Main.PADDLE_WIDTH / 3.0;
            double ballCenterX = ball.getTranslateX();

            // Check if the ball's center is in the left third
            if (ballCenterX < paddle.getBoundsInParent().getMinX() + paddleThird) {
                Main.ballSpeedX = -Math.abs(Main.ballSpeedX); // Move left
                Main.ballSpeedY = -Main.ballSpeedY;
            }
            // Check if the ball's center is in the right third
            else if (ballCenterX > paddle.getBoundsInParent().getMinX() + 2 * paddleThird) {
                Main.ballSpeedX = Math.abs(Main.ballSpeedX); // Move right
                Main.ballSpeedY = -Main.ballSpeedY;
            }
            // Middle section, reflect normally
            else {
                Main.ballSpeedY = -Main.ballSpeedY; // Reverse Y direction
            }
        }
    }
}
class NormalBlock extends Block {
    public NormalBlock() {
        super(1, false, false,  Color.PAPAYAWHIP);
    }
}

class MultiHitBlock extends Block {

    public MultiHitBlock(int hitsRequired) {
        super(hitsRequired, false, false,  getColorForType(hitsRequired));
    }

    private static Color getColorForType(int hitsRequired) {
        if (hitsRequired == 2) {
            return Color.LIGHTSALMON;
        } else {
            return Color.DARKSEAGREEN;
        }
    }
}


class SpecialBlock extends Block {
    public SpecialBlock() {
        super(2, true, false,  Color.BLUE);
    }

    public void takeHit(Paddle paddle) {
        super.takeHit();

        // Set the flag to indicate that the ball is caught
        Main.ballCaught = true;

        // Reset ball position to paddle
        Main.ball.setTranslateX(paddle.paddle_getX() + paddle.getTranslateX() + Main.PADDLE_WIDTH / 2);
        Main.ball.setTranslateY(Main.WINDOW_HEIGHT - Main.PADDLE_HEIGHT - Main.BALL_RADIUS);

        // Resume ball movement
        Main.ballSpeedX = 1;
        Main.ballSpeedY = -1.3;
    }
}

class PowerUpBlock extends Block {
    public PowerUpBlock() {
        super(3, false, true,  Color.SILVER);
    }

    @Override
    public void takeHit() {
        super.takeHit();

        // Generate a power-up at the block's position
        PowerUp powerUp = new PowerUp();
        powerUp.placePowerUp(this.getTranslateX(), this.getTranslateY());

        // Add the power-up to the root group
        ((Group) this.getParent()).getChildren().add(powerUp);
    }
}

class PowerUp extends Group {
    private static final double FALL_SPEED = 0.75; // Adjust the fall speed as needed
    private Circle powerUp;

    private double POWER_UP_RADIUS = 6;

    public Color color;

    public PowerUp() {
        powerUp = new Circle(POWER_UP_RADIUS, getcolor());
        getChildren().add(powerUp);
    }

    public Color getcolor(){
        Random random = new Random();

        // Generate a random number between 0 (inclusive) and 4 (exclusive)
        int randomNumber = random.nextInt(4);

        if ((randomNumber)%4==0){
            color=Color.YELLOW;
        }
        else if ((randomNumber)%4==1){
            color=Color.MAGENTA;
        }
        else if ((randomNumber)%4==2){
            color=Color.HONEYDEW;
        }
        else{
            color=Color.FUCHSIA;
        }
        return color;
    }

    public void updatePosition() {
        powerUp.setTranslateY(powerUp.getTranslateY() + FALL_SPEED);
    }

    public void placePowerUp(double x_coordinate, double y_coordinate) {
        powerUp.setTranslateX(x_coordinate);
        powerUp.setTranslateY(y_coordinate);
    }

    public boolean intersectsPaddle(Paddle paddle) {
        return powerUp.getBoundsInParent().intersects(paddle.getBoundsInParent());
    }
    public void handlePowerUpCollection(PowerUp powerUp) {

        if (color == Color.YELLOW){
            Main.PADDLE_WIDTH+=30;
            Main.paddle.paddle.setWidth(Main.PADDLE_WIDTH);
        }

        if (color == Color.MAGENTA){
            Main.ballSpeedY *= 0.9;
            Main.ballSpeedX *= 0.9;
        }

        if (color == color.HONEYDEW){
            Main.ballSpeedX *= 1.2;
            Main.ballSpeedX *= 1.2;
        }

        if (color == color.FUCHSIA){
            Main.PADDLE_WIDTH -= 15;
            Main.ballSpeedX -= 15;
        }
    }
}