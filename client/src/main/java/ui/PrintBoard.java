package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class PrintBoard {

    public static void printGame(ChessBoard board, int direction) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

//         BLACK'S PERSPECTIVE
        printWhole(board, direction, out);

//        out.print("\n\n\n");
//
//        // WHITE'S PERSPECTIVE
//        printWhole(board,2,out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void printWhole(ChessBoard board, int direction, PrintStream out) {
        printHeaderFooter(direction, out);
        printBoard(board, direction, out);
        printHeaderFooter(direction, out);
    }

    private static void printHeaderFooter(int direction, PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        if (direction != 1) {
            out.print("   "+ " a "+" b "+" c "+" d "+" e "+" f "+" g "+" h " + "   ");
        } else {
            out.print("   " + " h "+" g "+" f "+" e "+" d "+" c "+" b "+" a " + "   ");
        }

        out.print(SET_BG_COLOR_DARK_GREY);
        out.print("\n");
    }


    private static void printBoard(ChessBoard board, int direction, PrintStream out) {
        out.print(SET_TEXT_COLOR_BLACK);

        if (direction == 1) {
            for (int row = 1; row < 9; row++) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.printf(" %s ", row);

                for (int col = 1; col < 9; col++) {
                    if ((col%2 == 0 && row%2 == 0) || (col%2 != 0 && row%2 != 0)) {
                        out.print(SET_BG_COLOR_WHITE);
                    } else {
                        out.print(SET_BG_COLOR_BLACK);
                    }

                    ChessPosition position = new ChessPosition(row, col);

                    if (board.getPiece(position) == null) {
                        out.print("   ");
                    } else {
                        printPiece(board.getPiece(position), out);
                    }

                    if (col == 8) {
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                        out.print(SET_TEXT_COLOR_BLACK);
                        out.printf(" %s ", row);
                        out.printf(SET_BG_COLOR_DARK_GREY+"\n");
                    }
                }
            }
        } else {
            for (int row = 8; row > 0; row--) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.printf(" %s ", row);

                for (int col = 8; col > 0; col--) {
                    if ((col%2 == 0 && row%2 == 0) || (col%2 != 0 && row%2 != 0)) {
                        out.print(SET_BG_COLOR_WHITE);
                    } else {
                        out.print(SET_BG_COLOR_BLACK);
                    }

                    ChessPosition position = new ChessPosition(row, col);

                    if (board.getPiece(position) == null) {
                        out.print("   ");
                    } else {
                        printPiece(board.getPiece(position), out);
                    }

                    if (col == 1) {
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                        out.print(SET_TEXT_COLOR_BLACK);
                        out.printf(" %s ", row);
                        out.printf(SET_BG_COLOR_DARK_GREY+"\n");
                    }
                }
            }
        }
    }

    private static void printPiece(ChessPiece piece, PrintStream out) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            out.print(SET_TEXT_COLOR_RED);
        }

        out.print(determinePieceType(piece, out));
    }

    private static String determinePieceType(ChessPiece piece, PrintStream out) {
        return switch (piece.getPieceType()) {
            case QUEEN -> " Q ";
            case ROOK -> " R ";
            case KING -> " K ";
            case KNIGHT -> " N ";
            case BISHOP -> " B ";
            case PAWN -> " P ";
            default -> "   ";
        };
    }

}