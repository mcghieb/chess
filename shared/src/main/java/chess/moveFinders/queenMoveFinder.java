package chess.moveFinders;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class queenMoveFinder {
    public static HashSet<ChessMove> findMoves (ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        HashSet<ChessMove> diagonals = bishopMoveFinder.findMoves(board, myPosition);
        HashSet<ChessMove> files = rookMoveFinder.findMoves(board, myPosition);

        if (!diagonals.isEmpty()) {
            possibleMoves.addAll(diagonals);
        }
        if (!files.isEmpty()) {
            possibleMoves.addAll(files);
        }

        return possibleMoves;
    }
}
