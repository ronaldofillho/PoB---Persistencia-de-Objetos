package dao;

import modelo.Jogo;

import java.util.List;
import jakarta.persistence.TypedQuery;

public class DAOJogo extends DAO<Jogo> {
    @Override
	public Jogo read (Object chave){
		try {
            int id = (int) chave;
            TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.id=:id", Jogo.class);
            q.setParameter("id", id);
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
