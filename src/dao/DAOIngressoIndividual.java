package dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;

public class DAOIngressoIndividual extends DAO<IngressoIndividual> {
    public IngressoIndividual read(Object chave) {
        try {
            int codigo = (int) chave;
            TypedQuery<IngressoIndividual> q = manager.createQuery("select i from IngressoIndividual i where i.codigo=:x", IngressoIndividual.class);
            q.setParameter("x", codigo);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}