package websockets;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

public class WebSocketAnnotation {
	
	private static final AtomicInteger sequence = new AtomicInteger(1);
	
	@ServerEndpoint(value = "/ws")
	public WebSocketAnnotation(){
		
	}
}
