package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class NewAdminAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private int option;

	@Override
	public String execute() throws RemoteException{
		this.session.put("newProjectID", option);
		return SUCCESS;
	}
	
	public ConnectToRMIBean getConnectToRMIBean() {
		
		if (!session.containsKey("RMIBean")) {
			this.setConnectToRMIBean(new ConnectToRMIBean());
		}
		
		return (ConnectToRMIBean) session.get("RMIBean");
	}
	
	public void setOption(String option){
		this.option = Integer.parseInt(option);
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