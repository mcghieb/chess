package handler.response;

import model.GameData;

import java.util.ArrayList;

public class GameListResponse extends ResponseContainer {
    ArrayList<GameData> games;

    public GameListResponse(ArrayList<GameData> games, String message) {
        super(message);
        this.games = games;
    }

}
