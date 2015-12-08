package fundstarter.model;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import rmiServer.RMIServerInterface;
import rmiServer.ClientRequest;

public class ConnectToRMIBean {
	
	private RMIServerInterface connectToRMI;
	private ClientRequest postCard;
	private Object[] dataToSend;
	private String username;
	private String password;

	public ConnectToRMIBean(){
	
		try{
			
			System.out.println("Ligando ao RMI...");
			this.connectToRMI = (RMIServerInterface) Naming.lookup("//localhost:7777/fundStarter");
			System.out.println("Connectado com o RMI");
			
		}catch(NotBoundException|MalformedURLException|RemoteException e){
			e.printStackTrace();
		}
	}
	
	public String logIn(){
		
		System.out.println("[ConnectToRMI]LogIn");
		String[] credentials = new String[2];
		
		credentials[0] = this.username;
		credentials[1] = this.password;
		
		this.dataToSend = new Object[2];
		this.dataToSend[1] = credentials;
		
		this.postCard = new ClientRequest("1",this.dataToSend,"tempo");
		
		try{
			
			this.postCard = this.connectToRMI.verificaLogIn(this.postCard);

			if(this.postCard.getResponse()[0].equals("log_in_correcto")){
				return "main_menu";
			}

		}catch(RemoteException e){
			e.printStackTrace();
		}
		
		return "log_in";
	}
	
public String signIn(){
		
		System.out.println("[ConnectToRMI]SignIn");
		String[] credentials = new String[2];
		
		credentials[0] = this.username;
		credentials[1] = this.password;
		
		this.dataToSend = new Object[2];
		this.dataToSend[1] = credentials;
		
		this.postCard = new ClientRequest("1",this.dataToSend,"tempo");
		
		try{
			
			this.postCard = this.connectToRMI.novoUtilizador(this.postCard);

			if(this.postCard.getResponse()[0].equals("infosave")){
				return "main_menu";
			}

		}catch(RemoteException e){
			e.printStackTrace();
		}
		
		return "sig_in";
	}
	

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
