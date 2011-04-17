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
import de.uplinkgmbh.lms.entitys.User;

public class GroupUserListItem extends AbstractFileWebTemplate{


	public GroupUserListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		User u = (User) object;

		getHolder().setValue( "name", u.getLoginname()+" ["+u.getFirstname()+" "+u.getSurename()+"]" );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "group_id", ""+this.getParameter( "groupId" ) );
		parameters.put( "user_id", ""+u.getId() );
		parameters.put( "application_id", ""+this.getParameter( "applicationId" ) );
		parameters.put( "action", ""+this.getParameter( "usertype" ) );
		String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		getHolder().setValue( "link", listpage );
		
		getHolder().setValue( "group_id", this.getParameter( "groupId" ) );
		getHolder().setValue( "user_id", u.getId() );
		getHolder().setValue( "application_id", this.getParameter( "applicationId" ) );
		getHolder().setValue( "action", this.getParameter( "usertype" ) );
		
		getHolder().render(object, request, response, translator);
	}

}
