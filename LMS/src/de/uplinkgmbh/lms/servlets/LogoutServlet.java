package de.uplinkgmbh.lms.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.WebTemplateFactory;
import de.uplinkgmbh.lms.user.Login;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.Tokenaizer;
import de.uplinkgmbh.lms.webtemplate.Context;

public class LogoutServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	   static final long serialVersionUID = 43489323222243432L;
	   private Context context = Context.getSingelton();
	   
		public LogoutServlet() {
			super();
		}
		
		@Override
		public void init( javax.servlet.ServletConfig config ) throws ServletException {
			
			super.init( config );
			
			context.setServletContext( this.getServletContext() );
			context.setWebTemplateFactory( new WebTemplateFactory() );
		}
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			response.setCharacterEncoding( "utf-8" );
			response.setContentType( "text/html;charset=utf-8" );
			
			if( request.getParameter( "cleantime" ) != null ){
				Tokenaizer.killTokenByTime( Long.parseLong( request.getParameter( "cleantime" ) ) );
			}else{
			
				LMSToken token = (LMSToken)request.getSession().getAttribute( "token" );
				if( token == null ){
					response.sendRedirect( request.getContextPath()+"/" );
					return;
				}
				Login login = new Login();
				login.logOut( token );
				response.sendRedirect( request.getContextPath()+"/Login.html" );
				return;
			}
		}	
}
