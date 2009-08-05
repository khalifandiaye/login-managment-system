package de.uplinkgmbh.lms.webtemplate.groups;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class GroupsEdit extends AbstractFileWebTemplate{

	public GroupsEdit() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {

		getHolder().setValue( "name", this.getParameter( "name" ) );
		getHolder().setValue( "border_name", this.getParameter( "border_name" ) );
		getHolder().setValue( "appid", this.getParameter( "appid" ).toString() );
		getHolder().setValue( "groupid", this.getParameter( "groupid" ).toString() );
		
		getHolder().render( object, request, response, null );
	}
	
}
