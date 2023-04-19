package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.Ingresso;

public class DAOIngresso extends DAO<Ingresso> {

	public Ingresso read(Object chave) {
	    Integer codigo = (Integer) chave;
	    Query q = manager.query();
	    q.constrain(Ingresso.class);
	    q.descend("codigo").constrain(codigo);
	    List<Ingresso> resultados = q.execute();
	    if (resultados.size() > 0) {
	        return resultados.get(0);
	    } else {
	        return null;
	    }
	}
}