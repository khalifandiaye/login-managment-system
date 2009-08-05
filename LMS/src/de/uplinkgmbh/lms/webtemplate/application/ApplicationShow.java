package de.uplinkgmbh.lms.webtemplate.application;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.utils.UserStatus;

public class ApplicationShow extends AbstractFileWebTemplate{

	public ApplicationShow() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "name", this.getParameter( "name" ) );
		if( ((UserStatus)this.getParameter( "userstatus" )) == UserStatus.SYSTEMADMIN )
			getHolder().setValue( "linkedit", "<a href=\""+request.getContextPath()+request.getServletPath()+"?action=edit&amp;application_id="+this.getParameter( "id" )+"\">edit</a>" );
		else
			getHolder().setValue( "linkedit", "" );
		getHolder().setValue( "linkrole", request.getContextPath()+"/Role.html?action=list&amp;application_id="+this.getParameter( "id" ) );
		getHolder().setValue( "linkgroups", request.getContextPath()+"/Groups.html?action=list&amp;application_id="+this.getParameter( "id" ) );
		
		getHolder().render( object, request, response, null );
	}
}
