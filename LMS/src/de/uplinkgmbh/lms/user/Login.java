package de.uplinkgmbh.lms.user;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.exceptions.LoginException;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.Password;
import de.uplinkgmbh.lms.utils.Tokenaizer;

public class Login {

	private String username;
	private String password;
	private String application;
	private boolean allowed = false;
	private User user = null;
	
	/**
	 * This function set and check the userdata. After this and is all correct then you
	 * can log the user in. Use login(); 
	 * 
	 * This parameters are for the implementation's use.
	 * 
	 * @param username string
	 * @param password string
	 * @param application string
	 */
	public void check( String username, String password, String application ) throws LoginException{
	
		this.username = username;
		this.password = password;
		this.application = application;
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = null;
		try{
			em = pm.getEntityManager();
			em.getTransaction().begin();
			Query q = em.createNamedQuery( "UserFetchByLoginname" );
			q.setParameter( "loginname", username );
			try{
				user = (User)q.getSingleResult();	
			}catch( NoResultException e ){
				throw new LoginException( LoginException.WRONGUSERNAME );
			}
			em.getTransaction().commit();
			
			if( ! user.getPassword().equals( Password.processIt( password ) ) ){
				allowed = false;
			
				throw new LoginException( LoginException.WRONGPASSWORD );
			}
			
			List<Groups> list = user.getGroupList();
			List<Role> list2 = user.getRoleList();
			
			if( list.size() == 0 && list2.size() == 0 ){
				allowed = false;
				
				throw new LoginException( LoginException.USERHASNOGROUPORROLE );
			}
			
			if( list.size() > 0 ){
				for( Groups gr : list ){
					
					if( gr.getApplication().getName().equals( application ) ){
						allowed = user.isActiv();
						return;
					}
				}
			}
			if( list2.size() > 0 ){
				for( Role r : list2 ){
					
					if( r.getApplication().getName().equals( application ) ){
						allowed = user.isActiv();
						
						return;
					}
				}
			}
		
		allowed = false;
		throw new LoginException( LoginException.WRONGAPPLICATION );
		}finally{
			pm.closeEntityManager( em );
		}
	}
	
	/**
	 * First use Login.check( .. ) to set userdata and check the rights of user. If check() not before used
	 * then logIn() returns null
	 * 
	 */
	public String logIn(){
		
		if( user != null ){
			
			user.setLastlogin(  new Date( System.currentTimeMillis() ) );
			user.setLogincounter( user.getLogincounter()+1 );
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
		try{	
			em.getTransaction().begin();
			em.merge( user );
			em.getTransaction().commit();
		}finally{
			if( em != null && em.isOpen() ) em.close();
		}
			return Tokenaizer.buildAESToken( application, user.getId() );
			
		}else return null;
	}
	
	public void logOut(){
		
		if( user != null ){
			
			user.setLastlogout( new Date( System.currentTimeMillis() ) );
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
		try{	
			em.getTransaction().begin();
			em.merge( user );
			em.getTransaction().commit();
		}finally{
			pm.closeEntityManager( em );
		}
		}
	}
	
	public boolean logOut( String AESToken ){
		
		LMSToken token = Tokenaizer.restoreLMSToken( AESToken.getBytes() );
		if( token == null ){
			return false;
		}
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		try{
		em.getTransaction().begin();
		try{
			user = em.find( User.class, token.userId );
		}catch( NoResultException e ){
			em.close();
			return false;
		}
		em.getTransaction().commit();
		}finally{
			pm.closeEntityManager( em );
		}
		logOut();
		return true;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public boolean isAllowed() {
		return allowed;
	}

	public User getUser() {
		return user;
	}

	public String getApplication() {
		return application;
	}
	
	
}
