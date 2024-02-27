package dataAccess.memorydataaccess;

import model.UserData;
import dataAccess.interfaces.UserDAO;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class MemoryUserDAO implements UserDAO {
    private Vector<String> usernameList = new Vector<>();
    private HashMap<String, UserData> users = new HashMap<>();

    public MemoryUserDAO() {
    }

    public String getUser(String username) {
        if (usernameList.contains(username)) {
            return username;
        }
        return null;
    }

    public void createUser(String username, String password, String email) {
        usernameList.add(username);
        users.put(username, new UserData(username, password, email));
    }

    public String checkCredentials(String username, String password) {
        UserData userData = users.get(username);
        if (userData != null && Objects.equals(userData.getPassword(), password)) {
            return username;
        }

        return null;
    }
}
