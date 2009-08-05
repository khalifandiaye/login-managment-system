package de.uplinkgmbh.lms.presistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ejb.EntityManagerFactoryImpl;

public class MyPersistenceManager {
	
	private static MyPersistenceManager instance = null;
	private EntityManagerFactory factory = null;
	private EntityManager entityManager = null;
	
	private MyPersistenceManager(){
		
		factory = Persistence.createEntityManagerFactory( "Lms" );	
	}

    public static MyPersistenceManager getInstance() {

        if (instance == null) {
            instance = new MyPersistenceManager();
        }
        return instance;
    }

	public EntityManager getEntityManager() {
		
		entityManager = factory.createEntityManager();
		return entityManager;
	}
}
