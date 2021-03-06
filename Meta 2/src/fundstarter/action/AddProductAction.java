package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class AddProductAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String productType;
	private int option;

	@Override
	public String execute() throws RemoteException {

		if (this.getConnectToRMIBean().addProduct(this.option,this.productType).equals("success")) {
			this.getConnectToRMIBean().listProjectDetails(this.option);
			return SUCCESS;
		} else {
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

	public void setOption(String option) {
		this.option = Integer.parseInt(option);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}

}
