package g54ubi.chat.integration;

import g54ubi.chat.client.IChatSession;
import g54ubi.chat.client.IChatSessionFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A wrapper around ChatSession which will wait for the expected number of responses to be received from the server
 * after each interaction. If responses are not received before the specified timeout then an assertion will trigger.
 */
public final class IntegrationTestChatSession {
    private final IChatSession chatSession;
    private final long responseTimeoutSeconds;
    private final CopyOnWriteArrayList<String> serverResponses;

    private String userName;
    private CountDownLatch serverResponseReceivedLatch;

    public IntegrationTestChatSession(final IChatSessionFactory chatSessionFactory, final long responseTimeoutSeconds) {
        this.chatSession = chatSessionFactory.create(this::responseReceived);
        this.responseTimeoutSeconds = responseTimeoutSeconds;
        this.serverResponses = new CopyOnWriteArrayList<>();
        this.userName = "";
    }

    public CopyOnWriteArrayList<String> getReceivedServerResponses() {
        return serverResponses;
    }

    public void start() {
        executeSessionCommand(chatSession::start);
    }

    public void stop() {
        chatSession.stop();
    }

    public void listCurrentUsers() {
        executeSessionCommand(chatSession::listCurrentUsers);
    }

    public void getSessionStatistics() {
        executeSessionCommand(chatSession::getSessionStatistics);
    }

    public void quit() {
        executeSessionCommand(chatSession::quit);
    }

    public void setUserName(final String userName) {
        this.userName = userName;
        executeSessionCommand(() -> chatSession.setUserName(userName));
    }

    public void broadcastMessage(final String message, final IntegrationTestChatSession... allClients) {
        for (final IntegrationTestChatSession client : allClients) {
            client.setLatch(2);
        }

        executeSessionCommand(() -> chatSession.broadcastMessage(message), 2);

        for (final IntegrationTestChatSession client : allClients) {
            client.awaitServerResponse();
        }
    }

    public void sendPrivateMessage(final IntegrationTestChatSession recipient, final String message) {
        recipient.setLatch(2);
        executeSessionCommand(() -> chatSession.sendPrivateMessage(recipient.userName, message));
        recipient.awaitServerResponse();
    }

    private void responseReceived(final String response) {
        serverResponses.add(response);
        updatedLatch();
    }

    private void updatedLatch() {
        serverResponseReceivedLatch.countDown();
    }

    private void executeSessionCommand(final IChatSessionCommand command) {
        executeSessionCommand(command, 1);
    }

    private void executeSessionCommand(final IChatSessionCommand command, final int numberOfExpectedResponses) {
        setLatch(numberOfExpectedResponses);
        command.execute();
        awaitServerResponse();
    }

    private void setLatch(final int expectedNumberOfResponse) {
        serverResponseReceivedLatch = new CountDownLatch(expectedNumberOfResponse);
    }

    private void awaitServerResponse() {
        boolean timedOut = true;

        try {
            timedOut = !serverResponseReceivedLatch.await(responseTimeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        assertThat("Timed out waiting for server response", timedOut, is(false));
    }

    private interface IChatSessionCommand {
        void execute();
    }
}
