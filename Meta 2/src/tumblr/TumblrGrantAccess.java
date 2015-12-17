package tumblr;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import java.util.Scanner;

import org.apache.struts2.interceptor.SessionAware;
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


public class TumblrGrantAccess extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private String authorizationURL = null;

    @Override
    public String execute() {
        //Tumblr tumblr = new TumblrFactory().getInstance();
    	
        String consumer_key = "QYs2J3eSSXEvcLMUst6ZF6EhEdMuNCqPQZ93d3atJMNBc9arHG";
        String consumer_secret = "7MKeu8XqZXdkC3zzT8dQD3hvkkh3EPUYrY6fQHDyvU3FL7q34b";

        OAuthService tumblrService = new ServiceBuilder()
                .provider(TumblrApi.class)
                .apiKey(consumer_key)
                .apiSecret(consumer_secret)
                .callback("http://localhost:8080/FundStarterWeb/tumblrRedirect.jsp")
                .build();
        Token requestToken = tumblrService.getRequestToken();
        authorizationURL = tumblrService.getAuthorizationUrl(requestToken);
        System.out.println(authorizationURL);
        session.put("tumblrService", tumblrService);
        session.put("requestToken", requestToken);
        session.put("tumblr_logged", true);
        return SUCCESS;
    }

    public String getAuthorizationURL() {
        return this.authorizationURL;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}