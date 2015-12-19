package tumblr;
import com.opensymphony.xwork2.ActionSupport;


import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import com.google.gson.Gson;
import org.json.*;

import java.rmi.RemoteException;
import java.util.Map;
import fundstarter.model.ConnectToRMIBean;





public class GetDataFromTumblr extends ActionSupport implements SessionAware{
	
    private Map<String, Object> session;
    private String key;
    private String secret;
    //returned from twitter
    private String oauth_token;
    private String oauth_verifier;
    private String token;
    private String username;
    private String blog;
    private String type;
    private String title = "Test Title";
    private String body;
    private String reblog_key;
    long id;

    
    @Override
    public String execute(){
    	
    	int i = 0;
    	String userUri = "api.tumblr.com/v2/user/info";
    	String postUri = "api.tumblr.com/v2/blog/";
    	String likeUri= "api.tumblr.com/v2/user/like";
    	
    	String post = "/post";
    	String url="https://";
    	url = url+userUri;
    	
    	System.out.println(url);
        Token accessToken = (Token)(session.get("accessToken"));
        OAuthService tumblrService = (OAuthService)session.get("tumblrService");
        
    	OAuthRequest request = new OAuthRequest(Verb.GET, url, tumblrService);
    	request.addHeader("Accept", "application/json");
    	tumblrService.signRequest(accessToken, request);

    	Response response = request.send();


    	
    
    	
    	try{
    		/* ESTA VAI BUSCAR O USER E O BLOG*/
    		JSONObject obj = (JSONObject) JSONValue.parse(response.getBody());
    		JSONObject resp = (JSONObject)obj.get("response");
        	System.out.println(resp);
        	
    		JSONObject user = (JSONObject)resp.get("user");
    		JSONArray arr = (JSONArray)user.get("blogs");
    		username = (String)user.get("name");
    		System.out.println(username);
    		if (arr.size() > 0){
    			JSONObject bloginfo = (JSONObject)arr.get(0);
    			blog = (String)bloginfo.get("name")+".tumblr.com";
    			
    		}
    		System.out.println(blog);
    		
    		/* ESTA FAZ POSTS*/
    		/*url="https://";
    		url = url+postUri+blog+post;
			request = new OAuthRequest(Verb.POST, url, tumblrService);
        	request.addBodyParameter("type", "text");
        	request.addBodyParameter("title", "Test Title");
        	request.addBodyParameter("body", "This is the body");
        	tumblrService.signRequest(accessToken, request);

        	response = request.send();
        	System.out.println(response);*/
        	
    		
    		/* ESTA METE LIKES */
    		/*
        	url="https://";
        	String getID = "api.tumblr.com/v2/blog/"+
        			blog
        			+ "/posts";
        	url = url+getID;
        	request = new OAuthRequest(Verb.GET, url, tumblrService);
			request.addHeader("Accept", "application/json");
			tumblrService.signRequest(accessToken, request);
        	response = request.send();
        	System.out.println(response.getBody());
        	
        	obj = (JSONObject) JSONValue.parse(response.getBody());
    		resp = (JSONObject)obj.get("response");
    	
    		JSONArray posts = (JSONArray)(resp.get("posts"));
    		while ((i<posts.size())&&((String)((JSONObject)posts.get(i)).get("title")).equals(title) ==false){
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
        	*/
    		
        	
    		
    	}
    	catch(NullPointerException ex){
    		System.out.println("Algo esta mal");
    		return ERROR;
    	}

    	
		this.getConnectToRMIBean().setUsername(username);
		this.getConnectToRMIBean().setBlog(blog);
		String  result = this.getConnectToRMIBean().tumblrSignIn();
		System.out.println(result);
		if (result.equals("Done")){
			this.session.put("logged",true);
			return SUCCESS;}
		else
			return ERROR;
    	
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
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */

    public void setKey(String key) {
        this.key = key;
    }
    
    public void setToken(String token){
    	this.token = token;
    }
    
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    /**
     * @param oauth_token the oauth_token to set
     */
  
    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }
    
    public String getToken(){
    	return token;
    }
    public String getOauth_verifier() {
        return oauth_verifier;
    }


    public void setOauth_verifier(String oauth_verifier) {
        this.oauth_verifier = oauth_verifier;
    }
}


/*public class SignInAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 6529685098267757690L;
	private Map<String, Object> session;
	private String username = null;
	private String password = null;

	@Override
	public String execute() throws RemoteException {



		if (this.getConnectToRMIBean().signIn().equals("main_menu")) {
			this.session.put("normal_logged",true);
			return SUCCESS;
		}

		return LOGIN;

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
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

*/