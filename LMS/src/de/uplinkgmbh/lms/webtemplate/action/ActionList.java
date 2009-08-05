package de.uplinkgmbh.lms.webtemplate.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.tools.E;
import de.axone.webtemplate.Renderer;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.axone.webtemplate.list.AbstractListRenderer;
import de.axone.webtemplate.list.DefaultPager;
import de.axone.webtemplate.list.ListProvider;
import de.axone.webtemplate.list.Pager;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.servlets.forms.ActionForm;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;

public class ActionList extends AbstractListRenderer<Action> {


	public ActionList(HttpServletRequest req, String name, int itemsPerPage,
			ListProvider<Action> listProvider, Renderer itemTemplate) {
		super( req, name, itemsPerPage, listProvider, itemTemplate );

	}

	@Override
	public void render( Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator )
			throws IOException, WebTemplateException, Exception {

		super.render(object, request, response, translator);
	}	
}
