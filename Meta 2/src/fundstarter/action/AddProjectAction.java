package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class AddProjectAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String name;
	private String projDescription;
	private String goalValue;
	private String limitDate;
	private String productType;
	
	@Override
	public String execute() throws RemoteException{
		
		this.getConnectToRMIBean().addProject(this.name, this.projDescription, this.goalValue, this.limitDate, this.productType);
		
		return SUCCESS;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	public void setProjDescription(String projDescription) {
		this.projDescription = projDescription;
	}
	
	public void setGoalValue(String goalValue) {
		this.goalValue = goalValue;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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
