package de.uplinkgmbh.lms.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
import de.uplinkgmbh.lms.entitys.Organisation;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.Tokenaizer;

public class OrganisationService implements Service{
	
	private static Log log = Logging.getLog( OrganisationService.class );

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
		if( "getOrgas".equals( operation ) )	
			result = getOrgas( request );
		else if( "newOrga".equals( operation ) )
			result = newOrga( request );
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

	
	private Wash getOrgas(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
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
			
			if( !AuthorizationsChecker.isAllowed( token, "APPLICATION", "organisation.getOrgas", token.application ) ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG PERMISSION" );
				return result;
			}
			
			List<Organisation> ol = null;
			em.getTransaction().begin();
			Query q = em.createNamedQuery( "AllOrganisation" );
			ol = (List<Organisation>) q.getResultList();
			em.getTransaction().commit();
			
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			result.addField( "SIZE", Type.INTEGER, ol.size() );
			/*
			 * ORGANAME-n	string	ORGAURL-n	string	ORGAPHONE-n	string	ORGAFAX-n	string	
			 * ORGACITY-n	string	ORGASTATE-n	string	ORGACOUNTRY-n	string	ORGAZIP-n	string	
			 * ORGASTREET-n	string	ORGASTREETNR-n	string	ORGAWASHSTORE-n	string
			 */
			int i = 0;
			for( Organisation o : ol ){
				result.addField( "Orga-"+i+".ORGANAME", Type.STRING, o.getName() );
				if( o.getUrl() != null )
					result.addField( "Orga-"+i+".ORGAURL", Type.STRING, o.getUrl().toString() );
				else
					result.addField( "Orga-"+i+".ORGAURL", Type.STRING, "" );
				result.addField( "Orga-"+i+".ORGAPHONE", Type.STRING, o.getPhone() );
				result.addField( "Orga-"+i+".ORGAFAX", Type.STRING, o.getFax() );
				result.addField( "Orga-"+i+".ORGACITY", Type.STRING, o.getCity() );
				result.addField( "Orga-"+i+".ORGASTATE", Type.STRING, o.getState() );
				if( o.getCountry() != null )
					result.addField( "Orga-"+i+".ORGACOUNTRY", Type.STRING, o.getCountry().getDisplayCountry() );
				else
					result.addField( "Orga-"+i+".ORGACOUNTRY", Type.STRING, "" );
				result.addField( "Orga-"+i+".ORGAZIP", Type.STRING, o.getZip() );
				result.addField( "Orga-"+i+".ORGASTREET", Type.STRING, o.getStreet() );
				result.addField( "Orga-"+i+".ORGASTREETNR", Type.STRING, o.getStreetnr() );
				result.addField( "Orga-"+i+".ORGAWASHSTORE", Type.STRING, o.getWashstore() );
				i++;
			}
			if( em.isOpen() ) em.clear(); em.close();
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
		return result;
	} 
	
	private Wash newOrga(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;

		if( !request.getString( "LMSTOKEN" ).equals( "" ) ||
			!request.getString( "ORGANAME" ).equals( "" ) ){
			
			LMSToken token = Tokenaizer.restoreLMSToken( request.getString( "LMSTOKEN" ).getBytes() );
			if( token == null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG LMSTOKEN" );
				return result;
			}
			
			if( !AuthorizationsChecker.isAllowed( token, "APPLICATION", "organisation.newOrga", token.application ) ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG PERMISSION" );
				return result;
			}
			/*
			 * LMSTOKEN	string		ORGANAME	string	ORGAURL	string	ORGAPHONE	string	
			 * ORGAFAX	string	ORGACITY	string	ORGASTATE	string	ORGACOUNTRY	string	
			 * ORGAZIP	string	ORGASTREET	string	ORGASTREETNR	string	ORGAWASHSTORE	string
			 */
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
			Organisation orga = null;
			em.getTransaction().begin();
			Query q = em.createNamedQuery( "OrgaFetchByName" );
			q.setParameter( "name", request.getString( "ORGANAME" ) );
			try{
				orga = (Organisation) q.getSingleResult();
			}catch( NoResultException e ){}
			em.getTransaction().commit();
			if( orga != null ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "ORGANAME USED BY OTHER ORGA" );
				return result;
			}
			orga = new Organisation();
			if( !request.getString( "ORGAURL" ).equals( "" ) ){
				try {
					orga.setUrl( new URL( request.getString( "ORGAURL" ) ) );
				} catch (MalformedURLException e) {
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "MALFROMEDURL" );
					return result;
				}
			}
			if( !request.getString( "ORGACITY" ).equals( "" ) )
				orga.setCity( request.getString( "ORGACITY" ) );
			if( !request.getString( "ORGACOUNTRY" ).equals( "" ) )
				orga.setCountry( new Locale( request.getString( "ORGACOUNTRY" ) ) );
			if( !request.getString( "ORGAFAX" ).equals( "" ) )
				orga.setFax( request.getString( "ORGAFAX" ) );
			if( !request.getString( "ORGANAME" ).equals( "" ) )
				orga.setName( request.getString( "ORGANAME" ) );
			if( !request.getString( "ORGAPHONE" ).equals( "" ) )
				orga.setPhone( request.getString( "ORGAPHONE" ) );
			if( !request.getString( "ORGASTATE" ).equals( "" ) )
				orga.setState( request.getString( "ORGASTATE" ) );
			if( !request.getString( "ORGASTREET" ).equals( "" ) )
				orga.setStreet( request.getString( "ORGASTREET" ) );
			if( !request.getString( "ORGASTREETNR" ).equals( "" ) )
				orga.setStreetnr( request.getString( "ORGASTREETNR" ) );
			if( !request.getString( "ORGAWASHSTORE" ).equals( "" ) )
				orga.setWashstore( request.getString( "ORGAWASHSTORE" ) );
			if( !request.getString( "ORGAZIP" ).equals( "" ) )
				orga.setZip( request.getString( "ORGAZIP" ) );
			
			em.getTransaction().begin();
			em.persist( orga );
			em.getTransaction().commit();
			
			result = new DefaultWash();
			result.addField( "STATUS", Type.BOOLEAN, true );
			result.addField( "REASON", Type.STRING, "" );
			
			if( em.isOpen() ) em.clear(); em.close();
			
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		}
	
		return result;
	} 
}
