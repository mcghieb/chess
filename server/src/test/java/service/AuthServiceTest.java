package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.UserDAO;
import handler.response.ResponseContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private static DataAccess dataAccess;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static AuthService authService;


    @BeforeAll
    public static void init() throws DataAccessException {
        dataAccess=new MemoryDataAccess();
        authService=new AuthService(dataAccess);
        userDAO=dataAccess.getUserDAO();
        authDAO=dataAccess.getAuthDAO();
    }


    @Test
    @Order(1)
    void createAuthGoodRequest() {
        String authToken = authService.createAuth("User1");

        Assertions.assertNotNull(authToken, "createAuth() Failed.");
    }

    @Test
    @Order(2)
    void createAuthBadRequest() {
        String authToken = authService.createAuth(null);

        Assertions.assertNull(authToken, "createAuth() should have populated an error response message.");
    }

    @Test
    @Order(3)
    void inGoodRequest() {
        String authToken = authService.createAuth("User1");
        String confirmedAuthToken = authService.in(authToken);

        Assertions.assertEquals(authToken, confirmedAuthToken, "in() Failed.");
    }

    @Test
    @Order(4)
    void inBadRequest() {
        String authToken = "thisIsABadToken";
        String returnedAuthToken = authService.in(authToken);

        Assertions.assertNull(returnedAuthToken,  "in() should have returned null.");
    }

    @Test
    @Order(5)
    void logoutGoodRequest() throws DataAccessException {
        String authToken = authDAO.createAuth("User1");

        authService.logout(authToken);
        Assertions.assertNull(authService.in(authToken), "logout() failed to delete authentication");
    }

    @Test
    @Order(6)
    void logoutBadRequest() throws DataAccessException {
        ResponseContainer responseContainer = authService.logout("authToken");
        Assertions.assertNotNull(responseContainer.getMessage(), "logout() failed to populate error message in response.");
    }

    @Test
    void authenticateGoodRequest() {

    }

    @Test
    void getUsername() {
    }
}