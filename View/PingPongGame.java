package com.example.pongstage1.View;

// Import statements
import com.example.pongstage1.Model.Ball;
import com.example.pongstage1.Model.DatabaseConnection;
import com.example.pongstage1.Model.Game;
import com.example.pongstage1.Model.Racket;
import com.example.pongstage1.Controller.gameControls;
import com.example.pongstage1.Controller.collisionControls;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;


/**
 * The main class for the Ping Pong game application.
 */

public class PingPongGame extends Application {

    // Instance variables

    private TextField player1NameInput;
    private TextField player2NameInput;
    private Text player1Score;
    private Text player2Score;
    private Slider ballSpeedSlider;
    private Slider racketSizeSlider;
    private Slider maxScoreSlider;
    private Button startGameButton;
    private Button exitButton;
    private Scene setupScene;
    private Scene gameScene;
    private Stage primaryStage;
    private StackPane root;
    private Label goalMessageLabel;
    private Group gameComponents;
    private Ball ball;
    private collisionControls collisionControls;
    private AnimationTimer gameLoop;

    private int player1ScoreValue = 0;
    private int player2ScoreValue = 0;
    private Racket player1Racket, player2Racket;
    private static final double RACKET_SPEED = 5;
    final double OFFSET_FROM_SIDE = 2;
    private StackPane pauseMenu;
    private boolean isPaused = false;




    /**
     * Sets up the pause menu UI components and behavior.
     */

    private void setupPauseMenu() {
        pauseMenu = new StackPane();
        VBox menuItems = new VBox(10);
        menuItems.setAlignment(Pos.CENTER);

        // Create buttons for the pause menu
        Button resumeButton = createButton("Resume");
        resumeButton.setOnAction(event -> togglePause());

        Button restartButton = createButton("Restart");
        restartButton.setOnAction(event -> {
            togglePause();
            resetGame();
        });

        Button exitButton = createButton("Exit");
        exitButton.setOnAction(event -> primaryStage.close());

        Button saveGameButton = createButton("Save Game");
        saveGameButton.setOnAction(event -> saveGame());

        Button loadGameButton = createButton("Load Game");
        loadGameButton.setOnAction(event -> loadGame());

        Button saveDbButton = createButton("Save to Database");
        saveDbButton.setOnAction(event -> {
            // asks the user for a game name and save the game
            TextInputDialog dialog = new TextInputDialog("DefaultGameName");
            dialog.setTitle("Save Game");
            dialog.setHeaderText("Save Game to Database");
            dialog.setContentText("Please enter a name for your game:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(gameName -> saveGameToDatabase(gameName));
        });
        menuItems.getChildren().add(saveDbButton);

        Button loadDbButton =  createButton("Load Database");
        loadDbButton.setOnAction(event -> loadGameFromDatabase());


        final double maxWidth = 150;
        resumeButton.setMaxWidth(maxWidth);
        restartButton.setMaxWidth(maxWidth);
        exitButton.setMaxWidth(maxWidth);
        saveGameButton.setMaxWidth(maxWidth);
        loadGameButton.setMaxWidth(maxWidth);
        saveDbButton.setMaxWidth(maxWidth);
        loadDbButton.setMaxWidth(maxWidth);

        menuItems.getChildren().addAll(resumeButton, restartButton, saveGameButton, loadGameButton, exitButton, loadDbButton);
        pauseMenu.getChildren().add(menuItems);
        pauseMenu.setVisible(false);
        pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        pauseMenu.setAlignment(Pos.CENTER);
        root.getChildren().add(pauseMenu);

    }


    /**
     * Toggles the game between paused and running states.
     */


    private void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);

