package service;

import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.UserDAO;
import handler.request.LoginRequest;
import handler.request.RegisterRequest;
import handler.response.LoginResponse;
import handler.response.RegisterResponse;
import org.junit.jupiter.api.*;
import dataAccess.*;

import java.sql.SQLException;

public class UserServiceTests {
    private static DataAccess dataAccess;
    private static UserDAO userDAO;
    private static UserService userService;


    @BeforeAll
    public static void init() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        userDAO = dataAccess.getUserDAO();
    }

    @Test
    @Order(1)
    public void registerGoodRequest() throws DataAccessException, SQLException {
        RegisterRequest registerRequest = new RegisterRequest(
                "testusername", "thispassword", "thisemail@gmail.com");
        userService.register(registerRequest);

        String username = userDAO.getUser("testusername");
        Assertions.assertEquals(username, "testusername", "Register failed.");
    }

    @Test
    @Order(2)
    public void registerBadRequest() throws DataAccessException, SQLException {
        RegisterRequest registerRequest = new RegisterRequest(null, null, null);
        RegisterResponse registerResponse = userService.register(registerRequest);

        Assertions.assertEquals(registerResponse.getMessage(), "Error: bad request", "userService did not" +
                "populate the error message corresponding to code(400)");
    }

    @Test
    @Order(3)
    public void loginGoodRequest() throws DataAccessException, SQLException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        userService.register(registerRequest);

        LoginResponse loginResponse = userService.login(new LoginRequest("username", "password"));
        Assertions.assertNull(loginResponse.getMessage(), "login() populated an error message.");
    }

    @Test
    @Order(3)
    public void loginBadRequest() throws DataAccessException, SQLException {
        LoginRequest loginRequest = new LoginRequest(null, null);
        LoginResponse loginResponse = userService.login(loginRequest);

        Assertions.assertEquals(loginResponse.getMessage(), "Error: unauthorized", "Login Failed.");
    }

}
