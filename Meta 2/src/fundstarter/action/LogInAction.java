package fundstarter.action;
/*
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
*/
import java.rmi.RemoteException;
import java.util.Map;
import fundstarter.model.ConnectToRMIBean;

public class LogInAction /*extends ActionSupport implements SessionAware*/ {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String password = null;

	public String execute() throws RemoteException {
		
		this.getConnectToRMIBean().setUsername(this.username);
		this.getConnectToRMIBean().setPassword(this.password);
		
		this.getConnectToRMIBean().logIn();
		
		
		return "done";
	}

	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		this.password = password; // what about this input?
	}

	public ConnectToRMIBean getConnectToRMIBean() {
		if(!session.containsKey("ConnectToRMIBean"))
			this.setConnectToRMIBean(new ConnectToRMIBean());
		return (ConnectToRMIBean) session.get("ConnectToRMIBean");
	}

	public void setConnectToRMIBean(ConnectToRMIBean RMIBean) {
		this.session.put("ConnectToRMIBean", RMIBean);
	}

}
