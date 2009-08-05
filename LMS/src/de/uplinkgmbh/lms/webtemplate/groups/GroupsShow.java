package de.uplinkgmbh.lms.webtemplate.groups;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.Base64;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;

public class GroupsShow extends AbstractFileWebTemplate{
	
	public GroupsShow() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		getHolder().setValue( "name", this.getParameter( "name" ) );
		
		getHolder().setValue( "linkedit", "<a href=\""+
				request.getContextPath()+
				request.getServletPath()+
				"?action=edit&application_id="+
				this.getParameter( "appid" )+
				"&group_id="+this.getParameter( "groupid" )+
				"\">edit</a>" );
		
		String target = request.getContextPath()+request.getServletPath()+
			"?application_id="+this.getParameter( "appid" )+
			"&group_id="+this.getParameter( "groupid" )+
			"&action=delete";
		String source = request.getContextPath()+request.getServletPath()+"?"+request.getQueryString();

		String target64 = Base64.encode( target );
		String source64 = Base64.encode( source );

		String url = "Warning.html?target="+
			target64+
			"&source="+
			source64+
			"&info=delete+group";

		getHolder().setValue( "linkdel", "<a href=\""+url+"\">delete</a>" );
	
		getHolder().render( object, request, response, null );
	}
}
