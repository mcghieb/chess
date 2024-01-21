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


      if (!possibleTakes.isEmpty()) {
          possibleMoves.addAll(possibleTakes);
      }
      return possibleMoves;
  }

//  private static boolean pawnBlocked(ChessBoard board, ChessPosition myPosition) {
//      int initialRow = myPosition.getRow()+1;
//      int initialCol = myPosition.getColumn()+1;
//      ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
//      if (board.getPiece(new ChessPosition(initialRow+1,initialCol)).getTeamColor() != color) {
//
//      }
//  }

  private static HashSet<ChessMove> possibleTakes(ChessBoard board, ChessPosition myPosition) {
      HashSet<ChessMove> possibleTakes = new HashSet<>();
      int initialRow = myPosition.getRow()+1;
      int initialCol = myPosition.getColumn()+1;
      ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

      if (color == ChessGame.TeamColor.BLACK){
          if (specialRules.isPiece(board, initialRow-1, initialCol+1)
                  && board.getPiece(new ChessPosition(initialRow-1,initialCol+1)).getTeamColor() != color) {
              if (initialRow-1 == 0) {
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol+1), ChessPiece.PieceType.KNIGHT));
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol+1), ChessPiece.PieceType.BISHOP));
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol+1), ChessPiece.PieceType.ROOK));
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol+1), ChessPiece.PieceType.QUEEN));
              } else {
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(initialRow - 1, initialCol + 1), null));
              }
          }
          
          if (specialRules.isPiece(board, initialRow-1, initialCol-1)
                  && board.getPiece(new ChessPosition(initialRow-1,initialCol-1)).getTeamColor() != color) {
              if (initialRow-1 == 0) {
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol-1), ChessPiece.PieceType.KNIGHT));
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol-1), ChessPiece.PieceType.BISHOP));
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol-1), ChessPiece.PieceType.ROOK));
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(0, initialCol-1), ChessPiece.PieceType.QUEEN));
              } else {
                  possibleTakes.add(new ChessMove(myPosition, new ChessPosition(initialRow - 1, initialCol - 1), null));
              }
          }
      }

      return possibleTakes;
  }
}
