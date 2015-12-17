package fundstarter.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import fundstarter.model.ConnectToRMIBean;

public class ReplyMessageAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private int messageID;
	private String reply;

	public String execute() throws RemoteException {

		System.out.println("[SeeMessageAction]<execute>:\n\tMessage ID -> " + this.messageID);
		System.out.println("\tReply Message -> " + this.reply);
		if (this.getConnectToRMIBean().replyMessage(this.messageID, this.reply).equals("success")) {
			return SUCCESS;
		} else {
			return ERROR;
		}

	}

	public void setMessageID(String messageID) {
		this.messageID = Integer.parseInt(messageID);
	}

	public void setReply(String reply) {
		this.reply = reply;
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
