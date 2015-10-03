import java.io.Serializable;
import java.util.*;
import java.util.LinkedList;

public class DBData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int order;
	private int rows;
	private Queue<String> data;

	public DBData () {
		this.data = new LinkedList<String> ();
		this.rows = 0;
	}

	public void setOrder (int n) {
		this.order = n;
	}

	public void setRow (int n) {
		this.rows = n;
	}

	public void addData (String str) {
		data.add(str);
	}
 
	public int getOrder () {
		return this.order;
	}

	public int getRow () {
		return this.rows;
	}

	public String pollData () {
		return data.poll();
	}
}