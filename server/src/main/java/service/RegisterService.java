package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.UserDAO;
import handler.request.RegisterRequest;
import handler.response.RegisterResponse;

public class RegisterService {
    private DataAccess dataAccess;
    public RegisterService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        UserDAO userDAO = dataAccess.getUserDAO();
        AuthDAO authDAO = dataAccess.getAuthDAO();

        if (username == null || password == null || email == null
                || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return new RegisterResponse(null, null, "Error: bad request");
        }

        if ((userDAO.getUser(username)) == null) {
            userDAO.createUser(username, password, email);
            String authToken = authDAO.createAuth(username);

            return new RegisterResponse(username, authToken, null);
        } else {
            return new RegisterResponse(null,null, "Error: already taken");
        }

    }

}
