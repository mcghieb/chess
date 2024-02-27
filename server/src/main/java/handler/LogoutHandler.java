package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.response.LogoutResponse;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

    public LogoutHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public LogoutResponse handleLogout(String authToken, Response response) throws DataAccessException {
        LogoutService service = new LogoutService(dataAccess);

        LogoutResponse logoutResponse = service.logout(authToken);

        if (logoutResponse != null) {
            response.status(401);
            return logoutResponse;
        } else {
            response.status(200);
        }

        return null;
    }
}
