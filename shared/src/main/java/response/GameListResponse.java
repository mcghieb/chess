package response;

import model.GameData;

import java.util.ArrayList;

public class GameListResponse extends ResponseContainer {
    ArrayList<GameData> games;

    public GameListResponse(ArrayList<GameData> games, String message) {
        super(message);
        this.games = games;
    }

    public String getList() {
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (GameData game : games ) {
            sb.append(String.format("%s -> ", counter)).append(game.toString()).append("\n");
            counter++;
        }
        return sb.toString();
    }

}
