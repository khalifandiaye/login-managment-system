package de.uplinkgmbh.lms.webtemplate.application;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.business.STATICS;
import de.uplinkgmbh.lms.utils.UserStatus;

public class ApplicationPage extends AbstractFileWebTemplate{
	
	public ApplicationPage() throws KeyException, IOException {
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
		getHolder().setValue( "lang", this.getParameter( "lang" ) );
		getHolder().setValue( "formcolor", STATICS.FROMCOLOR );
		getHolder().setValue( "company", "uplink gmbh" );
		getHolder().setValue( "applist", this.getParameter( "applist" ) );
		getHolder().setValue( "applistpager", this.getParameter( "applistpager" ) );
		getHolder().setValue( "app", this.getParameter( "app" ) );
		getHolder().setValue( "path", this.getParameter( "path" ) );
		
		if( this.getParameter( "userstatus" ) == UserStatus.SYSTEMADMIN ){
			getHolder().setValue( "usermanagment", "<a href=\"User.html\"><span>User</span></a>" );
			getHolder().setValue( "orgamanagment", "<a href=\"Organisation.html\"><span>Organisation</span></a>" );
			getHolder().setValue( "newapp", "<a href=\""+request.getContextPath()+request.getServletPath()+"?action=new\"><h1>New Application</h1></a>" );
		}else{
			getHolder().setValue( "usermanagment", "" );
			getHolder().setValue( "orgamanagment", "" );
			getHolder().setValue( "newapp", "" );
		}
		getHolder().render( object, request, response, null );
	}
	
}
