package ibroker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import beans.UserBean;

/**
 * Servlet implementation class WebSocketServlet
 */

@SuppressWarnings("deprecation")
@WebServlet("/DataWebSocketServlet")
public class DataWebSocketServlet extends WebSocketServlet {
	
	private final AtomicInteger connectionIds = new AtomicInteger (0);
	private final Set < DataInbound > connections = new CopyOnWriteArraySet < DataInbound >();
	
	private static final long serialVersionUID = 1L;
    
    public final class DataInbound extends MessageInbound {

    	private final String username;
    	
    	DataInbound(String s) {
			this.username = s;
		}
    	
    	protected void onOpen(WsOutbound outbound) {
			connections.add(this);;
		}

		protected void onClose(int status) {
			connections.remove(this);
		}
		
		public void broadcast(String message, String user) { // send message to all
			for (DataInbound connection : connections) {
				try {
					if (connection.getUsername().equals(user) || user.equals("ALL") ) {
						CharBuffer buffer = CharBuffer.wrap(message);
						connection.getWsOutbound().writeTextMessage(buffer);
					}
				} catch (IOException ignore) {}
			}
		}
		
    	
		public String getUsername() {
			return username;
		}
		

		@Override
		protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onTextMessage(CharBuffer arg0) throws IOException {
			// TODO Auto-generated method stub
		}
    	
    }

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		UserBean user = (UserBean) session.getAttribute("user");
		DataInbound ws = new DataInbound(user.getUsername());
		session.setAttribute("ws", ws);
		return ws;
	}

}
