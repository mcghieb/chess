package clientTests;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerDoesNotThrow() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");
        Assertions.assertDoesNotThrow(() -> serverFacade.register(registerRequest), "This should not throw.");
    }

    @Test
    public void registerDoesThrow() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(null, null, null);
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(registerRequest), "This should throw.");
    }

    @Test
    public void loginDoesNotThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        Assertions.assertDoesNotThrow(() -> serverFacade.login(loginRequest), "This should not throw.");
    }

    @Test
    public void loginDoesThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest(null, null);
        Assertions.assertThrows(Exception.class, () -> serverFacade.login(loginRequest), "This should throw.");
    }

    @Test
    public void logoutDoesNotThrow() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        String authToken = serverFacade.login(loginRequest).getAuthToken();

        Assertions.assertDoesNotThrow(() -> serverFacade.logout(authToken), "This should not throw.");
    }

    


}