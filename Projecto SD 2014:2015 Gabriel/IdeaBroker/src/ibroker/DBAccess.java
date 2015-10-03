package ibroker;

import terminal.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.*;


public class DBAccess {
	
	private String IP = "10.16.2.119";
	RMIServerInterface rmi_server;
	
	public DBAccess() throws MalformedURLException, RemoteException, NotBoundException{
		this.rmi_server = (RMIServerInterface) Naming.lookup("rmi://"+IP+":7000/RMIServer");
		System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());
	}

	public void setData(String query, Object... args) throws RemoteException {
		rmi_server.db_query(query, args);
	}
	
	public ArrayList<UserBean> getUser(String query, Object...args) throws RemoteException {
		try {
			return rmi_server.db_query_user_WEB(query,args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<TopicBean> getTopic(String query, Object...args) throws RemoteException {
		try {
			return rmi_server.db_query_topic_WEB(query,args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<IdeaBean> getIdea(String query, Object...args) throws RemoteException {
		try {
			return rmi_server.db_query_idea_WEB(query,args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<PackBean> getPack(String query, Object...args) throws RemoteException {
		try {
			return rmi_server.db_query_pack_WEB(query,args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<HistoricBean> getHistoric(String query, Object...args) throws RemoteException {
		try {
			return rmi_server.db_query_historic_WEB(query,args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int getInteger(String query, Object... args) throws RemoteException {
		return rmi_server.db_query_integer_WEB(query, args);
	}
	
}
