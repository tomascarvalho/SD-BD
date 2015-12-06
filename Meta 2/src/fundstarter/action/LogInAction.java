package fundstarter.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;
import fundstarter.model.ConnectToRMIBean;

public class LogInAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String password = null;

	@Override
	public String execute() throws RemoteException {
		
		System.out.println("OLA!!! Cá estou eu");
		this.getConnectToRMIBean().setUsername(this.username);
		this.getConnectToRMIBean().setPassword(this.password);
		System.out.println("Já meti o nome e a pass ");
		this.getConnectToRMIBean().logIn();
		
		
		return "done";
	}

	public void setUsername(String username) {
			this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectToRMIBean getConnectToRMIBean() {
		System.out.println("the D is silent");
		if(session.containsKey("ConnectToRMIBean")==false){
			System.out.println("hello little trouble maker");
			this.setConnectToRMIBean(new ConnectToRMIBean());
		}
		System.out.println("Hey snowball");
		return (ConnectToRMIBean) session.get("ConnectToRMIBean");
	}

	public void setConnectToRMIBean(ConnectToRMIBean RMIBean) {
		System.out.println("OLA MIcas");
		this.session.put("ConnectToRMIBean", RMIBean);
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}

}
