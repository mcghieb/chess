package client;

import exception.ResponseException;
import request.GameCreateRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.GameCreateResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private String authToken = null;
    private String serverUrl;
    private ServerFacade server;
    private State state = State.LOGGEDOUT;
    private String username = "LOGGED_OUT";

    public ChessClient(String url) {
        serverUrl = url;
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> registerUser(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create_game" -> createGame(params[0]);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length == 3 && state == State.LOGGEDOUT) {
            RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse registerResponse = server.register(registerRequest);

            username = registerResponse.getUsername();
            state = State.LOGGEDIN;
            authToken = registerResponse.getAuthToken();

            return String.format("Successfully registered [%s]\n", username) + help();
        }

        throw new ResponseException(400, "Bad request.\n");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2 && state == State.LOGGEDOUT) {
            LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
            LoginResponse loginResponse = server.login(loginRequest);

            username = loginResponse.getUsername();
            state = State.LOGGEDIN;
            authToken = loginResponse.getAuthToken();

            return String.format("Logged in as [%s]\n", loginResponse.getUsername()) + help();
        }

        throw new ResponseException(400, "Bad request.\n");
    }

    public String createGame(String gameName) throws ResponseException {
        assertSignedIn();
        if (gameName != null && !gameName.isEmpty()) {
            GameCreateRequest gameCreateRequest = new GameCreateRequest(gameName);
            GameCreateResponse gameCreateResponse = server.createGame(gameCreateRequest, authToken);

            return String.format("Created game: %s", gameCreateResponse.getGameID());
        }

        throw new ResponseException(400, "Bad request.\n");
    }

    public String logout() throws ResponseException {
        assertSignedIn();

        server.logout(authToken);

        authToken = null;
        state = State.LOGGEDOUT;
        username = "LOGGED_OUT";

        return "Logged out.";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - help
                    - quit
                    """;
        }
        return """
                - help
                - logout
                - create_game <game_name>
                - list_games
                - join_game <game_id> [WHITE | BLACK | empty]
                - observe <game_id>
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    public State getState() {
        return state;
    }


}
