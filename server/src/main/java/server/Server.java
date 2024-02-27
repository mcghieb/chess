package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import dataAccess.DataAccess;
import service.RegisterService;
import spark.*;

public class Server {
    private DataAccess dataAccess;

    public Server() {

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        dataAccess = new MemoryDataAccess();

        Spark.externalStaticFileLocation("server/src/resources/web");

        Spark.delete("/clear", this::clear);
        Spark.post("/user", this::registerHandler);
//        Spark.post("/session", this::loginHandler);
//        Spark.delete("/session", this::logoutHandler);
//        Spark.get("/game", this::getGameListHandler);
//        Spark.post("/game", this::createGameHandler);
//        Spark.put("/game", this.joinGameHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop(){
        Spark.stop();
        Spark.awaitStop();
    }

    // RETURNS AUTHTOKEN
    private Response registerHandler(Request req, Response res) throws DataAccessException {
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

// GO BACK AND FIX THIS CLASS AFTER MEMORYDATAACCESS CLASSES ARE FINISHED
    private Object clear(Request req, Response res) {
        return "";
    }

}
