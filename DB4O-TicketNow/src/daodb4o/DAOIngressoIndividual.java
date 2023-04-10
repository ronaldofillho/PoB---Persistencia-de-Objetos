package daodb4o;
import java.util.List;
import com.db4o.query.Query;
import modelo.IngressoIndividual;
import modelo.Jogo;
import modelo.Time;
public class DAOIngressoIndividual extends DAO<IngressoIndividual> {
    public IngressoIndividual read(Object chave) {
        int id = (Integer) chave;
        Query q = manager.query();
        q.constrain(IngressoIndividual.class);
        q.descend("codigo").constrain(id);
        List<IngressoIndividual> resultados = q.execute();
        if (resultados.size() > 0)
            return resultados.get(0);
        else
            return null;
    }
    public List<IngressoIndividual> buscarPorJogo(Jogo jogo) {
        Query q = manager.query();
        q.constrain(IngressoIndividual.class);
        q.descend("jogo").descend("id").constrain(jogo.getId());
        return q.execute();
    }
    public List<IngressoIndividual> buscarPorTime(Time time) {
        Query q = manager.query();
        q.constrain(IngressoIndividual.class);
        q.descend("jogo").descend("time1").descend("nome").constrain(time.getNome()).or(
                q.descend("jogo").descend("time2").descend("nome").constrain(time.getNome()));
        return q.execute();
    }
    
}