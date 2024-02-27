package handler.response;

public class GameJoinResponse {
    String message;

    public GameJoinResponse(String message) {
        this.message = message;
    };

    public Object getMessage() {
        return message;
    }
}
