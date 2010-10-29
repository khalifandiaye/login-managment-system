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
import de.uplinkgmbh.lms.entitys.Groups;

public class UserGroupListItem extends AbstractFileWebTemplate{


	public UserGroupListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		Groups g = (Groups) object;

		getHolder().setValue( "name", g.getApplication().getName()+" - "+g.getName() );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "group_id", ""+g.getId() );
		parameters.put( "application_id", ""+g.getApplication().getId() );
		parameters.put( "action", "show" );
		String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		getHolder().setValue( "link", listpage );

		getHolder().render(object, request, response, translator);
	}

}
