package serviceTests;

import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import dataAccess.interfaces.UserDAO;
import handler.request.RegisterRequest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import dataAccess.*;

public class ServiceUnitTests {
    private static DataAccess dataAccess;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserService userService;
    private static GameService gameService;
    private static AuthService authService;


    @BeforeAll
    public static void init() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        userDAO = dataAccess.getUserDAO();
        authDAO = dataAccess.getAuthDAO();
        gameDAO = dataAccess.getGameDAO();
    }

    @Test
    @Order(1)
    public void register() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(
                "testusername", "thispassword", "thisemail@gmail.com");
        userService.register(registerRequest);

        UserDAO userDAO = dataAccess.getUserDAO();
        String username = userDAO.getUser("testusername");
        Assertions.assertEquals(username, "testusername", "Register failed.");
        
    }

}
