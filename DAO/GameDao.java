package com.example.pongstage1.dao;

import com.example.pongstage1.Model.Game;

public interface GameDao {
    void saveGame(Game game);
    Game loadLatestGame();
}
