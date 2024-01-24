package chess;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition _start;
    private ChessPosition _end;
    private ChessPiece.PieceType _piece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this._start = startPosition;
        this._end = endPosition;
        this._piece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return _start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return _end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return _piece;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("start: ").append(_start.getRow()).append(",").append(_start.getColumn()).append(";");
        stringBuilder.append("end: ").append(_end.getRow()).append(",").append(_end.getColumn()).append(";");
        stringBuilder.append("piece:").append(_piece).append("||");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove=(ChessMove)o;
        return Objects.equals(_start, chessMove._start) && Objects.equals(_end, chessMove._end) && Objects.equals(_piece, chessMove._piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_start, _end, _piece);
    }
}
