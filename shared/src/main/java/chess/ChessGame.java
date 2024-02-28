package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import chess.moveFinders.SpecialRules;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turn;


    public ChessGame() {
        turn= TeamColor.WHITE;
        board= new ChessBoard();
        board.resetBoard();

    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }


    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn= team;
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
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        HashSet<ChessMove> validMoves = new HashSet<>();
        TeamColor teamColor = board.getPiece(startPosition).getTeamColor();


        for (ChessMove move : possibleMoves) {
            ChessPiece pieceAtEndPosition = board.getPiece(move.getEndPosition());

            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(startPosition, null);

            if (!isInCheck(teamColor)) {
                validMoves.add(move);
            }

            board.addPiece(startPosition, piece);
            board.addPiece(move.getEndPosition(), pieceAtEndPosition);
        }

        return validMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor pieceColor = piece.getTeamColor();
        TeamColor oppositeTeam = (pieceColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> validMoveList = validMoves(startPosition);


        if (pieceColor!= turn || !validMoveList.contains(move)) {
            throw new InvalidMoveException("InvalidMoveException");
        }

        if (move.getPromotionPiece() == null) {
            board.addPiece(endPosition, piece);
            board.addPiece(startPosition, null);
        } else {
            ChessPiece promotionPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(endPosition, promotionPiece);
            board.addPiece(startPosition, null);
        }

        setTeamTurn(oppositeTeam);

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
                if (move.getEndPosition().equals(kingPosition)) {
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
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> validMoves = validMoves(position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (SpecialRules.isPiece(board, row, col) && board.getPiece(position).getTeamColor() == teamColor && !validMoves(position).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board= board;
    }


    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    private ChessPosition findKing(TeamColor color) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition possiblePosition = new ChessPosition(row, col);

                if (SpecialRules.isPiece(board, row, col) && board.getPiece(possiblePosition).getPieceType() == ChessPiece.PieceType.KING
                        && board.getPiece(possiblePosition).getTeamColor() == color) {
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
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == color) {
                    allMoves.add(piece.pieceMoves(board, position));
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
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }
}
