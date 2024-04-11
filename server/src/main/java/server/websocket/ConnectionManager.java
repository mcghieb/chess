package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, ArrayList<Connection>> games = new ConcurrentHashMap<>();


    public void add(String username, Session session, String gameId) {
        var connection = new Connection(username, session);

        if (games.containsKey(gameId)) {
            ArrayList<Connection> conns = games.get(gameId);
            conns.add(connection);
            games.put(gameId, conns);
        } else {
            ArrayList<Connection> conns = new ArrayList<>();
            conns.add(connection);
            games.put(gameId, conns);
        }
    }

    public void remove(String username) {
        for (var connections : games.values()) {
            Iterator<Connection> iterator = connections.iterator();

            while (iterator.hasNext()) {
                Connection connection = iterator.next();
                if (Objects.equals(connection.username, username)) {
                    iterator.remove();
                }
            }
        }
    }

    public void broadcast(String excludeUsername, ServerMessage notification, String gameId) throws IOException {
        var removeList = new ArrayList<Connection>();
        ArrayList<Connection> connections = games.get(gameId);

        for (var c : connections) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any games that were left open.
        for (var c : removeList) {
            games.remove(c.username);
        }
    }

    public void broadcastToRoot(String username, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        ArrayList<Connection> connections = games.get(notification.getMessage());

        for (var c : connections) {
            if (c.session.isOpen()) {
                if (c.username.equals(username)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any games that were left open.
        for (var c : removeList) {
            games.remove(c.username);
        }
    }
}