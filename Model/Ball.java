package com.example.pongstage1.Model;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.Random;

/**
 * Represents a ball in the Pong game.
 */
public class Ball {

    private Circle circle;
    // Horizontal movement of the ball
    private double dx = 1;
    // Vertical movement of the ball
    private double dy = 1;
    private Random random = new Random();
    private int bounceCount = 0;
    // Factor by which speed increases after each bounce
    private final double speedIncreaseFactor = 1.1;

    /**
     * Constructor for creating a Ball object.
     *
     * @param radius The radius of the ball.
     * @param color  The color of the ball.
     */
    public Ball(double radius, Color color) {
        circle = new Circle(radius); // Creating a new Circle object with specified radius which is the ball
        circle.setFill(color); // Setting the color of the ball
        randomizeDirection(); // Randomizing initial direction of ball
    }

    /**
     * Updates the position of the ball based on its current velocity.
     */
    public void updatePosition() {
        circle.setCenterX(circle.getCenterX() + dx); // Updating the x-coordinate of the center of the circle
        circle.setCenterY(circle.getCenterY() + dy); // Updating the y-coordinate of the center of the circle
    }

    /**
     * Randomizes the direction of the ball.
     */
    public void randomizeDirection() {
        // Generate random directions and speeds
        double angle = random.nextDouble() * 2 * Math.PI; // Random angle
        double speed = random.nextDouble() * 5 + 1; // Random speed between 1 and 5
        dx = Math.cos(angle) * speed; // the horizontal movement based on the angle and speed
        dy = Math.sin(angle) * speed; // the vertical movement based on the random angle and speed
    }

    /**
     * Gets the visual representation of the ball.
     *
     * @return The Circle object representing the ball.
     */
    public Circle getVisualRepresentation() {
        return circle;
    }

    /**
     * Gets the horizontal movement of the ball.
     *
     * @return The horizontal movement (dx) of the ball.
     */
    public double getDx() {
        return dx;
    }

    /**
     * Sets the horizontal movement of the ball.
     *
     * @param dx The new horizontal movement (dx) of the ball.
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Gets the vertical movement of the ball.
     *
     * @return The vertical movement (dy) of the ball.
     */
    public double getDy() {
        return dy;
    }

    /**
     * Sets the vertical movement of the ball.
     *
     * @param dy The new vertical movement (dy) of the ball.
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Increases the speed of the ball.
     */
    public void increaseSpeed() {
        dx *= speedIncreaseFactor; // Increasing horizontal speed
        dy *= speedIncreaseFactor; // Increasing vertical speed
    }


    /**
     * Increments the bounce count of the ball and optionally increases its speed every second bounce.
     */
    public void incrementBounceCount() {
        bounceCount++;
        if (bounceCount % 2 == 0) { // checks if the bounce count is even
            increaseSpeed();
        }
    }

    public double getRadius() {
        return circle.getRadius();
    }

}
