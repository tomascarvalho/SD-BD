package ws;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.server.ServerEndpoint;

import fundstarter.model.ConnectToRMIBean;

import javax.servlet.http.HttpSession;
import javax.websocket.*;

@ServerEndpoint(value = "/ws", configurator = HandShake.class)
public class WSEndPoint {

	private Session session;
	private ConnectToRMIBean sessionUser;
	private HttpSession httpSession;
	private static final Set<WSEndPoint> myConnections = new CopyOnWriteArraySet<WSEndPoint>();

	public WSEndPoint() {
	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {

		this.session = session;
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		myConnections.add(this);
		this.sessionUser = (ConnectToRMIBean) httpSession.getAttribute("RMIBean");
		// notifyAdmin();
	}

	@OnClose
	public void onClose() {
		myConnections.remove(this);
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@OnMessage
	public void onMessage(String message) throws IOException {
		
		String[] data = message.split("/");
		ArrayList<Integer> projectAdmins = null;
		String newMessage = "";
		int projectID = Integer.parseInt(data[1]);
		
		if (data[0].equals("gotMessage")) {
			
			newMessage = "Tem uma mensagem nova!";
		} else {
			newMessage = "Recebeu uma doação de " + data[0] + " euros no Projecto com Id: " + data[1];
		}

		try {
			projectAdmins = this.sessionUser.getProjectAdmins(projectID);	
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} 	
		if (projectAdmins == null)
			return;

		for (WSEndPoint person : myConnections) {

			for (int i = 0; i < projectAdmins.size(); i++) {

				if (person.sessionUser.getUserID() == projectAdmins.get(i)) {
					try {
						person.session.getBasicRemote().sendText(newMessage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void notifyAdmin() {

		if (sessionUser == null)
			return;
		else {
			for (WSEndPoint person : myConnections) {
				try {
					person.session.getBasicRemote().sendText("TEST MESSAGE");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
