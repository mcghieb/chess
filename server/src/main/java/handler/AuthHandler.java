package handler;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.response.ResponseContainer;
import service.AuthService;
import service.UserService;

public class AuthHandler extends Handler{
    AuthService service;

    public AuthHandler(DataAccess dataAccess) throws DataAccessException {
        super(dataAccess);
        this.service = new AuthService(dataAccess);
    }



    public ResponseContainer handleLogout(String authToken) throws DataAccessException {
        return service.logout(authToken);
    }
}
