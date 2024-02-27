package dataAccess.memorydataaccess;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.interfaces.GameDAO;
import model.GameData;


import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games = new HashMap<>();
    public MemoryGameDAO(){}

    public HashMap<Integer, GameData> listGames() {
        return games;
    }

    public Integer createGame(String gameName) {
        Integer size = games.size();
        Integer gameID = size + 1;
        ChessGame game = new ChessGame();

        GameData gameData = new GameData(gameID, null, null, gameName, game);
        games.put(gameID, gameData);
        return gameID;
    }

    public void updateGame(Integer gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        GameData game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Error: bad request");
        }

        if (playerColor == ChessGame.TeamColor.BLACK && game.getBlackUsername() == null) {
            game.setBlackUsername(username);
        } else if (playerColor == ChessGame.TeamColor.WHITE && game.getWhiteUsername() == null) {
            game.setWhiteUsername(username);
        } else {
            throw new DataAccessException("Error: already taken");
        }
    }

}
