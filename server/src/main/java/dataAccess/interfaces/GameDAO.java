package dataAccess.interfaces;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;

public interface GameDAO {
    HashMap<Integer, GameData> listGames();
    Integer createGame(String gameName);
    void updateGame(Integer gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException, SQLException;

    void clearGame();

    void updateBoard(String gameID, ChessGame chessGame);
}
