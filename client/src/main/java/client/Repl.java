package client;

import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

public class Repl {
    public ChessClient client;

    public Repl(String serverURL) {
        client = new ChessClient(serverURL, this);
    }

    public void run() {
        System.out.println("WELCOME TO CHESS!!!");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg + "\n");
            }
        }

        System.out.println();
    }

    public void notify(ServerMessage msg) {
        System.out.println(msg.getMessage());
    }

    public void printGame() {
        client.printBoard();
    }
}