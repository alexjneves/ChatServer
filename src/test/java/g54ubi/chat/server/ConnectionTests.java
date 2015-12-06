package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public final class ConnectionTests {
    private IChatClient mockChatClient;
    private IChatServer mockChatServer;
    private IMessageListener mockMessageListener;
    private Connection connection;

    private IMessageReceivedListener messageReceivedListener;

    @Before
    public void setUp() {
        mockChatClient = mock(IChatClient.class);
        mockChatServer = mock(IChatServer.class);

        mockMessageListener = mock(IMessageListener.class);

        doAnswer((i) -> {
            messageReceivedListener = (IMessageReceivedListener) i.getArguments()[0];
            return "";
        }).when(mockMessageListener).registerMessageReceivedListener(any());

        connection = new Connection(mockChatClient, mockChatServer, mockMessageListener);
    }

    @Test
    public void createConnection_RegistersMessageReceivedListener() {
        assertThat(mockMessageListener, is(notNullValue()));
    }

    @Test
    public void messageForConnection_SendsExpectedMessage() {

    }

    @Test
    public void getUserName_BeforeIdenHasBeenCalled_ReturnsNull() {
    }

    @Test
    public void getUserName_AfterIdenHasBeenCalled_ReturnsExpectedUserName() {
    }


}
