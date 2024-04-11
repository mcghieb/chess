package client;

import chess.ChessBoard;
import chess.ChessGame;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.GameData;
import request.GameCreateRequest;
import request.GameJoinRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.GameCreateResponse;
import response.GameListResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.ServerFacade;
import ui.PrintBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class ChessClient {
    private final Repl notificationHandler;
    private WebSocketFacade ws;
    private String authToken = null;
    private String serverUrl;
    private ServerFacade server;
    private State state = State.LOGGEDOUT;
    private String username = "LOGGED_OUT";
    private int boardDirection;
    private String gameId;
    private HashMap<String, GameData> gameList;

    public ChessClient(String url, Repl notificationHandler) {
        this.notificationHandler = notificationHandler;
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
                case "list_games" -> listGames();
                case "join_game", "observe" -> joinGame(params);

                case "redraw_board" -> printBoard();
                case "leave" -> leave();
//                case "make_move" -> ;
//                case "resign" -> ;
//                case "highlight_legal_moves" -> ;


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

            return String.format("Created game: %s\n", gameCreateResponse.getGameID());
        }

        throw new ResponseException(400, "Bad request.\n");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        GameListResponse gameListResponse = server.listGames(authToken);
        gameList = new HashMap<>();

        ArrayList<GameData> games = gameListResponse.getList();

        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (GameData game : games ) {
            gameList.put(String.format("%s", counter), game);

            sb.append(String.format("%s -> ", counter)).append(game).append("\n");
            counter++;
        }
        return sb.toString();
    }

    void loadGames() throws ResponseException {
        assertSignedIn();
        GameListResponse gameListResponse = server.listGames(authToken);
        gameList = new HashMap<>();

        ArrayList<GameData> games = gameListResponse.getList();

        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (GameData game : games ) {
            gameList.put(String.format("%s", counter), game);

            sb.append(String.format("%s -> ", counter)).append(game).append("\n");
            counter++;
        }
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        loadGames();
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        gameId = params[0];

        if (params.length == 2) {
            int id = gameList.get(params[0]).getID();

            if (Objects.equals(params[1], "white")) {
                GameJoinRequest gameJoinRequest = new GameJoinRequest(ChessGame.TeamColor.WHITE, id);

                server.joinGame(gameJoinRequest, authToken);
                ws.joinPlayer(authToken, username,"white", params[0]);
//                printBoard(params[0], 2);
                boardDirection= 2;

            } else if (Objects.equals(params[1], "black")) {
                GameJoinRequest gameJoinRequest= new GameJoinRequest(ChessGame.TeamColor.BLACK, id);

                server.joinGame(gameJoinRequest, authToken);
                ws.joinPlayer(authToken, username,"black", params[0]);
//                printBoard(params[0], 1);
                boardDirection= 1;
            }

            state = State.INGAME;
            return String.format("Joined game [%s] as %s\n", params[0], params[1]);

        } else if (params.length == 1) {
            int id = gameList.get(params[0]).getID();
            GameJoinRequest gameJoinRequest = new GameJoinRequest(null, id);
            server.joinGame(gameJoinRequest, authToken);
//            printBoard(params[0], 2);

            ws.joinObserver(authToken, username, params[0]);
            boardDirection= 2;
            state = State.INGAME;
            return String.format("Joined game [%s] as OBSERVER\n", params[0]);
        }

        throw new ResponseException(400, "Bad request.\n");
    }


    public String printBoard() {
        GameData gameData = gameList.get(gameId);
        ChessGame game = gameData.getGame();
        ChessBoard board = game.getBoard();

        PrintBoard.printGame(board, boardDirection);

        return "";
    }


    public String leave() throws ResponseException {
        ws.leave(username, gameId);

        boardDirection = 0;
        gameId = null;
        gameList = null;

        return "You have left. Please join another game or logout.\n";
    }


    public String logout() throws ResponseException {
        assertSignedIn();

        server.logout(authToken);

        authToken = null;
        state = State.LOGGEDOUT;
        username = "LOGGED_OUT";
        boardDirection = 0;
        gameId = null;
        gameList = null;

        return "Logged out.\n";
    }

    public String help() {
        switch (state) {
            case State.LOGGEDOUT:
                return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - help
                    - quit
                    """;
            case State.LOGGEDIN:
                return """
                - help
                - logout
                - create_game <game_name>
                - list_games
                - join_game <game_id> [WHITE | BLACK | empty]
                - observe <game_id>
                - quit
                """;
            case State.GAMEOVER:
                return """
                        - help
                        - redraw_board
                        - leave""";
            case State.INGAME:
                return """
                        - help
                        - redraw_board
                        - leave
                        - make_move <start position> <end position>
                        - resign
                        - highlight_legal_moves <piece position>
                        (positions are entered like [n,n] where n is a digit.)
                        """;
            default:
                return "This is a secret menu and we don't know how you found it. Good job kiddo. \n(This is a feature not a bug):\n- quit\n\n";
        }
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
