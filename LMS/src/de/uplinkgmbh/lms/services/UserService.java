package de.uplinkgmbh.lms.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import de.axone.logging.Log;
import de.axone.logging.Logging;
import de.axone.tools.E;
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
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Organisation;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.Tokenaizer;

public class UserService implements Service{
	
	private static Log log = Logging.getLog( UserService.class );

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
		if( "getTemplateUsers".equals( operation ) )	
			result = getTemplateUsers( request );
		else if( "getUsers".equals( operation ) )
			result = getUsers( request );
		else if( "getUser".equals( operation ) )
			result = getUser( request );
		else if( "getLoginUser".equals( operation ) )
			result = getLoginUser( request );
		else if( "newUser".equals( operation ) )
			result = newUser( request );
		else if( "updateUser".equals( operation ) )
			result = updateUser( request );
		else if( "removeUserFromApplication".equals( operation ) )
			result = removeUserFromApplication( request );
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

	
	private Wash getTemplateUsers(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
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

			if( !AuthorizationsChecker.isAllowed( token, "APPLICATION", "user.getTemplateUsers", token.application ) ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG PERMISSION" );
				return result;
			}
			
			List<User> ul = null;
			EntityManager em = pm.getEntityManager();
			try{
			em.getTransaction().begin();	
			Query q = em.createNamedQuery( "AllTemplateUser" );
			ul = (List<User>)q.getResultList();
			em.getTransaction().commit();

			String logstr = "User: getTemplateUsers ["+ul.size()+"]\n";
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "SIZE", Type.INTEGER, ul.size() );
			
			for( int i = 0; i < ul.size(); i++ ){
				result.addField( "TemplateUser-"+i+".LOGINNAME", Type.STRING, ul.get(i).getLoginname() );
				logstr = logstr.concat( ul.get(i).getLoginname()+"\n" );
			}
			log.debug( logstr );
			}finally{
				pm.closeEntityManager( em );
			}

		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
	
