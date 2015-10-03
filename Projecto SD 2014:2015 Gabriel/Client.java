import java.io.*;

public class Client {
	
	private String userName;
	private ObjectOutputStream oos;

	public Client (String user, ObjectOutputStream output) {
		this.userName = user;
		this.oos = output;
	}

	public String getUsername () {
		return this.userName;
	}

	public ObjectOutputStream getOutputStream () {
		return this.oos;
	}
}