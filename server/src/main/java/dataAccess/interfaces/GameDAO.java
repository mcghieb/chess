package dataAccess.interfaces;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    HashMap<Integer, GameData> listGames();
    Integer createGame(String gameName);
//    Object updateGame();
}
