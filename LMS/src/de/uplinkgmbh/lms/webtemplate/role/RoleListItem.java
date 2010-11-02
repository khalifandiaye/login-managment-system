package de.uplinkgmbh.lms.webtemplate.role;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.Base64;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.Role;

public class RoleListItem extends AbstractFileWebTemplate{


	public RoleListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		Role r = (Role) object;
		
		getHolder().setValue( "rolename", r.getName() );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+r.getApplication().getId() );
		parameters.put( "role_id", ""+r.getId() );
		parameters.put( "action", "show" );
		String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		getHolder().setValue( "rolelink", listpage );
	
		parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+r.getApplication().getId() );
		parameters.put( "role_id", ""+r.getId() );
		parameters.put( "action", "edit" );
		listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		getHolder().setValue( "roleeditlink", listpage );

		parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+r.getApplication().getId() );
		parameters.put( "role_id", ""+r.getId() );
		parameters.put( "action", "delete" );
		String target = HttpLinkBuilder.makeLink( request, true, false, parameters );
		String source = HttpLinkBuilder.makeLink( request, true, false, null );
		
		String target64 = Base64.encode( target );
		String source64 = Base64.encode( source );

		
		String url = "Warning.html?target="+
			target64+
			"&source="+
			source64+
			"&info=delete+action";
		
		getHolder().setValue( "roledellink", url );
		
		
		getHolder().render(object, request, response, translator);
	}

}
