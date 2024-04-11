package client.websocket;

import client.Repl;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    Repl notificationHandler;


    public WebSocketFacade(String url, Repl notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    switch (notification.getServerMessageType()) {
                        case NOTIFICATION -> notificationHandler.notify(notification);
                        case LOAD_GAME -> notificationHandler.printGame();
                        case MARK_GAME_OVER -> markGameOver(notification);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void markGameOver(ServerMessage notification) {
        notificationHandler.client.gameOver = Objects.equals(notification.getMessage(), "true");
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String authToken, String username, String payload, String gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(authToken, username, UserGameCommand.CommandType.JOIN_PLAYER, payload, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    public void joinObserver(String authToken, String username, String gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(authToken, username, UserGameCommand.CommandType.JOIN_OBSERVER, null, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String username, String gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(null, username, UserGameCommand.CommandType.LEAVE, null, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String username, String gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(null, username, UserGameCommand.CommandType.RESIGN, null, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void checkGame(String gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(null, null, UserGameCommand.CommandType.CHECK_GAME, null, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}