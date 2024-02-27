package handler.request;

import chess.ChessGame;

public class GameJoinRequest {
    final ChessGame.TeamColor playerColor;
    final Integer gameID;

    public GameJoinRequest(ChessGame.TeamColor playerColor, Integer gameID) {
        this.playerColor=playerColor;
        this.gameID=gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }
}
