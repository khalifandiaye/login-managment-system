package de.uplinkgmbh.lms.webtemplate.role;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class RoleEdit extends AbstractFileWebTemplate{

	public RoleEdit() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {

		getHolder().setValue( "name", this.getParameter( "name" ) );
		getHolder().setValue( "sort", this.getParameter( "sort" ) );
		getHolder().setValue( "appid", this.getParameter( "appid" ).toString() );
		getHolder().setValue( "action", this.getParameter( "action" ) );
		getHolder().setValue( "roleid", this.getParameter( "roleid" ).toString() );
		
		getHolder().render( object, request, response, null );
	}
	
}
