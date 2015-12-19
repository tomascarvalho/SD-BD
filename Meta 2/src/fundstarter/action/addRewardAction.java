package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class addRewardAction extends ActionSupport implements SessionAware  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	
	private String valor;
	private String option;
	private String titulo;
	private String status = "0";
	
	public String execute() throws RemoteException{
		this.session.put("newProjectID", option);
		System.out.println(this.option + ":::::");
		if(this.getConnectToRMIBean().addReward(this.valor, this.option, this.titulo, this.status).equals("success")){
			return SUCCESS;
		}	
		else{
			return ERROR;
		}
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public void setOption(String option) {
		this.option= option;
	}
	
	public void setTitulo(String titulo) {
		this.titulo= titulo;
	}
/*
	public void setStatus() {
		this.status= "false";
	}
*/
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