/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package modelo;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Ingresso  {
    @Id
    protected int codigo;

    @ManyToMany(mappedBy = "ingressos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    protected ArrayList<Jogo> jogos = new ArrayList<Jogo>();
    public Ingresso() {}
    
    public Ingresso(int codigo) {
        this.codigo = codigo;
    }
    
    public abstract double calcularValor();
    
    public int getCodigo() {
        return codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
