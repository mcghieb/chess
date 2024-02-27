package server;

import com.google.gson.Gson;
import handler.*;
import dataAccess.*;
import handler.response.RegisterResponse;
import spark.*;

public class Server {
    private DataAccess dataAccess;

    public Server() {

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        dataAccess = new MemoryDataAccess();

        Spark.externalStaticFileLocation("server/src/main/resources/web");

        Spark.delete("/clear", this::clear);
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
    private Object clear(Request req, Response res) {
        return "";
    }

}
