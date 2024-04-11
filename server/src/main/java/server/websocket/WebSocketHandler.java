package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.GameDAO;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    public ArrayList<String> finishedGames;
    private DataAccess dataAccess;

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess=dataAccess;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, command);
            case JOIN_OBSERVER -> joinObserver(session,command);
            case MAKE_MOVE -> makeMove(session, command);
//            case LEAVE -> leave(session, command.getUsername(), command.getGameID());
            case RESIGN -> resign(command);
            case CHECK_GAME -> checkGame(command.getGameID());
        }
    }

    private void makeMove(Session session, UserGameCommand command) throws DataAccessException, InvalidMoveException, IOException {
        GameDAO gameDAO = dataAccess.getGameDAO();
        GameData gameData = gameDAO.listGames().get(Integer.parseInt(command.gameID));
        ChessGame chessGame = gameData.getGame();
        ChessMove chessMove = command.move;

        if (chessGame.validMoves(chessMove.getStartPosition()).contains(chessMove)) {
            chessGame.makeMove(chessMove);
            gameDAO.updateBoard(command.gameID, chessGame);
            var loadNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,command.gameID, null);
            loadNotification.game = chessGame;

            connections.broadcast("", loadNotification, command.gameID);

            String username = dataAccess.getAuthDAO().getUsername(command.authToken);

            var msg = username + " made a move.";
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, msg, null );
            connections.broadcast(username, notification, command.gameID);
        } else {
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "400: Bad Request.");
            session.getRemote().sendString(new Gson().toJson(notification));
        }

    }

    private void checkGame(String gameId) throws IOException {
        boolean gameOver = false;

        if (finishedGames.contains(gameId)) {
            gameOver = true;
        }

        String msg = !gameOver ? "false" : "true";

        var notification = new ServerMessage(ServerMessage.ServerMessageType.MARK_GAME_OVER, msg, null);
        connections.broadcast(" ",notification, gameId);

    }


    private void resign(UserGameCommand command) throws IOException, DataAccessException {
        String username = dataAccess.getAuthDAO().getUsername(command.authToken);

        var message = String.format("%s has resigned.", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
        connections.broadcast("", notification, command.gameID);
        finishedGames.add(command.gameID);
    }

    private void leave(Session session, UserGameCommand command) throws IOException, DataAccessException {
        String username = dataAccess.getAuthDAO().getUsername(command.authToken);
        session.disconnect();
        connections.remove(username, command.gameID);

        var message = String.format("%s has left.", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
        connections.broadcast(username, notification, command.gameID);
    }


    private void joinPlayer(Session session,UserGameCommand command) throws IOException, DataAccessException, SQLException {
        String username = dataAccess.getAuthDAO().getUsername(command.authToken);

        if (dataAccess.getGameDAO().listGames().get(Integer.parseInt(command.gameID)) != null) {
            connections.add(username, session, command.gameID);

            GameData gameData = dataAccess.getGameDAO().listGames().get(Integer.parseInt(command.gameID));

            String color = (command.playerColor == ChessGame.TeamColor.WHITE) ? "white" : "black";
            var message = String.format("%s joined the game as %s", username, color);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
            ChessGame game = gameData.getGame();
            var loadNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,command.gameID, null);
            loadNotification.game = game;

            if (command.playerColor == ChessGame.TeamColor.WHITE) {
                if (!Objects.equals(gameData.getWhiteUsername(), username)) {
                    notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR,null, "400: Bad Request.");
                    session.getRemote().sendString(new Gson().toJson(notification));
                } else {
                    connections.broadcast(username, notification, command.gameID);
                    session.getRemote().sendString(new Gson().toJson(loadNotification));
                }


            } else {
                if (!Objects.equals(gameData.getBlackUsername(), username)) {
                    notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "400: Bad Request.");
                    session.getRemote().sendString(new Gson().toJson(notification));
                } else {
                    connections.broadcast(username, notification, command.gameID);
                    session.getRemote().sendString(new Gson().toJson(loadNotification));
                }

            }
        } else {
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "400: Bad Request.");
            session.getRemote().sendString(new Gson().toJson(notification));
        }

//            dataAccess.getGameDAO().updateGame(Integer.parseInt(command.gameID), command.playerColor, username);

    }


    private void joinObserver(Session session, UserGameCommand command) throws IOException, DataAccessException {
        String username = dataAccess.getAuthDAO().getUsername(command.authToken);

        if (username != null && dataAccess.getGameDAO().listGames().get(Integer.parseInt(command.gameID)) != null) {
            connections.add(username, session, command.gameID);

            var message = String.format("%s joined the game as an observer", username);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);

            connections.broadcast(username, notification, command.gameID);

            ChessGame game = dataAccess.getGameDAO().listGames().get(Integer.parseInt(command.gameID)).getGame();
            var loadNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,command.gameID, null);
            loadNotification.game = game;

            session.getRemote().sendString(new Gson().toJson(loadNotification));
        } else {
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "400: Bad Request.");
            session.getRemote().sendString(new Gson().toJson(notification));
        }

    }


}