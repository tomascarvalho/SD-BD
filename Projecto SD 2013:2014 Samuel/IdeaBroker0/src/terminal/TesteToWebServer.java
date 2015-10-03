package terminal;

import java.net.HttpURLConnection;
import java.net.URL;

public class TesteToWebServer {

	public static void main(String[] args){
		String urlStr = "http://localhost:8080/IdeaBroker/TesteTCP?packOwner=pedro&notification=merda";  
        try{  
	        URL url = new URL(urlStr);  
	        HttpURLConnection connection= (HttpURLConnection)url.openConnection();  
	        //connection.connect();  
	        connection.setAllowUserInteraction(true);  
	        connection.setRequestMethod("GET");  
	        connection.setDoOutput(true);  
	        connection.setUseCaches(false);  
	        connection.getInputStream();
	        //connection.setRequestProperty("Content-Length","application/x-www-form-urlencoded");  
	          
	        System.out.println("*************CONNECTED*************");  
	        //connection.disconnect();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
    }
}
