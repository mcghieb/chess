package dataAccess.interfaces;

public interface UserDAO {
    public String getUser(String username);
    public void createUser(String username, String password, String email);
}
