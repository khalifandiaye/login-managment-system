package de.uplinkgmbh.lms.webtemplate.organisation;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class OrganisationPage extends AbstractFileWebTemplate{
	

	public OrganisationPage() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "title", "LMS::Organisation" );
		getHolder().setValue( "message", this.getParameter( "message" ) );
		getHolder().setValue( "keywords", "" );
		getHolder().setValue( "description", "" );
		getHolder().setValue( "company", "uplink gmbh" );
		getHolder().setValue( "orgalist", this.getParameter( "orgalist" ) );
		getHolder().setValue( "orgalistpager", this.getParameter( "orgalistpager" ) );
		getHolder().setValue( "orga", this.getParameter( "orga" ) );
		getHolder().setValue( "path", this.getParameter( "path" ) );
		getHolder().setValue( "applicationmanagment", request.getContextPath()+"/Application.html" );
		getHolder().setValue( "orgamanagment", "<a href=\"Organisation.html\"><span>Organisation</span></a>" );
		getHolder().setValue( "usermanagment", "<a href=\"User.html\"><span>User</span></a>" );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "action", "new" );
		String listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
		
		if( ! this.getParameter( "orgalist" ).equals( "" ) ) getHolder().setValue( "neworga", "<a href=\""+listpage+"\"><h1>new Organisation</h1></a>" );
		else getHolder().setValue( "neworga", "" );
		
		
		getHolder().render( object, request, response, null );
	}
	

}
