package clientTests;

import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import request.GameCreateRequest;
import request.GameJoinRequest;
import request.LoginRequest;
import request.RegisterRequest;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
        try {
            serverFacade.clear();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    public void registerDoesNotThrow() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");
        Assertions.assertDoesNotThrow(() -> serverFacade.register(registerRequest), "This should not throw.");
    }

    @Test
    @Order(2)
    public void registerDoesThrow() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(null, null, null);
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(registerRequest), "This should throw.");
    }

    @Test
    @Order(3)
    public void loginDoesNotThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        Assertions.assertDoesNotThrow(() -> serverFacade.login(loginRequest), "This should not throw.");
    }

    @Test
    @Order(4)
    public void loginDoesThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest(null, null);
        Assertions.assertThrows(Exception.class, () -> serverFacade.login(loginRequest), "This should throw.");
    }

    @Test
    @Order(5)
    public void logoutDoesNotThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        authToken = serverFacade.login(loginRequest).getAuthToken();

        Assertions.assertDoesNotThrow(() -> serverFacade.logout(authToken), "This should not throw.");
    }

    @Test
    @Order(6)
    public void logoutDoesThrow() throws ResponseException {
        Assertions.assertThrows(Exception.class, () -> serverFacade.logout("badToken"), "This should throw.");
    }


    @Test
    @Order(7)
    public void createGameDoesNotThrow() throws ResponseException {
        GameCreateRequest gameCreateRequest = new GameCreateRequest("TESTGAMENAME1");

        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(gameCreateRequest, authToken), "This should not throw.");
    }

    @Test
    @Order(8)
    public void createGameDoesThrow() throws ResponseException {
        Assertions.assertThrows(Exception.class,() -> serverFacade.createGame(null, null), "This should throw.");
    }

    @Test
    @Order(9)
    public void listGamesDoesNotThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        String authToken = serverFacade.login(loginRequest).getAuthToken();

        Assertions.assertDoesNotThrow(() -> serverFacade.listGames(authToken), "This should not throw.");
    }

    @Test
    @Order(10)
    public void listGamesDoesThrow() throws ResponseException {
        Assertions.assertThrows(Exception.class,() -> serverFacade.listGames("FAKEAUTH"), "This should throw.");
    }

    @Test
    @Order(11)
    public void joinGameDoesNotThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        String authToken = serverFacade.login(loginRequest).getAuthToken();

        GameJoinRequest gameJoinRequest = new GameJoinRequest(ChessGame.TeamColor.WHITE, 1);

        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(gameJoinRequest, authToken), "This should not throw.");
    }

}