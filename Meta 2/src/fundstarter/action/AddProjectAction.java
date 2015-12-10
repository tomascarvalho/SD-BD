package fundstarter.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class AddProjectAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String name;
	private String projDescription;
	private String limitDate;
	private String productType;
	
	@Override
	public String execute(){
		
		
		return SUCCESS;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	public void setProjDescription(String projDescription) {
		this.projDescription = projDescription;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
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
