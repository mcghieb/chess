package chess.moveFinders;

import chess.*;

import java.util.HashSet;

import static chess.moveFinders.FileBlocked.fileBlocked;

public class RookMoveFinder {
    public static HashSet<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        int initialRow = myPosition.getRow();
        int initialCol = myPosition.getColumn();
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        for (int[] direction : directions) {
            int i = 1;
            while(!fileBlocked(board, initialRow+(direction[0]*i), initialCol + (direction[1]*i), color)) {
                possibleMoves.add(new ChessMove(myPosition,
                        new ChessPosition(initialRow+(direction[0]*i), initialCol + (direction[1]*i)),
                        null));
                if (SpecialRules.isPiece(board, initialRow+(direction[0]*i), initialCol + (direction[1]*i))
                        && board.getPiece(new ChessPosition(initialRow+(direction[0]*i), initialCol + (direction[1]*i))).getTeamColor() != color) {
                    break;
                }
                i++;
            }
        }
        return possibleMoves;
    }

}
