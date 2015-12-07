package g54ubi.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public final class ChatServer implements IChatServer {
    private final IResourceListener<IConnection> connectionListener;
    private final ArrayList<IConnection> connections;

    public ChatServer(final IConnectionListenerFactory connectionListenerFactory) {
        this.connectionListener = connectionListenerFactory.create(this);
		this.connections = new ArrayList<>();
	}

	@Override
    public void start() {
        connectionListener.listen(connections::add);
	}

	@Override
    public ArrayList<String> getUserList() {
		ArrayList<String> userList = new ArrayList<String>();
		for( IConnection clientThread: connections){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				userList.add(clientThread.getUserName());
			}
		}
		return userList;
	}
	
	@Override
    public boolean doesUserExist(String newUser) {
		boolean result = false;
		for( IConnection clientThread: connections){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				result = clientThread.getUserName().compareTo(newUser)==0;
			}
		}
		return result;
	}
	
	@Override
    public void broadcastMessage(String theMessage){
		System.out.println(theMessage);
		for( IConnection clientThread: connections){
			clientThread.messageForConnection(theMessage + System.lineSeparator());	
		}
	}
	
	@Override
    public boolean sendPrivateMessage(String message, String user) {
		for( IConnection clientThread: connections) {
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
		Iterator<IConnection> it = connections.iterator();
		while (it.hasNext()) {
            IConnection c = it.next();
			if(!c.isRunning())
				it.remove();
		}
	}
	
	@Override
    public int getNumberOfUsers() {
		return connections.size();
	}
	
	protected void finalize() throws IOException{
        connectionListener.stop();
	}
		
}
