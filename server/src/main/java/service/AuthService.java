package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;
import response.ResponseContainer;

import java.sql.SQLException;

public class AuthService {
    private AuthDAO authDAO;
    public AuthService(DataAccess dataAccess) throws DataAccessException {
        this.authDAO = dataAccess.getAuthDAO();
    }

    public String createAuth(String username) throws SQLException, DataAccessException {
        return authDAO.createAuth(username);
    }

    public String in(String authToken) {
        return authDAO.in(authToken);
    }

    public ResponseContainer logout(String authToken) throws DataAccessException {
        if (authDAO.in(authToken) == null) {
            return new ResponseContainer("Error: unauthorized");
        }

        authDAO.deleteAuth(authToken);
        return new ResponseContainer(null);
    }

    public boolean authenticate(String authToken) {
        return authDAO.in(authToken) == null;
    }

    public String getUsername(String authToken) {
        return authDAO.getUsername(authToken);
    }


}

