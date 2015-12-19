package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import fundstarter.model.ConnectToRMIBean;

public class AddProjectAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String name;
	private String projDescription;
	private String goalValue;
	private String limitDate;
	private String productType;
	private String blog;
	
	@Override
	public String execute() throws RemoteException{
		String result = this.getConnectToRMIBean().addProject(this.name, this.projDescription, this.goalValue, this.limitDate, this.productType);
		if(result.equals("success")){
			session.put("newProjectID", this.getConnectToRMIBean().getNewProjectID());
			return SUCCESS;
		}
		else if(result.equals("tumblr")){
			session.put("newProjectID", this.getConnectToRMIBean().getNewProjectID());
			
			blog = this.getConnectToRMIBean().getBlog();
	    	String postUri = "api.tumblr.com/v2/blog/";
	    	
	    	String post = "/post";
	    	String url="https://";

	    	System.out.println(url);
	    	Token accessToken = (Token)(session.get("accessToken"));
	        OAuthService tumblrService = (OAuthService)session.get("tumblrService");
	        
	        url="https://";
    		url = url+postUri+blog+post;
    		OAuthRequest request = new OAuthRequest(Verb.POST, url, tumblrService);
        	request.addBodyParameter("type", "text");
        	request.addBodyParameter("title", ""+session.get("newProjectID"));
        	request.addBodyParameter("body", "Acabei de criar no FundStarter o meu Projecto, que se chama: "+this.name+"<br>"+this.projDescription);
        	tumblrService.signRequest(accessToken, request);

        	Response response = request.send();
        	System.out.println(response);
			
			return SUCCESS;
		}
		else{
			return ERROR;
		}
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
