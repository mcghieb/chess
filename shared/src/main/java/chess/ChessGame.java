package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import chess.*;
import chess.moveFinders.specialRules;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard _board;
    private TeamColor _turn;


    public ChessGame() {
        _turn = TeamColor.WHITE;
        _board = new ChessBoard();
        _board.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return _turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        _turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        TeamColor opposingTeamColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        HashSet<Collection<ChessMove>> opposingMoves = findAllMoves(opposingTeamColor);

        for (Collection<ChessMove> pieceMoves : opposingMoves) {
            for (ChessMove move : pieceMoves) {
                if (move.getEndPosition() == kingPosition) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        _board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return _board;
    }


    // SOMETHING IS WRONG HERE, THE POSITION OF THE BLACK KING SHOULD BE
    private ChessPosition findKing(TeamColor color) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition possiblePosition = new ChessPosition(row, col);

                if (specialRules.isPiece(_board, row, col) && _board.getPiece(possiblePosition).getPieceType() == ChessPiece.PieceType.KING
                        && _board.getPiece(possiblePosition).getTeamColor() == color) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }

    private HashSet<Collection<ChessMove>> findAllMoves(TeamColor color) {
        HashSet<Collection<ChessMove>> allMoves = new HashSet<>();

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = _board.getPiece(position);
                if (piece != null && piece.getTeamColor() == color) {
                    allMoves.add(piece.pieceMoves(_board, position));
                }
            }
        }

        return allMoves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return Objects.equals(_board, chessGame._board) && _turn == chessGame._turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_board, _turn);
    }
}
