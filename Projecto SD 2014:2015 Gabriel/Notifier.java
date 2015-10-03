import java.util.ArrayList;
import java.io.IOException;

public class Notifier {
	
	ArrayList<Client> clients;

	public Notifier () {
		this.clients = new ArrayList<Client> ();
	}
	
	public synchronized void sendNotification (String user, Data d) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().equals(user)) {
				//ensiar a notificação
				try {
					clients.get(i).getOutputStream().writeObject(d);
				} catch(IOException e) {
				}
				break;
			}
		}
	}

	public int isOnline (String user) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().equals(user)) {
				return 1;
			}
		}
		return 0;
	}

	public void addClient (Client c) {
		this.clients.add(c);
		Utils.print("cliente adicionado " + c.getUsername());
	}

	public void removeClient (Client c) {
		this.clients.remove(c);
	}
}