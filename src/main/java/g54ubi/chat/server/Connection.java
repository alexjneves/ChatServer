package g54ubi.chat.server;

import java.io.IOException;
import java.util.ArrayList;

public final class Connection implements Runnable {
	final static int STATE_UNREGISTERED = 0;
	final static int STATE_REGISTERED = 1;
	
	private volatile boolean running;
	private int messageCount;
	private int state;
	private IChatClient chatClient;
    private IMessageListener messageListener;
    private IChatServer serverReference;
	private String username;
	
	public Connection(final IChatClient chatClient, final IChatServer chatServer, final IMessageListenerFactory messageListenerFactory) {
		this.serverReference = chatServer;
		this.chatClient = chatClient;
        this.messageListener = messageListenerFactory.create(this::validateMessage);
        this.state = STATE_UNREGISTERED;
		messageCount = 0;
	}

	@Override
	public void run() {
        sendOverConnection("OK Welcome to the chat server, there are currently " + serverReference.getNumberOfUsers() + " user(s) online");
        messageListener.listen();
	}
	
	private void validateMessage(String message) {
		
		if(message.length() < 4){
			sendOverConnection ("BAD invalid command to server");
		} else {
			switch(message.substring(0,4)){
				case "LIST":
					list();
					break;
					
				case "STAT":
					stat();
					break;
					
				case "IDEN":
					iden(message.substring(5));
					break;
					
				case "HAIL":
					hail(message.substring(5));
					break;
				
				case "MESG":
					mesg(message.substring(5));
					break;
				
				case "QUIT":
					quit();
					break;
				
				default:
					sendOverConnection("BAD command not recognised");
					break;
			}
		}
			
	}
	
	private void stat() {
		String status = "There are currently "+ serverReference.getNumberOfUsers()+" user(s) on the server ";
		switch(state) {
			case STATE_REGISTERED:
				status += "You are logged im and have sent " + messageCount + " message(s)";
				break;
			
			case STATE_UNREGISTERED:
				status += "You have not logged in yet";
				break;		
		}
		sendOverConnection("OK " + status);
	}
	
	private void list() {
		switch(state) {
			case STATE_REGISTERED:
				ArrayList<String> userList = serverReference.getUserList();
				String userListString = new String();
				for(String s: userList) {
					userListString += s + ", ";
				}
				sendOverConnection("OK " + userListString);
				break;
			
			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}
		
	}
	
	private void iden(String message) {
		switch(state) {
			case STATE_REGISTERED:
				sendOverConnection("BAD you are already registerd with username " + username);
				break;
			
			case STATE_UNREGISTERED:
				String username = message.split(" ")[0];
				if(serverReference.doesUserExist(username)) {
					sendOverConnection("BAD username is already taken");
				} else {
					this.username = username;
					state = STATE_REGISTERED;
					sendOverConnection("OK Welcome to the chat server " + username);			
				}
				break;
		}	
	}
	
	private void hail(String message) {
		switch(state) {
			case STATE_REGISTERED:
				serverReference.broadcastMessage("Broadcast from " + username + ": " + message);
				messageCount++;
				break;
			
			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}
	}

	public boolean isRunning(){
		return running;
	}
	
	private void mesg(String message) {
		
		switch(state) {
			case STATE_REGISTERED:
				
				if(message.contains(" ")) {
					int messageStart = message.indexOf(" ");
					String user = message.substring(0, messageStart);
					String pm = message.substring(messageStart+1);
					if(serverReference.sendPrivateMessage("PM from " + username + ":" + pm, user)){
						sendOverConnection("OK your message has been sent");
					} else {
						sendOverConnection("BAD the user does not exist");
					}	
				}
				else{
					sendOverConnection("BAD Your message is badly formatted");
				}
				break;
			
			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}
	}
	
	private void quit() {
		switch(state) {
			case STATE_REGISTERED:
				sendOverConnection("OK thank you for sending " + messageCount + " message(s) with the chat service, goodbye. ");
				break;
			case STATE_UNREGISTERED:
				sendOverConnection("OK goodbye");
				break;
		}
		running = false;
        messageListener.stop();
		try {
			chatClient.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		serverReference.removeDeadUsers();
	}
	
	private synchronized void sendOverConnection (String message){
		chatClient.sendMessage(message);
	}
	
	public void messageForConnection (String message){
		sendOverConnection(message);
	}
	
	public int getState() {
		return state;
	}
	
	public String getUserName() {
		return username;
	}
	
}

	