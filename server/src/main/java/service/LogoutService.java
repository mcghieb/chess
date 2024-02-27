package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;
import handler.response.ResponseContainer;
import spark.Response;

public class LogoutService {
    private DataAccess dataAccess;

    public LogoutService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ResponseContainer logout(String authToken) throws DataAccessException {
        AuthDAO authDAO = dataAccess.getAuthDAO();

        if (authDAO.in(authToken) == null) {
            return new ResponseContainer("Error: unauthorized");
        }

        authDAO.deleteAuth(authToken);
        return null;
    }
}