		return result;
	} 
	
	private Wash getUsers(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
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
			
			if( !AuthorizationsChecker.isAllowed( token, "APPLICATION", "user.getUsers", token.application ) ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG PERMISSION" );
				return result;
			}
			
			Application app = null;
			HashSet<User> ul = new HashSet<User>();
			EntityManager em = pm.getEntityManager();
			try{
			em.getTransaction().begin();	
			Query q = em.createNamedQuery( "ApplicationFetchByName" );
			q.setParameter( "name", token.application );
			try{
			app = (Application)q.getSingleResult();
			}catch( NoResultException e ){
				result = new DefaultWash();
				result.addField( "ERROR", Type.STRING, "APPLICATION NOT FOUND" );
				return result;
			}
			for( Groups g : app.getGroupList() ){
				ul.addAll( g.getUserList() );
			}
			
			for( Role r : app.getRoleList() ){
				ul.addAll( r.getUserList() );
			}
			
			em.getTransaction().commit();
			
			String logstr = "User: getUsers ["+ul.size()+"]\n";
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			result.addField( "SIZE", Type.INTEGER, ul.size() );
			
			int i=0;
			Iterator<User> it = ul.iterator();
			while( it.hasNext() ){
				User u = it.next();
				
				result.addField( "User-"+i+".LOGINNAME", Type.STRING, u.getLoginname() );
				result.addField( "User-"+i+".PASSWORD", Type.STRING, u.getPassword() );
				result.addField( "User-"+i+".FIRSTNAME", Type.STRING, u.getFirstname() );
				result.addField( "User-"+i+".SURENAME", Type.STRING, u.getSurename() );
				result.addField( "User-"+i+".EMAIL", Type.STRING, u.getEmail() );
				result.addField( "User-"+i+".PHONEPRIV", Type.STRING, u.getPhonepriv() );
				result.addField( "User-"+i+".PHONEWORK", Type.STRING, u.getPhonework() );
				result.addField( "User-"+i+".MOBILE", Type.STRING, u.getMobile() );
				result.addField( "User-"+i+".FAX", Type.STRING, u.getFax() );
				result.addField( "User-"+i+".CITY", Type.STRING, u.getCity() );
				result.addField( "User-"+i+".STATE", Type.STRING, u.getState() );
				result.addField( "User-"+i+".ZIP", Type.STRING, u.getZip() );
				result.addField( "User-"+i+".STREET", Type.STRING, u.getStreet() );
				result.addField( "User-"+i+".STREETNR", Type.STRING, u.getStreetnr() );
				if( u.getCountry() != null )
					result.addField( "User-"+i+".COUNTRY", Type.STRING, u.getCountry().getDisplayCountry() );
				else
					result.addField( "User-"+i+".COUNTRY", Type.STRING, "" );
				if( u.getLanguage() != null )
					result.addField( "User-"+i+".LANGUAGE", Type.STRING, u.getLanguage().getDisplayLanguage() );
				else
					result.addField( "User-"+i+".LANGUAGE", Type.STRING, "" );
				if( u.getLastlogin() != null )
					result.addField( "User-"+i+".LASTLOGIN", Type.INTEGER, u.getLastlogin().getTime() );
				else
					result.addField( "User-"+i+".LASTLOGIN", Type.INTEGER );
				if( u.getLastlogout() != null )
					result.addField( "User-"+i+".LASTLOGOUT", Type.INTEGER, u.getLastlogout().getTime() );
				else
					result.addField( "User-"+i+".LASTLOGOUT", Type.INTEGER );
				result.addField( "User-"+i+".LOGINCOUNTER", Type.INTEGER, u.getLogincounter() );
				result.addField( "User-"+i+".WASHSTORE", Type.STRING, u.getWashstore() );
				
				if( u.getOrganisation() != null ){
					result.addField( "User-"+i+".ORGANAME", Type.STRING, u.getOrganisation().getName() );
					if( u.getOrganisation().getUrl() != null )
						result.addField( "User-"+i+".ORGAURL", Type.STRING, u.getOrganisation().getUrl().toString() );
					else
						result.addField( "User-"+i+".ORGAURL", Type.STRING, "" );
					result.addField( "User-"+i+".ORGAPHONE", Type.STRING, u.getOrganisation().getPhone() );
					result.addField( "User-"+i+".ORGAFAX", Type.STRING, u.getOrganisation().getFax() );
					result.addField( "User-"+i+".ORGACITY", Type.STRING, u.getOrganisation().getCity() );
					result.addField( "User-"+i+".ORGASTATE", Type.STRING, u.getOrganisation().getState() );
					if( u.getOrganisation().getCountry() != null )
						result.addField( "User-"+i+".ORGACOUNTRY", Type.STRING, u.getOrganisation().getCountry().getDisplayCountry() );
					else
						result.addField( "User-"+i+".ORGACOUNTRY", Type.STRING, "" );
					result.addField( "User-"+i+".ORGAZIP", Type.STRING, u.getOrganisation().getZip() );
					result.addField( "User-"+i+".ORGASTREET", Type.STRING, u.getOrganisation().getStreet() );
					result.addField( "User-"+i+".ORGASTREETNR", Type.STRING, u.getOrganisation().getStreetnr() );
					result.addField( "User-"+i+".ORGAWASHSTORE", Type.STRING, u.getOrganisation().getWashstore() );
				}else{
					result.addField( "User-"+i+".ORGANAME", Type.STRING, "" );
					result.addField( "User-"+i+".ORGAURL", Type.STRING, "" );
					result.addField( "User-"+i+".ORGAPHONE", Type.STRING, "" );
					result.addField( "User-"+i+".ORGAFAX", Type.STRING, "" );
					result.addField( "User-"+i+".ORGACITY", Type.STRING, "" );
					result.addField( "User-"+i+".ORGASTATE", Type.STRING, "" );
					result.addField( "User-"+i+".ORGACOUNTRY", Type.STRING, "" );
					result.addField( "User-"+i+".ORGAZIP", Type.STRING, "" );
					result.addField( "User-"+i+".ORGASTREET", Type.STRING, "" );
					result.addField( "User-"+i+".ORGASTREETNR", Type.STRING, "" );
					result.addField( "User-"+i+".ORGAWASHSTORE", Type.STRING, "" );
				}
												
				//logstr = logstr.concat( u.getLoginname()+"\n" );
				i++;
			
			}

			log.debug( logstr );
			}finally{
				pm.closeEntityManager( em );
			}
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
		return result;
	}
	
	private Wash getUser(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;
		if( !request.getString( "LMSTOKEN" ).equals( "" ) && !request.getString( "LOGINNAME" ).equals( "" ) ){
			
			LMSToken token = Tokenaizer.restoreLMSToken( request.getString( "LMSTOKEN" ).getBytes() );
			if( token == null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG LMSTOKEN" );
				return result;
			}
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			
			if( !AuthorizationsChecker.isAllowed( token, "APPLICATION", "user.getUser", token.application ) ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG PERMISSION" );
				return result;
			}
			
			
			
			User u = null;
			EntityManager em = pm.getEntityManager();
			try{
			em.getTransaction().begin();	
			Query q = em.createNamedQuery( "UserFetchByLoginname" );
			q.setParameter( "loginname", request.getString( "LOGINNAME" ) );
			try{
			u = (User)q.getSingleResult();
			}catch( NoResultException e ){
				result = new DefaultWash();
				result.addField( "ERROR", Type.STRING, "USER NOT FOUND" );
				return result;
			}
			em.getTransaction().commit();
			
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			
			
			result.addField( "User.LOGINNAME", Type.STRING, u.getLoginname() );
			result.addField( "User.PASSWORD", Type.STRING, u.getPassword() );
			result.addField( "User.FIRSTNAME", Type.STRING, u.getFirstname() );
			result.addField( "User.SURENAME", Type.STRING, u.getSurename() );
			result.addField( "User.EMAIL", Type.STRING, u.getEmail() );
			result.addField( "User.PHONEPRIV", Type.STRING, u.getPhonepriv() );
			result.addField( "User.PHONEWORK", Type.STRING, u.getPhonework() );
			result.addField( "User.MOBILE", Type.STRING, u.getMobile() );
			result.addField( "User.FAX", Type.STRING, u.getFax() );
			result.addField( "User.CITY", Type.STRING, u.getCity() );
			result.addField( "User.STATE", Type.STRING, u.getState() );
			result.addField( "User.ZIP", Type.STRING, u.getZip() );
			result.addField( "User.STREET", Type.STRING, u.getStreet() );
			result.addField( "User.STREETNR", Type.STRING, u.getStreetnr() );
			if( u.getCountry() != null )
				result.addField( "User.COUNTRY", Type.STRING, u.getCountry().getDisplayCountry() );
			else
				result.addField( "User.COUNTRY", Type.STRING, "" );
			if( u.getLanguage() != null )
				result.addField( "User.LANGUAGE", Type.STRING, u.getLanguage().getDisplayLanguage() );
			else
				result.addField( "User.LANGUAGE", Type.STRING, "" );
			if( u.getLastlogin() != null )
				result.addField( "User.LASTLOGIN", Type.INTEGER, u.getLastlogin().getTime() );
			else
				result.addField( "User.LASTLOGIN", Type.INTEGER );
			if( u.getLastlogout() != null )
				result.addField( "User.LASTLOGOUT", Type.INTEGER, u.getLastlogout().getTime() );
			else
				result.addField( "User.LASTLOGOUT", Type.INTEGER );
			result.addField( "User.LOGINCOUNTER", Type.INTEGER, u.getLogincounter() );
			result.addField( "User.WASHSTORE", Type.STRING, u.getWashstore() );
			
			if( u.getOrganisation() != null ){
				result.addField( "User.ORGANAME", Type.STRING, u.getOrganisation().getName() );
				if( u.getOrganisation().getUrl() != null )
					result.addField( "User.ORGAURL", Type.STRING, u.getOrganisation().getUrl().toString() );
				else
					result.addField( "User.ORGAURL", Type.STRING, "" );
				result.addField( "User.ORGAPHONE", Type.STRING, u.getOrganisation().getPhone() );
				result.addField( "User.ORGAFAX", Type.STRING, u.getOrganisation().getFax() );
				result.addField( "User.ORGACITY", Type.STRING, u.getOrganisation().getCity() );
				result.addField( "User.ORGASTATE", Type.STRING, u.getOrganisation().getState() );
				if( u.getOrganisation().getCountry() != null )
					result.addField( "User.ORGACOUNTRY", Type.STRING, u.getOrganisation().getCountry().getDisplayCountry() );
				else
					result.addField( "User.ORGACOUNTRY", Type.STRING, "" );
				result.addField( "User.ORGAZIP", Type.STRING, u.getOrganisation().getZip() );
				result.addField( "User.ORGASTREET", Type.STRING, u.getOrganisation().getStreet() );
				result.addField( "User.ORGASTREETNR", Type.STRING, u.getOrganisation().getStreetnr() );
				result.addField( "User.ORGAWASHSTORE", Type.STRING, u.getOrganisation().getWashstore() );
			}else{
				result.addField( "User.ORGANAME", Type.STRING, "" );
				result.addField( "User.ORGAURL", Type.STRING, "" );
				result.addField( "User.ORGAPHONE", Type.STRING, "" );
				result.addField( "User.ORGAFAX", Type.STRING, "" );
				result.addField( "User.ORGACITY", Type.STRING, "" );
				result.addField( "User.ORGASTATE", Type.STRING, "" );
				result.addField( "User.ORGACOUNTRY", Type.STRING, "" );
				result.addField( "User.ORGAZIP", Type.STRING, "" );
				result.addField( "User.ORGASTREET", Type.STRING, "" );
				result.addField( "User.ORGASTREETNR", Type.STRING, "" );
				result.addField( "User.ORGAWASHSTORE", Type.STRING, "" );
			}
	
			}finally{
				pm.closeEntityManager( em );
			}
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
		return result;
	}
	
	private Wash getLoginUser(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{

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
			
			User user = null;
			em.getTransaction().begin();	
			user = em.find( User.class, token.userId );
			em.getTransaction().commit();
			try{
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			result.addField( "LOGINNAME", Type.STRING, user.getLoginname() );
			result.addField( "PASSWORD", Type.STRING, user.getPassword() );
			result.addField( "FIRSTNAME", Type.STRING, user.getFirstname() );
			result.addField( "SURENAME", Type.STRING, user.getSurename() );
			result.addField( "EMAIL", Type.STRING, user.getEmail() );
			result.addField( "PHONEPRIV", Type.STRING, user.getPhonepriv() );
			result.addField( "PHONEWORK", Type.STRING, user.getPhonework() );
			result.addField( "MOBILE", Type.STRING, user.getMobile() );
			result.addField( "FAX", Type.STRING, user.getFax() );
			result.addField( "CITY", Type.STRING, user.getCity() );
			result.addField( "STATE", Type.STRING, user.getState() );
			result.addField( "ZIP", Type.STRING, user.getZip() );
			result.addField( "STREET", Type.STRING, user.getStreet() );
			result.addField( "STREETNR", Type.STRING, user.getStreetnr() );
			if( user.getCountry() != null )
				result.addField( "COUNTRY", Type.STRING, user.getCountry().getDisplayCountry() );
			else
				result.addField( "COUNTRY", Type.STRING, "" );
			if( user.getLanguage() != null )
				result.addField( "LANGUAGE", Type.STRING, user.getLanguage().getDisplayLanguage() );
			else
				result.addField( "LANGUAGE", Type.STRING, "" );
			if( user.getLastlogin() != null )
				result.addField( "LASTLOGIN", Type.INTEGER, user.getLastlogin().getTime() );
			else
				result.addField( "LASTLOGIN", Type.INTEGER );
			if( user.getLastlogout() != null )
				result.addField( "LASTLOGOUT", Type.INTEGER, user.getLastlogout().getTime() );
			else
				result.addField( "LASTLOGOUT", Type.INTEGER );
			result.addField( "LOGINCOUNTER", Type.INTEGER, user.getLogincounter() );
			result.addField( "WASHSTORE", Type.STRING, user.getWashstore() );
			
			if( user.getOrganisation() != null ){
				result.addField( "ORGANAME", Type.STRING, user.getOrganisation().getName() );
				if( user.getOrganisation().getUrl() != null )
					result.addField( "ORGAURL", Type.STRING, user.getOrganisation().getUrl().toString() );
				else
					result.addField( "ORGAURL", Type.STRING, "" );
				result.addField( "ORGAPHONE", Type.STRING, user.getOrganisation().getPhone() );
				result.addField( "ORGAFAX", Type.STRING, user.getOrganisation().getFax() );
				result.addField( "ORGACITY", Type.STRING, user.getOrganisation().getCity() );
				result.addField( "ORGASTATE", Type.STRING, user.getOrganisation().getState() );
				if( user.getOrganisation().getCountry() != null )
					result.addField( "ORGACOUNTRY", Type.STRING, user.getOrganisation().getCountry().getDisplayCountry() );
				else
					result.addField( "ORGACOUNTRY", Type.STRING, "" );
				result.addField( "ORGAZIP", Type.STRING, user.getOrganisation().getZip() );
				result.addField( "ORGASTREET", Type.STRING, user.getOrganisation().getStreet() );
				result.addField( "ORGASTREETNR", Type.STRING, user.getOrganisation().getStreetnr() );
				result.addField( "ORGAWASHSTORE", Type.STRING, user.getOrganisation().getWashstore() );
			}else{
				result.addField( "ORGANAME", Type.STRING, "" );
				result.addField( "ORGAURL", Type.STRING, "" );
				result.addField( "ORGAPHONE", Type.STRING, "" );
				result.addField( "ORGAFAX", Type.STRING, "" );
				result.addField( "ORGACITY", Type.STRING, "" );
				result.addField( "ORGASTATE", Type.STRING, "" );
				result.addField( "ORGACOUNTRY", Type.STRING, "" );
				result.addField( "ORGAZIP", Type.STRING, "" );
				result.addField( "ORGASTREET", Type.STRING, "" );
				result.addField( "ORGASTREETNR", Type.STRING, "" );
				result.addField( "ORGAWASHSTORE", Type.STRING, "" );
			}
			}finally{
				pm.closeEntityManager( em );
			}
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
		return result;
	}
	
	private Wash newUser(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;
		if( !request.getString( "LMSTOKEN" ).equals( "" ) &&
			!request.getString( "LOGINNAME" ).equals( "" ) &&
			!request.getString( "PASSWORD" ).equals( "" ) ){
			
			LMSToken token = Tokenaizer.restoreLMSToken( request.getString( "LMSTOKEN" ).getBytes() );
			if( token == null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG LMSTOKEN" );
				return result;
			}

			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();

			if( !AuthorizationsChecker.isAllowed( token, "APPLICATION", "user.newUser", token.application ) ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG PERMISSION" );
				return result;
			}
			EntityManager em = pm.getEntityManager();
			try{
			em.getTransaction().begin();
			
			User newUser = null;
			
			Query q = em.createNamedQuery( "UserFetchByLoginname" );
			q.setParameter( "loginname", request.getString( "LOGINNAME" ) );
			try{
				newUser = (User)q.getSingleResult();
			}catch( NoResultException e ){}
			em.getTransaction().commit();
	
			if( newUser != null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "LOGINNAME USED BY OTHER USER" );
				return result;
			}
			
			newUser = new User();
			newUser.setLoginname( request.getString( "LOGINNAME" ) );
			newUser.setPassword( request.getString( "PASSWORD" ) );
			em = pm.getEntityManager();
			em.getTransaction().begin();
			em.persist( newUser );
			em.getTransaction().commit();
	
			if( !request.getString( "TEMPLATELOGINNAME" ).equals( "" ) ){
				em = pm.getEntityManager();
				em.getTransaction().begin();
				User templateUser = null;
				q = em.createNamedQuery( "UserFetchByLoginname" );
				q.setParameter( "loginname", request.getString( "TEMPLATELOGINNAME" ) );
				try{
					templateUser = (User) q.getSingleResult();
				}catch( NoResultException e ){
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "WRONG TEMPLATE" );
					em.remove( newUser );
					em.getTransaction().commit();
					return result;
				}
				em.getTransaction().commit();
		
				newUser.setCountry( templateUser.getCountry() );
				newUser.setLanguage( templateUser.getLanguage() );
				newUser.getGroupList().addAll( templateUser.getGroupList() );
				newUser.getRoleList().addAll( templateUser.getRoleList() );
	
			}
			
			newUser.setActiv( true );
			newUser.setTemplate( false );
			newUser.setLogincounter( 0 );
			
			if( !request.getString( "STATE" ).equals( "" ) )
				newUser.setState( request.getString( "STATE" ) );
			if( !request.getString( "CITY" ).equals( "" ) )
				newUser.setCity( request.getString( "CITY" ) );
			if( !request.getString( "EMAIL" ).equals( "" ) )
				newUser.setEmail( request.getString( "EMAIL" ) );
			if( !request.getString( "FAX" ).equals( "" ) )
				newUser.setFax( request.getString( "FAX" ) );
			if( !request.getString( "FIRSTNAME" ).equals( "" ) )
				newUser.setFirstname( request.getString( "FIRSTNAME" ) );
			if( !request.getString( "MOBILE" ).equals( "" ) )
				newUser.setMobile( request.getString( "MOBILE" ) );
			if( request.getString( "ORGANAME" ) != null && !request.getString( "ORGANAME" ).equals( "" ) ){
				em = pm.getEntityManager();
				em.getTransaction().begin();
				Organisation orga = null;
				q = em.createNamedQuery( "OrgaFetchByName" );
				q.setParameter( "name", request.getString( "ORGANAME" ) );
				try{
					orga = (Organisation) q.getSingleResult();
				}catch( NoResultException e ){
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "WRONG ORGANISATION" );
					em.remove( newUser );
					em.getTransaction().commit();
					return result;
				}
				em.getTransaction().commit();
			
				newUser.setOrganisation( orga );
			}
			if( !request.getString( "PHONEPRIV" ).equals( "" ) )
				newUser.setPhonepriv( request.getString( "PHONEPRIV" ) );
			if( !request.getString( "PHONEWORK" ).equals( "" ) )
				newUser.setPhonework( request.getString( "PHONEWORK" ) );
			if( !request.getString( "STREET" ).equals( "" ) )
				newUser.setStreet( request.getString( "STREET" ) );
			if( !request.getString( "STREETNR" ).equals( "" ) )
				newUser.setStreetnr( request.getString( "STREETNR" ) );
			if( !request.getString( "SURENAME" ).equals( "" ) )
				newUser.setSurename( request.getString( "SURENAME" ) );
			if( !request.getString( "WASHSTORE" ).equals( "" ) )
				newUser.setWashstore( request.getString( "WASHSTORE" ) );
			if( !request.getString( "ZIP" ).equals( "" ) )
				newUser.setZip( request.getString( "ZIP" ) );
			if( !request.getString( "COUNTRY" ).equals( "" ) )
				newUser.setCountry( new Locale( request.getString( "COUNTRY" ).trim() ) );
			if( !request.getString( "LANGUAGE" ).equals( "" ) )
				newUser.setLanguage( new Locale( request.getString( "LANGUAGE" ).toLowerCase() ) );
			em = pm.getEntityManager();
			em.getTransaction().begin();
			em.merge( newUser );
			em.getTransaction().commit();
			
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			}finally{
				pm.closeEntityManager( em );
			}
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		}
		return result;
	}
	
	private Wash removeUserFromApplication(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
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
			try{
			User user = null;
			Application app = null;
			em.getTransaction().begin();	
			user = em.find( User.class, token.userId );
			Query q = em.createNamedQuery( "ApplicationFetchByName" );
			q.setParameter( "name", token.application );
			app = (Application) q.getSingleResult();
			em.getTransaction().commit();
			em.close();
			if( app.getGroupList().size() > 0 ){
				for( Groups g: app.getGroupList() ){
					g.getUserList().remove( user );
				}
			}
			
			if( app.getRoleList().size() > 0 ){
				for( Role r : app.getRoleList() ){
					r.getUserList().remove( user );
				}
			}
			em = pm.getEntityManager();
			em.getTransaction().begin();
			em.merge( app );
			em.refresh( user );
			em.getTransaction().commit();
			em.close();
			if( user.getGroupList().size() == 0 && user.getRoleList().size() == 0 ){
				em = pm.getEntityManager();
				em.getTransaction().begin();
				em.remove( user );
				em.getTransaction().commit();
				em.close();
			}
			
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			}finally{
				pm.closeEntityManager( em );
			}
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		}
		return result;
	}
	
	private Wash updateUser(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;
		if( !request.getString( "LMSTOKEN" ).equals( "" ) &&
			!request.getString( "LOGINNAME" ).equals( "" ) &&
			!request.getString( "PASSWORD" ).equals( "" ) ){
			
			LMSToken token = Tokenaizer.restoreLMSToken( request.getString( "LMSTOKEN" ).getBytes() );
			if( token == null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG LMSTOKEN" );
				return result;
			}
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
			try{
			em.getTransaction().begin();
			
			User newUser = null;
			
			Query q = em.createNamedQuery( "UserFetchByLoginname" );
			q.setParameter( "loginname", request.getString( "LOGINNAME" ) );
			try{
				newUser = (User)q.getSingleResult();
			}catch( NoResultException e ){}
			em.getTransaction().commit();
			
			if( newUser != null && newUser.getId() != token.userId ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "LOGINNAME USED BY OTHER USER" );
				return result;
			}
	
			E.rr( token.userId );
			em = pm.getEntityManager();
			em.getTransaction().begin();
			newUser = em.find( User.class, token.userId );
			em.getTransaction().commit();
		
			E.rr( newUser );
			newUser.setLoginname( request.getString( "LOGINNAME" ) );
			newUser.setPassword( request.getString( "PASSWORD" ) );
			
			if( !request.getString( "STATE" ).equals( "" ) )
				newUser.setState( request.getString( "STATE" ) );
			if( !request.getString( "CITY" ).equals( "" ) )
				newUser.setCity( request.getString( "CITY" ) );
			if( !request.getString( "EMAIL" ).equals( "" ) )
				newUser.setEmail( request.getString( "EMAIL" ) );
			if( !request.getString( "FAX" ).equals( "" ) )
				newUser.setFax( request.getString( "FAX" ) );
			if( !request.getString( "FIRSTNAME" ).equals( "" ) )
				newUser.setFirstname( request.getString( "FIRSTNAME" ) );
			if( !request.getString( "MOBILE" ).equals( "" ) )
				newUser.setMobile( request.getString( "MOBILE" ) );
			if( request.getString( "ORGANAME" ) != null && !request.getString( "ORGANAME" ).equals( "" ) ){
				em = pm.getEntityManager();
				em.getTransaction().begin();
				Organisation orga = null;
				q = em.createNamedQuery( "OrgaFetchByName" );
				q.setParameter( "name", request.getString( "ORGANAME" ) );
				try{
					orga = (Organisation) q.getSingleResult();
				}catch( NoResultException e ){
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "WRONG ORGANISATION" );
					em.remove( newUser );
					em.getTransaction().commit();
					return result;
				}
				em.getTransaction().commit();
				
				newUser.setOrganisation( orga );
			}
			if( !request.getString( "PHONEPRIV" ).equals( "" ) )
				newUser.setPhonepriv( request.getString( "PHONEPRIV" ) );
			if( !request.getString( "PHONEWORK" ).equals( "" ) )
				newUser.setPhonework( request.getString( "PHONEWORK" ) );
			if( !request.getString( "STREET" ).equals( "" ) )
				newUser.setStreet( request.getString( "STREET" ) );
			if( !request.getString( "STREETNR" ).equals( "" ) )
				newUser.setStreetnr( request.getString( "STREETNR" ) );
			if( !request.getString( "SURENAME" ).equals( "" ) )
				newUser.setSurename( request.getString( "SURENAME" ) );
			if( !request.getString( "WASHSTORE" ).equals( "" ) )
				newUser.setWashstore( request.getString( "WASHSTORE" ) );
			if( !request.getString( "ZIP" ).equals( "" ) )
				newUser.setZip( request.getString( "ZIP" ) );
			if( !request.getString( "COUNTRY" ).equals( "" ) )
				newUser.setCountry( new Locale( request.getString( "COUNTRY" ).trim() ) );
			if( !request.getString( "LANGUAGE" ).equals( "" ) )
				newUser.setLanguage( new Locale( request.getString( "LANGUAGE" ).toLowerCase() ) );
			em = pm.getEntityManager();
			em.getTransaction().begin();
			em.merge( newUser );
			em.getTransaction().commit();
			
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			}finally{
				pm.closeEntityManager( em );
			}
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		}
		return result;
	}
}
