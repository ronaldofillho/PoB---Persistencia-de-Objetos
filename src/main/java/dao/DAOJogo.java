package dao;

import modelo.Jogo;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.TypedQuery;

public class DAOJogo extends DAO<Jogo> {

	public Jogo read (Object chave){

		int id = (int) chave;
		try {
            TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.id=:d", Jogo.class);
            q.setParameter("d", id);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
	}
    
    /* -- Consultas -- */

    public List<Jogo> listarJogosDeUmaData(String data) {
        TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.data=:x", Jogo.class);
        q.setParameter("x", data);
        return q.getResultList();
    }

    public List<Jogo> listarJogosDeUmTime(String time) {
        TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j join j.times t where t.nome=:n", Jogo.class);
        q.setParameter("n", time);
        return q.getResultList();
    }

    public List<Jogo> listarJogosComIngressosDisponiveis() {
        TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.estoque >= 1", Jogo.class);
        return q.getResultList();
    }

    @Override
    public List<Jogo> readAll() {
        return super.readAll();
        //função readAll herdada pelo DAO original
    }
}
