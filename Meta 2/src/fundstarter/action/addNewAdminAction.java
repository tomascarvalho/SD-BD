package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class addNewAdminAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String user;
	private String option;
	
	@Override
	public String execute() throws RemoteException{
		
		
		
		if(this.getConnectToRMIBean().addNewAdmin(this.user, this.option).equals("success")){
			session.put("newProjectID", this.getConnectToRMIBean().getNewProjectID());
			return SUCCESS;
		}	
		else{
			return ERROR;
		}
	}
	

	public void setUser(String user) {
		this.user= user;
	}

	public void setOption(String option) {
		this.option = option;
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
