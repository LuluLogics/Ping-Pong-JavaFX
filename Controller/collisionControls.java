package com.example.pongstage1.Controller;

import com.example.pongstage1.Model.Ball;
import com.example.pongstage1.Model.Racket;


/**
 * Class responsible for controlling collisions between game objects.
 */
public class collisionControls {

    // Declaring objects and variables
    private Ball ball;
    private Racket player1Racket;
    private Racket player2Racket;
    private double stageWidth;
    private double stageHeight;

    /**
     * Constructor for initializing CollisionControls objects.
     *
     * @param ball          The ball object.
     * @param player1Racket Racket object representing player 1's racket.
     * @param player2Racket Racket object representing player 2's racket.
     * @param stageWidth    The width of the stage.
     * @param stageHeight   The height of the stage.
     */
    public collisionControls(Ball ball, Racket player1Racket, Racket player2Racket, double stageWidth, double stageHeight) {
        // Initializing objects
        this.ball = ball;
        this.player1Racket = player1Racket;
        this.player2Racket = player2Racket;
        this.stageWidth = stageWidth;
        this.stageHeight = stageHeight;
    }

    /**
     * Method to set the stage width.
     *
     * @param width The width of the stage.
     */
    public void setStageWidth(double width) {
        this.stageWidth = width;
    }

    /**
     * Method to set the stage height.
     *
     * @param height The height of the stage.
     */
    public void setStageHeight(double height) {
        this.stageHeight = height;
    }

    /**
     * Method to check collisions and determine if players scored.
     *
     * @return The collision result containing information about player scores.
     */
    public CollisionResult checkCollisions() {
        CollisionResult result = new CollisionResult();
        double ballMinX = ball.getVisualRepresentation().getCenterX() - ball.getVisualRepresentation().getRadius();
        double ballMaxX = ball.getVisualRepresentation().getCenterX() + ball.getVisualRepresentation().getRadius();
        double ballMinY = ball.getVisualRepresentation().getCenterY() - ball.getVisualRepresentation().getRadius();
        double ballMaxY = ball.getVisualRepresentation().getCenterY() + ball.getVisualRepresentation().getRadius();

        // Collision with top and bottom walls
        if (ballMinY <= 0 || ballMaxY >= stageHeight) {
            ball.setDy(-ball.getDy()); // Invert vertical direction
            if (ballMinY <= 0) {
                ball.getVisualRepresentation().setCenterY(ball.getVisualRepresentation().getRadius()); // Adjust ball position to avoid going out of bounds
            } else {
                ball.getVisualRepresentation().setCenterY(stageHeight - ball.getVisualRepresentation().getRadius()); // Adjust ball position to avoid going out of bounds
            }
        }

        // Collision with left and right walls
        if (ballMinX <= 0 || ballMaxX >= stageWidth) {
            ball.setDx(-ball.getDx()); // Invert horizontal direction
            if (ballMinX <= 0) {
                result.player2Scored = true; // Player 2 scored
            } else if (ballMaxX >= stageWidth) {
                result.player1Scored = true; // Player 1 scored
            }
        }

        // Check collision with rackets
        if (player1Racket.getVisual().getBoundsInParent().intersects(ball.getVisualRepresentation().getBoundsInParent()) ||
                player2Racket.getVisual().getBoundsInParent().intersects(ball.getVisualRepresentation().getBoundsInParent())) {
            ball.setDx(-ball.getDx()); // Invert horizontal direction
        }

        return result;
    }

    /**
     * Class representing the result of collisions.
     */
    public static class CollisionResult {
        // Indicates whether player 1 scored
        public boolean player1Scored = false;
        // Indicates whether player 2 scored.
        public boolean player2Scored = false;

    }

    public boolean player2Scores() {
        return ball.getVisualRepresentation().getCenterX() <= 10;
    }

}
