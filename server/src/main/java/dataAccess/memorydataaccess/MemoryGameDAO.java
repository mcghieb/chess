package dataAccess.memorydataaccess;
import chess.ChessGame;
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

}
