package handler.response;

public class GameCreateResponse extends ResponseContainer {
    Integer gameID;

    public GameCreateResponse(Integer gameID, String message) {
        super(message);
        this.gameID=gameID;
    }

}
