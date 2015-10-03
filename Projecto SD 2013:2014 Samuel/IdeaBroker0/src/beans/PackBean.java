package beans;

import java.io.Serializable;

public class PackBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String owner;
	private int idea;
	private String title;
	private int shares;
	private int price;
	private int lastPrice;
	
	public PackBean () {
		
	}
	
	public PackBean (int id, String owner, int idea, String title, int shares, int price, int lastPrice) {
		
		this.id = id;
		this.owner = owner;
		this.idea = idea;
		this.title = title;
		this.shares = shares;
		this.price = price;
		this.lastPrice = lastPrice;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public int getIdea() {
		return idea;
	}

	public void setIdea(int idea) {
		this.idea = idea;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(int lastPrice) {
		this.lastPrice = lastPrice;
	}
	
}
