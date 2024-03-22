package client;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private String authToken;
    private String serverUrl;
    private ServerFacade server;
    private State state = State.LOGGEDOUT;

    public ChessClient(String url) {
        serverUrl = url;
        server = new ServerFacade(url);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "registerUser" -> registerUser(params);

                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        assertSignedIn();

        if (params.length)

    }



    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - register
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }


}
