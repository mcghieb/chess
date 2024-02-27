package server;

import com.google.gson.Gson;
import handler.*;
import dataAccess.*;
import handler.response.ClearResponse;
import handler.response.RegisterResponse;
import spark.*;

public class Server {
    private DataAccess dataAccess;

    public Server() {

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        dataAccess = new MemoryDataAccess();

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
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

    private String register(Request req, Response res) throws DataAccessException {
        RegisterHandler registerHandler = new RegisterHandler(dataAccess);

        RegisterResponse response = registerHandler.handleRegister(req, res);

        return new Gson().toJson(response);
    }

// GO BACK AND FIX THIS CLASS AFTER MEMORYDATAACCESS CLASSES ARE FINISHED
    private String clear(Request req, Response res) {
        ClearHandler clearHandler = new ClearHandler(dataAccess);

        ClearResponse response = clearHandler.handleClear(res);

        return new Gson().toJson(response);
    }

}
