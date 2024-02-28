package handler.response;

public class LoginResponse extends ResponseContainer {
    String username;
    String authToken;

    public LoginResponse(String username, String authToken, String message) {
        super(message);
        this.username=username;
        this.authToken=authToken;
    }

    public String getUsername() {
        return username;
    }
}
