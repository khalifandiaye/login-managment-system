package de.uplinkgmbh.lms.user;

import java.util.List;

import de.axone.tools.E;
import de.uplinkgmbh.lms.business.DBList;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.Tokenaizer;

public abstract class AuthorizationsChecker {

	public static boolean isAllowed( LMSToken token, String state, String action, String target ){
		
		DBList list = new DBList();
		list.setNamedQuery( "RoleFetchByAppnameAndUserId" );
		list.getQuery().setParameter( "appname", token.application );
		list.getQuery().setParameter( "userId", token.userId );
		list.execute();
		
		List<Role> rolelist = (List<Role>) list.getResultList();
		
		for( Role r : rolelist ){
			for( Action a : (List<Action>) r.getActionList() ){
				//E.rr( target+" "+a.getTarget()+" "+action+" "+a.getAction()+" "+state+" "+a.getState() );
				if( target.matches( a.getTarget() ) 
						&& action.matches( a.getAction() ) 
						&& state.matches( a.getState() ) ){
					if( a.getRule().equalsIgnoreCase( "ACCEPT" ) ) return true;
					else return false;
				}
			}
		}
		return false;
	}
	
	public static boolean isAllowed( String AESToken, String state, String action, String target ){
		
		LMSToken token = Tokenaizer.restoreLMSToken( AESToken.getBytes() );
		return isAllowed( token, state, action, target );
	}
}
