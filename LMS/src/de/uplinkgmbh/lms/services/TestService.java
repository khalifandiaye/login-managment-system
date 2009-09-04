package de.uplinkgmbh.lms.services;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import de.axone.wash.DefaultWash;
import de.axone.wash.Wash;
import de.axone.wash.Wash.DuplicateEntryException;
import de.axone.wash.Wash.NotFoundException;
import de.axone.wash.Wash.Type;
import de.axone.wash.Wash.WashException;
import de.axone.wash.Wash.WrongTypeException;
import de.axone.wash.handler.Handler.HandlerException;
import de.axone.wash.handler.Handler.OperationNotFoundException;
import de.axone.wash.service.Service;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.exceptions.LoginException;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;

public class TestService implements Service{

	@Override
	public Wash service( Wash request, HttpServletRequest httpRequest ) throws HandlerException {
		String operation=null;
		try {
			operation = request.getString( "wash.op" );
			
		} catch( WashException e ) {
			
			throw new OperationNotFoundException();
		}
		
		if( ! "test".equals( operation ) )
			throw new OperationNotFoundException();
		
		Wash result = new DefaultWash();
		try {
			result = makemyday( request );
		} catch (LoginException e) {
			
			result = new DefaultWash();
			try {
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "USER NOT EXIST" );
			} catch (DuplicateEntryException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (WrongTypeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NotFoundException e1) {
				e1.printStackTrace();
			}
			
		}

		return result;
	}

	public Wash makemyday( Wash request ) throws LoginException{
		
		System.out.println( "mein ausgabechen " +request.serialize() );
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = null;
		try{
			em = pm.getEntityManager();
			em.getTransaction().begin();
			Query q = em.createNamedQuery( "UserFetchByLoginname" );
			q.setParameter( "loginname", "a.kre" );
			try{
				User user = (User)q.getSingleResult();	
			}catch( NoResultException e ){
				throw new LoginException( LoginException.WRONGUSERNAME );
			}
			em.getTransaction().commit();
			
		}finally{
			pm.closeEntityManager( em );
		}
		
		DefaultWash dw = new DefaultWash();

		try {
			dw.addField( "result", Type.STRING, "Hello World" );
		} catch (DuplicateEntryException e) {
			e.printStackTrace();
		} catch (WrongTypeException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		
		return dw;
	}
}
