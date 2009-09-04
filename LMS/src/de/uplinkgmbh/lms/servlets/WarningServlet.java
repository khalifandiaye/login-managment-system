package de.uplinkgmbh.lms.servlets;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.Base64;

import de.axone.webtemplate.WebTemplate;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.WebTemplateFactory;
import de.uplinkgmbh.lms.webtemplate.Context;


/**
 * Servlet implementation class for Servlet: TestServlet
 *
 */
 public class WarningServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Context context = Context.getSingelton();
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public WarningServlet() {
		super();
		
	}   	
	
	@Override
	public void init( javax.servlet.ServletConfig config ) throws ServletException {
		
		super.init( config );
		
		context.setServletContext( this.getServletContext() );
		context.setWebTemplateFactory( new WebTemplateFactory() );
	}
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding( "utf-8" );
		response.setContentType( "text/html;charset=utf-8" );
		
		try {
			
			String templatePath = context.getServletContext().getRealPath( "/template/warningpage.xhtml" );
			File templateFile = new File( templatePath );
			
			WebTemplate template = context.getWebTemplateFactory().templateFor( templateFile );
			
			if( request.getParameter( "target" ) == null || request.getParameter( "source" ) == null ){
				
				response.sendRedirect( request.getContextPath()+"/Login.html" );
				return;
				
			}
				
			template.setParameter( "warntarget", Base64.decode( request.getParameter( "target" ) ) );
			template.setParameter( "warnsource", Base64.decode( request.getParameter( "source" ) ) );
			template.setParameter( "info", request.getParameter( "info" ) );

			template.render( null, request, response, null );
			
		} catch (WebTemplateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet( request, response );
	} 

}