package client;

import java.util.Scanner;

public class Repl {
    public ChessClient client;

    public Repl(String serverURL) {
        client = new ChessClient(serverURL);
    }

    public void run() {
        System.out.println("WELCOME TO CHESS!!!");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }

        System.out.println();
    }

}