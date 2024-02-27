package dataAccess.interfaces;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    HashMap<Integer, GameData> listGames();
    Integer createGame(String gameName);
    void updateGame(Integer GameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException;
}
