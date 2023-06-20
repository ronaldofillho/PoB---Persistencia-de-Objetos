import modelo.Usuario

public class DAOUsuario extends DAO<Usuario>{

    public Usuario read (Object chave) {
        try {
            String email = (String) chave;
            TypedQuery<Usuario> q = manager.createQuery("select u from Usuario u where u.email=:n", Usuario.class);
            q.setParameter("n", nome);
            Usuario u = q.getSingleResult();
            return u;
        } catch (NoResultExecption e) {
            return null;
        }
    }
}