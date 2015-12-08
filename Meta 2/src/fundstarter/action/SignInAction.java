package fundstarter.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;
import fundstarter.model.ConnectToRMIBean;

public class SignInAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 6529685098267757690L;
	private Map<String, Object> session;
	private String username = null;
	private String password = null;

	@Override
	public String execute() throws RemoteException {

		this.getConnectToRMIBean().setUsername(this.username);
		this.getConnectToRMIBean().setPassword(this.password);

		if (this.getConnectToRMIBean().signIn().equals("main_menu")) {
			return SUCCESS;
		}

		return LOGIN;

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
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
