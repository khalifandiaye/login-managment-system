package de.uplinkgmbh.lms.webtemplate.groups;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.Base64;

import de.axone.wash.Wash;
import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.UserStatus;

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
		String listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
		getHolder().setValue( "link", listpage );
		
		getHolder().render(object, request, response, translator);
	}

}
