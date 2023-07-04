package dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.IngressoGrupo;

public class DAOIngressoGrupo extends DAO<IngressoGrupo> {
    @Override
    public IngressoGrupo read(Object chave) {
        try {
            int codigo = (int) chave;
            TypedQuery<IngressoGrupo> q = manager.createQuery("select i from IngressoGrupo i where i.codigo=:x", IngressoGrupo.class);
            q.setParameter("x", codigo);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
