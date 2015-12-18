package tumblr;
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



// Step 1: Create Tumblr Account

// Step 2: Create Application 

public class TumblrClient {

	// Access codes #1: per application used to get access codes #2	
	private static final String API_APP_KEY = "QYs2J3eSSXEvcLMUst6ZF6EhEdMuNCqPQZ93d3atJMNBc9arHG";
	private static final String API_APP_SECRET = "7MKeu8XqZXdkC3zzT8dQD3hvkkh3EPUYrY6fQHDyvU3FL7q34b";
	
	// Access codes #2: per user per application
	private static final String API_USER_TOKEN = "";
	private static final String API_USER_SECRET = "";
	
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		OAuthService service = new ServiceBuilder()
		.provider(TumblrApi.class)
		.apiKey(API_APP_KEY)
		.apiSecret(API_APP_SECRET)
		.callback("http://127.0.0.1")
		.build();

		try {


			if ( API_USER_TOKEN.equals("") || API_USER_SECRET.equals("") ) {
				System.out.println("Fetching the Request Token...");
				Token requestToken = service.getRequestToken();
				System.out.println("Now go and authorize Scribe here:");
				System.out.println(service.getAuthorizationUrl(requestToken));
				System.out.println("And paste the verifier here");
				System.out.print(">>");
				Verifier verifier = new Verifier(in.nextLine());
				Token accessToken = service.getAccessToken(requestToken, verifier);
				System.out.println("Define API_USER_TOKEN: " + accessToken.getToken());
				System.out.println("Define API_USER_SECRET: " + accessToken.getSecret());
				System.exit(0);
			}

			Token accessToken = new Token( API_USER_TOKEN, API_USER_SECRET);
			
			OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.tumblr.com/v2/blog/alcides.tumblr.com/posts", service);
			request.addHeader("Accept", "application/json");
			service.signRequest(accessToken, request);
			System.out.println(request.getHeaders().keySet());
			Response response = request.send();
			System.out.println("Got it! Lets see what we found...");
			System.out.println("HTTP RESPONSE: =============");
			System.out.println(response.getCode());
			System.out.println(response.getBody());
			System.out.println("END RESPONSE ===============");

			JSONObject inf = (JSONObject) JSONValue.parse(response.getBody());
			JSONArray arr = (JSONArray) ((JSONObject) inf.get("response")).get("posts");
			for(int i=0; i< arr.size(); i++) {
				JSONObject item = (JSONObject) arr.get(i);
				JSONObject user = (JSONObject) item.get("user");
				System.out.println(item.get("date") + " said: " + item.get("type"));
			}
			
		} catch(OAuthException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}



	}

}
