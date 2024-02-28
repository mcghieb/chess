package chess.moveFinders;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

public class FileBlocked {

    public static boolean fileBlocked(ChessBoard board, int row, int col, ChessGame.TeamColor color){
        if (!SpecialRules.onBoard(row, col)) { return true; }

        return SpecialRules.isPiece(board, row, col)
                && board.getPiece(new ChessPosition(row, col)).getTeamColor() == color;
    }
}
