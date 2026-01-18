package com.example.pongstage1.Model;

/**
 * Represents a player in the Pong game.
 */
public class Player {

    // declaring objects and variables

    /** The name of the player. */
    private String name;

    /** The score of the player. */
    private int score;

    /** The racket of the player. */
    private Racket racket;

    /**
     * Constructs a player object with a specified name and racket.
     *
     * @param name   The name of the player.
     * @param racket The racket of the player.
     */
    public Player(String name, Racket racket) {
        this.name = name; // player's name
        this.score = 0; // starting players score to 0
        this.racket = racket; // initializing players racket
    }

    /**
     * Increments the player's score by one.
     */
    public void incrementScore() {
        this.score++;
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name The new name of the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the score of the player.
     *
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the player.
     *
     * @param score The new score of the player.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the racket of the player.
     *
     * @return The racket of the player.
     */
    public Racket getRacket() {
        return racket;
    }
}
