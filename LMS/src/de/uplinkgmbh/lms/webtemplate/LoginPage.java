package de.uplinkgmbh.lms.webtemplate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.axone.webtemplate.form.WebForm;

public class LoginPage extends AbstractFileWebTemplate{
	
	public LoginPage() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response , Translator translator ) throws IOException,
			WebTemplateException, Exception {
		
		WebForm form = (WebForm) object;

		getHolder().setValue( "title", "LMS::Login" );
		getHolder().setValue( "message", this.getParameter( "message" ) );
		getHolder().setValue( "keywords", "" );
		getHolder().setValue( "description", "" );
		getHolder().setValue( "company", "uplink gmbh" );
		getHolder().setValue( "username", form.getHtmlInput( "username" ) );
		getHolder().setValue( "password", form.getHtmlInput( "password" ) );
		
		getHolder().render( object, request, response, null );
	}
}
