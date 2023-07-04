/************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh o Ayres
 ************/

package regras_negocio;

import java.util.*;

import dao.*;
import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;
import modelo.Jogo;
import modelo.Time;
import modelo.Usuario;

public class Fachada {
	private Fachada() {}	

	private static DAOUsuario daousuario = new DAOUsuario(); 
	private static DAOTime daotime = new DAOTime(); 
	private static DAOJogo daojogo = new DAOJogo(); 
	private static DAOIngresso daoingresso = new DAOIngresso(); 
	private static DAOIngressoIndividual daoingressoindividual = new DAOIngressoIndividual(); 
	private static DAOIngressoGrupo daoingressogrupo = new DAOIngressoGrupo(); 


	public static Usuario logado;
	public static void inicializar(){
		DAO.open();
	}
	public static void finalizar(){
		DAO.close();
	}


	public static List<Time> listarTimes() {
		DAO.begin();
		List<Time> listagemTimes =  daotime.readAll();
		DAO.commit();
		return listagemTimes;
	}
	
	public static List<Jogo> listarJogos() {
		DAO.begin();
		List<Jogo> listagemDosJogos =  daojogo.readAll();
		DAO.commit();
		return listagemDosJogos;
	}
	
	public static List<Usuario> listarUsuarios() {
		DAO.begin();
		List<Usuario> listagemDosUsuarios =  daousuario.readAll();
		DAO.commit();
		return listagemDosUsuarios;
	}
	
	public static List<Ingresso> listarIngressos() {
		DAO.begin();
		List<Ingresso> listagemDosIngressos =  daoingresso.readAll();
		DAO.commit();
		return listagemDosIngressos;
	}
	public static List<Jogo> listarJogosPorData(String data) {
		DAO.begin();
		List<Jogo> listagemDosJogosPorData =  daojogo.listarJogosDeUmaData(data);
		DAO.commit();
		return listagemDosJogosPorData;
	}
	public static Ingresso localizarIngresso(int codigo) {
		DAO.begin();
		Ingresso ingresso = daoingresso.read(codigo);
		DAO.commit();
		return ingresso;
	}

	public static Jogo localizarJogo(int id) {
		DAO.begin();
		Jogo jogo =  daojogo.read(id);
		DAO.commit();
		return jogo;
	}

	public static Usuario criarUsuario(String email, String senha) throws Exception{
		DAO.begin(); 
		Usuario usu = daousuario.read(email);
		if (usu!=null) {
			DAO.rollback();
			throw new Exception("Usuario ja cadastrado:" + email);
		}
		usu = new Usuario(email, senha);

		daousuario.create(usu);
		DAO.commit();
		return usu;
	}
	public static Usuario validarUsuario(String email, String senha) {
		DAO.begin();
		Usuario usu = daousuario.read(email);
		if (usu==null)
			return null;

		if (! usu.getSenha().equals(senha))
			return null;

		DAO.commit();
		return usu;
	}

	public static Time criarTime(String nome, String origem) throws Exception {
		DAO.begin();
		if(nome.trim().equals("")) {
			throw new Exception("Nome do time está vazio");
		}

		if(origem.trim().equals("")) {
			throw new Exception("Origem está vazia");
		}

		Time timeTemp = daotime.read(nome);

		if(timeTemp != null) {
			DAO.rollback();
			throw new Exception("Nome do time já existente.");
		}
		//Criação do time
		Time time = new Time(nome, origem);
		//Salvar no banco
		daotime.create(time);
		DAO.commit();
		return time;
	}


	public static Jogo criarJogo(String data, String local, int estoque, double preco, String nomeTime1, String nomeTime2) throws Exception {
		DAO.begin();

		if(!nomeTime1.equals(nomeTime2)) {
			Jogo jogo = new Jogo(data, local, estoque, preco);

			Time time1 = daotime.read(nomeTime1);
			Time time2 = daotime.read(nomeTime2);

			if(time1 == null || time2 == null) {
				throw new Exception("Time 1 ou 2 não existe.");
			}

			if(preco == 0) {
				throw new Exception("O valor não pode ser igual a 0.");
			}

			if(estoque == 0) {
				throw new Exception("O estoque não pode ser 0.");
			}

			else {
				jogo.setTime1(time1);
				jogo.setTime2(time2);
				time1.adicionar(jogo);
				time2.adicionar(jogo);
				daojogo.create(jogo);
				daotime.update(time1);
				daotime.update(time2);
				DAO.commit();
				return jogo;
			}
		}
		throw new Exception("Os nomes dos times são iguais.");
	}

	public static IngressoIndividual criarIngressoIndividual(int id) throws Exception {
		DAO.begin();

		// Verificar regras de negócio

		// Gerar código baseado na data atual
		Date dataAtual = new Date();
		int codigo = (int) dataAtual.getTime();
		IngressoIndividual ingressoIndividual;

		Jogo jogo = daojogo.read(id);
		if (jogo == null) {
			throw new Exception("Jogo não existe");
		}

		System.out.println(jogo.getEstoque());

		do {
			ingressoIndividual = (IngressoIndividual) daoingressoindividual.read(codigo);
			codigo++;
		} while (ingressoIndividual != null);

		// Criar o ingresso individual
		ingressoIndividual = new IngressoIndividual(codigo);

		// Relacionar este ingresso com o jogo e vice-versa
		ingressoIndividual.setJogo(jogo);
		jogo.adicionar(ingressoIndividual);

		daoingressoindividual.create(ingressoIndividual);
		daojogo.update(jogo);

		// Gravar ingresso no banco
		try {
			DAO.commit();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
			DAO.rollback();
			throw e;
		}

		System.out.println("aqui3");

		return ingressoIndividual;
	}

