package fundstarter.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;
import fundstarter.model.ConnectToRMIBean;

public class LogOutAction extends ActionSupport implements SessionAware { 



	private Map<String, Object> session;

	@Override
	public String execute() throws RemoteException {

		this.session.put("logged",false);
		return SUCCESS;

	}

	

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;

	}

}
