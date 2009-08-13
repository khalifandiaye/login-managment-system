package de.uplinkgmbh.lms.servlets;


import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.WebTemplate;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.WebTemplateFactory;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.elements.impl.HtmlInputElement;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.axone.webtemplate.list.DefaultPager;
import de.axone.webtemplate.list.ListProvider;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.UserStatus;
import de.uplinkgmbh.lms.webtemplate.Context;
import de.uplinkgmbh.lms.webtemplate.application.ApplicationList;


/**
 * Servlet implementation class for Servlet: TestServlet
 *
 */
 public class ApplicationServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	//private static Log log = LogFactory.getLog( TestSite.class );
	private Context context = Context.getSingelton();
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ApplicationServlet() {
		super();
		
	}   	
	
	public void init( javax.servlet.ServletConfig config ) throws ServletException {
		
		super.init( config );
		
		context.setServletContext( this.getServletContext() );
		context.setWebTemplateFactory( new WebTemplateFactory() );
	}
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding( "utf-8" );
		response.setContentType( "text/html;charset=utf-8" );
		
		LMSToken token = (LMSToken)request.getSession().getAttribute( "token" );
		if( token == null ){
			response.sendRedirect( request.getContextPath()+"/" );
			return;
		}
		
		UserStatus userstatus = null;
		
		if( AuthorizationsChecker.isAllowed( token, "SYSTEMADMIN", "DOALL", "Application" ) ) userstatus = UserStatus.SYSTEMADMIN;
		else if( AuthorizationsChecker.isAllowed( token, "APPADMIN", "SHOW", "Application" ) ) userstatus = UserStatus.APPADMIN;
		else{
			response.sendRedirect( request.getContextPath()+"/" );
			return;
		}
		try {
			
			String templatePath = context.getServletContext().getRealPath( "/template/ApplicationPage.xhtml" );
			File templateFile = new File( templatePath );
			
			WebTemplate template = context.getWebTemplateFactory().templateFor( templateFile );
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
			
			template.setParameter( "path", "Application" );
			
			if( request.getParameter( "application_id" ) != null  ){
				
				de.uplinkgmbh.lms.entitys.Application app = null;
				
				if( !request.getParameter( "application_id" ).equals( "" ) ){
					em.getTransaction().begin();	
					app = em.find( de.uplinkgmbh.lms.entitys.Application.class, new Long( request.getParameter( "application_id" ) ) );
					em.getTransaction().commit();
					
					if( ! AuthorizationsChecker.isAllowed( token, "ADMIN", "DOALL", app.getName() ) ){
						response.sendRedirect( request.getContextPath()+"/Login" );
						return;
					}
				}

				if( request.getParameter( "action" ) != null ){
					if( request.getParameter( "action" ).equals( "save") && userstatus == UserStatus.SYSTEMADMIN ){
						
						ApplicationEditForm form = new ApplicationEditForm();
						form.initialize( request );
						if( form.isValid() ){
							
							if( app == null ){
								app = new Application();
								app.setName( request.getParameter( "name" ) );
								em.getTransaction().begin();	
								em.persist( app );
								em.getTransaction().commit();
							}else{
								app.setName( request.getParameter( "name" ) );
								em.getTransaction().begin();	
								em.merge( app );
								em.getTransaction().commit();
							}
							em.getTransaction().begin();
							
							Query query = em.createNamedQuery( "ApplicationFetchByName" );
							query.setParameter( "name", context.getApplicationname() );
							List<Application>  alist = query.getResultList();
							
							Application mainApp = alist.get( 0 );
							Role r = new Role();
							r.setName( app.getName() );
							r.setSort( 1 );
							em.persist( r );
							r.setApplication( mainApp );
							
							Action a = new Action();
							a.setAction( "DOALL" );
							a.setName( "admin" );
							a.setRule( "ACCEPT" );
							a.setSort( 0 );
							a.setState( "ADMIN" );
							a.setTarget( app.getName() );
							em.persist( a );
							
							r.getActionList().add( a );
							a.setRole( r );

							mainApp.getRoleList().add( r );
							em.merge( mainApp );
							em.getTransaction().commit();
							
							response.sendRedirect( request.getContextPath()+request.getServletPath()+"?action=show&application_id="+app.getId() );
							return;
						}else{
							String appEdit = context.getServletContext().getRealPath( "/template/appedit.xhtml" );
							File appEditFile = new File( appEdit );
							WebTemplate appEditTemp = context.getWebTemplateFactory().templateFor( appEditFile );
							if( app != null )
								appEditTemp.setParameter( "id", app.getId() );
							else
								appEditTemp.setParameter( "id", "" );
							appEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
							template.setParameter( "app", appEditTemp );
							template.setParameter( "path", "Application  &gt; edit" );
						}
					
					}else if( request.getParameter( "action" ).equals( "show") ){
							
						String appShow = context.getServletContext().getRealPath( "/template/appshow.xhtml" );
						File appShowFile = new File( appShow );
						WebTemplate appShowTemp = context.getWebTemplateFactory().templateFor( appShowFile );
						appShowTemp.setParameter( "id", app.getId() );
						appShowTemp.setParameter( "name", app.getName() );
						appShowTemp.setParameter( "userstatus", userstatus );
						template.setParameter( "app", appShowTemp );
						template.setParameter( "path", "Application  &gt; show" );
			
					}else if( request.getParameter( "action" ).equals( "edit") && userstatus == UserStatus.SYSTEMADMIN ){
				
						ApplicationEditForm form = new ApplicationEditForm();
						form.setName( app.getName() );
						
						String appEdit = context.getServletContext().getRealPath( "/template/appedit.xhtml" );
						File appEditFile = new File( appEdit );
						WebTemplate appEditTemp = context.getWebTemplateFactory().templateFor( appEditFile );
						appEditTemp.setParameter( "id", app.getId() );
						appEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
						template.setParameter( "app", appEditTemp );
						template.setParameter( "path", "Application  &gt; edit" );
					}
				}
				
			}else if( request.getParameter( "action" ) != null ){
				if( request.getParameter( "action" ).equals( "new") ){
				
					ApplicationEditForm form = new ApplicationEditForm();
					String appEdit = context.getServletContext().getRealPath( "/template/appedit.xhtml" );
					File appEditFile = new File( appEdit );
					WebTemplate appEditTemp = context.getWebTemplateFactory().templateFor( appEditFile );
					appEditTemp.setParameter( "id", "" );
					appEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
					template.setParameter( "app", appEditTemp );
					template.setParameter( "path", "Application  &gt; new" );
				}
			}
			em.clear();
			
			String Applicationitem = context.getServletContext().getRealPath( "/template/ApplicationListItem.xhtml" );
			File ApplicationitemFile = new File( Applicationitem );
			WebTemplate ApplicationitemTemp = context.getWebTemplateFactory().templateFor( ApplicationitemFile );
			DefaultPager Applicationpager = new DefaultPager();
			ApplicationListProvider ap = new ApplicationListProvider( token );
			ApplicationList al = new ApplicationList( request, "applicationlist", 10, ap, ApplicationitemTemp );
			al.initPager( Applicationpager );
			
			template.setParameter( "userstatus", userstatus );
			
			template.setParameter( "applist", al );
			template.setParameter( "applistpager", Applicationpager );

			template.render( token, request, response, null );
			
		} catch (WebTemplateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet( request, response );
	} 

	private static final String NAME = "name";
	
	public static class ApplicationEditForm extends WebFormImpl {
		
		private FormValue<String> name;
		
		public ApplicationEditForm() throws WebTemplateException {
			
			name = HtmlInputElement.createTextValue( NAME, 255, false );
			this.addFormValue( NAME, name );
		}
		
		public String getName() throws ConverterException{
			return name.getValue();
		}
		
		public void setName( String name ) throws ConverterException{
			this.name.setValue( name );
		}
	
	}
	
	private static class ApplicationListProvider implements ListProvider<Application> {
		
		private List<Application> list = new LinkedList<Application>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query query = null;
		private LMSToken token;
		
		@SuppressWarnings("unchecked")
		public ApplicationListProvider( LMSToken token ){
			this.token = token;
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllApplication" );
			List<Application>  ilist = query.getResultList();
			em.getTransaction().commit();
			for( Application a : ilist ) {
				
				if( AuthorizationsChecker.isAllowed( token, "ADMIN", "DOALL", a.getName() ) ){
					list.add( a );
				}
			}
		}

		@Override
		public Iterable<Application> getList(int beginIndex, int count,
				String sort) {
		
			int end = 0;
			if( beginIndex+count >= list.size() ) end = list.size();
			else end = beginIndex+count;
			return (Iterable<Application>) list.subList( beginIndex, end);
		}

		@Override
		public int getTotalCount() {
			return list.size();
		}
		
	}
}