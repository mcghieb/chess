package chess.moveFinders;

import chess.*;
import chess.ChessMove;

import javax.imageio.event.IIOWriteWarningListener;
import java.util.HashSet;

public class pawnMoveFinder {
  public static HashSet<ChessMove> findMoves(ChessBoard board,ChessPosition myPosition) {
      HashSet<ChessMove> possibleMoves = new HashSet<>();
      HashSet<ChessMove> possibleForward = forwardMoves(board,new ChessPosition(myPosition.getRow(), myPosition.getColumn()));
      HashSet<ChessMove> possibleDiag = possibleTakes(board, myPosition);
      if (!possibleForward.isEmpty()) {
          possibleMoves.addAll(possibleForward);
      }
      if (!possibleDiag.isEmpty()) {
          possibleMoves.addAll(possibleDiag);
      }

      return possibleMoves;
  }

  private static HashSet<ChessMove> forwardMoves(ChessBoard board,ChessPosition myPosition) {
      HashSet<ChessMove> possibleForward = new HashSet<>();
      int initialRow = myPosition.getRow();
      int initialCol = myPosition.getColumn();
      ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
      int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
      boolean firstMove = (color == ChessGame.TeamColor.WHITE && initialRow == 2) || (color == ChessGame.TeamColor.BLACK && initialRow == 7);
      boolean lastMove = (color == ChessGame.TeamColor.WHITE && initialRow == 7) || (color == ChessGame.TeamColor.BLACK && initialRow == 2);

      ChessPosition moveOne = new ChessPosition(initialRow+direction, initialCol);
      ChessPosition moveTwo = new ChessPosition(initialRow+direction+direction, initialCol);

      if (!pawnForwardBlocked(board, moveOne) && !lastMove) {
          possibleForward.add(new ChessMove(myPosition, moveOne, null));
          if (firstMove && !pawnForwardBlocked(board, moveTwo)){
              possibleForward.add(new ChessMove(myPosition, moveTwo, null));
          }
      }
      else if (lastMove && !pawnForwardBlocked(board, moveOne)) {
          possibleForward.add(new ChessMove(myPosition, moveOne, ChessPiece.PieceType.KNIGHT));
          possibleForward.add(new ChessMove(myPosition, moveOne, ChessPiece.PieceType.BISHOP));
          possibleForward.add(new ChessMove(myPosition, moveOne, ChessPiece.PieceType.ROOK));
          possibleForward.add(new ChessMove(myPosition, moveOne, ChessPiece.PieceType.QUEEN));
      }

      return possibleForward;
  }

  private static boolean pawnForwardBlocked(ChessBoard board, ChessPosition thisPosition) {
      if (!specialRules.onBoard(thisPosition.getRow(), thisPosition.getColumn())) {
          return false;
      }
      return board.getPiece(thisPosition) != null;
  }

  private static HashSet<ChessMove> possibleTakes(ChessBoard board, ChessPosition myPosition) {
      HashSet<ChessMove> possibleTakes = new HashSet<>();
      int initialRow = myPosition.getRow();
      int initialCol = myPosition.getColumn();
      ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
      int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
      boolean lastMove = (color == ChessGame.TeamColor.WHITE && initialRow == 7) || (color == ChessGame.TeamColor.BLACK && initialRow == 2);
      ChessPosition pieceRight = new ChessPosition(initialRow+direction, initialCol+1);
      ChessPosition pieceLeft = new ChessPosition(initialRow+direction, initialCol-1);


      if (!lastMove) {
          if (specialRules.isPiece(board, initialRow+direction, initialCol+1) && board.getPiece(pieceRight).getTeamColor() != color) {
              possibleTakes.add(new ChessMove(myPosition, pieceRight, null));
          }
          if (specialRules.isPiece(board, initialRow+direction, initialCol-1) && board.getPiece(pieceLeft).getTeamColor() != color) {
              possibleTakes.add(new ChessMove(myPosition, pieceLeft, null));
          }
      } else if (lastMove) {
          if (specialRules.isPiece(board, initialRow+direction, initialCol+1) && board.getPiece(pieceRight).getTeamColor() != color) {
              possibleTakes.add(new ChessMove(myPosition, pieceRight, ChessPiece.PieceType.KNIGHT));
              possibleTakes.add(new ChessMove(myPosition, pieceRight, ChessPiece.PieceType.BISHOP));
              possibleTakes.add(new ChessMove(myPosition, pieceRight, ChessPiece.PieceType.ROOK));
              possibleTakes.add(new ChessMove(myPosition, pieceRight, ChessPiece.PieceType.QUEEN));
          }
          if (specialRules.isPiece(board, initialRow+direction, initialCol-1) && board.getPiece(pieceLeft).getTeamColor() != color) {
              possibleTakes.add(new ChessMove(myPosition, pieceLeft, ChessPiece.PieceType.KNIGHT));
              possibleTakes.add(new ChessMove(myPosition, pieceLeft, ChessPiece.PieceType.BISHOP));
              possibleTakes.add(new ChessMove(myPosition, pieceLeft, ChessPiece.PieceType.ROOK));
              possibleTakes.add(new ChessMove(myPosition, pieceLeft, ChessPiece.PieceType.QUEEN));
          }
      }

      return possibleTakes;
  }

}