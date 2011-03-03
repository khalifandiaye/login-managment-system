package de.uplinkgmbh.lms.webtemplate.groups;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.business.STATICS;
import de.uplinkgmbh.lms.utils.UserStatus;

public class GroupsPage extends AbstractFileWebTemplate{

	public GroupsPage() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "title", "LMS::Groups" );
		getHolder().setValue( "message", this.getParameter( "message" ) );
		getHolder().setValue( "keywords", "" );
		getHolder().setValue( "description", "" );
		getHolder().setValue( "lang", this.getParameter( "lang" ) );
		getHolder().setValue( "formcolor", STATICS.FROMCOLOR );
		getHolder().setValue( "company", "uplink gmbh" );
		getHolder().setValue( "grouplist", this.getParameter( "grouplist" ) );
		getHolder().setValue( "grouplistpager", this.getParameter( "grouplistpager" ) );
		getHolder().setValue( "group", this.getParameter( "group" ) );
		getHolder().setValue( "path", this.getParameter( "path" ) );
		
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "application_id", ""+this.getParameter( "appid" ) );
		parameters.put( "action", "edit" );
		String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "group_id=[a-zA-Z_0-9]*", "" );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		getHolder().setValue( "newgroup", "<a href=\""+listpage+"\" ><h1 class=\"listtop\">new group</h></a>" );
				
		if( this.getParameter( "userstatus" ) == UserStatus.SYSTEMADMIN ){
			getHolder().setValue( "usermanagment", "<a href=\"User.html\"><span>User</span></a>" );
			getHolder().setValue( "orgamanagment", "<a href=\"Organisation.html\"><span>Organisation</span></a>" );
			
		}else{
			getHolder().setValue( "usermanagment", "" );
			getHolder().setValue( "orgamanagment", "" );
		}
		
		if( this.getParameter( "rolelist" ) != null ){
			getHolder().setValue( "activeusers", "Active Users: " );
			getHolder().setValue( "deactiveusers", "Deactive Users: " );
			getHolder().setValue( "userlist", this.getParameter( "userlist" ) );
			getHolder().setValue( "userlistdeactiv", this.getParameter( "userlistdeactiv" ) );
			getHolder().setValue( "rolelist", this.getParameter( "rolelist" ) );
			getHolder().setValue( "userlistpager", this.getParameter( "userlistpager" ) );
			getHolder().setValue( "userlistdeactivpager", this.getParameter( "userlistdeactivpager" ) );
			getHolder().setValue( "rolelistpager", this.getParameter( "rolelistpager" ) );
			getHolder().setValue( "activroles", "Activ Role: " );
			
		}else{
			getHolder().setValue( "activeusers", "" );
			getHolder().setValue( "deactiveusers", "" );
			getHolder().setValue( "userlist", "" );
			getHolder().setValue( "userlistdeactiv", "" );
			getHolder().setValue( "rolelist", "" );
			getHolder().setValue( "userlistpager", "" );
			getHolder().setValue( "userlistdeactivpager", "" );
			getHolder().setValue( "rolelistpager", "" );
			getHolder().setValue( "activroles", "" );
			
		}
		
		
		getHolder().render( object, request, response, null );
	}
	

}
