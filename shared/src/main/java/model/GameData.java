package model;

import chess.ChessGame;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameData {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;
    private ArrayList<String> observers;

    public GameData(int gameID, String whiteUsername , String blackUsername, String gameName, ChessGame game) {
        this.gameID=gameID;
        this.whiteUsername=whiteUsername;
        this.blackUsername=blackUsername;
        this.gameName=gameName;
        this.game=game;
        this.observers = new ArrayList<>();
    }

    public ArrayList<String> getObservers() {
        return observers;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername=whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername=blackUsername;
    }

    public String toString() {
        return String.format("GameID: [%s]; GameName: [%s]; White: [%s]; Black: [%s];", gameID, gameName, whiteUsername, blackUsername);
    }

    public int getID() {
        return gameID;
    }
}
