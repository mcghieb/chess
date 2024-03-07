package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.UserDAO;
import handler.request.LoginRequest;
import handler.request.RegisterRequest;
import handler.response.LoginResponse;
import handler.response.RegisterResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;
    private AuthService authService;

    public UserService(DataAccess dataAccess) throws DataAccessException {
        userDAO = dataAccess.getUserDAO();
        authService = new AuthService(dataAccess);
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException, SQLException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String username = request.getUsername();

        String email = request.getEmail();

        if (username == null || request.getPassword() == null || email == null
                || username.isEmpty() || request.getPassword().isEmpty() || email.isEmpty()) {
            return new RegisterResponse(null, null, "Error: bad request");
        }

        String password = encoder.encode(request.getPassword());

        if ((userDAO.getUser(username)) == null) {
            userDAO.createUser(username, password, email);
            String authToken = authService.createAuth(username);

            return new RegisterResponse(username, authToken, null);
        } else {
            return new RegisterResponse(null,null, "Error: already taken");
        }
    }
}
