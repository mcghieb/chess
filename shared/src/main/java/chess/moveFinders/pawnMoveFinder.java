package chess.moveFinders;

import chess.*;
import chess.ChessMove;
import java.util.HashSet;

public class pawnMoveFinder {
  public static HashSet<ChessMove> findMoves(ChessBoard board,ChessPosition myPosition) {
      HashSet<ChessMove> possibleMoves = new HashSet<>();
      ChessPiece thisPawn = board.getPiece(myPosition);
      int initialRow = myPosition.getRow()+1;
      int col = myPosition.getColumn()+1;

      HashSet<ChessMove> possibleTakes = possibleTakes(board, myPosition);
      HashSet<ChessMove> forwardMoves = forwardMoves(board, myPosition);

      if (!possibleTakes.isEmpty()) {
          possibleMoves.addAll(possibleTakes);
      }
      if (!forwardMoves.isEmpty()){
          possibleMoves.addAll(forwardMoves);
      }
      return possibleMoves;
  }


  private static boolean pawnBlocked(ChessBoard board, ChessPosition myPosition) {
      int initialRow = myPosition.getRow()+1;
      int initialCol = myPosition.getColumn()+1;
      ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
      int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;

      return specialRules.isPiece(board, initialRow + direction, initialCol);
  }


  private static HashSet<ChessMove> forwardMoves(ChessBoard board, ChessPosition myPosition) {
      HashSet<ChessMove> possibleForward = new HashSet<>();
      int initialRow = myPosition.getRow()+1;
      int col = myPosition.getColumn()+1;
      ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

      int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
      boolean firstMove = (color == ChessGame.TeamColor.WHITE && initialRow == 2) ||
              (color == ChessGame.TeamColor.BLACK && initialRow == 7);
      boolean lastMove = (color == ChessGame.TeamColor.WHITE && initialRow == 7) ||
              (color == ChessGame.TeamColor.BLACK && initialRow == 2);

      if (firstMove && !pawnBlocked(board, myPosition)) {
          possibleForward.add(new ChessMove(myPosition, new ChessPosition(initialRow+direction, col), null));
          if (board.getPiece(new ChessPosition(initialRow+(direction*2), col)) == null) {
              possibleForward.add(new ChessMove(myPosition, new ChessPosition(initialRow+(direction*2), col), null));
          }
      }

      else if (lastMove && !pawnBlocked(board, myPosition)) {
          possibleForward.add(new ChessMove(myPosition, new ChessPosition(initialRow+direction, col), ChessPiece.PieceType.QUEEN));
          possibleForward.add(new ChessMove(myPosition, new ChessPosition(initialRow+direction, col), ChessPiece.PieceType.ROOK));
          possibleForward.add(new ChessMove(myPosition, new ChessPosition(initialRow+direction, col), ChessPiece.PieceType.BISHOP));
          possibleForward.add(new ChessMove(myPosition, new ChessPosition(initialRow+direction, col), ChessPiece.PieceType.KNIGHT));
      }

      else if (!pawnBlocked(board, myPosition)) {
          possibleForward.add(new ChessMove(myPosition, new ChessPosition(initialRow+direction, col), null));
      }


      return possibleForward;
  }


  private static HashSet<ChessMove> possibleTakes(ChessBoard board, ChessPosition myPosition) {
      HashSet<ChessMove> possibleTakes=new HashSet<>();
      int initialRow=myPosition.getRow() + 1;
      int initialCol=myPosition.getColumn() + 1;
      ChessGame.TeamColor color=board.getPiece(myPosition).getTeamColor();
      int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;

      if (specialRules.isPiece(board, initialRow + direction, initialCol + 1)
              && board.getPiece(new ChessPosition(initialRow + direction, initialCol + 1)).getTeamColor() != color) {
          if (initialRow + direction == 0) {
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol + 1), ChessPiece.PieceType.KNIGHT));
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol + 1), ChessPiece.PieceType.BISHOP));
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol + 1), ChessPiece.PieceType.ROOK));
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol + 1), ChessPiece.PieceType.QUEEN));
          } else {
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(initialRow + direction, initialCol + 1), null));
          }
      }

      if (specialRules.isPiece(board, initialRow + direction, initialCol - 1)
              && board.getPiece(new ChessPosition(initialRow + direction, initialCol - 1)).getTeamColor() != color) {
          if (initialRow + direction == 0) {
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol - 1), ChessPiece.PieceType.KNIGHT));
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol - 1), ChessPiece.PieceType.BISHOP));
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol - 1), ChessPiece.PieceType.ROOK));
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol - 1), ChessPiece.PieceType.QUEEN));
          } else {
              possibleTakes.add(new ChessMove(myPosition, new ChessPosition(initialRow + direction, initialCol - 1), null));
          }
      }

      return possibleTakes;
  }
}
