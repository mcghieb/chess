package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.request.RegisterRequest;
import handler.response.RegisterResponse;
import service.RegisterService;
import spark.Request;

import java.util.Objects;

public class RegisterHandler extends Handler {
    public RegisterHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public RegisterResponse handleRegister(Request request) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);

        RegisterService service = new RegisterService(dataAccess);

        return service.register(registerRequest);
    }
}
