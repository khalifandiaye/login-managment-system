package de.uplinkgmbh.lms.webtemplate.organisation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.Base64;

import de.axone.wash.Wash;
import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.UserStatus;
import de.uplinkgmbh.lms.entitys.Organisation;

public class OrganisationListItem extends AbstractFileWebTemplate{


	public OrganisationListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		Organisation o = (Organisation) object;
		
		getHolder().setValue( "name", o.getName() );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "organisation_id", ""+o.getId() );
		parameters.put( "action", "show" );
		String listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
		getHolder().setValue( "showlink", listpage );
		
		parameters = new HashMap<String,String>();
		parameters.put( "organisation_id", ""+o.getId() );
		parameters.put( "action", "edit" );
		listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
		getHolder().setValue( "editlink", listpage );
		
		parameters = new HashMap<String,String>();
		parameters.put( "organisation_id", ""+o.getId() );
		parameters.put( "action", "delete" );
		listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
		
		String target = listpage;
	
		String source = HttpLinkBuilder.makeLink( request, true, null );;
	
		String target64 = Base64.encode( target );
		String source64 = Base64.encode( source );

		String url = "Warning.html?target="+
		target64+
		"&source="+
		source64+
		"&info=delete+user";
	
		getHolder().setValue( "dellink", url );
		
		getHolder().render(object, request, response, translator);
	}

}
