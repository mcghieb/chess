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
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        
        HashSet<ChessMove> diagUpRight = moveDiagonally(board, myPosition, 1, 1);
        HashSet<ChessMove> diagUpLeft = moveDiagonally(board, myPosition, 1, -1);
        HashSet<ChessMove> diagDownRight = moveDiagonally(board, myPosition, -1, 1);
        HashSet<ChessMove> diagDownLeft = moveDiagonally(board, myPosition, -1, -1);
        
        possibleMoves.addAll(diagUpRight);
        possibleMoves.addAll(diagUpLeft);
        possibleMoves.addAll(diagDownRight);
        possibleMoves.addAll(diagDownLeft);
        
        return possibleMoves;
    }
    
    private static HashSet<ChessMove> moveDiagonally(ChessBoard board, ChessPosition myPosition, int rowMod, int colMod) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int initialRow = myPosition.getRow()+1;
        int initialCol = myPosition.getColumn()+1;
        ChessPosition initialPosition = new ChessPosition(initialRow, initialCol);


        ChessGame.TeamColor initialColor = board.getPiece(new ChessPosition(initialRow,initialCol)).getTeamColor();

        for (int i = 1; true ; i++){
            int newRow = initialRow + (rowMod*i);
            int newCol = initialCol + (colMod*i);
            ChessPosition newPosition = new ChessPosition(newRow, newCol);

            // if space blank, add then continue
            // if it is a piece of same color, break, else add then break
            if (!specialRules.onBoard(newRow, newCol)){ break; }

            if (specialRules.isPiece(board, newRow, newCol)) {
              if (board.getPiece(newPosition).getTeamColor() != initialColor) {
                possibleMoves.add(new ChessMove(initialPosition, newPosition, null));
                break;
              } else if (board.getPiece(newPosition).getTeamColor() == initialColor) {
                break;
              }
            }

            possibleMoves.add(new ChessMove(initialPosition, newPosition, null));
        }

        return possibleMoves;
    }

}
