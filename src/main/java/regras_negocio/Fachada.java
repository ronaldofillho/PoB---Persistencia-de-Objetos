/************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh o Ayres
 ************/

package regras_negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		List<Jogo> listagemDosJogos =  daojogo.listarJogos();
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
		List<Jogo> listagemDosJogosPorData =  daojogo.buscarPorData(data);
		DAO.commit();
		return listagemDosJogosPorData;
	}
	public static Ingresso localizarIngresso(int codigo) {
		DAO.begin();
		Ingresso ingresso = daoingresso.read(Integer.valueOf(codigo));
		DAO.commit();
		return ingresso;
	}

	public static Jogo	localizarJogo(int id) {
		DAO.begin();
		Jogo jogo =  daojogo.read(id);
		DAO.commit();
		return jogo;
	}

	public static Usuario criarUsuario(String email, String senha) throws Exception{
		DAO.begin(); 
		Usuario usu = daousuario.read(email);
		if (usu!=null)
			throw new Exception("Usuario ja cadastrado:" + email);
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
	    try {
	        if (nome.trim().equals("")) {
	            throw new Exception("Nome do time vazio.");
	        }

	        if (origem.trim().equals("")) {
	            throw new Exception("Origem do time vazio.");
	        }

	        DAOTime daotime = new DAOTime();
	        Time timeExistente = daotime.read(nome);
	        if (timeExistente == null) {
	            Time time = new Time(nome, origem);
	            daotime.create(time);
	            DAO.commit();
	            return time;
	        } else {
	            DAO.rollback();
	            throw new Exception("Time já existe.");
	        }
	    } catch (Exception e) {
	        DAO.rollback();
	        throw e;
	    }
	}


	public static Jogo criarJogo(String data, String local, int estoque, double preco, String nomeTime1, String nomeTime2) throws Exception {
		DAO.begin();

		// Verifica se a data e o local foram informados
		if (data == null || data.trim().isEmpty() || local == null || local.trim().isEmpty()) {
			throw new Exception("Data e/ou local devem ser preenchidos.");
		}

		// Verifica se o estoque e o preço são maiores que zero
		if (estoque <= 0 || preco <= 0.0) {
			throw new Exception("Estoque e/ou preço devem ser maiores que zero.");
		}
		
		//RN4
		int newId = daojogo.gerarId();
		
		// Verifica se os times são diferentes
		
		if (nomeTime1.equals(nomeTime2)) {
			throw new Exception("Um jogo não pode ter dois times iguais.");
		}
		
		if (nomeTime2.equals(nomeTime1)) {
			throw new Exception("Um jogo não pode ter dois times iguais.");
		}

		// Localiza os times
		Time time1 = daotime.read(nomeTime1);
		Time time2 = daotime.read(nomeTime2);

		if (time1 == null || time2 == null) {
			throw new Exception("Times inválidos ou inexistentes.");
		}

		// Cria o jogo
		Jogo jogo = new Jogo(data, local, estoque, preco);
		jogo.setId(newId);
		
		jogo.setTime1(time1);
		jogo.setTime2(time2);

		// Relaciona o jogo com os times
		jogo.setTime1(time1);
		jogo.setTime2(time2);
		time1.adicionar(jogo);
		time2.adicionar(jogo);

		// Grava o jogo no banco
		daojogo.create(jogo);
		DAO.commit();
		return jogo;
	}

	public static IngressoIndividual criarIngressoIndividual(int id) throws Exception {
	    DAO.begin();
	    // Verificar regras de negócio
	    Jogo jogo = daojogo.read(id);
	    if (jogo != null) {
	        // Array com todos os id já registrados
	        ArrayList<Integer> idIngressos = new ArrayList<>();
	        for (Ingresso i : jogo.getIngressos()) {
	            idIngressos.add(i.getCodigo());
	        }

	        // Gerar código aleatório
	        int codigo;
	        boolean codigoUnico;
	        do {
	            codigo = new Random().nextInt(1000000) + 1;
	            codigoUnico = !idIngressos.contains(codigo);
	        } while (!codigoUnico);

	        // Criar o ingresso individual
	        IngressoIndividual ingresso = new IngressoIndividual(codigo);

	        // Relacionar este ingresso com o jogo e vice-versa
	        ingresso.setJogo(jogo);
	        // Utilizando o método personalizado "adicionar()"
	        jogo.adicionar(ingresso);
	        jogo.setEstoque(jogo.getEstoque() - 1);

	        // Gravar ingresso no banco
	        daoingressoindividual.create(ingresso);
	        DAO.commit();
	        return ingresso;
	    }
	    throw new Exception("Jogo não encontrado");
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

	    // Gerar código aleatório único
	    IngressoGrupo ingressoGrupo;
	    int codigo;
	    do {
	        codigo = new Random().nextInt(1000000) + 1;
	        ingressoGrupo = daoingressogrupo.read(codigo);
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
	    Ingresso ingresso = daoingresso.read(codigo);
	    if (ingresso == null) {
	        throw new Exception("Não existe ingresso com o código " + codigo + ".");
	    }

	    if (ingresso instanceof IngressoGrupo) {
	        IngressoGrupo grupo = (IngressoGrupo) ingresso;
	        ArrayList<Jogo> jogos = grupo.getJogos();
	        for (Jogo jogo : jogos) {
	            // Atualizar estoque do jogo
	            jogo.setEstoque(jogo.getEstoque() + 1);
	            daojogo.update(jogo);
	        }
	    } else if (ingresso instanceof IngressoIndividual) {
	        IngressoIndividual individuo = (IngressoIndividual) ingresso;
	        Jogo jogo = individuo.getJogo();
	        if (jogo.getIngressos().contains(individuo)) {
	            jogo.remover(individuo);
	            jogo.setEstoque(jogo.getEstoque() + 1);
	            daojogo.update(jogo);
	        } else {
	            throw new Exception("O ingresso já foi vendido e não pode ser apagado.");
	        }
	    }

	    DAO.begin();
	    // Apagar ingresso no banco
	    daoingresso.delete(ingresso);
	    DAO.commit();
	}

	public static void apagarTime(String nome) throws Exception {
	    DAO.begin();
	    DAOJogo daoJogo = new DAOJogo();
	    DAOTime daoTime = new DAOTime();

	    Time time = daoTime.read(nome);
	    if (time == null) {
	        throw new Exception("Time não encontrado.");
	    }

	    // RN8: Um time não poderá ser excluído se possuir jogos
	    List<Jogo> jogosDoTime = daoJogo.buscarPorTime(time);
	    if (!jogosDoTime.isEmpty()) {
	        throw new Exception("Não é possível excluir o time pois possui jogos associados.");
	    }

	    daoTime.delete(time);
	    DAO.commit();
	}

	public static void apagarJogo(int id) throws Exception {
	    DAO.begin();
	    DAOJogo daoJogo = new DAOJogo();
	    Jogo jogo = daoJogo.read(id);
	    if (jogo != null) {
	        // Verifica se o jogo possui ingressos
	        if (!jogo.getIngressos().isEmpty()) {
	            throw new Exception("Não é possível excluir um jogo que possui ingressos.");
	        }

	        // Verifica se os times são diferentes
	        if (jogo.getTime1().equals(jogo.getTime2())) {
	            throw new Exception("Um jogo não pode ter dois times iguais.");
	        }

	        // Remove o jogo
	        daoJogo.delete(jogo);
	        jogo.setId(id); // Mantém o ID original do jogo

	        DAO.commit();
	    } else {
	        throw new Exception("Jogo não encontrado.");
	    }
	}
	/**********************************
	 * 5 Consultas
	 **********************************/
	
//	Consulta 1: Verifica as datas dos jogos de um time
	public static ArrayList<Jogo> listarJogosDoTime(String nomeTime) throws Exception{
		DAO.begin();
		Time time = daotime.read(nomeTime);
		if (time == null){
			DAO.rollback();
			throw new Exception("Nao existe um time com este nome");
		}
		ArrayList<Jogo> jogos = time.getGames();


		DAO.commit();
		return jogos;
	}
	
	//Consulta 2: Verifica a quantidade no estoque de ingressos disponíveis para cada jogo de um time específico
		public static ArrayList<Jogo> estoqueGeralDoTime(String nomeTime) throws Exception {
			return listarJogosDoTime(nomeTime);
		}
	
	// Consulta 3: Jogos de um time específico 
		public static List<Jogo> jogosDeUmTimeEspecifico(String time) throws Exception {
			DAO.begin();
			List<Jogo> jogos = daojogo.jogosDeUmTimeEspecifico(time);
			System.out.println(jogos);
			System.out.println(time);
			if (jogos.size() == 0) {
				throw new Exception("N o existe jogos com esse time");
			}
			DAO.commit();
			return jogos;
		}
	// Consulta 4: Verifica times que jogaram em determinado local
		public static List<Time> timesQueJogaramEmUmLocal(String local) throws Exception{
			DAO.begin();
			List<Time> times = daotime.LocalTeam(local);
			if (times.size() == 0) {
				throw new Exception("Nao existem times que jogaram no local informado");
			}
			DAO.commit();
			return times;
		}
	
	// Consulta 5: Verifica a quantidade de ingressos individuais vendidos de um time
		public static int numIndividualTicketsSold(String nomeTime) throws Exception {
			ArrayList<Jogo> jogosDoTime = listarJogosDoTime(nomeTime);
			int somaIng = 0;
			for(Jogo jogo : jogosDoTime){
				ArrayList<Ingresso> ingressosDoJogo = jogo.getIngressos();
				for(Ingresso ingresso : ingressosDoJogo){
					if (ingresso instanceof IngressoIndividual){
						somaIng++;
					}
				}
			}
			return somaIng;
		}
	
}