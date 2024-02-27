package handler.response;

public class LoginResponse {
    String username;
    String authToken;
    String message;

    public LoginResponse(String username, String authToken, String message) {
        this.username=username;
        this.authToken=authToken;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
