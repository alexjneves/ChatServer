package g54ubi.chat.server;

import java.io.IOException;

public interface IChatClient {
    void close() throws IOException;
    String readLine() throws IOException;
    void println(String message);
}
