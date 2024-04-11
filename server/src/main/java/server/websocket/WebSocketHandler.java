package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, command.getUsername(), command.getPayload(), command.getGameId());
            case JOIN_OBSERVER -> joinObserver(session, command.getUsername(), command.getGameId());
            case LEAVE -> leave(session, command.getUsername(), command.getGameId());
        }
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