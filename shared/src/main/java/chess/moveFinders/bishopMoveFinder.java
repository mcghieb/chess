package chess.moveFinders;

import chess.*;

import java.util.HashSet;

public class bishopMoveFinder {

  /**
   * Returns a HashSet of Valid Moves.
   * @param board
   * @param myPosition
   * @return
   */
  public static HashSet<ChessMove> findMoves(ChessBoard board,ChessPosition myPosition) {
        int[][] directions = {{1,1}, {1,-1}, {-1,-1}, {-1,1}};
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        int initialRow = myPosition.getRow();
        int initialCol = myPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        for (int[] diag : directions) {
            for (int length = 1; length < 9; length++) {
                int newRow = initialRow+(diag[0]*length);
                int newCol = initialCol+(diag[1]*length);
                ChessPosition newPosition = new ChessPosition(newRow, newCol);

                if (bishopBlocked(board, newPosition, color)) { break; }

                if (specialRules.isPiece(board, newRow, newCol) && board.getPiece(newPosition).getTeamColor() != color) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        
        return possibleMoves;
  }

  private static boolean bishopBlocked(ChessBoard board, ChessPosition newPosition, ChessGame.TeamColor color) {
      int newRow = newPosition.getRow();
      int newCol = newPosition.getColumn();

      if (!specialRules.onBoard(newRow,newCol)) { return true; }

      return specialRules.isPiece(board, newRow,newCol) && board.getPiece(newPosition).getTeamColor() == color;
  }
}
