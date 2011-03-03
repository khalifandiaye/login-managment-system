package de.uplinkgmbh.lms.servlets;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.WebTemplate;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.WebTemplateFactory;
import de.axone.webtemplate.list.DefaultPager;
import de.axone.webtemplate.list.ListProvider;
import de.uplinkgmbh.lms.business.STATICS;
import de.uplinkgmbh.lms.entitys.Organisation;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.servlets.forms.OrganisationForm;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.webtemplate.Context;
import de.uplinkgmbh.lms.webtemplate.organisation.OrganisationList;


/**
 * Servlet implementation class for Servlet: TestServlet
 *
 */
 public class OrganisationServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Context context = Context.getSingelton();
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public OrganisationServlet() {
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
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding( "utf-8" );
		response.setContentType( "text/html;charset=utf-8" );
		
		
		LMSToken token = (LMSToken)request.getSession().getAttribute( "token" );
		if( token == null ){
			response.sendRedirect( request.getContextPath()+"/" );
			return;
		}
		if( AuthorizationsChecker.isAllowed( token, "SYSTEMADMIN", "DOALL", "User" ) ){
			
		}else{
			response.sendRedirect( request.getContextPath()+"/" );
			return;
		}

		try {
			
			String templatePath = context.getServletContext().getRealPath( "/template/OrganisationPage.xhtml" );
			File templateFile = new File( templatePath );
			
			WebTemplate template = context.getWebTemplateFactory().templateFor( templateFile );
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
			try{
			template.setParameter( "path", "Organisation" );
			
			if( request.getParameter( "action" ) != null ){
				if( request.getParameter( "action" ).equals( "save") ){
					
					OrganisationForm form = new OrganisationForm();
					form.initialize( request );
					if( form.isValid() ){
						
						Organisation orga = null;
						// ohne organisation_id
						if( ! request.getParameter( "organisation_id" ).equals( "" ) ){
							
							em.getTransaction().begin();	
							orga = em.find( Organisation.class, new Long( request.getParameter( "organisation_id" ) ) );
							em.getTransaction().commit();
							
							orga.setName( form.getOrgaName() );
							if( form.getUrl() == "" )
								orga.setUrl( null );
							else
								orga.setUrl( new URL( form.getUrl() ) );
							orga.setZip( form.getZip() );
							orga.setCity( form.getCity() );
							orga.setCountry( new Locale( form.getCountry().toLowerCase(), form.getCountry().toUpperCase() ) );
							orga.setFax( form.getFax() );
							orga.setPhone( form.getPhone() );
							orga.setState( form.getState() );
							orga.setStreet( form.getStreet() );
							orga.setStreetnr( form.getStreetnr() );
							orga.setWashstore( form.getWashstore() );
							
							em.getTransaction().begin();	
							em.merge( orga );
							em.getTransaction().commit();
							HashMap<String, String> parameters = new HashMap<String,String>();
							parameters.put( "organisation_id", ""+orga.getId() );
							parameters.put( "action", "show" );
							String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
							listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
							response.sendRedirect( listpage );
							
							return;
						
						// mit organisation_id
						}else{
							orga = null;
							em.getTransaction().begin();	
							Query q = em.createNamedQuery( "OrgaFetchByName" );
							q.setParameter( "name", form.getOrgaName() );
							try{
								orga = (Organisation)q.getSingleResult();
							}catch( NoResultException e ){}
							em.getTransaction().commit();
							
							if( orga != null ){
								form.getHtmlInput( "name" ).setValid( false );
								String orgaEdit = context.getServletContext().getRealPath( "/template/organisationedit.xhtml" );
								File orgaEditFile = new File( orgaEdit );
								WebTemplate orgaEditTemp = context.getWebTemplateFactory().templateFor( orgaEditFile );
							
								if( request.getParameter( "user_id" ) != null ){
									orgaEditTemp.setParameter( "userid", request.getParameter( "user_id" ) );
								}
								orgaEditTemp.setParameter( "orgaid", "" );
								orgaEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
								orgaEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
								orgaEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
								orgaEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
								orgaEditTemp.setParameter( "url", form.getHtmlInput( "url" ) );
								orgaEditTemp.setParameter( "phone", form.getHtmlInput( "phone" ) );
								orgaEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
								orgaEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
								orgaEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
								orgaEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
								orgaEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
								template.setParameter( "orga", orgaEditTemp );
								
							}else{
							
								orga = new Organisation();
								
								orga.setName( form.getOrgaName() );
								if( form.getUrl().equals( "" ) )
									orga.setUrl( null );
								else
									orga.setUrl( new URL( form.getUrl() ) );
								orga.setZip( form.getZip() );
								orga.setCity( form.getCity() );
								orga.setCountry( new Locale( form.getCountry() ) );
								orga.setFax( form.getFax() );
								orga.setPhone( form.getPhone() );
								orga.setState( form.getState() );
								orga.setStreet( form.getStreet() );
								orga.setStreetnr( form.getStreetnr() );
								orga.setWashstore( form.getWashstore() );
								em.getTransaction().begin();	
								em.persist( orga );
								em.getTransaction().commit();
								// wenn user_id bei save mitkommt dann zur�ck zu User
								if( request.getParameter( "user_id" ) != null ){
									HashMap<String, String> parameters = new HashMap<String,String>();
									parameters.put( "organisation_id", ""+orga.getId() );
									parameters.put( "user_id", request.getParameter( "user_id" ) );
									parameters.put( "action", "saveorga" );
									String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
									listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
									response.sendRedirect( listpage );
								
									return;
								}
								
								HashMap<String, String> parameters = new HashMap<String,String>();
								parameters.put( "organisation_id", ""+orga.getId() );
								parameters.put( "action", "show" );
								String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
								listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
								response.sendRedirect( listpage );
								return;	
							}
						}
						
					}else{
						
						String orgaEdit = context.getServletContext().getRealPath( "/template/organisationedit.xhtml" );
						File orgaEditFile = new File( orgaEdit );
						WebTemplate orgaEditTemp = context.getWebTemplateFactory().templateFor( orgaEditFile );
					
						if( request.getParameter( "organisation_id" ) != null ){
							orgaEditTemp.setParameter( "orgaid", request.getParameter( "organisation_id" ) );
						}else{
							orgaEditTemp.setParameter( "orgaid", "" );
						}
						
						if( request.getParameter( "user_id" ) != null ){
							orgaEditTemp.setParameter( "userid", request.getParameter( "user_id" ) );
						}
				
						orgaEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
						orgaEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
						orgaEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
						orgaEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
						orgaEditTemp.setParameter( "url", form.getHtmlInput( "url" ) );
						orgaEditTemp.setParameter( "phone", form.getHtmlInput( "phone" ) );
						orgaEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
						orgaEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
						orgaEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
						orgaEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
						orgaEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
						template.setParameter( "orga", orgaEditTemp );
						template.setParameter( "path", "Organisation &gt; save" );
					}
					
				}else if( request.getParameter( "action" ).equals( "new") ){
					
					if( request.getParameter( "user_id" ) == null ){
						String orgaEdit = context.getServletContext().getRealPath( "/template/organisationedit.xhtml" );
						File orgaEditFile = new File( orgaEdit );
						WebTemplate orgaEditTemp = context.getWebTemplateFactory().templateFor( orgaEditFile );
					
						OrganisationForm form = new OrganisationForm();
					
						orgaEditTemp.setParameter( "orgaid", "" );
						orgaEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
						orgaEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
						orgaEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
						orgaEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
						orgaEditTemp.setParameter( "url", form.getHtmlInput( "url" ) );
						orgaEditTemp.setParameter( "phone", form.getHtmlInput( "phone" ) );
						orgaEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
						orgaEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
						orgaEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
						orgaEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
						orgaEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
						template.setParameter( "orga", orgaEditTemp );
						
						template.setParameter( "path", "Organisation &gt; new" );
						
					// wenn User_id mitkommt dann kommt der request vom neuen user anlegen
					}else{
						String orgaEdit = context.getServletContext().getRealPath( "/template/organisationedit.xhtml" );
						File orgaEditFile = new File( orgaEdit );
						WebTemplate orgaEditTemp = context.getWebTemplateFactory().templateFor( orgaEditFile );
					
						OrganisationForm form = new OrganisationForm();
					
						orgaEditTemp.setParameter( "userid", request.getParameter( "user_id" ) );
						orgaEditTemp.setParameter( "orgaid", "" );
						orgaEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
						orgaEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
						orgaEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
						orgaEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
						orgaEditTemp.setParameter( "url", form.getHtmlInput( "url" ) );
						orgaEditTemp.setParameter( "phone", form.getHtmlInput( "phone" ) );
						orgaEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
						orgaEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
						orgaEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
						orgaEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
						orgaEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
						template.setParameter( "orga", orgaEditTemp );
						template.setParameter( "orgalist", "" );
						template.render( token, request, response, null );
						return;
					}
					
				}else if( request.getParameter( "action" ).equals( "show") ){
						
					String orgaShow = context.getServletContext().getRealPath( "/template/organisationshow.xhtml" );
					File orgaShowFile = new File( orgaShow );
					WebTemplate orgaShowTemp = context.getWebTemplateFactory().templateFor( orgaShowFile );
					if( request.getParameter( "organisation_id" ) != null ){
						
						Organisation orga = null;
						em.getTransaction().begin();	
						orga = em.find( Organisation.class, new Long( request.getParameter( "organisation_id" ) ) );
						em.getTransaction().commit();
						orgaShowTemp.setParameter( "id", orga.getId() );
						orgaShowTemp.setParameter( "city", orga.getCity() );
						orgaShowTemp.setParameter( "country", orga.getCountry().getCountry() );
						orgaShowTemp.setParameter( "fax", orga.getFax() );
						orgaShowTemp.setParameter( "name", orga.getName() );
						orgaShowTemp.setParameter( "url", orga.getUrl() );
						orgaShowTemp.setParameter( "phone", orga.getPhone() );
						orgaShowTemp.setParameter( "state", orga.getState() );
						orgaShowTemp.setParameter( "street", orga.getStreet() );
						orgaShowTemp.setParameter( "streetnr", orga.getStreetnr() );
						orgaShowTemp.setParameter( "washstore", orga.getWashstore() );
						orgaShowTemp.setParameter( "zip", orga.getZip() );
					}
					template.setParameter( "path", "Organisation &gt; show" );
					template.setParameter( "orga", orgaShowTemp );
					
				}else if( request.getParameter( "action" ).equals( "delete") ){
					
					Organisation orga = null;
					em.getTransaction().begin();	
					orga = em.find( Organisation.class, new Long( request.getParameter( "organisation_id" ) ) );
					em.remove( orga );
					em.getTransaction().commit();
					
					HashMap<String, String> parameters = new HashMap<String,String>();
					parameters.put( "action", "" );
					String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
					listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
					response.sendRedirect( listpage );
					return;	
					
				}else if( request.getParameter( "action" ).equals( "edit") ){
			
					String orgaEdit = context.getServletContext().getRealPath( "/template/organisationedit.xhtml" );
					File orgaEditFile = new File( orgaEdit );
					WebTemplate orgaEditTemp = context.getWebTemplateFactory().templateFor( orgaEditFile );
					if( request.getParameter( "organisation_id" ) != null ){
					
						Organisation orga = null;
						em.getTransaction().begin();	
						orga = em.find( Organisation.class, new Long( request.getParameter( "organisation_id" ) ) );
						em.getTransaction().commit();
						
						OrganisationForm form = new OrganisationForm();
					
						form.setCity( orga.getCity() );
						form.setCountry( orga.getCountry().getCountry().toUpperCase() );
						form.setFax( orga.getFax() );
						form.setOrgaName( orga.getName() );
						if( orga.getUrl() != null )
							form.setUrl( orga.getUrl().toString() );
						else
							form.setUrl( "" );
						form.setPhone( orga.getPhone() );
						form.setState( orga.getState() );
						form.setStreet( orga.getStreet() );
						form.setStreetnr( orga.getStreetnr() );
						form.setWashstore( orga.getWashstore() );
						form.setZip( orga.getZip() );
						
						orgaEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
						orgaEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
						orgaEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
						orgaEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
						orgaEditTemp.setParameter( "url", form.getHtmlInput( "url" ) );
						orgaEditTemp.setParameter( "phone", form.getHtmlInput( "phone" ) );
						orgaEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
						orgaEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
						orgaEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
						orgaEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
						orgaEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
						orgaEditTemp.setParameter( "orgaid", orga.getId() );
					}
					template.setParameter( "path", "Organisation &gt; edit" );
					template.setParameter( "orga", orgaEditTemp );
				}
			}
			
			User user=null;
			em.getTransaction().begin();	
			user = em.find( User.class, token.userId );
			em.getTransaction().commit();
			
			if( user != null && user.getLanguage() != null ) template.setParameter( "lang", user.getLanguage().getLanguage() );
			else template.setParameter( "lang", STATICS.SYSLANG );
			
			}finally{
				pm.closeEntityManager( em );
			}
			String orgaList = context.getServletContext().getRealPath( "/template/OrganisationListItem.xhtml" );
			File orgaListFile = new File( orgaList );
			WebTemplate orgaListTemp = context.getWebTemplateFactory().templateFor( orgaListFile );
			
			DefaultPager OrgaPager = new DefaultPager();
			OrganisationListProvider op = new OrganisationListProvider( );
			OrganisationList ol = new OrganisationList( request, "orgalist", 10, op, orgaListTemp );
			ol.initPager( OrgaPager );
	
			template.setParameter( "orgalist", ol );
			template.setParameter( "orgalistpager", OrgaPager );
			
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
	
	private static class OrganisationListProvider implements ListProvider<Organisation> {
		
		private List<Organisation> list = new LinkedList<Organisation>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = null;
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		
		public OrganisationListProvider( ){
			try{
			em = pm.getEntityManager();
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "AllOrganisationCount" );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
			}finally{
				pm.closeEntityManager( em );
			}
		}

		@Override
		public Iterable<Organisation> getList(int beginIndex, int count,
				String sort) {
			em = pm.getEntityManager();
			try{
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllOrganisation" );
			query.setFirstResult( beginIndex );
			query.setMaxResults( count );
			list = query.getResultList();
			em.getTransaction().commit();
			}finally{
				pm.closeEntityManager( em );
			}
			return (Iterable<Organisation>) list;
		}

		@Override
		public int getTotalCount() {
			return maxResults.intValue();
		}
		
	}
}