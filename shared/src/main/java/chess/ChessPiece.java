package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        // Bishop testing stuff
        if (this._type == PieceType.BISHOP){

            final int row =myPosition.getRow();
            final int col = myPosition.getColumn();

            for (int i=1; i<=5; i++) {

                if (onBoard(row+i,col+i)){
                    possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row+i,col+i), null));
                }
                if (onBoard(row+i,col-i)){
                    possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row+i,col-i), null));
                }
                if (onBoard(row-i,col-i)){
                    possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row-i,col-i), null));
                }
                if (onBoard(row-i,col+i)){
                    possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row-i,col+i), null));
                }
            }
        }

        return possibleMoves;
    }

    /**
     * Calculates whether a specific position is on the board.
     *
     * @return a boolean
     */
    public boolean onBoard(int row, int col) {
      return row >= 1 && col >= 1 && row <= 8 && col <= 8;
    }
}
