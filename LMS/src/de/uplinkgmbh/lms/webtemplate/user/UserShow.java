package de.uplinkgmbh.lms.webtemplate.user;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class UserShow extends AbstractFileWebTemplate{

	public UserShow() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "activ", this.getParameter( "activ" ) );
		getHolder().setValue( "organisation", this.getParameter( "organisation" ) );
		getHolder().setValue( "city", this.getParameter( "city" ) );
		getHolder().setValue( "country", this.getParameter( "country" ) );
		getHolder().setValue( "email", this.getParameter( "email" ) );
		getHolder().setValue( "fax", this.getParameter( "fax" ) );
		getHolder().setValue( "firstname", this.getParameter( "firstname" ) );
		getHolder().setValue( "language", this.getParameter( "language" ) );
		getHolder().setValue( "loginname", this.getParameter( "loginname" ) );
		getHolder().setValue( "mobile", this.getParameter( "mobile" ) );
		getHolder().setValue( "password", this.getParameter( "password" ) );
		getHolder().setValue( "phonepriv", this.getParameter( "phonepriv" ) );
		getHolder().setValue( "phonework", this.getParameter( "phonework" ) );
		getHolder().setValue( "state", this.getParameter( "state" ) );
		getHolder().setValue( "street", this.getParameter( "street" ) );
		getHolder().setValue( "streetnr", this.getParameter( "streetnr" ) );
		getHolder().setValue( "surename", this.getParameter( "surename" ) );
		getHolder().setValue( "template", this.getParameter( "template" ) );
		getHolder().setValue( "washstore", this.getParameter( "washstore" ) );
		getHolder().setValue( "zip", this.getParameter( "zip" ) );
	
		getHolder().render( object, request, response, null );
	}
}
