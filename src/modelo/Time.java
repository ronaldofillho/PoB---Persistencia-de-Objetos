
package modelo;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Time {
	@Id
	private String nome;
	private String origem;

	@OneToMany (
		cascade = {
			CascadeType.ALL
		}
	)
	private List<Jogo> jogos = new ArrayList<>();
	
	public Time(String nome, String origem) {
		super();
		this.nome = nome;
		this.origem = origem;
	}

	public Time() {}

	public double obterValorArrecadado() {
		double soma=0;
		for(Jogo j : jogos)
			soma = soma + j.obterValorArrecadado();
		
		return soma;
	}

	public void adicionar(Jogo j) {
		jogos.add(j);
	}
	public void remover(Jogo j) {
		jogos.remove(j);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}
	
	public List<Jogo> getJogos() {
		return this.jogos;
	}
	
	public int getNumJogos() {
		return this.jogos.size();
	}

	@Override
	public String toString() {
		String texto = "nome=" + nome + ", origem=" + origem ;
		
		texto += 	"\njogos: " ;
		for(Jogo j : jogos)
			texto += j.getId() +"=" + j.getData()+ "," + j.getLocal() +"  ";
		return texto;
	}
}
