package g54ubi.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient implements IChatClient {
    private final java.net.Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ChatClient(final Socket socket) throws IOException  {
        this.socket = socket;

        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public String readLine() throws IOException {
        return in.readLine();
    }

    @Override
    public void println(final String message) {
        out.println(message);
    }
}
