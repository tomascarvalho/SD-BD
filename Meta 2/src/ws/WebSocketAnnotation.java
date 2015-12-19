package ws;


import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint("/ws")
public class WebSocketAnnotation {
	
	private static final AtomicInteger sequence = new AtomicInteger(1);
	private Session session;
	private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();
	
	public WebSocketAnnotation(){
		System.out.println("[WebSockets]cheguei a esta função");
	}
	
	@OnOpen
    public void start(Session session) {
        System.out.println("huelelelelel");
		this.session = session;
		users.add(this);
		System.out.println("[WebSockets]");
		
    }

    @OnClose
    public void end() {
    	
    }

    @OnMessage
    public void receiveMessage(String message) {
		
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }


}
