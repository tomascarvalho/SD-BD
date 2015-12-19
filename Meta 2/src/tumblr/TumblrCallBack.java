package tumblr;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;


import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;


public class TumblrCallBack extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private String key;
    private String secret;
    //returned from twitter
    private String oauth_token;
    private String oauth_verifier;
    private String token;
    
    @Override
    public String execute() {
    	System.out.println("Is this called");
        if (session.containsKey("accessToken") && session.get("accessToken") != null) {
        	this.session.put("logged",true);
            return SUCCESS; //accessToken already exists!
        }
        Token requestToken = (Token) session.get("requestToken");

		if (requestToken == null) {
            super.addActionError("requestToken is null");
            return ERROR;
        }
        OAuthService tumblrService = (OAuthService) session.get("tumblrService");
        System.out.println(tumblrService);
        System.out.println(requestToken.toString());
        System.out.println(token);
     
        //Token accessToken = twitter.getOAuthAccessToken(requestToken, this.getOauth_verifier());
        System.out.println("erro1");
        Token accessToken = tumblrService.getAccessToken(requestToken, new Verifier(this.token));
        System.out.println("erro2");
        session.put("accessToken", accessToken);
        this.setKey(accessToken.getToken()); //just to see something happen
        System.out.println(this.key);
        this.setSecret(accessToken.getSecret());//just to see something happen
        System.out.println(this.secret);
        this.session.put("logged",true);
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