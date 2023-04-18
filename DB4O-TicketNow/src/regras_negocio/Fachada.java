/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package regras_negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import daodb4o.DAO;
import daodb4o.DAOIngresso;
import daodb4o.DAOIngressoGrupo;
import daodb4o.DAOIngressoIndividual;
import daodb4o.DAOJogo;
import daodb4o.DAOTime;
import daodb4o.DAOUsuario;
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
		Ingresso ingresso = daoingresso.read(codigo);
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
	    DAOTime daotime = new DAOTime();
	    Time timeExistente = daotime.read(nome);
	    if (timeExistente == null) {
	        Time time = new Time(nome, origem);
	        daotime.create(time);
	        DAO.commit();
	        return time;
	    } else {
	        throw new Exception("Time já existe.");
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
		time1.adicionar(jogo);
		time2.adicionar(jogo);

		// Grava o jogo no banco
		daojogo.create(jogo);
		DAO.commit();
		return jogo;
	}

	public static IngressoIndividual criarIngressoIndividual(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		Jogo jogo = daojogo.read(id);
		if (jogo != null) {
			//array com todos os id já registrados 
			ArrayList<Integer> idIngressos = new ArrayList<>();
			for(Ingresso i: jogo.getIngressos()) {
				idIngressos.add(i.getCodigo());
			}
			//gerar codigo aleatório 
			int codigo = new Random().nextInt(1000000);
			//verificar unicidade do codigo no sistema 
			while(idIngressos.contains(codigo)) {
				codigo = new Random().nextInt(1000000);
			}
			//criar o ingresso individual 
			IngressoIndividual ingresso = new IngressoIndividual(codigo);

			//relacionar este ingresso com o jogo e vice-versa
			ingresso.setJogo(jogo);
			 // Utilizando o método personalizado "adicionar()"
			jogo.adicionar(ingresso);
			jogo.setEstoque(jogo.getEstoque()-1);
			//gravar ingresso no banco
			daoingressoindividual.create(ingresso);
			DAO.commit();
			return ingresso;
		}
		throw new Exception("Jogo nao encontrado");
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
	        codigo = new Random().nextInt(1000000);
	        ingressoGrupo = (IngressoGrupo) daoingressogrupo.read(codigo);
	    } while (ingressoGrupo != null);

	    // Criar o ingresso grupo
	    ingressoGrupo = new IngressoGrupo(codigo);

	    // Relacionar este ingresso com os jogos indicados e vice-versa
	    for (Jogo j : jogos) {
	        j.adicionar(ingressoGrupo);
	        j.setEstoque(j.getEstoque() - 1);
	        daojogo.update(j);
	        ingressoGrupo.adicionar(j);
	    }

	    daoingressogrupo.create(ingressoGrupo);
	    DAO.commit();
	    return ingressoGrupo;
	}

	public static void apagarIngresso(int codigo) throws Exception {
		DAO.begin();
		Ingresso ingresso = daoingresso.read(codigo);
		if (ingresso == null) {
			throw new Exception("Ingresso " + codigo + " não encontrado.");
		}
		if (ingresso instanceof IngressoIndividual) {
			Jogo jogo = ((IngressoIndividual) ingresso).getJogo();
			if (jogo == null) {
				throw new Exception("Ingresso " + codigo + " não está associado a nenhum jogo.");
			}
			jogo.remover(ingresso);
			jogo.setEstoque(jogo.getEstoque() + 1);
		} else {
			List<Jogo> jogos = ((IngressoGrupo) ingresso).getJogos();
			if (jogos == null || jogos.isEmpty()) {
				throw new Exception("Ingresso " + codigo + " não está associado a nenhum jogo.");
			}
			for (Jogo jogo : jogos) {
				jogo.remover(ingresso);
				jogo.setEstoque(jogo.getEstoque() + 1);
			}
		}
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
	    } else {
	        throw new Exception("Jogo não encontrado.");
	    }
	    DAO.commit();
	}

	

	/**********************************
	 * 5 Consultas
	 **********************************/
	public List<Jogo> jogosComIngressosEsgotados() throws Exception {
	    DAOJogo daoJogo = new DAOJogo();
	    List<Jogo> jogos = daoJogo.jogosComIngressosEsgotados();
	    if (jogos.isEmpty()) {
	        throw new Exception("Não existem jogos com ingressos esgotados.");
	    }
	    return jogos;
	}
	
	public List<Jogo> buscarPorTime(Time time) {
	    DAOJogo daoJogo = new DAOJogo();
	    List<Jogo> jogos = daoJogo.buscarPorTime(time);
	    if (jogos.isEmpty()) {
	        System.out.println("Não foram encontrados jogos para o time informado.");
	    }
	    return jogos;
	}

	public List<Jogo> buscarPorData(String data) {
	    DAOJogo daoJogo = new DAOJogo();
	    List<Jogo> jogos = daoJogo.buscarPorData(data);
	    if (jogos.isEmpty()) {
	        System.out.println("Não foram encontrados jogos para a data informada.");
	    }
	    return jogos;
	}
	
	public List<Time> buscarPorNome(String nome) {
	    DAOTime daoTime = new DAOTime();
	    List<Time> times = daoTime.buscarPorNome(nome);
	    if (times.isEmpty()) {
	        System.out.println("Não foram encontrados times com o nome informado.");
	    }
	    return times;
	}
	
	public List<Time> buscarPorParticipacaoEmJogos(int minimo, int maximo) {
	    DAOTime daoTime = new DAOTime();
	    List<Time> times = daoTime.buscarPorParticipacaoEmJogos(minimo, maximo);
	    if (times.isEmpty()) {
	        System.out.println("Não foram encontrados times com a participação informada em jogos.");
	    }
	    return times;
	}
}