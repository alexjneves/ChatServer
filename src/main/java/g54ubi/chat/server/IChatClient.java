package g54ubi.chat.server;

import java.io.IOException;

public interface IChatClient {
    void close() throws IOException;
    String readMessage() throws IOException;
    void sendMessage(String message);
}
