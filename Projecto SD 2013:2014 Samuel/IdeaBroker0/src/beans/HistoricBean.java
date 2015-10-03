package beans;

import java.io.Serializable;

public class HistoricBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String buyer;
	private String seller;
	private String title;
	private int shares;
	private int price;
	
	
	public HistoricBean () {
		
	}
	
	public HistoricBean (int id, String buyer, String seller, int shares, String title, int price) {
		this.id = id;
		this.buyer = buyer;
		this.seller = seller;
		this.shares = shares;
		this.title = title;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
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
	
}
