package de.uplinkgmbh.lms.webtemplate.role;

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
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.UserStatus;

public class RoleGroupListItem extends AbstractFileWebTemplate{


	public RoleGroupListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		Groups g = (Groups) object;
		
		getHolder().setValue( "name", g.getName() );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "group_id", ""+g.getId() );
		parameters.put( "role_id", ""+this.getParameter( "roleId" ) );
		parameters.put( "application_id", ""+this.getParameter( "applicationId" ) );
		parameters.put( "action", ""+this.getParameter( "usertype" ) );
		String listpage = HttpLinkBuilder.makeLink( request, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
		getHolder().setValue( "link", listpage );
		
		
		getHolder().render(object, request, response, translator);
	}

}
