package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class PledgeAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private int projectID;
	private int amount;

	public String execute() throws RemoteException{
		
		String result = this.getConnectToRMIBean().pledgeToProject(this.projectID, this.amount);
		
		if(result.equals("success")){
			this.getConnectToRMIBean().listProjectDetails(this.projectID);
			return SUCCESS;
		}
		else if(result.equals("error")){
			return ERROR;
		}
		else{
			this.session.put("Reward", result);
			this.getConnectToRMIBean().listProjectDetails(this.projectID);
			return SUCCESS;
		}
		
	}
	
	public void setProjectID(String projectID){
		this.projectID = Integer.parseInt(projectID);
	}
	
	public void setAmount(String amount){
		this.amount = Integer.parseInt(amount);
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
