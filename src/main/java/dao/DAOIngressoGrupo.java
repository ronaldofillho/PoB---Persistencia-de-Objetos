package dao;

import java.util.List;
import java.util.ArrayList;

import com.db4o.query.Query;

import modelo.IngressoGrupo;
import modelo.Jogo;
import modelo.Time;

public class DAOIngressoGrupo extends DAO<IngressoGrupo> {

    public IngressoGrupo read(Object chave) {

        Integer codigo = (Integer) chave; // casting para o tipo da chave
        Query q = manager.query();
        q.constrain(IngressoGrupo.class);
        q.descend("codigo").constrain(codigo);
        List<IngressoGrupo> resultados = q.execute();
        if (resultados.size() > 0)
            return resultados.get(0);
        else
            return null;
}

    public List<IngressoGrupo> buscarPorJogo(Jogo jogo) {
        List<IngressoGrupo> ingressosPorJogo = new ArrayList<>();
        Query q = manager.query();
        q.constrain(IngressoGrupo.class);
        List<IngressoGrupo> resultados = q.execute();
        for (IngressoGrupo ingresso : resultados) {
            for (Jogo j : ingresso.getJogos()) {
                if (j.getId() == jogo.getId()) {
                    ingressosPorJogo.add(ingresso);
                    break;
                }
            }
        }
        return ingressosPorJogo;
    }

    public List<IngressoGrupo> buscarPorTime(Time time) {
        Query q = manager.query();
        q.constrain(IngressoGrupo.class);
        q.descend("jogo").descend("time1").constrain(time).or(
                q.descend("jogo").descend("time2").constrain(time));
        return q.execute();
    }

}