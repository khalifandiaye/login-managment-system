package de.uplinkgmbh.lms.servlets;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.tools.E;
import de.axone.tools.Str;
import de.axone.webtemplate.WebTemplate;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.WebTemplateFactory;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.uplinkgmbh.lms.exceptions.LoginException;
import de.uplinkgmbh.lms.utils.Tokenaizer;
import de.uplinkgmbh.lms.webtemplate.Context;
import de.uplinkgmbh.lms.webtemplate.FVFactory;

/**
 * Servlet implementation class for Servlet: Login
 *
 */
 public class LoginServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   private Context context = Context.getSingelton();
   
	public LoginServlet() {
		super();
	}
	
	public void init( javax.servlet.ServletConfig config ) throws ServletException {
		
		super.init( config );
		
		context.setServletContext( this.getServletContext() );
		context.setWebTemplateFactory( new WebTemplateFactory() );
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding( "utf-8" );
		response.setContentType( "text/html;charset=utf-8" );
		

		String templatePath = context.getServletContext().getRealPath( "/template/loginPage.xhtml" );
		File templateFile = new File( templatePath );
		
		// Instantiate empty form
		LoginForm form = null;
		try {
			form = new LoginForm();
		} catch (WebTemplateException e1) {
			e1.printStackTrace();
		}
		
		try {
			WebTemplate template = context.getWebTemplateFactory().templateFor( templateFile );
			
			form.initialize( request );
			
			if( form.isValid() ){
				
				String username = form.getUsername();
				String password = form.getPassword();
				
				E.rr( username, password );
				
				// DO WHATEVER NEEDS TO BE DONE
				
				de.uplinkgmbh.lms.user.Login login = new de.uplinkgmbh.lms.user.Login();
				try{
					login.check( username, password, context.getApplicationname() );
				}catch( LoginException lInE ){
					E.rr( lInE.status );
					if( lInE.status == LoginException.USERHASNOGROUPORROLE ){
						template.setParameter( "message", "USERHASNOGROUP" );
					}else if( lInE.status == LoginException.WRONGAPPLICATION ){
						template.setParameter( "message", "WRONGAPPLICATION" );
					}else if( lInE.status == LoginException.WRONGPASSWORD ){
						template.setParameter( "message", "WRONGPASSWORD" );
					}else if( lInE.status == LoginException.WRONGUSERNAME ){
						template.setParameter( "message", "WRONGUSERNAME" );
					}
					template.render( form, request, response, null );
					return;
				}
				String AESToken = login.logIn();
				request.getSession().setAttribute( "token", Tokenaizer.restoreLMSToken( AESToken.getBytes() ) );
				response.sendRedirect( "Application.html" );
				return;
			} else {
				
				List<String> failures = form.validate();
				template.setParameter( "message", Str.join( ", ", failures ) );
				
			}
			
			template.render( form, request, response, null );
			
		} catch (WebTemplateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet( request, response );
	} 
	
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	
	public static class LoginForm extends WebFormImpl {
		
		private FormValue<String> username;
		private FormValue<String> password;
		
		public LoginForm() throws WebTemplateException {
			
			username = new FVFactory().createInputTextValue( USERNAME, 255, false );
			this.addFormValue( USERNAME, username );
			
			password = new FVFactory().createInputPasswordValue( PASSWORD, 255, false );
			this.addFormValue( PASSWORD, password );

		}
		
		public String getUsername() throws ConverterException{
			return username.getValue();
		}
		public String getPassword() throws ConverterException{
			return password.getValue();
		}
		
		public void setUsername( String username ) throws ConverterException{
			this.username.setValue( username );
		}
		public void setPassword( String password ) throws ConverterException{
			this.password.setValue( password );
		}
	
	}
}