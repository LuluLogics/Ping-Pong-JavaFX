package com.example.pongstage1.Model;

/**
 * Represents the state of a game, including player names, scores, and the game limit.
 */
public class Game {
    private final String gameName;
    private final String player1Name;
    private final String player2Name;
    private final int player1Score;
    private final int player2Score;
    private final int gameLimit;

    // Private constructor used by the builder
    private Game(Builder builder) {
        this.gameName = builder.gameName;
        this.player1Name = builder.player1Name;
        this.player2Name = builder.player2Name;
        this.player1Score = builder.player1Score;
        this.player2Score = builder.player2Score;
        this.gameLimit = builder.gameLimit;
    }

    // Getters
    public String getName() {
        return gameName;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getGameLimit() {
        return gameLimit;
    }


    /**
     * The Builder class for constructing a Game instance.
     */
    public static class Builder {
        private String gameName;
        private String player1Name;
        private String player2Name;
        private int player1Score;
        private int player2Score;
        private int gameLimit;

        public Builder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public Builder setPlayer1Name(String player1Name) {
            this.player1Name = player1Name;
            return this;
        }

        public Builder setPlayer2Name(String player2Name) {
            this.player2Name = player2Name;
            return this;
        }

        public Builder setPlayer1Score(int player1Score) {
            this.player1Score = player1Score;
            return this;
        }

        public Builder setPlayer2Score(int player2Score) {
            this.player2Score = player2Score;
            return this;
        }

        public Builder setGameLimit(int gameLimit) {
            this.gameLimit = gameLimit;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }
}
