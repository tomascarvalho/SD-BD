package ws;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class HandShake extends ServerEndpointConfig.Configurator {
		
		@Override
		public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response){
			HttpSession httpsession = (HttpSession) request.getHttpSession();
			config.getUserProperties().put(HttpSession.class.getName(), httpsession);
		}

}
