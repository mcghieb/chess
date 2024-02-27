package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.request.LoginRequest;
import handler.request.RegisterRequest;
import handler.response.LoginResponse;
import handler.response.RegisterResponse;
import service.LoginService;
import service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LoginHandler extends Handler {
    public LoginHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public LoginResponse handleLogin(Request request, Response response) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);

        LoginService service = new LoginService(dataAccess);
        LoginResponse loginResponse = service.login(loginRequest);

        if (loginResponse != null && Objects.equals(loginResponse.getMessage(), null)) {
            response.status(200);
        } else if (loginResponse != null && Objects.equals(loginResponse.getMessage(), "Error: unauthorized")) {
            response.status(401);
        } else {
            response.status(500);
        }

        return loginResponse;
    }
}
