package de.uplinkgmbh.lms.webtemplate.user;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.Role;

public class UserRoleListItem extends AbstractFileWebTemplate{


	public UserRoleListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		Role r = (Role) object;
		
		getHolder().setValue( "name", r.getApplication().getName()+" - "+r.getName() );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "role_id", ""+r.getId() );
		parameters.put( "application_id", ""+r.getApplication().getId() );
		parameters.put( "action", "show" );
		String listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
		getHolder().setValue( "link", listpage );
		
		getHolder().render(object, request, response, translator);
	}

}
