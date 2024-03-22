package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;
import service.UserService;
import spark.Request;

import java.sql.SQLException;

public class UserHandler extends Handler {
    UserService service;

    public UserHandler(DataAccess dataAccess) throws DataAccessException {
        super(dataAccess);
        this.service = new UserService(dataAccess);
    }

    public LoginResponse handleLogin(Request request) throws DataAccessException, SQLException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);

        return service.login(loginRequest);
    }

    public RegisterResponse handleRegister(Request request) throws DataAccessException, SQLException {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);

        return service.register(registerRequest);
    }
}
