package de.uplinkgmbh.lms.webtemplate.warning;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class WarningPage extends AbstractFileWebTemplate{

	public WarningPage() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "title", "LMS::Application" );
		getHolder().setValue( "message", this.getParameter( "message" ) );
		getHolder().setValue( "keywords", "" );
		getHolder().setValue( "description", "" );
		getHolder().setValue( "company", "uplink gmbh" );
		getHolder().setValue( "warntarget", this.getParameter( "warntarget" ) );
		getHolder().setValue( "warnsource", this.getParameter( "warnsource" ) );
		getHolder().setValue( "info", this.getParameter( "info" ) );
		getHolder().setValue( "path", "LMS LoginManagerSystem::Warning" );
	
		getHolder().render( object, request, response, null );
	}
	

}
