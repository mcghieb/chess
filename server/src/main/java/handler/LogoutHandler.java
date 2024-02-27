package handler;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.response.ResponseContainer;
import service.LogoutService;
import spark.Response;

public class LogoutHandler extends Handler {

    public LogoutHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public ResponseContainer handleLogout(String authToken, Response response) throws DataAccessException {
        LogoutService service = new LogoutService(dataAccess);

        ResponseContainer logoutResponse = service.logout(authToken);

        if (logoutResponse != null) {
            response.status(401);
            return logoutResponse;
        } else {
            response.status(200);
        }

        return null;
    }
}
