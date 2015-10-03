package beans;

import java.io.Serializable;

public class IdeaBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String description;
	private String author;
	private int lastPrice;
	
	public IdeaBean () {
		
	}
	
	public IdeaBean (int id, String name, String description, String author, int lastPrice) {
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
		this.lastPrice = lastPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public int getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(int price) {
		this.lastPrice = price;
	}
		
}
