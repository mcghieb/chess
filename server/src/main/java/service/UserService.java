package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.UserDAO;
import handler.request.LoginRequest;
import handler.request.RegisterRequest;
import handler.response.LoginResponse;
import handler.response.RegisterResponse;

import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;
    private AuthService authService;

    public UserService(DataAccess dataAccess) throws DataAccessException {
        userDAO = dataAccess.getUserDAO();
        authService = new AuthService(dataAccess);
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException, SQLException {
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || password == null
                || username.isEmpty() || password.isEmpty()) {
            return new LoginResponse(null, null, "Error: unauthorized");
        }

        if (userDAO.checkCredentials(username, password) != null) {
            String authToken = authService.createAuth(username);
            return new LoginResponse(username, authToken, null);
        }

        return new LoginResponse(null, null, "Error: unauthorized");
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException, SQLException {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        if (username == null || password == null || email == null
                || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return new RegisterResponse(null, null, "Error: bad request");
        }

        if ((userDAO.getUser(username)) == null) {
            userDAO.createUser(username, password, email);
            String authToken = authService.createAuth(username);

            return new RegisterResponse(username, authToken, null);
        } else {
            return new RegisterResponse(null,null, "Error: already taken");
        }
    }
}
