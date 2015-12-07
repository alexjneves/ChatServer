package g54ubi.chat.server;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConnectionTests extends ConnectionTestBase {
    @Test
    public void createConnection_RegistersMessageReceivedListener() {
        assertThat(mockMessageListenerFactory, is(notNullValue()));
    }

    @Test
    public void messageForConnection_SendsExpectedMessageToClient() {
        final String expectedMessage = "Expected Message";

        connection.messageForConnection(expectedMessage);

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }

    @Test
    public void getUserName_BeforeIdenHasBeenCalled_ReturnsNull() {
        initialiseUnregisteredConnection();

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(nullValue()));
    }
}
