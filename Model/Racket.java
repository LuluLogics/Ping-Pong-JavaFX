package com.example.pongstage1.Model;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.io.Serializable;


/**
 * Represents a racket in the Pong game.
 */
public class Racket implements Serializable {

    private Rectangle visual;
    private double speed;

    /**
     * Constructs a Racket object with specified parameters.
     *
     * @param x      The x-coordinate of the racket.
     * @param y      The y-coordinate of the racket.
     * @param width  The width of the racket.
     * @param height The height of the racket.
     * @param color  The color of the racket.
     */
    public Racket(double x, double y, double width, double height, Color color) {
        visual = new Rectangle(x, y, width, height); // creating a new object
        visual.setFill(color); // color of the Rectangle
        speed = 0; // Initializing speed to 0
    }

    /**
     * Sets the y-coordinate of the racket.
     *
     * @param y The new y-coordinate of the racket.
     */
    public void setY(double y) {
        visual.setY(y);
    }

    /**
     * Gets the y-coordinate of the racket.
     *
     * @return The y-coordinate of the racket.
     */
    public double getY() {
        return visual.getY();
    }

    /**
     * Sets the speed of the racket.
     *
     * @param speed The new speed of the racket.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * Gets the x-coordinate of the racket.
     *
     * @return The x-coordinate of the racket.
     */
    public double getX() {
        return visual.getX();
    }

    /**
     * Gets the width of the racket.
     *
     * @return The width of the racket.
     */
    public double getWidth() {
        return visual.getWidth();
    }

    /**
     * Updates the position of the racket based on its speed.
     */
    public void update() {
        if (visual.getScene() != null) { // checks if the rectangle is attached to a scene
            double newY = getY() + speed; // calculates the new y-coordinate based on speed
            double sceneHeight = visual.getScene().getHeight(); // getting the height of the scene

            newY = Math.max(newY, 0); // Ensuring the new y-coordinate is not below 0
            newY = Math.min(newY, sceneHeight - visual.getHeight()); // Ensuring the racket stays within the scene

            visual.setY(newY); // Sets new y-coordinate of racket
        }
    }

    /**
     * Gets the visual representation (Rectangle) of the racket.
     *
     * @return The visual representation (Rectangle) of the racket.
     */
    public Rectangle getVisual() {
        return visual;
    }
}
