package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.request.LoginRequest;
import handler.request.RegisterRequest;
import handler.response.LoginResponse;
import handler.response.RegisterResponse;
import handler.response.ResponseContainer;
import service.UserService;
import spark.Request;

public class UserHandler extends Handler {
    UserService service;

    public UserHandler(DataAccess dataAccess) throws DataAccessException {
        super(dataAccess);
        this.service = new UserService(dataAccess);
    }

    public LoginResponse handleLogin(Request request) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);

        return service.login(loginRequest);
    }

    public RegisterResponse handleRegister(Request request) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);

        return service.register(registerRequest);
    }
}
