package de.uplinkgmbh.lms.services;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import de.axone.logging.Log;
import de.axone.logging.Logging;
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
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.Tokenaizer;

public class ActionService implements Service{
	
	private static Log log = Logging.getLog( ActionService.class );

	@Override
	public Wash service( Wash request, HttpServletRequest httpRequest ) throws HandlerException {
		String operation=null;
		try {
			operation = request.getString( "wash.op" );
			
		} catch( WashException e ) {
			
			throw new OperationNotFoundException();
		}
		
		Wash result = null;
		
		try {
		if( "getActions".equals( operation ) )	
			result = getActions( request );
		else if( "isAllowed".equals( operation ) )
			result = isAllowed( request );
		else
			throw new OperationNotFoundException();

		} catch (NotFoundException e) {
			e.printStackTrace();
			result = new DefaultWash();
			try {
				result.addField( "ERROR", Type.STRING, "WASH FIELD NOT FOUND" );
			} catch (DuplicateEntryException e1) {
			} catch (WrongTypeException e1) {
			} catch (NotFoundException e1) {
			}
			
		} catch (WrongTypeException e) {
			e.printStackTrace();
			result = new DefaultWash();
			try {
				result.addField( "ERROR", Type.STRING, "WRONG TYPE" );
			} catch (DuplicateEntryException e1) {
			} catch (WrongTypeException e1) {
			} catch (NotFoundException e1) {
			}
		} catch (DuplicateEntryException e) {
			e.printStackTrace();
			result = new DefaultWash();
			try {
				result.addField( "ERROR", Type.STRING, "DUPLICATE ENTRY" );
			} catch (DuplicateEntryException e1) {
			} catch (WrongTypeException e1) {
			} catch (NotFoundException e1) {
			}
		}
		
		if( result.hasField( "ERROR" ) ){
			try {
				log.error( result.getString( "ERROR" ) );
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (WrongTypeException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	private Wash getActions(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;
		if( !request.getString( "LMSTOKEN" ).equals( "" ) ){
			LMSToken token = Tokenaizer.restoreLMSToken( request.getString( "LMSTOKEN" ).getBytes() );
			if( token == null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG LMSTOKEN" );
				return result;
			}
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
			
			List<Role> rl = null;
			em.getTransaction().begin();
			Query q = em.createNamedQuery( "RoleFetchByAppnameAndUserId" );
			q.setParameter( "appname", token.application );
			q.setParameter( "userId", token.userId );
			rl = (List<Role>) q.getResultList();
			em.getTransaction().commit();
			
			List<Action> al = new LinkedList<Action>();
			
			for( Role r : rl ){
				al.addAll( r.getActionList() );
			}
			
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			result.addField( "SIZE", Type.INTEGER, al.size() );
			/*
			 * NAME-n	string	SORT-n	integer	TARGET-n	string	
			 * ACTION-n	string	STATE-n	string	RULE-n	string	ROLE-n	string
			 */
			int i = 0;
			for( Action a : al ){
				result.addField( "Action-"+i+".NAME", Type.STRING, a.getName() );
				result.addField( "Action-"+i+".SORT", Type.INTEGER, a.getSort() );
				result.addField( "Action-"+i+".TARGET", Type.STRING, a.getTarget() );
				result.addField( "Action-"+i+".ACTION", Type.STRING, a.getAction() );
				result.addField( "Action-"+i+".STATE", Type.STRING, a.getState() );
				result.addField( "Action-"+i+".RULE", Type.STRING, a.getRule() );
				result.addField( "Action-"+i+".ROLE", Type.STRING, a.getRole().getName() );
				i++;
			}
			if( em.isOpen() ) em.clear(); em.close();
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
		return result;
	} 
	
	private Wash isAllowed(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;

		if( !request.getString( "LMSTOKEN" ).equals( "" ) ){
			
			LMSToken token = Tokenaizer.restoreLMSToken( request.getString( "LMSTOKEN" ).getBytes() );
			if( token == null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG LMSTOKEN" );
				return result;
			}
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
			
			if( AuthorizationsChecker.isAllowed( token,
					request.getString( "STATE" ),
					request.getString( "ACTION" ),
					request.getString( "TARGET" ) ) ){
				
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, true );
				result.addField( "REASON", Type.STRING, "" );
				result.addField( "PERMISSION", Type.BOOLEAN, true );
			}else{
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, true );
				result.addField( "REASON", Type.STRING, "" );
				result.addField( "PERMISSION", Type.BOOLEAN, false );
			}
			if( em.isOpen() ) em.clear(); em.close();
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		}
	
		return result;
	} 
}
