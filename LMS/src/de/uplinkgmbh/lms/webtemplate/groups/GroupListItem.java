package de.uplinkgmbh.lms.webtemplate.groups;

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
import de.uplinkgmbh.lms.entitys.Groups;

public class GroupListItem extends AbstractFileWebTemplate{


	public GroupListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		Groups g = (Groups) object;
		
		getHolder().setValue( "name", g.getName() );
		
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+g.getApplication().getId() );
		parameters.put( "group_id", ""+g.getId() );
		parameters.put( "action", "show" );
		String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		getHolder().setValue( "showlink", listpage );
		
		parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+g.getApplication().getId() );
		parameters.put( "group_id", ""+g.getId() );
		parameters.put( "action", "edit" );
		listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		getHolder().setValue( "editlink", listpage );
		
		parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+g.getApplication().getId() );
		parameters.put( "group_id", ""+g.getId() );
		parameters.put( "action", "delete" );
		listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		
		String target = listpage;
		String source = HttpLinkBuilder.makeLink( request, true, true, null );;

		String target64 = Base64.encode( target );
		String source64 = Base64.encode( source );

		String url = "Warning.html?target="+
			target64+
			"&source="+
			source64+
			"&info=delete+group";
		getHolder().setValue( "deletelink", url );
		
		getHolder().render(object, request, response, translator);
	}

}
