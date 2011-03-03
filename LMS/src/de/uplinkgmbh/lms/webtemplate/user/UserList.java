package de.uplinkgmbh.lms.webtemplate.user;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.Renderer;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.axone.webtemplate.list.AbstractListRenderer;
import de.axone.webtemplate.list.ListProvider;
import de.uplinkgmbh.lms.entitys.User;

public class UserList extends AbstractListRenderer<User> {

	public UserList(HttpServletRequest req, String name, int itemsPerPage,
			ListProvider<User> listProvider, Renderer itemTemplate) {
		super( req, name, "loginname", itemsPerPage, listProvider, itemTemplate );
	}

	@Override
	public void render( Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator )
			throws IOException, WebTemplateException, Exception {

		super.render(object, request, response, translator);
	}	
}
