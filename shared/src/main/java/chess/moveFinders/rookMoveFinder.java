package chess.moveFinders;

import chess.*;

import java.util.HashSet;

public class rookMoveFinder {
    public static HashSet<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        int initialRow = myPosition.getRow()+1;
        int initialCol = myPosition.getColumn()+1;
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        for (int[] direction : directions) {
            int i = 1;
            while(!rookBlocked(board, initialRow+(direction[0]*i), initialCol + (direction[1]*i), color)) {
                possibleMoves.add(new ChessMove(myPosition,
                        new ChessPosition(initialRow+(direction[0]*i), initialCol + (direction[1]*i)),
                        null));
                if (specialRules.isPiece(board, initialRow+(direction[0]*i), initialCol + (direction[1]*i))
                        && board.getPiece(new ChessPosition(initialRow+(direction[0]*i), initialCol + (direction[1]*i))).getTeamColor() != color) {
                    break;
                }
                i++;
            }
        }
        return possibleMoves;
    }

    private static boolean rookBlocked(ChessBoard board, int row, int col, ChessGame.TeamColor color){
        if (!specialRules.onBoard(row, col)) { return true; }

        return specialRules.isPiece(board, row, col)
                && board.getPiece(new ChessPosition(row, col)).getTeamColor() == color;
    }
}
