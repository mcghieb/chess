package chess.moveFinders;
import chess.*;

public class SpecialRules {
 /**
  * Returns True if a piece is found on the board
  *
  * @param board
  * @param row
  * @param col
  * @Return: Boolean
  */
  public static boolean isPiece(ChessBoard board, int row, int col){
      return onBoard(row, col) && board.getPiece(new ChessPosition(row, col)) != null;
  }

  /**
   * Returns True if position is on board
   * @param row
   * @param col
   * @return
   */
  public static boolean onBoard(int row, int col){
      return row >= 1 && col >= 1 && row <= 8 && col <= 8;
  }

}
