package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.opensymphony.xwork2.ActionSupport;
import fundstarter.model.ConnectToRMIBean;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;



public class PledgeAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private int projectID;
	private int amount;

	public String execute() throws RemoteException{
		
		String[] result = this.getConnectToRMIBean().pledgeToProject(this.projectID, this.amount);
		String url;
		String userUri = "api.tumblr.com/v2/user/info";
    	String postUri = "api.tumblr.com/v2/blog/";
    	String likeUri= "api.tumblr.com/v2/user/like";
    	String getID = "api.tumblr.com/v2/blog/"+
    			result[1]
    			+ "/posts";
		int i = 0;
		long id;
		String reblog_key;
        Token accessToken = (Token)(session.get("accessToken"));
        OAuthService tumblrService = (OAuthService)session.get("tumblrService");
        
        
		if(result[0].equals("success")){
			this.getConnectToRMIBean().listProjectDetails(this.projectID);
			if (!result[1].equals("Lixo")){
				
				url="https://";
	        	url = url+getID;
	        	OAuthRequest request = new OAuthRequest(Verb.GET, url, tumblrService);
				request.addHeader("Accept", "application/json");
				tumblrService.signRequest(accessToken, request);
	        	Response response = request.send();
	        	System.out.println(response.getBody());
	        	
	        	JSONObject obj = (JSONObject) JSONValue.parse(response.getBody());
	    		JSONObject resp = (JSONObject)obj.get("response");
	    	
	    		JSONArray posts = (JSONArray)(resp.get("posts"));
	    		while ((i<posts.size())&&((String)((JSONObject)posts.get(i)).get("title")).equals(""+this.projectID) ==false){
	    			id = (long)((JSONObject)posts.get(i)).get("id");
	    			i++;
	    		}
	    		id = (long)((JSONObject)posts.get(i)).get("id");
	    		reblog_key = ((JSONObject)posts.get(i)).get("reblog_key").toString();
	    		System.out.println(""+id);
	    		

	    		url="https://";
	    		url = url+likeUri;
	    		System.out.println(url);
	        	request = new OAuthRequest(Verb.POST, url, tumblrService);
	        	request.addBodyParameter("id", ""+id);
	        	request.addBodyParameter("reblog_key", reblog_key);
	        	System.out.println(request);
	        	tumblrService.signRequest(accessToken, request);

	        	response = request.send();
	        	System.out.println(response);
			}
			return SUCCESS;
		}
		else if(result[0].equals("error")){
			return ERROR;
		}
		else if(result[0].equals("no_money")){
			return "no_money";
		}
		else{
			if (!result[1].equals("Lixo")){
				
				url="https://";
	        	url = url+getID;
	        	OAuthRequest request = new OAuthRequest(Verb.GET, url, tumblrService);
				request.addHeader("Accept", "application/json");
				tumblrService.signRequest(accessToken, request);
	        	Response response = request.send();
	        	System.out.println(response.getBody());
	        	
	        	JSONObject obj = (JSONObject) JSONValue.parse(response.getBody());
	    		JSONObject resp = (JSONObject)obj.get("response");
	    	
	    		JSONArray posts = (JSONArray)(resp.get("posts"));
	    		while ((i<posts.size())&&((String)((JSONObject)posts.get(i)).get("title")).equals(""+this.projectID) ==false){
	    			id = (long)((JSONObject)posts.get(i)).get("id");
	    			i++;
	    		}
	    		id = (long)((JSONObject)posts.get(i)).get("id");
	    		reblog_key = ((JSONObject)posts.get(i)).get("reblog_key").toString();
	    		System.out.println(""+id);
	    		

	    		url="https://";
	    		url = url+likeUri;
	    		System.out.println(url);
	        	request = new OAuthRequest(Verb.POST, url, tumblrService);
	        	request.addBodyParameter("id", ""+id);
	        	request.addBodyParameter("reblog_key", reblog_key);
	        	System.out.println(request);
	        	tumblrService.signRequest(accessToken, request);

	        	response = request.send();
	        	System.out.println(response);
			}
			this.getConnectToRMIBean().listProjectDetails(this.projectID);
			this.session.put("Reward", result[0]);
			this.session.put("pledgedProjectID", this.projectID);
			return SUCCESS;
		}
		
	}
	
	public void setProjectID(String projectID){
		this.projectID = Integer.parseInt(projectID);
	}
	
	public void setAmount(String amount){
		this.amount = Integer.parseInt(amount);
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
