package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class NewLevelAction extends ActionSupport implements SessionAware  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private int projectID;
	
	public String execute() throws RemoteException{
		
		this.getConnectToRMIBean().listProjectDetails(this.projectID);
		
		return SUCCESS;
	}
	
	public void setProjectID(String projectID){
		this.projectID = Integer.parseInt(projectID);
	}
	
	public ConnectToRMIBean getConnectToRMIBean() {

		if (!session.containsKey("RMIBean")) {
			this.setConnectToRMIBean(new ConnectToRMIBean());
		}
		return (ConnectToRMIBean) session.get("RMIBean");
	}

	public void setConnectToRMIBean(ConnectToRMIBean RMIBean) {
		this.session.put("RMIBean", RMIBean);
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}

}
