package chess;

import java.util.HashSet;
import java.util.Collection;
import java.util.Objects;
import chess.moveFinders.bishopMoveFinder;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor _color;
    private ChessPiece.PieceType _type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        _color = pieceColor;
        _type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return _color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return _type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves of class ChessMove
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();

        // Bishop testing stuff
        if (this._type == PieceType.BISHOP){
            return bishopMoveFinder.findMoves(board, myPosition);
        }

        return possibleMoves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return _color == that._color && _type == that._type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_color, _type);
    }
}
