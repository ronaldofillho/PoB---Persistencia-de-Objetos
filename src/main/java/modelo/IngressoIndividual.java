/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/
package modelo;
import jakarta.persistence.*;

@Entity
public class IngressoIndividual extends Ingresso {
	@ManyToMany(mappedBy="ingressos",
		cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
		}
	)
	private ArrayList<Jogo> jogos = new ArrayList<Jogo>();

	public IngressoIndividual() {}
	public IngressoIndividual(int codigo) {
		super(codigo);
	}

	public double calcularValor() {
		return 1.2 * jogo.getPreco();
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
		jogo.setEstoque(jogo.getEstoque() - 1 );
	}

	@Override
	public String toString() {
		return "codigo=" + codigo + ", jogo=" + jogo.getId();
	}

}