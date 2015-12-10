package fundstarter.model;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

//import com.sun.javafx.collections.HashMaopingChange.HashMao;

import rmiServer.RMIServerInterface;
import rmiServer.ClientRequest;

public class ConnectToRMIBean {

	private RMIServerInterface connectToRMI;
	private ClientRequest postCard;
	private Object[] dataToSend;
	private String username;
	private String password;
	private int userID;
	private ArrayList<HashMap<String, Object>> projects;
	private String storedProjects;

	public ConnectToRMIBean() {

		try {

			System.out.println("Connecting to RMI");
			this.connectToRMI = (RMIServerInterface) Naming.lookup("//localhost:7777/fundStarter");
			System.out.println("Connected to RMI");

		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			e.printStackTrace();
		}
	}

	public String logIn() {

		System.out.println("[ConnectToRMI]Log In");
		String[] credentials = new String[2];

		credentials[0] = this.username;
		credentials[1] = this.password;

		this.dataToSend = new Object[2];
		this.dataToSend[1] = credentials;

		this.postCard = new ClientRequest("1", this.dataToSend, "tempo");

		try {

			this.postCard = this.connectToRMI.verificaLogIn(this.postCard);

			if (this.postCard.getResponse()[0].equals("log_in_correcto")) {
				this.userID = (int) this.postCard.getResponse()[1];
				return "main_menu";
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return "log_in";
	}

	public String signIn() {

		System.out.println("[ConnectToRMI]Sign In");
		String[] credentials = new String[2];

		credentials[0] = this.username;
		credentials[1] = this.password;

		this.dataToSend = new Object[2];
		this.dataToSend[1] = credentials;

		this.postCard = new ClientRequest("1", this.dataToSend, "tempo");

		try {

			this.postCard = this.connectToRMI.novoUtilizador(this.postCard);

			if (this.postCard.getResponse()[0].equals("infosave")) {
				this.userID = (int) this.postCard.getResponse()[1];
				return "main_menu";
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return "sig_in";
	}

	/* function to better the access of projects in jsp */
	public void formatProjects(Object[] projects, int option) {

		HashMap<String, Object> auxProject;
		Object[] listOfProjects = (Object[]) projects[0];

		if (listOfProjects[0].equals("error_no_active_projects") || listOfProjects[0].equals("error_no_old_projects")) {

			this.projects = null;

		} else {

			if (option == 0) {
				this.storedProjects = "actual";
			} else {
				this.storedProjects = "old";
			}

			this.projects = new ArrayList<HashMap<String, Object>>();

			int i = 0;

			while (i < (int) projects[1]) {

				auxProject = new HashMap<String, Object>();

				auxProject.put("ID", listOfProjects[i]);
				i++;

				auxProject.put("Titulo", listOfProjects[i]);
				i++;

				auxProject.put("ValorActual", listOfProjects[i]);
				i++;

				auxProject.put("ValorPretendido", listOfProjects[i]);
				i++;

				this.projects.add(auxProject);

			}
		}
	}

	public String listProjects(int option) throws RemoteException {

		System.out.println("[ConnectToRMI]List Projects");

		this.dataToSend = new Object[2];
		this.dataToSend[1] = option;

		this.postCard = new ClientRequest("2", this.dataToSend, "tempo");

		try {
			this.postCard = this.connectToRMI.getActualProjects(this.postCard);
		} catch (Exception e) {
			e.printStackTrace();
		}

		formatProjects((Object[]) this.postCard.getResponse(), option);

		return "sucesso";

	}

	public int getBalance() throws RemoteException {

		System.out.println("[ConnectToRMI]Get balance");

		this.dataToSend = new Object[2];
		this.dataToSend[1] = this.userID;

		this.postCard = new ClientRequest("2", this.dataToSend, "tempo");

		try {
			this.postCard = this.connectToRMI.getUserSaldo(this.postCard);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (int)this.postCard.getResponse()[0];
	}

	public ArrayList<HashMap<String, Object>> getProjects() {
		System.out.println("[ConnectToRMI]Returning Projects");
		return this.projects;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStoredProjects() {
		return this.storedProjects;
	}
}
