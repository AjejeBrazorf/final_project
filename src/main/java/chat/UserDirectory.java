package chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDirectory {

	private Map<String,String> userConnected;
	
	//qui gestisco utenti che si connettevano,
	//avrò mappa per cui al websocket 1234 c'è utente con nickname alfredo
	public UserDirectory() {
		userConnected = new ConcurrentHashMap<String,String>();
	}

	public void putUser(String sessionId, String name) {
		userConnected.put(sessionId, name);
		
	}

	public List<String> getUsers() {
		return new ArrayList<String>(userConnected.values());
	}

	public String getUser(String sessionId) {
		return userConnected.get(sessionId);
	}

	public void removeUser(String sessionId) {
		userConnected.remove(sessionId);
		
	}

}
