package de.uplinkgmbh.lms.webtemplate.role;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.utils.UserStatus;

public class RolePage extends AbstractFileWebTemplate{

	public RolePage() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "title", "LMS::Role" );
		getHolder().setValue( "message", this.getParameter( "message" ) );
		getHolder().setValue( "keywords", "" );
		getHolder().setValue( "description", "" );
		getHolder().setValue( "company", "uplink gmbh" );
		getHolder().setValue( "rolelist", this.getParameter( "rolelist" ) );
		getHolder().setValue( "rolelistpager", this.getParameter( "rolelistpager" ) );
		getHolder().setValue( "role", this.getParameter( "role" ) );
		getHolder().setValue( "path", this.getParameter( "path" ) );
		getHolder().setValue( "actionlist", this.getParameter( "actionlist" ) );
		getHolder().setValue( "actionlistpager", this.getParameter( "actionlistpager" ) );
		getHolder().setValue( "emptyaction", this.getParameter( "emptyaction" ) );
		getHolder().setValue( "newrolelink", request.getContextPath()+"/Role.html?action=new&application_id="+this.getParameter( "appid" ) );
		
		if( this.getParameter( "userstatus" ) == UserStatus.SYSTEMADMIN ){
			getHolder().setValue( "usermanagment", "<a href=\"User.html\"><span>User</span></a>" );
			getHolder().setValue( "orgamanagment", "<a href=\"Organisation.html\"><span>Organisation</span></a>" );
			getHolder().setValue( "newapp", "<a href=\""+request.getContextPath()+request.getServletPath()+"?action=new\"><h1>New Application</h1></a>" );
		}else{
			getHolder().setValue( "usermanagment", "" );
			getHolder().setValue( "orgamanagment", "" );
			getHolder().setValue( "newapp", "" );
		}

		getHolder().setValue( "actionlisthead", this.getParameter( "actionlisthead" ) );

		if( this.getParameter( "userlist" ) != null ){
			getHolder().setValue( "activeusers", "Active Users: " );
			getHolder().setValue( "deactiveusers", "Deactive Users: " );
			getHolder().setValue( "userlist", this.getParameter( "userlist" ) );
			getHolder().setValue( "userlistdeactiv", this.getParameter( "userlistdeactiv" ) );
			getHolder().setValue( "userlistpager", this.getParameter( "userlistpager" ) );
			getHolder().setValue( "userlistdeactivpager", this.getParameter( "userlistdeactivpager" ) );
			getHolder().setValue( "activegroups", "Active Groups: " );
			getHolder().setValue( "deactivegroups", "Deactive Groups: " );
			getHolder().setValue( "groupslist", this.getParameter( "groupslist" ) );
			getHolder().setValue( "groupslistdeactiv", this.getParameter( "groupslistdeactiv" ) );
			getHolder().setValue( "groupslistpager", this.getParameter( "groupslistpager" ) );
			getHolder().setValue( "groupslistdeactivpager", this.getParameter( "groupslistdeactivpager" ) );
		}else{
			getHolder().setValue( "activeusers", "" );
			getHolder().setValue( "deactiveusers", "" );
			getHolder().setValue( "userlist", "" );
			getHolder().setValue( "userlistdeactiv", "" );
			getHolder().setValue( "userlistpager", "" );
			getHolder().setValue( "userlistdeactivpager", "" );
			getHolder().setValue( "activegroups", "" );
			getHolder().setValue( "deactivegroups", "" );
			getHolder().setValue( "groupslist", "" );
			getHolder().setValue( "groupslistdeactiv", "" );
			getHolder().setValue( "groupslistpager", "" );
			getHolder().setValue( "groupslistdeactivpager", "" );
		}
		
		getHolder().render( object, request, response, null );
	}
	

}
