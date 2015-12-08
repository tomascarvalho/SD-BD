
public class Projecto {
	int id, valorPretendido, valorActual;
	String titulo, descricao;
	int dataLimite[];	//!
	int horaLimite[];
	
	
	public Projecto(int id, int valorPretendido, int valorActual, String titulo, String descricao, int[] dataLimite,
			int[] horaLimite) {
		this.id = id;
		this.valorPretendido = valorPretendido;
		this.valorActual = valorActual;
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataLimite = dataLimite;
		this.horaLimite = horaLimite;
	}
	
	
}
