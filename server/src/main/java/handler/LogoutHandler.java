package handler;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.response.ResponseContainer;
import service.LogoutService;

public class LogoutHandler extends Handler {

    public LogoutHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public ResponseContainer handleLogout(String authToken) throws DataAccessException {
        LogoutService service = new LogoutService(dataAccess);

        return service.logout(authToken);
    }
}
