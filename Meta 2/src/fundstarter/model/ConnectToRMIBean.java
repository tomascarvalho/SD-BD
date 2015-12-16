package fundstarter.model;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
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
	private HashMap<String, Object> projectDetails;
	private ArrayList<String> projectRewards;
	private int newProjectID;
	private ArrayList<HashMap<String, Object>> myMessages;

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
	

	public Object[] listUserProjects(int option) throws RemoteException {
		System.out.println("[ConnectToRMI]List User Projects");

		this.dataToSend = new Object[2];
		System.out.println(this.userID);
		this.dataToSend[1] = this.userID;

		this.postCard = new ClientRequest("2", this.dataToSend, "tempo");

		
		this.postCard = this.connectToRMI.getUserProjects(this.postCard);
		//formatProjects((Object[]) this.postCard.getResponse(), option);
		System.out.println(((ArrayList<Integer>) this.postCard.getResponse()[0]).get(0));
		return this.postCard.getResponse();
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

		return (int) this.postCard.getResponse()[0];
	}
	
	
	public void listProjectRewards(int projectID) throws RemoteException {

		System.out.println("[ConnectToRMI]List project details");

		this.dataToSend = new Object[3];
		
		this.dataToSend[0] = projectID;
		this.dataToSend[1] = this.userID;
		this.dataToSend[2] = 1;

		this.postCard = new ClientRequest("2", this.dataToSend, "tempo");

		this.postCard = connectToRMI.listarRecompensas(this.postCard);

		this.projectRewards = (ArrayList<String>)this.postCard.getResponse()[3];
	}

	
	

	public void formatProjectDetails(Object[] project) {

		if (project.equals("no_project_to_show")) {

			this.projectDetails = null;

		} else {

			this.projectDetails = new HashMap<String, Object>();

			int i = 0;
			int rewardCounter;
			int levelsCounter;
			int productCounter;
			HashMap<String, Object> auxMap;
			ArrayList<HashMap<String, Object>> auxArray;
			
			System.out.println((String)project[0]);

			this.projectDetails.put("Titulo", project[i]);
			i++;

			this.projectDetails.put("Descricao", project[i]);
			i++;

			this.projectDetails.put("ValorPretendido", project[i]);
			i++;

			this.projectDetails.put("ValorActual", project[i]);
			i++;

			this.projectDetails.put("DataLimite", project[i]);
			i++;
			
			System.out.println(project[i]);
			rewardCounter = Integer.parseInt((String) project[i]);
			i++;

			if (rewardCounter != 0) {

				auxArray = new ArrayList<HashMap<String, Object>>();

				for (int j = 0; j < (rewardCounter / 2); j++) {

					auxMap = new HashMap<String, Object>();

					auxMap.put("TituloRecompensa", project[i]);
					i++;

					auxMap.put("ValorRecompensa", project[i]);
					i++;

					auxArray.add(auxMap);
				}

				this.projectDetails.put("Rewards", auxArray);

			}

			levelsCounter = Integer.parseInt((String) project[i]);
			i++;

			if (levelsCounter != 0) {

				auxArray = new ArrayList<HashMap<String, Object>>();

				for (int j = 0; j < (levelsCounter / 2); j++) {

					auxMap = new HashMap<String, Object>();

					auxMap.put("DescNivel", project[i]);
					i++;

					auxMap.put("ValorNivel", project[i]);
					i++;

					auxArray.add(auxMap);
				}

				this.projectDetails.put("Levels", auxArray);

			}

			productCounter = Integer.parseInt((String) project[i]);
			i++;

			if (productCounter != 0) {

				auxArray = new ArrayList<HashMap<String, Object>>();

				for (int j = 0; j < (productCounter / 2); j++) {

					auxMap = new HashMap<String, Object>();

					System.out.println("[ConnectToRMI]Product Type:" + (String) project[i]);
					auxMap.put("DescProduct", project[i]);
					i++;

					auxMap.put("Contador", project[i]);
					i++;

					auxArray.add(auxMap);
				}

				this.projectDetails.put("Products", auxArray);

			}

		}

	}

	public void listProjectDetails(int projectId) throws RemoteException {

		System.out.println("[ConnectToRMI]List project details");

		this.dataToSend = new Object[2];
		this.dataToSend[1] = projectId;

		this.postCard = new ClientRequest("2", this.dataToSend, "tempo");

		this.postCard = connectToRMI.getProjectDetails(this.postCard);

		formatProjectDetails((Object[]) this.postCard.getResponse()[0]);

	}

	public String cancelarProj(int projectId) throws RemoteException {
		System.out.println("[ConnectoToRMI] Cancelar um projecto");
		this.dataToSend[1] = projectId;
		this.postCard = new ClientRequest("", this.dataToSend, "");
		this.postCard = connectToRMI.apagaProjecto(this.postCard);

		return (String) this.postCard.getResponse()[0];

	}

	public String addProject(String name, String description, String goalValue, String limitDate, String productType)
			throws RemoteException {

		String[] projectInfo = new String[8];
		this.dataToSend = new Object[2];

		projectInfo[0] = name;
		projectInfo[1] = description;
		projectInfo[2] = goalValue;
		projectInfo[3] = limitDate;
		projectInfo[4] = "0";
		projectInfo[5] = "0";
		projectInfo[6] = "1";
		projectInfo[7] = productType;

		this.dataToSend[0] = Integer.toString(this.userID);
		this.dataToSend[1] = projectInfo;
		this.postCard = new ClientRequest("", this.dataToSend, "");

		this.postCard = this.connectToRMI.novoProjecto(this.postCard);

		System.out.println("MErdas" + (String) this.postCard.getResponse()[0]);
		if (this.postCard.getResponse()[0].equals("infosave")) {
			System.out.println("[ConnectToRMI]New project stored");
			this.newProjectID =  (int) this.postCard.getResponse()[1];
			return "success";
		}
		else{
			return "error";
		}
	}
	
	public String addReward(String valor, String id_proj, String titulo, String status) throws RemoteException{
		String[] projectInfo = new String[4];
		this.dataToSend = new Object[2];

		projectInfo[0] = valor;
		projectInfo[1] = id_proj;
		projectInfo[2] = titulo;
		projectInfo[3] = status;


		this.dataToSend[0] = Integer.toString(this.userID);
		this.dataToSend[1] = projectInfo;
		this.postCard = new ClientRequest("", this.dataToSend, "");

		this.postCard = this.connectToRMI.addReward(this.postCard);

		if(this.postCard.getResponse()[0].equals("infosave")){
			System.out.println("[ConnectToRMI]Reward stored");
			this.newProjectID = (int) this.postCard.getResponse()[1];
			return "success";
		} else {
			System.out.println(this.postCard.getResponse()[0]);
			return "error";
		}
	}

	public String addLevel(int projectID, String description, int value) throws RemoteException {

		this.dataToSend = new Object[5];

		this.newProjectID = projectID;

		dataToSend[0] = this.userID;
		dataToSend[1] = projectID;
		dataToSend[2] = description;
		dataToSend[3] = value;
		dataToSend[4] = 1;

		this.postCard = new ClientRequest("", this.dataToSend, "");

		this.postCard = this.connectToRMI.addReward(this.postCard);

		if ((int) this.postCard.getResponse()[1] == 1) {
			return "success";
		} else {
			return "error";
		}
	}
	
	public void listRewards(int id_proj) throws RemoteException{
		System.out.println("[ConnectToRMI]List Rewards");

		this.dataToSend = new Object[2];
		this.dataToSend[1] = id_proj;

		this.postCard = new ClientRequest("2", this.dataToSend, "tempo");

		this.postCard = connectToRMI.getProjectDetails(this.postCard);

		formatProjectDetails((Object[]) this.postCard.getResponse()[0]);
		
		
	}
	

	public String addProduct(int projectID, String productType) throws RemoteException {

		this.dataToSend = new Object[2];

		this.newProjectID = projectID;

		dataToSend[0] = projectID;
		dataToSend[1] = productType;

		this.postCard = new ClientRequest("", this.dataToSend, "");

		if(this.connectToRMI.addProductType(this.postCard).equals("product_save")){
			return "success";
		}
		else{
			return "error";
		}
	}
	
	public String pledgeToProject(int projectID,int amount) throws RemoteException{
		
		int[] pledgeInfo = new int[2];
		this.dataToSend = new Object[2];
		
		System.out.println("[ConnectToRMI]Ready to pledge to project with:");
		System.out.println("\tID -> " + projectID);
		System.out.println("\tAmount -> " + amount);
		System.out.println("\tUser ID -> " + this.userID);
		
		pledgeInfo[0] = amount;
		pledgeInfo[1] = projectID;
		
		this.dataToSend[0] = this.userID;
		this.dataToSend[1] = pledgeInfo;
		
		this.postCard = new ClientRequest("",this.dataToSend,"");
		
		this.postCard = this.connectToRMI.pledgeToProject(this.postCard);
		
		if(this.postCard.getResponse()[0].equals("pledged")){
			System.out.println("[ConnectToRMI]Pledged");
			System.out.println("\tResult -> " + this.postCard.getResponse()[3]);
		
			if(this.postCard.getResponse()[3].equals("No Reward") == false){
				System.out.println("[ConnectToRMIBean<plrdgeToProject>]Got Rewards!");
				return (String)this.postCard.getResponse()[3];
			}
			else{
				return "success";
			}
		}
		else{
			return "error";
		}
	}
	
	public String donateReward(int projectID, String username, String reward) throws RemoteException{
		
		this.dataToSend = new Object[4];
		
		this.dataToSend[1] = projectID;
		this.dataToSend[2] = username;
		this.dataToSend[3] = reward;
		
		
		this.postCard = new ClientRequest("", this.dataToSend, "");
		
		this.postCard = this.connectToRMI.donateReward(this.postCard);
		
		return (String) this.postCard.getResponse()[0];
		
	}
	
	public void voteOnProject(String description) throws RemoteException{
		
		this.dataToSend = new Object[2];
		
		this.dataToSend[1] = description;
		
		this.postCard = new ClientRequest("", this.dataToSend, "");
		
		this.postCard = this.connectToRMI.voteForProduct(this.postCard);
		
	}
	
	public String sendMessage(int projectID, String message) throws RemoteException{
		
		this.dataToSend = new Object[3];
		
		this.dataToSend[0] = this.userID;
		this.dataToSend[1] = projectID;
		this.dataToSend[2] = message;
		
		this.postCard = new ClientRequest("", this.dataToSend, "");
		
		this.postCard = this.connectToRMI.enviaMensagem(this.postCard);
		
		if(this.postCard.getResponse()[0].equals("Mensagem enviada")){
			return "success";
		}
		else{
			return "error";
		}
		
	}
	
	public void formatMessages(Object[] data){
		
		ArrayList<ArrayList<Integer>> listOfIds = (ArrayList<ArrayList<Integer>>) data[0];
		ArrayList<ArrayList<String>> listOfMessages = (ArrayList<ArrayList<String>>) data[1];
		ArrayList<HashMap<String, Object>> auxList;
		HashMap<String, Object> auxMap;
		HashMap<String,Object> auxMessage;
		this.myMessages = new ArrayList<HashMap<String,Object>>();
		
		System.out.println("[ConnectToRMI]<formatMessages>: listOfIds size -> " + listOfIds.size());
		
		System.out.println("[ConnectToRMI]<formatMessages>: listOfMessagess size -> " + listOfMessages.size());
		
		for(int i=0; i<listOfIds.size(); i++){
			
			auxMap = new HashMap<String, Object>();
			auxList = new ArrayList<HashMap<String, Object>>();
			
			auxMap.put("ProjectID", listOfIds.get(0));
			
			for(int j=1; j<listOfIds.get(i).size(); j++){
				
				auxMessage = new HashMap<String, Object>();
				
				System.out.println("[ConnectToRMI]<formatMessages>: message " + j + " -> " + listOfMessages.get(j));
				
				auxMessage.put("MessageID", listOfIds.get(j));
				auxMessage.put("Message", listOfMessages.get(j));
				
				auxList.add(auxMessage);
			
			}
			
			auxMap.put("ProjectMessages", auxList);
			this.myMessages.add(auxMap);
		}
	}
	
	public void seeMyInbox() throws RemoteException{
		
		this.dataToSend = new Object[1];
		
		this.dataToSend[0] = this.userID;
		
		this.postCard = new ClientRequest("", this.dataToSend, "");
		
		System.out.println("[ConnectToRMI]<seeMyInbox> User ID -> " + this.userID);
		this.postCard = this.connectToRMI.caixaCorreio(this.postCard);
		
		formatMessages(this.postCard.getResponse());
	}
	
	public String deleteReward(int rewardID) throws RemoteException{
		
		this.dataToSend = new Object[4];
		
		this.dataToSend[1] = rewardID;
		this.dataToSend[2] = 0;
		this.dataToSend[3] = this.userID;
		
		this.postCard = new ClientRequest("", this.dataToSend, "");
		
		this.postCard = this.connectToRMI.deleteReward(this.postCard);
		
		if(this.postCard.getResponse()[1].equals("not_yours")){
			return "error";
		}
		else{
			return "success";
		}
	}

	public ArrayList<HashMap<String, Object>> getProjects() {
		System.out.println("[ConnectToRMI]Returning Projects");
		return this.projects;
	}
	
	public ArrayList<HashMap<String, Object>> getMyMessages(){
		return this.myMessages;
	}

	public HashMap<String, Object> getProjectDetails() {
		return this.projectDetails;
	}
	
	public ArrayList<String> getProjectRewards(){
		return this.projectRewards;
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

	public int getNewProjectID() {
		return this.newProjectID;
	}
}
