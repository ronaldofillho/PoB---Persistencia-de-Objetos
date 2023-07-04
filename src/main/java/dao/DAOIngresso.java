package dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Ingresso;

import java.util.List;

public class DAOIngresso extends DAO<Ingresso> {

	public Ingresso read(Object chave) {
		try {
			int id = (Integer) chave;
			TypedQuery<Ingresso> q = manager.createQuery("select i from Ingresso i where i.id = :x", Ingresso.class);
			q.setParameter("x", id);
			return q.getSingleResult();
		} catch (NoResultException e ){
			return null;
		}
	}

	@Override
	public List<Ingresso> readAll() {
		TypedQuery<Ingresso> q = manager.createQuery("select i from Ingresso i", Ingresso.class);
		return q.getResultList();
	}
}
