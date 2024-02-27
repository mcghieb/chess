package handler.request;

public class GameCreateRequest {
    String gameName;

    public GameCreateRequest(String gameName) {
        this.gameName=gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName=gameName;
    }
}
