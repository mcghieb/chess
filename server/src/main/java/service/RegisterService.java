package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.UserDAO;
import model.AuthData;
import model.UserData;

public class RegisterService {
    private DataAccess dataAccess;
    public RegisterService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        String username = userData.getUsername();
        String password = userData.getPassword();
        String email = userData.getEmail();

        UserDAO userDAO = dataAccess.getUserDAO();
        AuthDAO authDAO = dataAccess.getAuthDAO();

        if ((userDAO.getUser(username)) == null) {
            userDAO.createUser(username, password, email);
            return authDAO.createAuth(username);
        }

        return null;
    }

    public AuthData login() {
        return null;
    }
}
