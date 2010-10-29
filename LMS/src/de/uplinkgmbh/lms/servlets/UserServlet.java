package de.uplinkgmbh.lms.servlets;


import java.io.File;
import java.io.IOException;
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

import de.axone.tools.E;
import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.WebTemplate;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.WebTemplateFactory;
import de.axone.webtemplate.element.HtmlInput;
import de.axone.webtemplate.list.DefaultPager;
import de.axone.webtemplate.list.ListProvider;
import de.uplinkgmbh.lms.business.DBList;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Organisation;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.servlets.forms.UserForm;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.webtemplate.Context;
import de.uplinkgmbh.lms.webtemplate.action.ActionList;
import de.uplinkgmbh.lms.webtemplate.groups.GroupList;
import de.uplinkgmbh.lms.webtemplate.role.RoleList;
import de.uplinkgmbh.lms.webtemplate.user.UserList;


/**
 * Servlet implementation class for Servlet: TestServlet
 *
 */
 public class UserServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	//private static Log log = LogFactory.getLog( TestSite.class );
	private Context context = Context.getSingelton();
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public UserServlet() {
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
		
		response.setCharacterEncoding( "UTF-8" );
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
			
			String templatePath = context.getServletContext().getRealPath( "/template/UserPage.xhtml" );
			File templateFile = new File( templatePath );
			
			WebTemplate template = context.getWebTemplateFactory().templateFor( templateFile );
			
			template.setParameter( "path", "User" );
			
			MyPersistenceManager pm = MyPersistenceManager.getInstance();
			EntityManager em = pm.getEntityManager();
			
			if( request.getParameter( "action" ) != null ){
				if( request.getParameter( "action" ).equals( "save") ){
					
					UserForm form = new UserForm();
					form.initialize( request );
					if( form.isValid() ){
						
						User user = null;
						if( ! request.getParameter( "user_id" ).equals( "" ) ){
							em.getTransaction().begin();	
							user = em.find( User.class, new Long( request.getParameter( "user_id" ) ) );
							em.getTransaction().commit();
							
							Organisation orga = null;
							if( request.getParameter( "organisation" ) != null ){
								em.getTransaction().begin();	
								orga = em.find( Organisation.class, new Long( request.getParameter( "organisation" ) ) );
								em.getTransaction().commit();
							}
							user.setOrganisation( orga );
							
							user.setLoginname( form.getLoginname() );
							user.setPassword( form.getPassword() );
							user.setFirstname( form.getFirstname() );
							user.setSurename( form.getSurename() );
							user.setZip( form.getZip() );
							user.setCity( form.getCity() );
							user.setActiv( form.getActiv() );
							user.setEmail( form.getEmail() );
							user.setCountry( new Locale( form.getCountry().toLowerCase(), form.getCountry().toUpperCase() ) );
							user.setFax( form.getFax() );
							user.setTemplate( form.getTemplate() );
							user.setLanguage( new Locale( form.getLanguage().toLowerCase() ) );
							user.setMobile( form.getMobile() );
							user.setPhonepriv( form.getPhonepriv() );
							user.setPhonework( form.getPhonework() );
							user.setState( form.getState() );
							user.setStreet( form.getStreet() );
							user.setStreetnr( form.getStreetnr() );
							user.setWashstore( form.getWashstore() );
							
							em.getTransaction().begin();	
							em.merge( user );
							em.getTransaction().commit();
							
							if( request.getParameter( "organisation" ).equals( "-2" ) ){
								HashMap<String, String> parameters = new HashMap<String,String>();
								parameters.put( "user_id", ""+user.getId() );
								parameters.put( "action", "new" );
								String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
								listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
								response.sendRedirect( listpage );
								return;
							}else{
								HashMap<String, String> parameters = new HashMap<String,String>();
								parameters.put( "user_id", ""+user.getId() );
								parameters.put( "action", "show" );
								String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
								listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
								response.sendRedirect( listpage );
								return;
							}
							
						}else{
							user = null;

							em.getTransaction().begin();	
							Query q = em.createNamedQuery( "UserFetchByLoginname" );
							q.setParameter( "loginname", form.getLoginname() );
							try{
								user = (User)q.getSingleResult();
							}catch( NoResultException e ){}
							em.getTransaction().commit();
							
							
							if( user != null ){
								form.getHtmlInput( "loginname" ).setValid( false );
								String userEdit = context.getServletContext().getRealPath( "/template/useredit.xhtml" );
								File userEditFile = new File( userEdit );
								WebTemplate userEditTemp = context.getWebTemplateFactory().templateFor( userEditFile );
							
								userEditTemp.setParameter( "userid", "" );
								userEditTemp.setParameter( "organisation", form.getOrganisationHtmlSelectElement( request.getParameter( "organisation" ) ) );
								userEditTemp.setParameter( "activ", form.getHtmlInput( "activ" ) );
								userEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
								userEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
								userEditTemp.setParameter( "email", form.getHtmlInput( "email" ) );
								userEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
								userEditTemp.setParameter( "firstname", form.getHtmlInput( "firstname" ) );
								userEditTemp.setParameter( "language", form.getHtmlInput( "language" ) );
								userEditTemp.setParameter( "loginname", form.getHtmlInput( "loginname" ) );
								userEditTemp.setParameter( "mobile", form.getHtmlInput( "mobile" ) );
								userEditTemp.setParameter( "password", form.getHtmlInput( "password" ) );
								userEditTemp.setParameter( "phonepriv", form.getHtmlInput( "phonepriv" ) );
								userEditTemp.setParameter( "phonework", form.getHtmlInput( "phonework" ) );
								userEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
								userEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
								userEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
								userEditTemp.setParameter( "surename", form.getHtmlInput( "surename" ) );
								userEditTemp.setParameter( "template", form.getHtmlInput( "template" ) );
								userEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
								userEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
								template.setParameter( "user", userEditTemp );
								
							}else{
							
								user = new User();
								
								Organisation orga = null;
								if( request.getParameter( "organisation" ) != null ){
									em.getTransaction().begin();	
									orga = em.find( Organisation.class, new Long( request.getParameter( "organisation" ) ) );
									em.getTransaction().commit();
								}
								user.setOrganisation( orga );
								
								user.setLoginname( form.getLoginname() );
								user.setPassword( form.getPassword() );
								user.setFirstname( form.getFirstname() );
								user.setSurename( form.getSurename() );
								user.setZip( form.getZip() );
								user.setCity( form.getCity() );
								user.setActiv( form.getActiv() );
								user.setEmail( form.getEmail() );
								user.setCountry( new Locale( form.getCountry() ) );
								user.setFax( form.getFax() );
								user.setTemplate( form.getTemplate() );
								user.setLanguage( new Locale( form.getLanguage() ) );
								user.setMobile( form.getMobile() );
								user.setPhonepriv( form.getPhonepriv() );
								user.setPhonework( form.getPhonework() );
								user.setState( form.getState() );
								user.setStreet( form.getStreet() );
								user.setStreetnr( form.getStreetnr() );
								user.setWashstore( form.getWashstore() );
								
								em.getTransaction().begin();	
								em.persist( user );
								em.getTransaction().commit();
								
								if( request.getParameter( "organisation" ).equals( "-2" ) ){
									HashMap<String, String> parameters = new HashMap<String,String>();
									parameters.put( "user_id", ""+user.getId() );
									parameters.put( "action", "new" );
									String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
									listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Organisation.html" );
									response.sendRedirect( listpage );
									return;
								}else{
									HashMap<String, String> parameters = new HashMap<String,String>();
									parameters.put( "user_id", ""+user.getId() );
									parameters.put( "action", "show" );
									String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
									listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
									response.sendRedirect( listpage );
									return;
								}
							}
						}
						
					// wenn nicht valide
					}else{
						String userEdit = context.getServletContext().getRealPath( "/template/useredit.xhtml" );
						File userEditFile = new File( userEdit );
						WebTemplate userEditTemp = context.getWebTemplateFactory().templateFor( userEditFile );
						if( request.getParameter( "user_id" ) != null ){
							userEditTemp.setParameter( "userid", request.getParameter( "user_id" ) );
						}else{
							userEditTemp.setParameter( "userid", "" );
						}
						
						String message = "";
						for( String s : form.validate() ){
							
							if( s.contains( "VALIDATOR_LENGTH_MISSMATCH" ) ) message = message.concat( "length missmatch " );
							else if( s.contains( "VALIDATOR_NO_LOCALE" ) ) message = message.concat( "Not a Locale (us,de,...) " );
							else if( s.contains( "VALIDATOR_NO_COUNTRY" ) ) message = message.concat( "Not a Country (us,de,...) " );
							else if( s.contains( "VALIDATOR_NO_EMAIL" ) ) message = message.concat( "Not a Email " );
							else if( s.contains( "VALIDATOR_IS_EMPTY" ) ) message = message.concat( "Empty field " );
						}
						
						form.isValid();
	
						userEditTemp.setParameter( "organisation", form.getOrganisationHtmlSelectElement( request.getParameter( "organisation" ) ) );
						userEditTemp.setParameter( "activ", form.getHtmlInput( "activ" ) );
						userEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
						userEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
						userEditTemp.setParameter( "email", form.getHtmlInput( "email" ) );
						userEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
						userEditTemp.setParameter( "firstname", form.getHtmlInput( "firstname" ) );
						userEditTemp.setParameter( "language", form.getHtmlInput( "language" ) );
						userEditTemp.setParameter( "loginname", form.getHtmlInput( "loginname" ) );
						userEditTemp.setParameter( "mobile", form.getHtmlInput( "mobile" ) );
						userEditTemp.setParameter( "password", form.getHtmlInput( "password" ) );
						userEditTemp.setParameter( "phonepriv", form.getHtmlInput( "phonepriv" ) );
						userEditTemp.setParameter( "phonework", form.getHtmlInput( "phonework" ) );
						userEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
						userEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
						userEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
						userEditTemp.setParameter( "surename", form.getHtmlInput( "surename" ) );
						userEditTemp.setParameter( "template", form.getHtmlInput( "template" ) );
						userEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
						userEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
						template.setParameter( "user", userEditTemp );
						
						template.setParameter( "message", message );
						template.setParameter( "path", "User &gt; save" );
						
					}
				}else if( request.getParameter( "action" ).equals( "saveorga") ){
					User user = null;
					em.getTransaction().begin();	
					user = em.find( User.class, new Long( request.getParameter( "user_id" ) ) );
					em.getTransaction().commit();
					
					Organisation orga = null;
					if( request.getParameter( "organisation_id" ) != null ){
						em.getTransaction().begin();	
						orga = em.find( Organisation.class, new Long( request.getParameter( "organisation_id" ) ) );
						em.getTransaction().commit();
					}
					user.setOrganisation( orga );
					
					em.getTransaction().begin();	
					em.merge( user );
					em.getTransaction().commit();
					
					HashMap<String, String> parameters = new HashMap<String,String>();
					parameters.put( "user_id", ""+user.getId() );
					parameters.put( "action", "show" );
					String listpage = HttpLinkBuilder.makeLink( request, true, true, parameters );
					listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "User.html" );
					response.sendRedirect( listpage );
					return;
					
				}else if( request.getParameter( "action" ).equals( "new") ){
					
					String userEdit = context.getServletContext().getRealPath( "/template/useredit.xhtml" );
					File userEditFile = new File( userEdit );
					WebTemplate userEditTemp = context.getWebTemplateFactory().templateFor( userEditFile );
				
					UserForm form = new UserForm();
					userEditTemp.setParameter( "userid", "" );
					
					userEditTemp.setParameter( "organisation", form.getOrganisationHtmlSelectElement( ) );
					HtmlInput htmlActiv = form.getHtmlInput( "activ" );
					htmlActiv.setValue( "0" );
					userEditTemp.setParameter( "activ", htmlActiv );
					userEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
					userEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
					userEditTemp.setParameter( "email", form.getHtmlInput( "email" ) );
					userEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
					userEditTemp.setParameter( "firstname", form.getHtmlInput( "firstname" ) );
					userEditTemp.setParameter( "language", form.getHtmlInput( "language" ) );
					userEditTemp.setParameter( "loginname", form.getHtmlInput( "loginname" ) );
					userEditTemp.setParameter( "mobile", form.getHtmlInput( "mobile" ) );
					userEditTemp.setParameter( "password", form.getHtmlInput( "password" ) );
					userEditTemp.setParameter( "phonepriv", form.getHtmlInput( "phonepriv" ) );
					userEditTemp.setParameter( "phonework", form.getHtmlInput( "phonework" ) );
					userEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
					userEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
					userEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
					userEditTemp.setParameter( "surename", form.getHtmlInput( "surename" ) );
					HtmlInput htmlTemplate = form.getHtmlInput( "template" );
					htmlTemplate.setValue( "0" );
					userEditTemp.setParameter( "template", htmlTemplate );
					userEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
					userEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
					
					template.setParameter( "user", userEditTemp );
					template.setParameter( "path", "User &gt; new" );
					
				}else if( request.getParameter( "action" ).equals( "show") ){
					
					String userShow = context.getServletContext().getRealPath( "/template/usershow.xhtml" );
					File userShowFile = new File( userShow );
					WebTemplate userShowTemp = context.getWebTemplateFactory().templateFor( userShowFile );
					if( request.getParameter( "user_id" ) != null ){
						
						User user = null;
						em.getTransaction().begin();	
						user = em.find( User.class, new Long( request.getParameter( "user_id" ) ) );
						em.getTransaction().commit();
						
						userShowTemp.setParameter( "id", user.getId() );
						if( user.getOrganisation() != null )
							userShowTemp.setParameter( "organisation", user.getOrganisation().getName() );
						else
							userShowTemp.setParameter( "organisation", "--" );
						if( user.isActiv() )
							userShowTemp.setParameter( "activ", "True" );
						else
							userShowTemp.setParameter( "activ", "False" );
						
						userShowTemp.setParameter( "city", user.getCity() );
						if( user.getCountry() != null )
							userShowTemp.setParameter( "country", user.getCountry().getCountry() );
						else
							userShowTemp.setParameter( "country", "" );
						userShowTemp.setParameter( "email", user.getEmail() );
						userShowTemp.setParameter( "fax", user.getFax() );
						userShowTemp.setParameter( "firstname", user.getFirstname() );
						if( user.getLanguage() != null )
							userShowTemp.setParameter( "language", user.getLanguage().getLanguage() );
						else
							userShowTemp.setParameter( "language", "" );
						userShowTemp.setParameter( "loginname", user.getLoginname() );
						userShowTemp.setParameter( "mobile", user.getMobile() );
						userShowTemp.setParameter( "password", user.getPassword() );
						userShowTemp.setParameter( "phonepriv", user.getPhonepriv() );
						userShowTemp.setParameter( "phonework", user.getPhonework() );
						userShowTemp.setParameter( "state", user.getState() );
						userShowTemp.setParameter( "street", user.getStreet() );
						userShowTemp.setParameter( "streetnr", user.getStreetnr() );
						userShowTemp.setParameter( "surename", user.getSurename() );
						if( user.isTemplate() )
							userShowTemp.setParameter( "template", "True" );
						else
							userShowTemp.setParameter( "template", "False" );
						userShowTemp.setParameter( "washstore", user.getWashstore() );
						userShowTemp.setParameter( "zip", user.getZip() );
						
						// groups lists
						String usergroupsList1 = context.getServletContext().getRealPath( "/template/UserGroupListItem.xhtml" );
						File usergroupsList1File = new File( usergroupsList1 );
						WebTemplate userGroupsList1Temp = context.getWebTemplateFactory().templateFor( usergroupsList1File );

						DefaultPager GroupsPager = new DefaultPager();
						GroupsListProvider gp = new GroupsListProvider( user.getId() );
						GroupList gl = new GroupList( request, "usergroupslist", 10, gp, userGroupsList1Temp );
						gl.initPager( GroupsPager );
						
						template.setParameter( "grouplist", gl );
						template.setParameter( "grouplistpager", GroupsPager );
						
						// role liste
						String userRoleList = context.getServletContext().getRealPath( "/template/UserRoleListItem.xhtml" );
						File userRoleListFile = new File( userRoleList );
						WebTemplate userRoleListTemp = context.getWebTemplateFactory().templateFor( userRoleListFile );
						
						DefaultPager UserRolePager = new DefaultPager();
						RoleListProvider rp = new RoleListProvider( user.getId() );
						RoleList rl = new RoleList( request, "userrolelist", 10, rp, userRoleListTemp );
						rl.initPager( UserRolePager );
						
						template.setParameter( "rolelist", rl );
						template.setParameter( "rolelistpager", UserRolePager );
						
						// action liste
						String actionList = context.getServletContext().getRealPath( "/template/PlainActionListItem.xhtml" );
						File actionListFile = new File( actionList );
						WebTemplate actionListTemp = context.getWebTemplateFactory().templateFor( actionListFile );
						
						DefaultPager ActionPager = new DefaultPager();
						em.getTransaction().begin();
						Query q = em.createNamedQuery( "RoleFetchByUserId" );
						q.setParameter( "userId", user.getId() );
						List<Role> rlist = q.getResultList();
						em.getTransaction().commit();
						LinkedList<Action> alist = new LinkedList<Action>();
						for( Role r : rlist ){
							for( Action a : r.getActionList() ){
								alist.add( a );
							}
						}
						ActionListProvider ap = new ActionListProvider( alist );
						ActionList al = new ActionList( request, "actionlist", 50, ap, actionListTemp );
						al.initPager( ActionPager );
						
						template.setParameter( "actionlist", al );
						template.setParameter( "actionlistpager", ActionPager );
					}
					template.setParameter( "path", "User &gt; show" );
					template.setParameter( "user", userShowTemp );

				}else if( request.getParameter( "action" ).equals( "delete") ){
					
					User user = null;
					em.getTransaction().begin();	
					user = em.find( User.class, new Long( request.getParameter( "user_id" ) ) );
					em.remove( user );
					em.getTransaction().commit();
					
					response.sendRedirect( request.getContextPath()+request.getServletPath() );
					return;
					
				}else if( request.getParameter( "action" ).equals( "edit") ){
			
					String userEdit = context.getServletContext().getRealPath( "/template/useredit.xhtml" );
					File userEditFile = new File( userEdit );
					WebTemplate userEditTemp = context.getWebTemplateFactory().templateFor( userEditFile );
					if( request.getParameter( "user_id" ) != null ){
					
						User user = null;
						em.getTransaction().begin();	
						user = em.find( User.class, new Long( request.getParameter( "user_id" ) ) );
						em.getTransaction().commit();
						
						UserForm form = new UserForm();
						
						form.setActiv( user.isActiv() );
						form.setCity( user.getCity() );
						if( user.getCountry() != null )
							form.setCountry( user.getCountry().getCountry().toUpperCase() );
						else
							form.setCountry( "" );
						form.setEmail( user.getEmail() );
						form.setFax( user.getFax() );
						form.setFirstname( user.getFirstname() );
						if( user.getLanguage() != null )
							form.setLanguage( user.getLanguage().getLanguage() );
						else
							form.setLanguage( "" );
						form.setLoginname( user.getLoginname() );
						form.setMobile( user.getMobile() );
						form.setPassword( user.getPassword() );
						form.setPhonepriv( user.getPhonepriv() );
						form.setPhonework( user.getPhonework() );
						form.setState( user.getState() );
						form.setStreet( user.getStreet() );
						form.setStreetnr( user.getStreetnr() );
						form.setSurename( user.getSurename() );
						form.setTemplate( user.isTemplate() );
						form.setWashstore( user.getWashstore() );
						form.setZip( user.getZip() );
						
						if( user.getOrganisation() != null )
							userEditTemp.setParameter( "organisation", form.getOrganisationHtmlSelectElement( String.valueOf( user.getOrganisation().getId() ) ) );
						else
							userEditTemp.setParameter( "organisation", form.getOrganisationHtmlSelectElement( ) );
						userEditTemp.setParameter( "activ", form.getHtmlInput( "activ" ) );
						userEditTemp.setParameter( "city", form.getHtmlInput( "city" ) );
						userEditTemp.setParameter( "country", form.getHtmlInput( "country" ) );
						userEditTemp.setParameter( "email", form.getHtmlInput( "email" ) );
						userEditTemp.setParameter( "fax", form.getHtmlInput( "fax" ) );
						userEditTemp.setParameter( "firstname", form.getHtmlInput( "firstname" ) );
						userEditTemp.setParameter( "language", form.getHtmlInput( "language" ) );
						userEditTemp.setParameter( "loginname", form.getHtmlInput( "loginname" ) );
						userEditTemp.setParameter( "mobile", form.getHtmlInput( "mobile" ) );
						userEditTemp.setParameter( "password", form.getHtmlInput( "password" ) );
						userEditTemp.setParameter( "phonepriv", form.getHtmlInput( "phonepriv" ) );
						userEditTemp.setParameter( "phonework", form.getHtmlInput( "phonework" ) );
						userEditTemp.setParameter( "state", form.getHtmlInput( "state" ) );
						userEditTemp.setParameter( "street", form.getHtmlInput( "street" ) );
						userEditTemp.setParameter( "streetnr", form.getHtmlInput( "streetnr" ) );
						userEditTemp.setParameter( "surename", form.getHtmlInput( "surename" ) );
						userEditTemp.setParameter( "template", form.getHtmlInput( "template" ) );
						userEditTemp.setParameter( "washstore", form.getHtmlInput( "washstore" ) );
						userEditTemp.setParameter( "zip", form.getHtmlInput( "zip" ) );
						userEditTemp.setParameter( "userid", user.getId() );
					}
					template.setParameter( "path", "User &gt; edit" );
					template.setParameter( "user", userEditTemp );
				}
				
			}
			em.clear();
			
			String userList = context.getServletContext().getRealPath( "/template/UserListItem.xhtml" );
			File userListFile = new File( userList );
			WebTemplate userListTemp = context.getWebTemplateFactory().templateFor( userListFile );
			
			DefaultPager UserPager = new DefaultPager();
			UserListProvider up = new UserListProvider( );
			UserList ul = new UserList( request, "userlist", 10, up, userListTemp );
			ul.initPager( UserPager );
			
			template.setParameter( "userlist", ul );
			template.setParameter( "userlistpager", UserPager );
			
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
	
	private static class ActionListProvider implements ListProvider<Action> {
		
		private List<Action> list;
		
		public ActionListProvider( List<Action> list ){
			this.list = list;
		}

		@Override
		public Iterable<Action> getList(int beginIndex, int count,
				String sort) {
			
			int end = 0;
			if( beginIndex+count >= list.size() ) end = list.size();
			else end = beginIndex+count;
			return (Iterable<Action>) list.subList(beginIndex, end);
		}

		@Override
		public int getTotalCount() {
			return list.size();
		}
		
	}
	
	private static class UserListProvider implements ListProvider<User> {
		
		private List<User> list = new LinkedList<User>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		
		public UserListProvider( ){
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "AllUserCount" );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<User> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllUser" );
			query.setFirstResult( beginIndex );
			query.setMaxResults( count );
			list = query.getResultList();
			em.getTransaction().commit();
	
			return (Iterable<User>) list;
		}

		@Override
		public int getTotalCount() {
			return maxResults.intValue();
		}
		
	}
	
	private static class GroupsListProvider implements ListProvider<Groups> {
		
		private List<Groups> list = new LinkedList<Groups>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		private long userId;
		
		public GroupsListProvider( long userId ){
			this.userId = userId;
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "AllGroupsByUserIdCount" );
			countQuery.setParameter( "userId", userId );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<Groups> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllGroupsByUserId" );
			query.setParameter( "userId", userId );
			query.setFirstResult( beginIndex );
			query.setMaxResults( count );
			list = query.getResultList();
			em.getTransaction().commit();
	
			return (Iterable<Groups>) list;
		}

		@Override
		public int getTotalCount() {
			return maxResults.intValue();
		}
		
	}
	
	private static class RoleListProvider implements ListProvider<Role> {
		
		private List<Role> list = new LinkedList<Role>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		private long userId;
		
	public RoleListProvider( long userId ){
		this.userId = userId;
		em.getTransaction().begin();
		countQuery = em.createNamedQuery( "RoleFetchByUserIdCount" );
		countQuery.setParameter( "userId", userId );
		maxResults = (Long) countQuery.getSingleResult();
		em.getTransaction().commit();
	}

	@Override
	public Iterable<Role> getList(int beginIndex, int count,
			String sort) {
		
		em.getTransaction().begin();
		query = em.createNamedQuery( "RoleFetchByUserId" );
		query.setParameter( "userId", userId );
		query.setFirstResult( beginIndex );
		query.setMaxResults( count );
		list = query.getResultList();
		em.getTransaction().commit();

		return (Iterable<Role>) list;
	}

	@Override
	public int getTotalCount() {
		return maxResults.intValue();
	}
	
}
}