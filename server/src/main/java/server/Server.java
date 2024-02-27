package server;

import com.google.gson.Gson;
import handler.*;
import dataAccess.*;
import handler.response.*;
import spark.*;

import javax.xml.crypto.Data;

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
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::getGameList);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

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

    private String login(Request req, Response res) throws DataAccessException {
        LoginHandler loginHandler = new LoginHandler(dataAccess);

        LoginResponse response = loginHandler.handleLogin(req, res);

        return new Gson().toJson(response);
    }

    private String logout(Request req, Response res) throws DataAccessException {
        LogoutHandler logoutHandler = new LogoutHandler(dataAccess);
        String authToken = req.headers("authorization");

        LogoutResponse response = logoutHandler.handleLogout(authToken, res);

        return new Gson().toJson(response);
    }

    private String getGameList(Request req, Response res) throws DataAccessException {
        GameHandler gameListHandler = new GameHandler(dataAccess);
        String authToken = req.headers("authorization");

        GameListResponse response = gameListHandler.handleGameList(authToken, res);

        return new Gson().toJson(response);
    }

    private String createGame(Request req, Response res) throws DataAccessException {
        GameHandler gameCreateHandler = new GameHandler(dataAccess);
        String authToken = req.headers("authorization");

        GameCreateResponse response = gameCreateHandler.handleGameCreate(authToken, req, res);

        return new Gson().toJson(response);
    }

    private String joinGame(Request req, Response res) throws DataAccessException {
        GameHandler gameJoinHandler = new GameHandler(dataAccess);
        String authToken = req.headers("authorization");

        GameJoinResponse response = gameJoinHandler.handleGameJoin(authToken,req,res);

        return new Gson().toJson(response);
    }

    private String clear(Request req, Response res) {
        ClearHandler clearHandler = new ClearHandler(dataAccess);

        ClearResponse response = clearHandler.handleClear(res);

        return new Gson().toJson(response);
    }

}
