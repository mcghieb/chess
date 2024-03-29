package chess.moveFinders;

import chess.*;

import java.util.HashSet;

public class KnightMoveFinder {
    public static HashSet<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        int initialRow = myPosition.getRow();
        int initialCol = myPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int[][] knightMoves = {{2, 1}, {2, -1}, {-2, -1}, {-2, 1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        for (int[] move : knightMoves) {
            if (!knightBlocked(board, color, initialRow + move[0], initialCol + move[1])) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(initialRow + move[0], initialCol + move[1]), null));
            }
        }

        return possibleMoves;
    }

    private static boolean knightBlocked(ChessBoard board, ChessGame.TeamColor color, int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);
        if (!SpecialRules.onBoard(row, col)) { return true; }

        return SpecialRules.isPiece(board, row, col) && board.getPiece(newPosition).getTeamColor() == color;
    }
}
