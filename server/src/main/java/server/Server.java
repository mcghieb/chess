package server;

import com.google.gson.Gson;
import handler.*;
import dataAccess.*;
import response.*;
import server.websocket.WebSocketHandler;
import spark.*;

import java.sql.SQLException;

public class Server {
    private DataAccess dataAccess;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

//        dataAccess = new MemoryDataAccess();
        try {
//            DatabaseManager.createDatabase();
            dataAccess = new SQLDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Spark.webSocket("/connect", webSocketHandler);

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

    private String register(Request req, Response res) throws DataAccessException, SQLException {
        UserHandler userHandler = new UserHandler(dataAccess);

        RegisterResponse response = userHandler.handleRegister(req);
        setStatus(response, res);

        return new Gson().toJson(response);
    }

    private String login(Request req, Response res) throws DataAccessException, SQLException {
        UserHandler userHandler = new UserHandler(dataAccess);

        LoginResponse response = userHandler.handleLogin(req);
        setStatus(response, res);

        return new Gson().toJson(response);
    }

    private String logout(Request req, Response res) throws DataAccessException {
        AuthHandler authHandler = new AuthHandler(dataAccess);
        String authToken = req.headers("authorization");

        ResponseContainer response = authHandler.handleLogout(authToken);
        setStatus(response, res);

        return new Gson().toJson(response);
    }

    private String getGameList(Request req, Response res) throws DataAccessException {
        GameHandler gameListHandler = new GameHandler(dataAccess);
        String authToken = req.headers("authorization");

        GameListResponse response = gameListHandler.handleGameList(authToken);
        setStatus(response, res);

        return new Gson().toJson(response);
    }

    private String createGame(Request req, Response res) throws DataAccessException {
        GameHandler gameCreateHandler = new GameHandler(dataAccess);
        String authToken = req.headers("authorization");

        GameCreateResponse response = gameCreateHandler.handleGameCreate(authToken, req);
        setStatus(response, res);

        return new Gson().toJson(response);
    }

    private String joinGame(Request req, Response res) throws DataAccessException {
        GameHandler gameJoinHandler = new GameHandler(dataAccess);
        String authToken = req.headers("authorization");

        ResponseContainer response = gameJoinHandler.handleGameJoin(authToken,req);
        setStatus(response, res);

        return new Gson().toJson(response);
    }

    private String clear(Request req, Response res) {
        ClearHandler clearHandler = new ClearHandler(dataAccess);

        ResponseContainer response = clearHandler.handleClear();
        setStatus(response, res);

        return new Gson().toJson(response);
    }

    private void setStatus(ResponseContainer responseContainer, Response response) {
        switch (responseContainer.getMessage()) {
            case "Error: bad request":
                response.status(400);
                break;
            case "Error: unauthorized":
                response.status(401);
                break;
            case "Error: already taken":
                response.status(403);
                break;
            case null:
                response.status(200);
                break;
            default:
                response.status(500);
        }
    }
}
