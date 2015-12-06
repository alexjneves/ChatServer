package g54ubi.chat.server;

import java.util.ArrayList;

public interface IChatServer {
    void run();
    ArrayList<String> getUserList();
    boolean doesUserExist(String newUser);
    void broadcastMessage(String theMessage);
    boolean sendPrivateMessage(String message, String user);
    void removeDeadUsers();
    int getNumberOfUsers();
}
