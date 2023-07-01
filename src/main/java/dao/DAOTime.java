package dao;

import java.util.List;

import com.db4o.query.Query;

import modelo.Time;

public class DAOTime extends DAO<Time> {

	public Time read (Object chave){

		String nome = (String) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Time.class);
		q.descend("nome").constrain(nome);
		List<Time> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}

    public List<Time> buscarPorNome(String nome) {
        Query q = manager.query();
        q.constrain(Time.class);
        q.descend("nome").constrain(nome);
        return q.execute();
    }

    public List<Time> buscarPorMaximoDeJogos(int maximo) {
        Query q = manager.query();
        q.constrain(Time.class);
        q.descend("jogos").constrain(maximo).smaller().equal();
        return q.execute();
    }

    
    public int getNumJogos(String nome) {
        Query q = manager.query();
        q.constrain(Time.class);
        q.descend("nome").constrain(nome);
        List<Time> resultados = q.execute();
        if (resultados.size() > 0) {
            Time time = resultados.get(0);
            return time.getJogos();
        } else {
            return -1; // ou outra indicação de que o time não foi encontrado
        }
    }
    
    public List<Time> LocalTeam(String local) {
        Query q = manager.query();
        q.constrain(Time.class);
        q.descend("jogos").descend("local").constrain(local).like();
        List<Time> resultados = q.execute();
        return resultados;
    }

}
