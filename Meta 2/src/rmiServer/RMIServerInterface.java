package rmiServer;


import java.rmi.*;

/*
 * FundStart
 *  Projecto para a cadeira de Sistemas Distribuidos
 *  Ano Lectivo 2015/1016
 *  Carlos Pinto 2011143469
 *  Diana Umbelino 2012169525
 *  Tomás Carvalho 2012138578
 */

public interface RMIServerInterface extends Remote {
	
	public ClientRequest tumblrSignIn(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest getProjectDetails(ClientRequest clrqst) throws RemoteException;

    public ClientRequest verificaLogIn(ClientRequest clrqst) throws RemoteException;

    public ClientRequest novoUtilizador(ClientRequest userInfo) throws RemoteException;

    public ClientRequest getUserSaldo(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest enviaMensagem(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest respMensagem (ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest veResposta (ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest caixaCorreio(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest apagaProjecto(ClientRequest clrqst) throws RemoteException;
    
    public void terminaProjecto() throws RemoteException;
    
    public ClientRequest getActualProjects(ClientRequest clrqst) throws RemoteException;
    
    public void DB() throws RemoteException;

    public ClientRequest novoProjecto(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest checkRequest(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest pledgeToProject(ClientRequest clrqst) throws RemoteException;

    public ClientRequest getUserProjects(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest addAdminToProject(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest voteForProduct(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest donateReward(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest listarRecompensas(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest addReward(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest deleteReward(ClientRequest clrqst) throws RemoteException;
    
    public String checkDataBaseConection() throws RemoteException;
    
    public String addProductType(ClientRequest clrqst) throws RemoteException;

	public ClientRequest deleteAdmin(ClientRequest postCard) throws RemoteException;
    
    public ClientRequest getProjectAdmins(ClientRequest clrqst) throws RemoteException;
}
