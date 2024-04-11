package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken, CommandType commandType, ChessGame.TeamColor playerColor, String gameId) {
        this.authToken = authToken;
        this.playerColor=playerColor;
        this.commandType = commandType;
        this.gameID= gameId;
    }

    public String getGameID() {
        return gameID;
    }


    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        CHECK_GAME
    }

    protected CommandType commandType;
    public String authToken;
    public ChessGame.TeamColor playerColor;
    public String gameID;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}