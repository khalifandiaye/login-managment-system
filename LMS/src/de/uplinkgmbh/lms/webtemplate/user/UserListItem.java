package de.uplinkgmbh.lms.webtemplate.user;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.Base64;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.User;

public class UserListItem extends AbstractFileWebTemplate{


	public UserListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator)
			throws IOException, WebTemplateException, Exception {
		
		User u = (User) object;
		
		getHolder().setValue( "name", u.getLoginname()+" ["+u.getFirstname()+" "+u.getSurename()+"]" );
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put( "user_id", ""+u.getId() );
		parameters.put( "action", "show" );
		String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
		getHolder().setValue( "showlink", listpage );
		
		parameters = new HashMap<String,String>();
		parameters.put( "user_id", ""+u.getId() );
		parameters.put( "action", "edit" );
		listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
		getHolder().setValue( "editlink", listpage );
		
		parameters = new HashMap<String,String>();
		parameters.put( "user_id", ""+u.getId() );
		parameters.put( "action", "delete" );
		listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
		listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
		
		String target = listpage;
	
		String source = HttpLinkBuilder.makeLink( request, true, false, null );;
	
		String target64 = Base64.encode( target );
		String source64 = Base64.encode( source );

		String url = "Warning.html?target="+
		target64+
		"&source="+
		source64+
		"&info=delete+user";
	
		getHolder().setValue( "dellink", url );
		
		getHolder().render(object, request, response, translator);
	}

}
