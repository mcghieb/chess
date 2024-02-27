package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.request.LoginRequest;
import handler.response.LoginResponse;
import service.LoginService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LoginHandler extends Handler {
    public LoginHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public LoginResponse handleLogin(Request request) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);

        LoginService service = new LoginService(dataAccess);

        return service.login(loginRequest);
    }
}
