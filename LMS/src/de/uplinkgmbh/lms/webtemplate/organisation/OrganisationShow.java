package de.uplinkgmbh.lms.webtemplate.organisation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class OrganisationShow extends AbstractFileWebTemplate{

	public OrganisationShow() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "id", this.getParameter( "id" ).toString() );
		
		getHolder().setValue( "city", this.getParameter( "city" ) );
		getHolder().setValue( "country", this.getParameter( "country" ) );
		getHolder().setValue( "fax", this.getParameter( "fax" ) );
		getHolder().setValue( "name", this.getParameter( "name" ) );
		if( this.getParameter( "url" ) != null )
			getHolder().setValue( "url", this.getParameter( "url" ).toString() );
		else
			getHolder().setValue( "url", "" );
		getHolder().setValue( "phone", this.getParameter( "phone" ) );
		getHolder().setValue( "state", this.getParameter( "state" ) );
		getHolder().setValue( "street", this.getParameter( "street" ) );
		getHolder().setValue( "streetnr", this.getParameter( "streetnr" ) );
		getHolder().setValue( "washstore", this.getParameter( "washstore" ) );
		getHolder().setValue( "zip", this.getParameter( "zip" ) );
	
		getHolder().render( object, request, response, null );
	}
}
