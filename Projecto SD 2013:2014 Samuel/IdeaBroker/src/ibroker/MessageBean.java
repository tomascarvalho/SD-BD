package ibroker;

public class MessageBean {
	
	private String message = "";
	
	public MessageBean () {
	}
	
	public MessageBean (String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

}
