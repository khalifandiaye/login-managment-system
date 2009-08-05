package de.uplinkgmbh.lms.webtemplate;

import javax.servlet.ServletContext;

import de.axone.webtemplate.WebTemplateFactory;

public class Context {
	
	private static Context context = null;
	private ServletContext servletContext;
	private WebTemplateFactory webTemplateFactory;
	private String applicationname = "LoginManagmentSystem";
	
	public String getApplicationname() {
		return applicationname;
	}

	private Context(){
		
	}
	
	public static Context getSingelton(){
		if( context == null ){
			context = new Context();
		}
		return context;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public WebTemplateFactory getWebTemplateFactory() {
		return webTemplateFactory;
	}

	public void setWebTemplateFactory(WebTemplateFactory webTemplateFactory) {
		this.webTemplateFactory = webTemplateFactory;
	}
}
