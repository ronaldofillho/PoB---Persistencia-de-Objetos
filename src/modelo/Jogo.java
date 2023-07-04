
package modelo;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
@Entity
public class Jogo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String data;
	private String local;
	private int estoque;
	private double preco;

	@OneToOne
	private Time time1;

	@OneToOne
	private Time time2;

	@ManyToMany(cascade= {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	private List<Ingresso> ingressos =  new ArrayList<>();


	public Jogo(String data, String local, int estoque, double preco) {
		//id sera gerado pelo banco;
		this.data = data;
		this.local = local;
		this.estoque = estoque;
		this.preco = preco;
	}
	public Jogo(int id, String data, String local, int estoque, double preco) {
		//id sera gerado pelo banco;
		this.id = id;
		this.data = data;
		this.local = local;
		this.estoque = estoque;
		this.preco = preco;
	}

	public Jogo () {}
	public void adicionar(Ingresso i){
		ingressos.add(i);
	}
	public void remover(Ingresso i){
		ingressos.remove(i);
	}

	public Ingresso localizar(int codigo){
		for(Ingresso i : ingressos){
			if(i.getCodigo() == codigo)
				return i;
		}
		return null;
	}

	public double obterValorArrecadado() {
		double soma=0;
		for(Ingresso i : ingressos)
			soma = soma + i.calcularValor();

		return soma;
	}

	public int getId() {
		return id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public int getEstoque() {
		return estoque;
	}

	public void setEstoque(int estoque) {
		this.estoque = estoque;
	}

	public Time getTime1() {
		return time1;
	}

	public Time getTime2() {
		return time2;
	}
	
	public void setTime1(Time time1) {
		this.time1 = time1;
	}

	public void setTime2(Time time2) {
		this.time2 = time2;
	}

	public List<Ingresso> getIngressos() {
		return ingressos;
	}
	public void setIngressos(Ingresso ingresso) {
		this.ingressos.add(ingresso);
	}
	@Override
	public String toString() {

		String texto = "id=" + id + ", data=" + data + ", local=" + local + ", estoque=" + estoque + ", preco=" + preco
				+ ", time1=" + this.getTime1().getNome() + " x time2=" + this.getTime2().getNome();

		texto += "\ningressos:";
		for(Ingresso i : ingressos)
			texto += i.getCodigo() + ",";
		return texto;
	}
}