        // If the game is now paused, we stop any ongoing animation or game logic
        if (isPaused) {
            System.out.println("Game Paused");
            gameLoop.stop(); // Stops the game loop to pause the game
        } else { // Resumes loop
            System.out.println("Game Resumed");
            gameLoop.start(); // Start the game loop to resume the game
            gameScene.getRoot().requestFocus(); // focus to receive key inputs
        }
    }

    /**
     * Resets the game logic, scores, and entities
     */

    private void resetGame() {
        player1ScoreValue = 0;
        player2ScoreValue = 0;
        updateScoreAndResetBall(true);

        pauseMenu.setVisible(false); // Hides pause menu
        isPaused = false; // Set game state to running
    }


    /**
     * Sets up keyboard controls for pausing the game
     */    private void setupControls() {
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
            }
        });
    }




    /**
     * Method representing the setup and control logic for the game, including menu design, labels, buttons,
     * updating scores, displaying scores, and determining the winners
     */
    @Override
    public void start(Stage primaryStage) {

        // Initializing stage
        this.primaryStage = primaryStage;
        root = new StackPane();

        setupGoalMessageLabel();

        gameComponents = new Group();
        setupScoreDisplay();



        // Setting up start menu
        GridPane setupLayout = new GridPane();
        setupLayout.setAlignment(Pos.CENTER);
        setupLayout.setPadding(new Insets(20));
        setupLayout.setVgap(10);
        setupLayout.setHgap(10);

        setupLayout.setStyle("-fx-background-color: #333;");

        // Setting up game menu and getting inputs
        player1NameInput = createTextField("Player 1");
        player2NameInput = createTextField("Player 2");
        ballSpeedSlider = createSlider(1, 10, 5);
        racketSizeSlider = createSlider(10, 100, 30);
        maxScoreSlider = createSlider(1, 10, 5);

        startGameButton = createButton("Start Game");
        startGameButton.setOnAction(event -> startGame());

        exitButton = createButton("Exit");
        exitButton.setOnAction(event -> primaryStage.close());

        setupLayout.add(createLabel("Player 1 Name:"), 0, 0);
        setupLayout.add(player1NameInput, 1, 0);
        setupLayout.add(createLabel("Player 2 Name:"), 0, 1);
        setupLayout.add(player2NameInput, 1, 1);
        setupLayout.add(createLabel("Ball Speed:"), 0, 2);
        setupLayout.add(ballSpeedSlider, 1, 2);
        setupLayout.add(createLabel("Racket Size:"), 0, 3);
        setupLayout.add(racketSizeSlider, 1, 3);
        setupLayout.add(createLabel("Max Score:"), 0, 4);
        setupLayout.add(maxScoreSlider, 1, 4);

        VBox buttonBox = new VBox(10, startGameButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        setupLayout.add(buttonBox, 1, 5);

        // Loads up menu scene
        setupScene = new Scene(setupLayout, 400, 400);
        primaryStage.setScene(setupScene);
        primaryStage.setTitle("Ping Pong Game Setup");
        primaryStage.show();
    }


    // Designs for the menu
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 16));
        label.setTextFill(Color.WHITE);
        return label;
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setMaxWidth(200);
        return textField;
    }

    private Slider createSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);
        return slider;
    }


    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 16));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #666;");
        button.setPadding(new Insets(10));
        button.setMaxWidth(Double.MAX_VALUE);
        GridPane.setFillWidth(button, true);
        return button;
    }

    // Method to update player score when the player scores and then resets ball
    private void updateScoreAndResetBall(boolean isPlayerOne) {
        String player1Name = player1NameInput.getText().isEmpty() ? "Player 1" : player1NameInput.getText();
        String player2Name = player2NameInput.getText().isEmpty() ? "Player 2" : player2NameInput.getText();

        player1Score.setText(player1Name + ": " + player1ScoreValue);
        player2Score.setText(player2Name + ": " + player2ScoreValue);

//        player1NameInput.setText(player1Name);
//        player2NameInput.setText(player2Name);

        // Used to ensure UI updates occur
        Platform.runLater(() -> {
            String playerName = isPlayerOne ? player1Name : player2Name;
            displayGoalMessage(playerName, isPlayerOne);

            ball.getVisualRepresentation().setCenterX(primaryStage.getWidth() / 2);
            ball.getVisualRepresentation().setCenterY(primaryStage.getHeight() / 2);
            ball.randomizeDirection();
        });
    }

    private void setupScoreDisplay() {
        player1Score = new Text(20, 30, "Player 1: 0");
        player2Score = new Text(primaryStage.getWidth() - 100, 30, "Player 2: 0");
        player1Score.setFill(Color.WHITE);
        player2Score.setFill(Color.WHITE);
        player1Score.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        player2Score.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        player1Score.setStroke(Color.BLUE);
        player1Score.setStrokeWidth(1);
        player1Score.setStrokeType(StrokeType.OUTSIDE);

        player2Score.setStroke(Color.RED);
        player2Score.setStrokeWidth(1);
        player2Score.setStrokeType(StrokeType.OUTSIDE);

        gameComponents.getChildren().addAll(player1Score, player2Score);

    }


    private void setupGoalMessageLabel() {
        goalMessageLabel = new Label();
        goalMessageLabel.setVisible(false);
        goalMessageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        goalMessageLabel.setTextFill(Color.WHITE);
        goalMessageLabel.setAlignment(Pos.CENTER);

    }

    private void displayGoalMessage(String playerName, boolean isPlayerOne) {
        Platform.runLater(() -> {
            String goalMessage = "GOAL " + (playerName.isEmpty() ? (isPlayerOne ? "Player 1" : "Player 2") : playerName);
            goalMessageLabel.setText(goalMessage);
            goalMessageLabel.setTextFill(isPlayerOne ? Color.BLUE : Color.RED);

            goalMessageLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> goalMessageLabel.setVisible(false));
            pause.play();
        });
    }

    private void displayWinner() {
        String winner = player1ScoreValue >= maxScoreSlider.getValue() ? player1NameInput.getText() : player2NameInput.getText();
        winner = winner.isEmpty() ? (player1ScoreValue >= maxScoreSlider.getValue() ? "Player 1" : "Player 2") : winner;
        String winMessage = winner + " wins the game!";

        Platform.runLater(() -> {
            // Show the winning message in a alert when max score has been reached by a player
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Game Over");
            winAlert.setHeaderText("Congrats!");
            winAlert.setContentText(winMessage);

            ButtonType exitButton = new ButtonType("Exit");
            winAlert.getButtonTypes().setAll(exitButton);

            winAlert.showAndWait();

            primaryStage.close();
        });

    }

