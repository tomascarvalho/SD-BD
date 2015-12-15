package fundstarter.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class VoteOnProduct extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String productDescription;
	
	@Override
	public String execute(){
		
		System.out.println("[VonteOnProject<execute>]Product Description:" + this.productDescription);
		return SUCCESS;
	}
	
	public void setProductDescription(String productDesc){
		this.productDescription = productDesc;
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}

}
