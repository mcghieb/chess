package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.UserDAO;
import handler.request.LoginRequest;
import handler.response.LoginResponse;
import handler.response.RegisterResponse;

public class LoginService {
    private DataAccess dataAccess;

    public LoginService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException {
        String username = request.getUsername();
        String password = request.getPassword();

        UserDAO userDAO = dataAccess.getUserDAO();
        AuthDAO authDAO = dataAccess.getAuthDAO();

        if (username == null || password == null
                || username.isEmpty() || password.isEmpty()) {
            return new LoginResponse(null, null, "Error: unauthorized");
        }

        if (userDAO.checkCredentials(username, password) != null) {
            String authToken = authDAO.createAuth(username);
            return new LoginResponse(username, authToken, null);
        }

        return new LoginResponse(null, null, "Error: unauthorized");
    }
}
