package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.Time;

public class DAOTime extends DAO<Time> {

    public Time read(Object chave) {
        String nome = (String) chave;
        Query q = manager.query();
        q.constrain(Time.class);
        q.descend("nome").constrain(nome);
        List<Time> resultados = q.execute();
        if (resultados.size() > 0)
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

    public List<Time> buscarPorParticipacaoEmJogos(int minimo, int maximo) {
        Query q = manager.query();
        q.constrain(Time.class);
        q.descend("jogos").constrain(minimo).greater().and(q.descend("jogos").constrain(maximo).smaller());
        return q.execute();
    }
}