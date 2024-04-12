package client;

import chess.*;
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
import webSocketMessages.userCommands.UserGameCommand;

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
    public boolean gameOver;
    private HashMap<String, GameData> gameList;
    public ChessBoard board;
    public ChessGame.TeamColor color;

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
                case "redraw_board" -> printBoard(board);
                case "leave" -> leave();
                case "make_move" -> makeMove(params);
                case "resign" -> resign();
//                case "highlight_legal_moves" -> ;

                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String makeMove(String... params) throws ResponseException {
        ChessPosition startPosition = new ChessPosition(Integer.parseInt(params[0].substring(1,2)), Integer.parseInt(params[0].substring(3,4)));
        ChessPosition endPosition = new ChessPosition(Integer.parseInt(params[1].substring(1,2)), Integer.parseInt(params[1].substring(3,4)));
        ChessPiece.PieceType promotionPiece = null;
        if (params.length == 3) {
            switch (params[2]) {
                case "bishop" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                case "queen" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                case "rook" -> promotionPiece = ChessPiece.PieceType.ROOK;
                case "knight" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
            }
        }

        ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);

        var notification = new UserGameCommand(authToken, UserGameCommand.CommandType.MAKE_MOVE, color, gameId);
        notification.move = move;
        notification.gameOver = gameOver;

        ws.sendToServer(notification);

        return "";
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

    private String processGameList(GameListResponse gameListResponse) {
        gameList = new HashMap<>();
        ArrayList<GameData> games = gameListResponse.getList();
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (GameData game : games) {
            gameList.put(String.format("%s", counter), game);
            sb.append(String.format("%s -> ", counter)).append(game).append("\n");
            counter++;
        }
        return sb.toString();
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        GameListResponse gameListResponse = server.listGames(authToken);
        return processGameList(gameListResponse);
    }

    void loadGames() throws ResponseException {
        assertSignedIn();
        GameListResponse gameListResponse = server.listGames(authToken);
        processGameList(gameListResponse);
    }


    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        loadGames();
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        gameId = params[0];

        if (params.length == 2) {
            int id = gameList.get(params[0]).getID();

            if (Objects.equals(params[1], "white")) {
                color = ChessGame.TeamColor.WHITE;
                GameJoinRequest gameJoinRequest = new GameJoinRequest(ChessGame.TeamColor.WHITE, id);

                server.joinGame(gameJoinRequest, authToken);

                UserGameCommand userGameCommand = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_PLAYER, ChessGame.TeamColor.WHITE , params[0]);
                ws.sendToServer(userGameCommand);
//                printBoard(params[0], 2);
                boardDirection= 2;

            } else if (Objects.equals(params[1], "black")) {
                color = ChessGame.TeamColor.BLACK;
                GameJoinRequest gameJoinRequest= new GameJoinRequest(ChessGame.TeamColor.BLACK, id);

                server.joinGame(gameJoinRequest, authToken);
                UserGameCommand userGameCommand = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_PLAYER, ChessGame.TeamColor.BLACK , params[0]);
                ws.sendToServer(userGameCommand);
//                printBoard(params[0], 1);
                boardDirection= 1;
            }

            state = State.PLAYER;
            return String.format("Joined game [%s] as %s\n", params[0], params[1]);

        } else if (params.length == 1) {
            color = null;
            int id = gameList.get(params[0]).getID();
            GameJoinRequest gameJoinRequest = new GameJoinRequest(null, id);
            server.joinGame(gameJoinRequest, authToken);
//            printBoard(params[0], 2);

            UserGameCommand userGameCommand = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_OBSERVER,null , params[0]);
            ws.sendToServer(userGameCommand);
            boardDirection= 2;
            state = State.OBSERVER;
            return String.format("Joined game [%s] as OBSERVER\n", params[0]);
        }

        throw new ResponseException(400, "Bad request.\n");
    }

    public String printBoard(ChessBoard board) {
        PrintBoard.printGame(board, boardDirection);
        this.board = board;

        return "";
    }

    public String leave() throws ResponseException {
        if (!player() && !observer()) {
            throw new ResponseException(400, "You must join a game first.\n");
        }

        ws.sendToServer(new UserGameCommand(authToken, UserGameCommand.CommandType.LEAVE, null, gameId));

        boardDirection = 0;
        color = null;
        gameId = null;
        gameList = null;
        state = State.LOGGEDIN;

        return "You have left. Please join another game or logout.\n";
    }

    public String resign() throws ResponseException {

        ws.sendToServer(new UserGameCommand(authToken, UserGameCommand.CommandType.RESIGN, null, gameId));

        gameOver = true;

        checkGame();

        return "";
    }

    public void checkGame() throws ResponseException {
        ws.checkGame(gameId);
    }

    public String logout() throws ResponseException {
        assertSignedIn();

        server.logout(authToken);
        ws.sendToServer(new UserGameCommand(authToken, UserGameCommand.CommandType.LEAVE, null, gameId));

        authToken = null;
        state = State.LOGGEDOUT;
        username = "LOGGED_OUT";
        boardDirection = 0;
        gameId = null;
        gameList = null;

        return "Logged out.\n";
    }

    private boolean player() {
        return state == State.PLAYER;
    }

    private boolean observer() {
        return state == State.OBSERVER;
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
            case State.PLAYER:
                return """
                        - help
                        - redraw_board
                        - leave
                        - make_move <start position> <end position>
                        - resign
                        - highlight_legal_moves <piece position>
                        (positions are entered like [n,n] where n is a digit.)
                        """;
            case State.OBSERVER:
                return """
                        - help
                        - redraw_board
                        - leave
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
