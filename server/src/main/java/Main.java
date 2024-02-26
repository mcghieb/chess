import chess.*;
import server.ChessServer;

public class Main {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
        try {
            var port = 8080;
            if (args.length > 1) {
                port = Integer.parseInt(args[0]);
            }

            // IMPLEMENT CHESSSERVER
            var server = new ChessServer().run(port);
            port = server.port();
            System.out.printf("Server started on port %d%n", port);

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

    }
}