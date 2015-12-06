package g54ubi.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public final class ChatServer implements IChatServer {

	private final IChatServerSocket chatServerSocket;
	private final ArrayList<Connection> connections;
	
	public ChatServer(final IChatServerSocket chatServerSocket) {
        this.chatServerSocket = chatServerSocket;
		this.connections = new ArrayList<>();
	}

	@Override
    public void run() {
		while(true) {
			Connection c = null;
			try {
				c = new Connection(chatServerSocket.accept(), this);
			}
			catch (IOException e) {
				System.err.println("error setting up new client conneciton");
				e.printStackTrace();
			}
			Thread t = new Thread(c);
			t.start();
			connections.add(c);
		}
	}
	
	@Override
    public ArrayList<String> getUserList() {
		ArrayList<String> userList = new ArrayList<String>();
		for( Connection clientThread: connections){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				userList.add(clientThread.getUserName());
			}
		}
		return userList;
	}
	
	@Override
    public boolean doesUserExist(String newUser) {
		boolean result = false;
		for( Connection clientThread: connections){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				result = clientThread.getUserName().compareTo(newUser)==0;
			}
		}
		return result;
	}
	
	@Override
    public void broadcastMessage(String theMessage){
		System.out.println(theMessage);
		for( Connection clientThread: connections){
			clientThread.messageForConnection(theMessage + System.lineSeparator());	
		}
	}
	
	@Override
    public boolean sendPrivateMessage(String message, String user) {
		for( Connection clientThread: connections) {
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				if(clientThread.getUserName().compareTo(user)==0) {
					clientThread.messageForConnection(message + System.lineSeparator());
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public void removeDeadUsers(){
		Iterator<Connection> it = connections.iterator();
		while (it.hasNext()) {
			Connection c = it.next();
			if(!c.isRunning())
				it.remove();
		}
	}
	
	@Override
    public int getNumberOfUsers() {
		return connections.size();
	}
	
	protected void finalize() throws IOException{
		chatServerSocket.close();
	}
		
}
