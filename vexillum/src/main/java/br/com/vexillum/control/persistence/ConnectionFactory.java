package br.com.vexillum.control.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.SpringFactory;

@Deprecated
public class ConnectionFactory {
	
	private static final String DATASOURCE = SpringFactory.getInstance().getBean("configProperties", Properties.class).getKey("dataSource");
	
	private ConnectionFactory() {
	}

	private static Session session;
	private static EntityManagerFactory entityManagerFactory;
	
	private static void initEntityManagerFactory(){
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence
					.createEntityManagerFactory(DATASOURCE);
		} 
	}

	public static EntityManager getEntityManager() {
		initEntityManagerFactory();

		return entityManagerFactory.createEntityManager();
	}
	
	public static Session getSession(){
		if(session == null){
			session = (Session) getEntityManager().getDelegate();  
		}
		return session;
	}
}
