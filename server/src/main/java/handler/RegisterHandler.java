package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.request.RegisterRequest;
import handler.response.RegisterResponse;
import service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class RegisterHandler extends Handler {
    public RegisterHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public RegisterResponse handleRegister(Request request, Response response) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);

        RegisterService service = new RegisterService(dataAccess);
        RegisterResponse registerResponse = service.register(registerRequest);

        if (Objects.equals(registerResponse.getMessage(), null)) {
            response.status(200);
        } else if (Objects.equals(registerResponse.getMessage(), "Error: bad request")) {
            response.status(400);
        } else if (Objects.equals(registerResponse.getMessage(), "Error: already taken")) {
            response.status(403);
        } else {
            response.status(500);
        }

        return registerResponse;
    }
}
