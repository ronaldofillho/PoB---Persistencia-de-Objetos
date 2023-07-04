package dao;

import java.util.List;
import jakarta.persistence.TypedQuery;

import modelo.Time;

public class DAOTime extends DAO<Time> {

	public Time read (Object chave){
        String nome = (String) chave;
        try {
            TypedQuery<Time> q = manager.createQuery("select t from Time t where t.nome=:n", Time.class);
            q.setParameter("n", nome);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
	}

    /* -- Consultas -- */

    public List<Time> buscarTimesPorLocal(String local) {
        TypedQuery<Time> q = manager.createQuery(
                "select t from Time t join t.jogos j where j.local=:l", Time.class
        );
        q.setParameter("l", local);
        return q.getResultList();
    }

    public List<Time> buscarTimesPorData(String data) {
        TypedQuery<Time> q = manager.createQuery("select t from Time t join t.jogos j where j.data=:s", Time.class);
        q.setParameter("s", data);
        return q.getResultList();
    }

    public List<Time> buscarTimesComIngressosDisponiveis() {
        TypedQuery<Time> q = manager.createQuery("select t from Time t join t.jogos j where j.estoque > 0", Time.class);
        return q.getResultList();
    }

    @Override
    public List<Time> readAll() {
        TypedQuery<Time> q = manager.createQuery("select t from Time t", Time.class);
        return q.getResultList();
    }
}
