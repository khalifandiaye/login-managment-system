package de.uplinkgmbh.lms.webtemplate.application;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class ApplicationEdit extends AbstractFileWebTemplate{

	public ApplicationEdit() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {

		getHolder().setValue( "name", this.getParameter( "name" ) );
		getHolder().setValue( "appid", this.getParameter( "id" ).toString() );
		
		getHolder().render( object, request, response, null );
	}
	
}
