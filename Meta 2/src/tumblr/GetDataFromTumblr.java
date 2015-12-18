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
    
    
    @Override
    public String execute(){
    	
    	String userUri = "api.tumblr.com/v2/user/info";
    	String userUrl="https://";
    	userUrl = userUrl+userUri;
    	
    	System.out.println(userUrl);
        Token accessToken = (Token)(session.get("accessToken"));
        OAuthService tumblrService = (OAuthService)session.get("tumblrService");
        
    	OAuthRequest request = new OAuthRequest(Verb.GET, userUrl, tumblrService);
    	request.addHeader("Accept", "application/json");
    	tumblrService.signRequest(accessToken, request);
    	System.out.println(request.getHeaders().keySet());
    	Response response = request.send();
    	System.out.println("Aqui");
    	System.out.println(response.getBody());
    	System.out.println("--");
    	JSONObject obj = (JSONObject)JSONValue.parse(response.getBody());
    	System.out.println("---");
    	JSONObject arr = (JSONObject)obj.get("response");
		for(int i=0; i< arr.size(); i++) {
			JSONObject item = (JSONObject) arr.get(i);
			JSONObject user = (JSONObject) item.get("user");
			System.out.println(user.get("name"));
		}
    	System.out.println("Got it! Lets see what we found...");
    	System.out.println("HTTP RESPONSE: =============");
    	System.out.println(response.getCode());
    	System.out.println(response.getBody());
    	
    	System.out.println("END RESPONSE ===============");
    	
		/*this.getConnectToRMIBean().setUsername(this.username);
		this.getConnectToRMIBean().setPassword(this.password);*/
    	return SUCCESS;
    	
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