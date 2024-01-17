package breakout;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
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

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_RADIUS = 10;

    private double ballX = WINDOW_WIDTH / 2;
    private double ballY = WINDOW_HEIGHT / 2;
    private double ballSpeedX = 2;
    private double ballSpeedY = 1;

    private int remainingBlocks = 20;

    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start (Stage stage) {


        Group root = new Group();

        // Create ball
        Circle ball = new Circle(BALL_RADIUS, Color.BLUE);
        ball.setTranslateX(ballX);
        ball.setTranslateY(ballY);

        // Create paddle
        Rectangle paddle = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT, Color.GREEN);
        paddle.setX(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2);
        paddle.setY(WINDOW_HEIGHT - PADDLE_HEIGHT - 10);

        // Create block
        Rectangle block = new Rectangle(50, 20, Color.RED);
        block.setTranslateX(WINDOW_WIDTH / 4);
        block.setTranslateY(WINDOW_HEIGHT / 4);

        root.getChildren().addAll(ball, paddle, block);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            // Update ball position
            ballX += ballSpeedX;
            ballY += ballSpeedY;
            ball.setTranslateX(ballX);
            ball.setTranslateY(ballY);

            // Ball-paddle collision
            if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
                ballSpeedY = -ballSpeedY; // Reverse Y direction
            }

            // Ball-block collision
            if (ball.getBoundsInParent().intersects(block.getBoundsInParent())) {
                root.getChildren().remove(block); // Remove block on collision
                ballSpeedY = -ballSpeedY; // Reverse Y direction
                remainingBlocks--;

                // Check if all blocks are destroyed
                if (remainingBlocks == 0) {
                    stopGame(stage, "Congratulations! You won!");
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
            if (ballY > paddle.getY()+10) {
                stopGame(stage, "Game Over. You lost.");
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Handle paddle movement
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                paddle.setTranslateX(paddle.getTranslateX() - 20);
            } else if (event.getCode() == KeyCode.RIGHT) {
                paddle.setTranslateX(paddle.getTranslateX() + 20);
            }
        });

        System.out.println(paddle.getY());
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


