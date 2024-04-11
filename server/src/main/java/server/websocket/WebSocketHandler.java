package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    public ArrayList<String> finishedGames;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, command.getUsername(), command.getPayload(), command.getGameId());
            case JOIN_OBSERVER -> joinObserver(session, command.getUsername(), command.getGameId());
            case LEAVE -> leave(session, command.getUsername(), command.getGameId());
            case RESIGN -> resign(command.getUsername(), command.getGameId());
            case CHECK_GAME -> checkGame(command.getGameId());
        }
    }

    private void checkGame(String gameId) throws IOException {
        boolean gameOver = false;

        if (finishedGames.contains(gameId)) {
            gameOver = true;
        }

        String msg = !gameOver ? "false" : "true";

        var notification = new ServerMessage(ServerMessage.ServerMessageType.MARK_GAME_OVER, msg);
        connections.broadcast(" ",notification, gameId);

    }


    private void resign(String username, String gameId) throws IOException {
        var message = String.format("%s has resigned.", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast("", notification, gameId);
        finishedGames.add(gameId);
    }

    private void leave(Session session, String username, String gameId) throws IOException {
        session.disconnect();
        connections.remove(username, gameId);

        var message = String.format("%s has left.", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification, gameId);
    }


    private void joinPlayer(Session session, String username, String payload, String gameId) throws IOException {
        connections.add(username, session, gameId);

        String color = payload.contains("white") ? "white" : "black";
        var message = String.format("%s joined the game as %s", username, color);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(username, notification, gameId);

        var loadNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameId);
        connections.broadcastToRoot(username, loadNotification);
    }


    private void joinObserver(Session session, String username, String gameId) throws IOException {
        connections.add(username, session, gameId);

        var message = String.format("%s joined the game as an observer", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(username, notification, gameId);

        var loadNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameId);
        connections.broadcastToRoot(username, loadNotification);
    }


}