package handler.response;

public class RegisterResponse {
    String username;
    String authToken;
    String message;

    public RegisterResponse(String username, String authToken, String message) {
        this.authToken = authToken;
        this.username = username;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
