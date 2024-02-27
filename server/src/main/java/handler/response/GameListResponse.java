package handler.response;

import model.GameData;

import java.util.ArrayList;

public class GameListResponse {
    ArrayList<GameData> games;
    String message;

    public GameListResponse(ArrayList<GameData> games, String message) {
        this.games = games;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