///// DIDNT GET TIME TO FINISH STERLIZATION.

    // Saves the game settings to a properties file.

    private void saveGame() {
        try {
            //  player names and scores into a properties file
            Properties properties = new Properties();
            properties.setProperty("player1Name", player1NameInput.getText());
            properties.setProperty("player2Name", player2NameInput.getText());
            properties.setProperty("player1Score", String.valueOf(player1ScoreValue));
            properties.setProperty("player2Score", String.valueOf(player2ScoreValue));
            properties.setProperty("racketHeight", String.valueOf(player1Racket.getVisual().getHeight()));

            // Save Ball properties
            properties.setProperty("ballX", String.valueOf(ball.getVisualRepresentation().getCenterX()));
            properties.setProperty("ballY", String.valueOf(ball.getVisualRepresentation().getCenterY()));
            properties.setProperty("ballDX", String.valueOf(ball.getDx()));
            properties.setProperty("ballDY", String.valueOf(ball.getDy()));

            // Save Racket positions and speeds
            properties.setProperty("player1RacketX", String.valueOf(player1Racket.getX()));
            properties.setProperty("player1RacketY", String.valueOf(player1Racket.getY()));
            properties.setProperty("player1RacketSpeed", String.valueOf(player1Racket.getSpeed()));
            properties.setProperty("player1RacketHeight", String.valueOf(player1Racket.getVisual().getHeight()));


            properties.setProperty("player2RacketX", String.valueOf(player2Racket.getX()));
            properties.setProperty("player2RacketY", String.valueOf(player2Racket.getY()));
            properties.setProperty("player2RacketSpeed", String.valueOf(player2Racket.getSpeed()));
            properties.setProperty("player2RacketHeight", String.valueOf(player2Racket.getVisual().getHeight()));

            properties.store(new FileOutputStream("gameSettings.properties"), "Game Save Properties");

            showAlert("Game Saved", "Your game has been successfully saved.");
        } catch (IOException ex) {
            showAlert("Error", "Failed to save the game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // laods the game settings from file

    private void loadGame() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("gameSettings.properties"));

            // Restore player names and scores
            player1NameInput.setText(properties.getProperty("player1Name"));
            player2NameInput.setText(properties.getProperty("player2Name"));
            player1ScoreValue = Integer.parseInt(properties.getProperty("player1Score"));
            player2ScoreValue = Integer.parseInt(properties.getProperty("player2Score"));
            double racketHeight = Double.parseDouble(properties.getProperty("racketHeight"));

            // Restore Ball properties
            double ballX = Double.parseDouble(properties.getProperty("ballX"));
            double ballY = Double.parseDouble(properties.getProperty("ballY"));
            double ballDX = Double.parseDouble(properties.getProperty("ballDX"));
            double ballDY = Double.parseDouble(properties.getProperty("ballDY"));
            ball.getVisualRepresentation().setCenterX(ballX);
            ball.getVisualRepresentation().setCenterY(ballY);
            ball.setDx(ballDX);
            ball.setDy(ballDY);

            // Restore Racket positions and speeds
            double player1RacketX = Double.parseDouble(properties.getProperty("player1RacketX"));
            double player1RacketY = Double.parseDouble(properties.getProperty("player1RacketY"));
            double player1RacketSpeed = Double.parseDouble(properties.getProperty("player1RacketSpeed"));
//            player1Racket.setX(player1RacketX);
            player1Racket.setY(player1RacketY);
            player1Racket.setSpeed(player1RacketSpeed);
            player1Racket.getVisual().setHeight(racketHeight);

            double player2RacketY = Double.parseDouble(properties.getProperty("player2RacketY"));
            double player2RacketSpeed = Double.parseDouble(properties.getProperty("player2RacketSpeed"));
            player2Racket.setY(player2RacketY);
            player2Racket.setSpeed(player2RacketSpeed);
            player2Racket.getVisual().setHeight(racketHeight);

            // Update the score display accordingly
            updateScoreAndResetBall(true); // true if player one has just scored, or false otherwise


            showAlert("Game Loaded", "Your game has been successfully loaded.");
        } catch (IOException | NumberFormatException ex) {
            showAlert("Error", "Failed to load the game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private void saveGameToDatabase(String gameName) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO Game (game_name, player1_name, player2_name, player1_score, player2_score, limit_score) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            pstmt.setString(2, player1NameInput.getText());
            pstmt.setString(3, player2NameInput.getText());
            pstmt.setInt(4, player1ScoreValue);
            pstmt.setInt(5, player2ScoreValue);
            pstmt.setInt(6, (int) maxScoreSlider.getValue());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Success", "Game saved to database successfully!");
            } else {
                showAlert("Error", "No rows affected. Something went wrong.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Error saving the game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadGameFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/pingponggame?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String password = "52Thewillows";

        String sql = "SELECT * FROM Game ORDER BY id DESC LIMIT 1";

        //  connect to the database and execute the query
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                // build a Game object from the ResultSet using Builder pattern
                Game loadedGame = new Game.Builder()
                        .setPlayer1Name(rs.getString("player1_name"))
                        .setPlayer2Name(rs.getString("player2_name"))
                        .setPlayer1Score(rs.getInt("player1_score"))
                        .setPlayer2Score(rs.getInt("player2_score"))
                        .setGameLimit(rs.getInt("limit_score"))
                        .build();

                // Update UI components with loaded game state using the getters of Game object
                Platform.runLater(() -> {
                    player1NameInput.setText(loadedGame.getPlayer1Name());
                    player2NameInput.setText(loadedGame.getPlayer2Name());
                    this.player1Score.setText("Player 1: " + loadedGame.getPlayer1Score());
                    this.player2Score.setText("Player 2: " + loadedGame.getPlayer2Score());
                    this.player1ScoreValue = loadedGame.getPlayer1Score();
                    this.player2ScoreValue = loadedGame.getPlayer2Score();
                    maxScoreSlider.setValue(loadedGame.getGameLimit());

                    // resets ball
                    ball.getVisualRepresentation().setCenterX(primaryStage.getWidth() / 2);
                    ball.getVisualRepresentation().setCenterY(primaryStage.getHeight() / 2);
                    ball.randomizeDirection();
                });

                showAlert("Game Loaded", "Successfully loaded game: " + loadedGame.getPlayer1Name() + " vs " + loadedGame.getPlayer2Name());
            } else {
                showAlert("Load Game", "No saved games to load.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error loading the game: " + e.getMessage());
        }
    }



    public void startGame() {

        String player1Name = player1NameInput.getText();
        String player2Name = player2NameInput.getText();
        double ballSpeed = ballSpeedSlider.getValue();
        double racketHeight = racketSizeSlider.getValue();
        int maxScore = (int) maxScoreSlider.getValue();


        player1Score.setText(player1Name + ": 0");
        player2Score.setText(player2Name + ": 0");

        Line middleLine = new Line();
        middleLine.startXProperty().bind(primaryStage.widthProperty().divide(2));
        middleLine.endXProperty().bind(primaryStage.widthProperty().divide(2));
        middleLine.setStartY(0);
        middleLine.endYProperty().bind(primaryStage.heightProperty());
        middleLine.setStroke(Color.WHITE);
        middleLine.setStrokeWidth(10);

        ball = new Ball(10, Color.WHITE);
        ball.randomizeDirection();



        player1Racket = new Racket(10, primaryStage.getHeight() / 2, 10, racketSizeSlider.getValue(), Color.WHITE);
        player1Racket.getVisual().setStroke(Color.BLUE);
        player1Racket.getVisual().setStrokeWidth(4);
        player2Racket = new Racket(primaryStage.getWidth() - 10, primaryStage.getHeight() / 2, 10, racketSizeSlider.getValue(), Color.WHITE);
        player2Racket.getVisual().setStroke(Color.RED);
        player2Racket.getVisual().setStrokeWidth(4);


        player1Racket.getVisual().yProperty().unbind();
        player2Racket.getVisual().xProperty().unbind();
        player2Racket.getVisual().yProperty().unbind();



        HBox scoreLayout = new HBox();
        scoreLayout.setAlignment(Pos.CENTER);
        scoreLayout.setSpacing(100);
        player1Score.xProperty().bind(primaryStage.widthProperty().divide(4).subtract(player1Score.getLayoutBounds().getWidth() / 2));
        player1Score.setY(60);
        player2Score.xProperty().bind(primaryStage.widthProperty().divide(4).multiply(3).subtract(player2Score.getLayoutBounds().getWidth() / 2));
        player2Score.setY(60);
        scoreLayout.getChildren().addAll(player1Score, player2Score);

        Group gameComponents = new Group();
        gameComponents.getChildren().addAll(player1Racket.getVisual(), player2Racket.getVisual());
        gameComponents.getChildren().addAll(player1Score, player2Score);
        gameComponents.getChildren().addAll(ball.getVisualRepresentation(), middleLine);


        root.getChildren().addAll(scoreLayout, gameComponents);
        root.getChildren().add(goalMessageLabel);
        root.setStyle("-fx-background-color: #333;");


        collisionControls = new collisionControls(ball, player1Racket, player2Racket, primaryStage.getWidth(), primaryStage.getHeight());

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) {
                    return;
                }
                ball.updatePosition();
                player1Racket.update();
                player2Racket.update();


                double oldDx = ball.getDx();
                double oldDy = ball.getDy();

                collisionControls.setStageWidth(primaryStage.getWidth());
                collisionControls.setStageHeight(primaryStage.getHeight());

                collisionControls.CollisionResult collisionResult = collisionControls.checkCollisions();
                if (collisionResult.player1Scored) {
                    player1ScoreValue++;
                    updateScoreAndResetBall(true); // Update UI and reset ball
                }
                if (collisionResult.player2Scored) {
                    player2ScoreValue++;
                    updateScoreAndResetBall(false); // Update UI and reset ball
                }

                if (oldDx != ball.getDx() || oldDy != ball.getDy()) {
                    ball.incrementBounceCount(); // Increase speed
                }

                if (player1ScoreValue >= maxScore || player2ScoreValue >= maxScore) {
                    stop(); // Stop the game loop
                    displayWinner(); // The end of the game and display the winner
                }

            }
        };gameLoop.start();


        gameScene = new Scene(root, 600, 400);

        setupControls();

        gameScene.widthProperty().addListener((observable, oldValue, newValue) -> {
            double newWidth = newValue.doubleValue();
            player2Racket.getVisual().setX(newWidth - player2Racket.getVisual().getWidth() - OFFSET_FROM_SIDE);
            ball.getVisualRepresentation().setCenterX(newWidth / 2);
        });

        gameControls gameController = new gameControls(player1Racket, player2Racket, RACKET_SPEED);
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
            } else {
                new gameControls(player1Racket, player2Racket, RACKET_SPEED).handleKeyPressed(event);
            }
        });
        gameScene.setOnKeyReleased(event -> {
            new gameControls(player1Racket, player2Racket, RACKET_SPEED).handleKeyReleased(event);
        });


        setupPauseMenu(); // Initialize the pause menu

        // Loads actually game after the menu
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Ping Pong Game");
        primaryStage.show();
        gameScene.getRoot().requestFocus();
//        loadGameSettings();
    }


//        saveGameSettings();


//
    public int getPlayer1Score() {
        return player1ScoreValue;
    }

    public int getPlayer2Score() {
        return player2ScoreValue;
    }

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments.
     */

    public static void main(String[] args) {
        launch(args);
    }
}
