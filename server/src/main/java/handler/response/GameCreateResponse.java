package handler.response;

public class GameCreateResponse {
    Integer gameID;
    String message;

    public GameCreateResponse(Integer gameID, String message) {
        this.gameID=gameID;
        this.message=message;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID=gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }
}
