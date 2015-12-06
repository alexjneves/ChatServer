package g54ubi.chat.server;

import java.io.IOException;

public interface IChatServerSocket {
    IChatClient accept() throws IOException;
    void close() throws IOException;
}
