package de.uplinkgmbh.lms.webtemplate.application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.wash.Wash;
import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.UserStatus;

public class ApplicationListItem extends AbstractFileWebTemplate{


	public ApplicationListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		Application a = (Application) object;
		boolean lmsAdmin = false;
		if( AuthorizationsChecker.isAllowed( ((LMSToken)request.getSession().getAttribute( "token" )), "SYSTEMADMIN", "DOALL", "Application" ) ) lmsAdmin = true;
		
		getHolder().setValue( "appname", a.getName() );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+a.getId() );
		parameters.put( "action", "show" );
		String listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		getHolder().setValue( "applink", listpage );
		if( lmsAdmin ){
			parameters = new HashMap<String,String>();
			parameters.put( "application_id", ""+a.getId() );
			parameters.put( "action", "edit" );
			listpage = HttpLinkBuilder.makeLink( request, true, parameters );
			getHolder().setValue( "edit", "<td><a href=\""+listpage+"\">&emsp;edit</a></td>" );
		}else getHolder().setValue( "edit", "" );
		parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+a.getId() );
		parameters.put( "action", "list" );
		listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
		getHolder().setValue( "appshowroleslink", listpage );
		parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+a.getId() );
		parameters.put( "action", "list" );
		listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		getHolder().setValue( "appshowgrouplink", listpage );
		
		
		getHolder().render(object, request, response, translator);
	}

}
