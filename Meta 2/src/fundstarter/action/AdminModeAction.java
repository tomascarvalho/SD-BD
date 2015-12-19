package fundstarter.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class AdminModeAction extends ActionSupport implements SessionAware  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private int option;
	private int aux_tam;
	private Object[] aux;
	
	@Override
	public String execute() throws RemoteException{
		
		System.out.println("[AdminMode]User option:" + this.option);
		aux = this.getConnectToRMIBean().listUserProjects(this.option);
		System.out.println(aux[0]);
		this.session.put("MyProjectIDs", aux[0]);
		this.session.put("MyProjects", aux[1]);
		
		
		aux_tam =  aux[1].toString().length();
		System.out.println("O TAMANHO E ESTE: " + aux_tam);
		//aux_tam = ((ArrayList<String>) aux[1]).size();
		//TESTE
		ArrayList<String> projectos = (ArrayList<String>)(aux[1]);
		System.out.println(projectos);
		int tam = ((ArrayList<String>) aux[1]).size();
		if( ((ArrayList<String>) aux[1]).isEmpty()){
			tam = 0;
		}
		
		//this.session.put("tamMyProject", ((ArrayList<String>) aux[1]).size());
		this.session.put("tamMyProject", tam);
		//System.out.println("O TAMANHO E ESTE TAMMY" + this.session.get("tamMyProject"));
		//System.out.println(this.session.get("MyProjectIDs"));
		return SUCCESS;
	}
	
	public void setOption(String option){
		System.out.println(option);
		this.option=Integer.parseInt(option);
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
