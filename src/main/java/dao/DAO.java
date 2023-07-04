/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de Objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package dao;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;


public abstract class DAO<T> implements DAOInterface<T> {
	protected static EntityManager manager;

	public static void open(){	
		manager = Util.conectarBanco();
	}

	public static void close(){
		Util.fecharBanco();
	}

	//----------CRUD-----------------------

	public void create(T obj){
		manager.persist( obj );
	}

	public abstract T read(Object chave);	//sobrescrito nas subclasses

	public T update(T obj){ return manager.merge(obj);}

	public void delete(T obj) {
		manager.remove(obj);
	}

	@SuppressWarnings("unchecked")
	public List<T> readAll(){
		Class<T> type =(Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		TypedQuery<T> query = manager.createQuery("select X from" + type.getSimpleName() + " x", type);
		return query.getResultList();
	}

	public List<T> readAllPagination(int firstResult, int maxResults) {
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		return manager.createQuery("select x from " + type.getSimpleName() + " x",type)
				.setFirstResult(firstResult - 1)
				.setMaxResults(maxResults)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	//deletar todos objetos de um tipo (e subtipo)
	public void deleteAll(){
		Class<T> type =(Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		String tabela = type.getSimpleName();
		Query query = manager.createQuery("delete from " + tabela);
		query.executeUpdate();
	}

	//--------transa��o---------------
	public static void begin(){
		if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
	}

	public static void commit(){
		if(!manager.getTransaction().isActive()) {
			manager.getTransaction().commit();
			manager.clear(); // esvazia o cache de objetos, se habilitado
		}
	}
	public static void rollback(){
		if(manager.getTransaction().isActive())
			manager.getTransaction().rollback();
	}

	public void lock(T obj) {
		manager.lock(obj, LockModeType.PESSIMISTIC_WRITE);
	}

	//	acesso direto a classe de conexão jdbc

	public static Connection getConnectionJdbc() {
		try {
			EntityManagerFactory factory = manager.getEntityManagerFactory();
			String driver = (String) factory.getProperties().get("jakarta.persistence.jdbc.driver");
			String url = (String)	factory.getProperties().get("jakarta.persistence.jdbc.url");
			String user = (String)	factory.getProperties().get("jakarta.persistence.jdbc.user");
			String pass = (String)	factory.getProperties().get("jakarta.persistence.jdbc.password");
			Class.forName(driver);
			return DriverManager.getConnection(url, user, pass);
		}
		catch (Exception ex) {
			return null;
		}
	}
}
