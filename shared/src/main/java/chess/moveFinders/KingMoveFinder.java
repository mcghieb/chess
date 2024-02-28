package chess.moveFinders;

import chess.*;

import java.util.HashSet;

import static chess.moveFinders.FileBlocked.fileBlocked;

public class KingMoveFinder {
    public static HashSet<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        int initialRow = myPosition.getRow();
        int initialCol = myPosition.getColumn();
        int[][] moveList = {{1,-1}, {1,0}, {1,1}, {0,-1}, {0,1}, {-1,-1}, {-1,0}, {-1,1}};
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        for (int[] move : moveList) {
            if (!fileBlocked(board, initialRow+move[0], initialCol+move[1], color)) {
                possibleMoves.add(new ChessMove(myPosition,
                        new ChessPosition(initialRow+move[0], initialCol+move[1]),
                        null));
            }
        }

        return possibleMoves;
    }

}
