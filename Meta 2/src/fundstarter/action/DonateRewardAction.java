package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class DonateRewardAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String username;
	
	@Override
	public String execute() throws RemoteException{
		
		System.out.println("[Donate<execute>]\n\tUsername:" + this.username);
		System.out.println("\tReward:" + this.session.get("Reward"));
		System.out.println("\tProjectID:" + this.session.get("pledgedProjectID"));
		
		if(this.getConnectToRMIBean().donateReward((int)this.session.get("pledgedProjectID"), this.username, (String)this.session.get("Reward")).equals("success")){
			return SUCCESS;
		}
		else{
			return ERROR;
		}
		
	}
	
	public void setUsername(String username){
		this.username = username;
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
