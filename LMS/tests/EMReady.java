
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EMReady {
	
	protected static EntityManagerFactory emFactory;
	protected static EntityManager em;
	
	public EMReady(){
		
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put( "hibernate.hbm2ddl.auto", "create" );
        
		emFactory = Persistence.createEntityManagerFactory( "Lms", parameters );
		em = emFactory.createEntityManager();
	}
}
