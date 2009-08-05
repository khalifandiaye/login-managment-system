package de.uplinkgmbh.lms.webtemplate.application;

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
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;

public class ApplicationList extends AbstractListRenderer<Application> {
	
	private Renderer itemTemplate;

	public ApplicationList(HttpServletRequest req, String name, int itemsPerPage,
			ListProvider<Application> listProvider, Renderer itemTemplate) {
		super( req, name, itemsPerPage, listProvider, itemTemplate );
		this.itemTemplate = itemTemplate;
	}

	@Override
	public void render( Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator )
			throws IOException, WebTemplateException, Exception {

		Iterable<Application> it = getList();

		for( Application a : it ) {
			itemTemplate.render( a, request, response, translator );
		}
	}	
}
