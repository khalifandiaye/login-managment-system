package de.uplinkgmbh.lms.presistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.axone.tools.E;

public class MyPersistenceManager {
	
	private static MyPersistenceManager instance = null;
	private EntityManagerFactory factory = null;
	
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

		return factory.createEntityManager();
	}
	
	public void closeEntityManager( EntityManager em ){
		
		if( em != null ){
			if( em.getTransaction().isActive() )
				em.getTransaction().rollback();
			em.close();
		}
	}
	
}
