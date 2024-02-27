package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    public RegisterHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public Response handleRegister(Request req, Response res) throws DataAccessException {
        RegisterService registerService = new RegisterService(dataAccess);

        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        AuthData authData = registerService.register(userData);

        if (authData != null) {
            res.status(200);
            res.body(authData.getAuthToken());
        } else {
            res.status(400);
            res.body("Error: ");
        }

        return res;
    }
}
