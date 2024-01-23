package breakout;

import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

import   javafx.animation.Animation;
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


/**
 * Feel free to completely change this code or delete it entirely.
 *
 * @author Divyansh Jain
 */

public class Main extends Application {
    // useful names for constant values used
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
    public static int remainingBlocks = 100;

    public static Paddle paddle;

    public static Circle ball;

    public static boolean ballCaught = false;

    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start (Stage stage) {

        Group root = new Group();

        // Create ball
        Circle ball = new Circle(BALL_RADIUS, Color.BLUE);
        ball.setTranslateX(WINDOW_WIDTH / 2 );
        ball.setTranslateY(WINDOW_HEIGHT-PADDLE_HEIGHT-BALL_RADIUS);
        Main.ball = ball;
        // Create paddle


        Main.paddle = new Paddle(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2, WINDOW_HEIGHT - PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
        // Create blocks

        // Parse configuration file and create blocks
        List<Block> blocks = ConfigParser.parseConfigFile("C:\\Users\\divya\\IdeaProjects\\projects\\breakout_dj200\\src\\main\\java\\breakout\\level1");
        root.getChildren().addAll(blocks);
        root.getChildren().addAll(ball,paddle);

        // Flag to check if the game has started
        AtomicBoolean gameStarted = new AtomicBoolean(false);


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {

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

            for (Node node : root.getChildren()) {
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
                        stopGame(stage, "Congratulations! You won!");
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
            if (ballY > paddle.paddle_getY()+10) {
                stopGame(stage, "Game Over. Better Luck Next time.");
            }


        // Update power-up positions
        List<Node> toRemove = new ArrayList<>();
            for (Node node : root.getChildren()) {
                if (node instanceof PowerUp) {
                    PowerUp powerUp = (PowerUp) node;
                    powerUp.updatePosition();

                    // Check if power-up intersects with the paddle
                    if (powerUp.intersectsPaddle(paddle)) {
                        System.out.println("hiii");
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
        root.getChildren().removeAll(toRemove);
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Handle paddle movement
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                    paddle.setTranslateX(paddle.getTranslateX() - 20);
            }
            else if (event.getCode() == KeyCode.RIGHT) {
                    paddle.setTranslateX(paddle.getTranslateX() + 20);
            }

            if (paddle.getTranslateX() < 0 - (WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2)-PADDLE_WIDTH) {
                paddle.setTranslateX(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2); // Move to the right side
            } else if (paddle.getTranslateX() > (WINDOW_WIDTH/2 - PADDLE_WIDTH/2 + PADDLE_WIDTH)) {
                paddle.setTranslateX(0-(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2)); // Move to the left side
            }

            if (event.getCode() == KeyCode.SPACE) {
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
                }
            }

        });


        System.out.println(paddle.paddle_getX());
        System.out.println(ball.getCenterY());


        stage.setTitle("Bouncing Ball Game");
        stage.setScene(scene);
        stage.show();
    }
            private void stopGame(Stage primaryStage, String message) {
                // Stop the game and display the message
                primaryStage.close();
                System.out.println(message);
            }

            public static void main(String[] args) {
                launch(args);
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
            case 5:
                return new ExplodingBlock();
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
    private boolean isExploding;
    private Color color;

    public Block(int hitsRequired, boolean isSpecial, boolean isPowerUp, boolean isExploding, Color color) {
        this.hitsRequired = hitsRequired;
        this.health = hitsRequired;
        this.isSpecial = isSpecial;
        this.isPowerUp = isPowerUp;
        this.isExploding = isExploding;
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
        super(1, false, false, false, Color.SIENNA);
    }
}

class MultiHitBlock extends Block {

    public MultiHitBlock(int hitsRequired) {
        super(hitsRequired, false, false, false, getColorForType(hitsRequired));
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
        super(2, true, false, false, Color.BLUE);
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
        super(3, false, true, false, Color.SILVER);
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
class ExplodingBlock extends Block {
    public ExplodingBlock() {
        super(4, false, false, true, Color.YELLOW);
    }

    @Override
    public void takeHit() {
        super.takeHit();
        // Implement logic to destroy or damage neighboring blocks
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
            System.out.println("width increase");
            Main.PADDLE_WIDTH+=30;
            Main.paddle.paddle.setWidth(Main.PADDLE_WIDTH);
        }

        if (color == Color.MAGENTA){
            System.out.println("width increase");
            Main.PADDLE_WIDTH+=30;
            Main.paddle.paddle.setWidth(Main.PADDLE_WIDTH);
        }

//        if (color == color.HONEYDEW){
//            Main.ballSpeedY -= 0.5;
//            Main.ballSpeedX -= 0.5;
//        }

//        if (color == color.FUCHSIA){
//            Main.ballSpeedY -= 0.5;
//            Main.ballSpeedX -= 0.5;
//        }

    }
}