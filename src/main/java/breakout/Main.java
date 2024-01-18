package breakout;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
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

    private static final int NUM_BLOCKS = 10;

    private double ballX = WINDOW_WIDTH / 2;
    private double ballY = WINDOW_HEIGHT / 2;
    private double ballSpeedX = 1;
    private double ballSpeedY = 1.5;
    private int remainingBlocks = NUM_BLOCKS*2;

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
        Paddle paddle = new Paddle(WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2, WINDOW_HEIGHT - PADDLE_HEIGHT - 10,PADDLE_WIDTH,PADDLE_HEIGHT);
        // Create blocks

        for (int i=0; i<NUM_BLOCKS;i++){
            Block block = new Block();
            block.place_block(block, 25*i, WINDOW_HEIGHT/4);
            root.getChildren().add(block);
        }

        for (int i=0; i<NUM_BLOCKS;i++){
            Block block = new Block();
            block.place_block(block,25*i, WINDOW_HEIGHT/2 );
            root.getChildren().add(block);
        }

        root.getChildren().addAll(ball,paddle);

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

            for (Node node : root.getChildren()) {
                if (node instanceof Block && ball.getBoundsInParent().intersects(node.getBoundsInParent())) {
                    root.getChildren().remove(node); // Remove block on collision
                    ballSpeedY = -ballSpeedY; // Reverse Y direction
                    remainingBlocks--;

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
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Handle paddle movement
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                if (paddle.paddle_getX() < 0) {
                    paddle.setTranslateX(WINDOW_WIDTH - PADDLE_WIDTH);
                } else {
                    paddle.setTranslateX(paddle.getTranslateX() - 75);
                }
            } else if (event.getCode() == KeyCode.RIGHT) {
                if (paddle.paddle_getX() > WINDOW_WIDTH - PADDLE_WIDTH) {
                    paddle.setTranslateX(0);
                } else {
                    paddle.setTranslateX(paddle.getTranslateX() + 75);
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


class Block extends Group{

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;


    public Block(){
        Rectangle block = new Rectangle(25,25,Color.RED);
        block.setStroke(Color.GREEN);
        block.setStrokeWidth(5);
        getChildren().add(block);
    }

    public void place_block(Block block, double x_coordinate, double y_coordinate){
        block.setTranslateX(x_coordinate);
        block.setTranslateY(y_coordinate);
    }

}

class Paddle extends Group {

    private Rectangle paddle;

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

    public Paddle(){
         paddle = new Rectangle(50,50,Color.GREEN);
         getChildren().add(paddle);

    }
    public Paddle(double x_coordinate, double y_coordinate,double width,double height){
        paddle = new Rectangle(width,height,Color.GREEN);
        paddle.setX(x_coordinate);
        paddle.setY(y_coordinate);
        getChildren().add(paddle);
    }


    public double paddle_getX() {
        return paddle.getX();
    }

    public double paddle_getY(){
        return paddle.getY();
    }
}





class SpeedBoost extends Group {

    private static final double POWER_UP_RADIUS = 10;

    public SpeedBoost() {
        Circle powerUp = new Circle(POWER_UP_RADIUS, Color.YELLOW);
        getChildren().add(powerUp);
    }
}
