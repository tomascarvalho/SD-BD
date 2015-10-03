package beans;

import java.io.Serializable;

public class UserBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private String username;
	private int id;
	private int saldo;
	
	public UserBean () {
	}
	
	public int getSaldo() {
		return saldo;
	}

	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername () {
		return username;
	}

	public void setUsername (String username) {
		this.username = username;
	}
}