	public static IngressoGrupo criarIngressoGrupo(int[] ids) throws Exception {
		DAO.begin();
		ArrayList<Jogo> jogos = new ArrayList<Jogo>();

		// Verificar se os jogos informados existem
		for (int id : ids) {
			Jogo jogo = daojogo.read(id);
			if (jogo == null) {
				throw new Exception("O jogo de id '" + id + "' não existe.");
			}
			jogos.add(jogo);
		}

		// Gerar código baseado na data atual
		Date dataAtual = new Date();
		int codigo = (int) dataAtual.getTime();
		IngressoGrupo ingressoGrupo;

		// Verificar unicidade do código no sistema
		do {
			ingressoGrupo = (IngressoGrupo) daoingressogrupo.read(codigo);
			codigo++;
		} while (ingressoGrupo != null);

		// Criar o ingresso grupo
		ingressoGrupo = new IngressoGrupo(codigo);

		// Relacionar este ingresso com os jogos indicados e vice-versa
		for (Jogo j : jogos) {
			if (j.getIngressos().contains(ingressoGrupo)) {
				throw new Exception("O ingresso grupo já está associado ao jogo de id '" + j.getId() + "'.");
			}
			j.adicionar(ingressoGrupo);
			j.setEstoque(j.getEstoque() - 1);
			daojogo.update(j);
			ingressoGrupo.adicionar(j);
		}

		// Atualizar o estoque de ingressos do jogo
		for (int id : ids) {
			Jogo jogo = daojogo.read(id);
			if (jogo != null) {
				jogo.setEstoque(jogo.getEstoque() - 1);
				daojogo.update(jogo);
			}
		}

		daoingressogrupo.create(ingressoGrupo);
		DAO.commit();
		return ingressoGrupo;
	}


	public static void apagarIngresso(int codigo) throws Exception {
	    DAO.begin();
		//argumento código pode ser individual ou grupo
		//remover o relacionamento entre o ingresso e o jogo

		Ingresso ingresso = daoingresso.read(codigo);

		if(ingresso == null) {
			throw new Exception("Código do Ingresso não é válido");
		}

		if ( ingresso instanceof IngressoGrupo grupo) {
			ArrayList<Jogo> jogos = grupo.getJogos();
			for(Jogo j : jogos) {
				j.remover(grupo);
				j.setEstoque(j.getEstoque()+1);
				daojogo.update(j);
			}
		} else if (ingresso instanceof IngressoIndividual individual) {
			Jogo jogo = individual.getJogo();
			jogo.remover(individual);
			jogo.setEstoque(jogo.getEstoque()+1);
			daojogo.update(jogo);
		}
		//apagando ingresso do banco
		daoingresso.delete(ingresso);
		DAO.commit();
	}

	public static void apagarTime(String nome) throws Exception {
	    DAO.begin();
	    Time time = daotime.read(nome);

		if(time == null) {
			throw new Exception("Este nome não pertence a um time cadastrado.");
		}

		if(time.getJogos().size() > 0) {
			throw new Exception("Este time possui jogos vinculados");
		}

		daotime.delete(time);
	    DAO.commit();
	}

	public static void apagarJogo(int id) throws Exception {
		DAO.begin();
		Jogo jogo = daojogo.read(id);
		if (jogo == null) {
			throw new Exception("Jogo não existe.");
		}
		if (jogo.getIngressos().size() > 0) {
			throw new Exception("Jogo possui ingressos ativos.");
		}

		daojogo.delete(jogo);
		DAO.commit();
	}
	/**********************************
	 * 5 Consultas
	 **********************************/
	
//	Consulta 1: Verifica os times que jogaram num determinado local
	public static List<Time> listarTimesQueJogaramNoLocal(String local) throws Exception{
		DAO.begin();
		List<Time> times = daotime.buscarTimesPorLocal(local);
		if(times.size() == 0) {
			throw new Exception("Não há times no registro.");
		}
		DAO.commit();
		return times;
	}
	
//Consulta 2: Verifica os times que possuem ingressos disponíveis
	public static List<Time> estoqueIngressosDoTime(String nomeTime) throws Exception {
		DAO.begin();
		List<Time> times = daotime.buscarTimesComIngressosDisponiveis();
		if (times.size() == 0) {
			throw new Exception("Não possuem times com jogos disponíveis");
		}
		DAO.commit();
		return times;
	}
	
	// Consulta 3: Jogos de um time específico 
	public static List<Jogo> jogosDeUmTimeEspecifico(String time) throws Exception {
		DAO.begin();
		List<Jogo> jogos = daojogo.listarJogosDeUmTime(time);
		System.out.println(jogos);
		System.out.println(time);
		if (jogos.size() == 0) {
			throw new Exception("Não existe jogos com esse time");
		}
		DAO.commit();
		return jogos;
	}

	// Consulta 4: Verifica os times que possuem Jogos com ingressos disponíveis
		public static List<Time> timesQueMaisJogaram(Integer numTimes) throws Exception{
			DAO.begin();
			List<Time> times = daotime.buscarTimesComIngressosDisponiveis();
			if (times.size() == 0) {
				throw new Exception("Nao existem times.");
			}
			DAO.commit();
			return times;
		}
	
	// Consulta 5: Verifica a quantidade de ingressos individuais vendidos de um time
		public static int numIndividualTicketsSold(String nomeTime) throws Exception {
			List<Jogo> jogosDoTime = daojogo.listarJogosDeUmTime(nomeTime);
			int somaIng = 0;
			for(Jogo jogo : jogosDoTime){
				List<Ingresso> ingressosDoJogo = jogo.getIngressos();
				for(Ingresso ingresso : ingressosDoJogo){
					if (ingresso instanceof IngressoIndividual){
						somaIng++;
					}
				}
			}
			return somaIng;
		}
	
}