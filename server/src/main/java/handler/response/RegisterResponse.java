package handler.response;

public class RegisterResponse extends ResponseContainer {
    String username;
    String authToken;

    public RegisterResponse(String username, String authToken, String message) {
        super(message);
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
