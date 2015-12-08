import java.util.ArrayList;

public class User {
	int id;
	String nome, apelido, username, password;
	ArrayList <Projecto> listaProj = new ArrayList<Projecto>(0);
	
	public User(String nome, String apelido, String username, String password){
		this.nome=nome;
		this.apelido=apelido;
		this.username=username;
		this.password=password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Projecto> getListaProj() {
		return listaProj;
	}

	public void setListaProj(ArrayList<Projecto> listaProj) {
		this.listaProj = listaProj;
	}

	

}
