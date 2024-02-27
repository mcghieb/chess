import dataAccess.MemoryDataAccess;
import server.Server;

public class Main {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
        try {
            var port = 8080;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            // IMPLEMENT CHESSSERVER
            int actualPort = new Server().run(port);
            System.out.println("Port: " + actualPort);

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

    }
}