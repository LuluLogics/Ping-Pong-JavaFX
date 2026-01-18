package com.example.pongstage1.dao;

import com.example.pongstage1.Model.Game;

import java.sql.*;

public class GameDAOImpl implements GameDao {
    private final String url = "jdbc:mysql://localhost:3306/pingponggame?allowPublicKeyRetrieval=true&useSSL=false";
    private final String user = "root";
    private final String password = "52Thewillows";

    @Override
    public void saveGame(Game game) {
        String sql = "INSERT INTO Game (game_name, player1_name, player2_name, player1_score, player2_score, limit_score) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getName());
            pstmt.setString(2, game.getPlayer1Name());
            pstmt.setString(3, game.getPlayer2Name());
            pstmt.setInt(4, game.getPlayer1Score());
            pstmt.setInt(5, game.getPlayer2Score());
            pstmt.setInt(6, game.getGameLimit());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Game loadLatestGame() {
        String sql = "SELECT * FROM Game ORDER BY id DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new Game.Builder()
                        .setPlayer1Name(rs.getString("player1_name"))
                        .setPlayer2Name(rs.getString("player2_name"))
                        .setPlayer1Score(rs.getInt("player1_score"))
                        .setPlayer2Score(rs.getInt("player2_score"))
                        .setGameLimit(rs.getInt("limit_score"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
