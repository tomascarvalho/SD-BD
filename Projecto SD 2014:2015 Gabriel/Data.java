import java.io.Serializable;
import java.util.*;
import java.util.LinkedList;

public class Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int order;
	private Queue<String> data;
	private Date time;

	public Data () {
		this.data = new LinkedList<String> ();
		this.time = new Date();
	}

	public void setOrder (int n) {
		this.order = n;
	}

	public void addData (String str) {
		data.add(str);
	}
 
	public int getOrder () {
		return this.order;
	}

	public String pollData () {
		return data.poll();
	}

	public Date getDate () {
		return this.time;
	}
}