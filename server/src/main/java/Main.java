import server.Server;

public class Main {
    public static void main(String[] args) {

        try {
            var port = 8080;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }           int actualPort = new Server().run(port);
            System.out.println("Port: " + actualPort);

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

    }
}