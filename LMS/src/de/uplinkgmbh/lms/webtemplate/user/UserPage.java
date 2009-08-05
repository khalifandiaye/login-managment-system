package de.uplinkgmbh.lms.webtemplate.user;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class UserPage extends AbstractFileWebTemplate{

	public UserPage() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "title", "LMS::User" );
		getHolder().setValue( "message", this.getParameter( "message" ) );
		getHolder().setValue( "keywords", "" );
		getHolder().setValue( "description", "" );
		getHolder().setValue( "company", "uplink gmbh" );
		getHolder().setValue( "userlist", this.getParameter( "userlist" ) );
		getHolder().setValue( "userlistpager", this.getParameter( "userlistpager" ) );
		getHolder().setValue( "user", this.getParameter( "user" ) );
		getHolder().setValue( "path", this.getParameter( "path" ) );
		getHolder().setValue( "applicationmanagment", request.getContextPath()+"/Application.html" );
		getHolder().setValue( "orgamanagment", "<a href=\"Organisation.html\"><span>Organisation</span></a>" );
		getHolder().setValue( "usermanagment", "<a href=\"User.html\"><span>User</span></a>" );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "action", "new" );
		String listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
		getHolder().setValue( "newuser", "<a href=\""+listpage+"\"><h1>new user</h1></a>" );
		
		if( this.getParameter( "grouplist" ) != null ){
			getHolder().setValue( "groupslist", this.getParameter( "grouplist" ) );	
			getHolder().setValue( "groupslistpager", this.getParameter( "grouplistpager" ) );
			getHolder().setValue( "groupshead", "Active Groups:" );
		}else{
			getHolder().setValue( "groupshead", "" );
			getHolder().setValue( "groupslist", "" );	
			getHolder().setValue( "groupslistpager", "" );
		}
		
		if( this.getParameter( "rolelist" ) != null ){
			getHolder().setValue( "rolelistpager", this.getParameter( "rolelistpager" ) );
			getHolder().setValue( "rolelist", this.getParameter( "rolelist" ) );
			getHolder().setValue( "rolehead", "Active Roles:" );
		}else{
			getHolder().setValue( "rolehead", "" );
			getHolder().setValue( "rolelist", "" );
			getHolder().setValue( "rolelistpager", "" );
		}
		
		if( this.getParameter( "actionlist" ) != null ){
			getHolder().setValue( "actionlistpager", this.getParameter( "actionlistpager" ) );
			getHolder().setValue( "actionlist", this.getParameter( "actionlist" ) );
			getHolder().setValue( "actionhead", "<tr><td>Name</td><td>Sort</td><td>State</td><td>Action</td><td>Target</td><td>Rule</td></tr>" );
		}else{
			getHolder().setValue( "actionhead", "" );
			getHolder().setValue( "actionlist", "" );
			getHolder().setValue( "actionlistpager", "" );
		}
		
		getHolder().render( object, request, response, null );
	}
	

}
