package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class AddLevelAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object>session;
	private String levelDesc;
	private String valueDesc;
	private String option;

	
	@Override
	public String execute() throws RemoteException{
		
		if(this.getConnectToRMIBean().addLevel(this.option, this.levelDesc, this.valueDesc).equals("success")){
			this.getConnectToRMIBean().listProjectDetails(Integer.parseInt(this.option));
			return SUCCESS;
		}
		else{
			return ERROR;
		}
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
	
	public void setOption(String option){
		this.option = option;
	}
	
	public void setLevelDesc(String levelDesc){
		this.levelDesc = levelDesc;
	}
	
	public void setValueDesc(String valueDesc){
		this.valueDesc = valueDesc;
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}
	

}
