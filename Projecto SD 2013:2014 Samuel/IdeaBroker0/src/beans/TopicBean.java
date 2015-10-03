package beans;

import java.io.Serializable;

public class TopicBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String author;
	
	public TopicBean () {
		
	}
	
	public TopicBean (int id, String name, String author) {
		
		this.id = id;
		this.name = name;
		this.author = author;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
