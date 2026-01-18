package com.example.pongstage1.Controller;

import com.example.pongstage1.Model.Racket;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class to handle game controls.
 */
public class gameControls {

    // Declaring a racket objects and speed
    private Racket player1Racket;
    private Racket player2Racket;
    private final double RACKET_SPEED;

    /**
     * Initializes game controls with player rackets and racket speed.
     *
     * @param player1Racket The racket object for player 1.
     * @param player2Racket The racket object for player 2.
     * @param racketSpeed   The speed of the rackets.
     */
    // initializing controls object with player rackets and racket speed
    public gameControls(Racket player1Racket, Racket player2Racket, double racketSpeed) {
        this.player1Racket = player1Racket;
        this.player2Racket = player2Racket;
        this.RACKET_SPEED = racketSpeed;
    }

    /**
     * Handles key press events to control player rackets.
     *
     * @param event The KeyEvent object representing the key press event.
     */
    // method to handle key press events
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.W) { // if 'W' key is pressed player 1 racket goes up
            player1Racket.setSpeed(-RACKET_SPEED);
        } else if (event.getCode() == KeyCode.S) { // if 'S' key is pressed player 1 racket goes down
            player1Racket.setSpeed(RACKET_SPEED);
        } else if (event.getCode() == KeyCode.UP) { // if 'UP' arrow key is pressed player 2  racket goes up
            player2Racket.setSpeed(-RACKET_SPEED);
        } else if (event.getCode() == KeyCode.DOWN) { // if 'UP' arrow key is pressed player 2 racket goes down
            player2Racket.setSpeed(RACKET_SPEED);
        }
    }

    /**
     * Handles key release events to stop player rackets.
     *
     * @param event The KeyEvent object representing the key release event.
     */
    // method to handle key release events
    public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.S) { // if 'W' or 'S' key is released
            player1Racket.setSpeed(0); // stops player 1's racket
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) { // if 'UP' or 'DOWN' arrow key is released
            player2Racket.setSpeed(0); // stops player 2's racket
        }
    }
}
