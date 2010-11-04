package de.uplinkgmbh.lms.servlets;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.Base64;

import de.axone.tools.E;
import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.WebTemplate;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.WebTemplateFactory;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.axone.webtemplate.list.DefaultPager;
import de.axone.webtemplate.list.DefaultSortSelector;
import de.axone.webtemplate.list.ListProvider;
import de.uplinkgmbh.lms.business.STATICS;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.servlets.forms.ActionForm;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.UserStatus;
import de.uplinkgmbh.lms.webtemplate.Context;
import de.uplinkgmbh.lms.webtemplate.FVFactory;
import de.uplinkgmbh.lms.webtemplate.action.ActionList;
import de.uplinkgmbh.lms.webtemplate.groups.GroupList;
import de.uplinkgmbh.lms.webtemplate.role.RoleList;
import de.uplinkgmbh.lms.webtemplate.user.UserList;


/**
 * Servlet implementation class for Servlet: TestServlet
 *
 */
 public class RoleServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Context context = Context.getSingelton();
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public RoleServlet() {
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
		
		UserStatus userstatus = null;
		
		if( AuthorizationsChecker.isAllowed( token, "SYSTEMADMIN", "DOALL", "Application" ) ) userstatus = UserStatus.SYSTEMADMIN;
		else if( AuthorizationsChecker.isAllowed( token, "APPADMIN", "SHOW", "Application" ) ) userstatus = UserStatus.APPADMIN;
		else{
			response.sendRedirect( request.getContextPath()+"/" );
			return;
		}
		
		try {
			
			String templatePath = context.getServletContext().getRealPath( "/template/RolePage.xhtml" );
			File templateFile = new File( templatePath );
			
			WebTemplate template = context.getWebTemplateFactory().templateFor( templateFile );
			
			template.setParameter( "path", "Role" );
			template.setParameter( "userstatus", userstatus );
			template.setParameter( "actionlist", "" );
			
			String head = "<tr><td><div><table><tr><td id=\"input_a_name_h\">Name</td><td id=\"input_a_sort_h\">Sort</td><td id=\"input_a_state_h\">State</td><td id=\"input_a_action_h\">Action</td><td id=\"input_a_target_h\">Target</td><td id=\"input_a_rule_h\">Rule</td><td></td><td></td></tr></table></div></td></tr>";

			if( request.getParameter( "application_id" ) != null  ){
				
				de.uplinkgmbh.lms.entitys.Application app = null;
				MyPersistenceManager pm = MyPersistenceManager.getInstance();
				EntityManager em = pm.getEntityManager();

				em.getTransaction().begin();	
				app = em.find( de.uplinkgmbh.lms.entitys.Application.class, new Long( request.getParameter( "application_id" ) ) );
				em.getTransaction().commit();
				
				template.setParameter( "path", app.getName()+" - Role" );
				
				if( ! AuthorizationsChecker.isAllowed( token, "ADMIN", "DOALL", app.getName() ) ){
					response.sendRedirect( request.getContextPath()+"/Login.html" );
					return;
				}
				
				if( request.getSession().getAttribute( "role1_appid" ) == null ){
					request.getSession().setAttribute( "role1_appid", app.getId() );
				}else{
					if( ((Long)request.getSession().getAttribute( "role1_appid" )).longValue() != ((Long)app.getId()).longValue() ){

						request.getSession().setAttribute( "role1_appid", app.getId() );
					
					}
				}
				
				// wenn eine action von der Action liste kommt.
				if( request.getParameter( "actionaction" ) != null ){
					if( request.getParameter( "actionaction" ).equals( "save") ){
						
						// ohne action id ist eine neue action
						if( request.getParameter( "action_id" ).equals( "" ) ){
							
							ActionForm aform = new ActionForm();
							aform.initialize( request );
							if( aform.isValid() ){
								Action ac = new Action();
								Role r = null;
								em.getTransaction().begin();	
								r = em.find( de.uplinkgmbh.lms.entitys.Role.class, new Long( request.getParameter( "role_id" ) ) );
								em.getTransaction().commit();
								
								// zur sicherheit ob die �bergebene role id auch zur erlaubten app geh�rt
								if( ! r.getApplication().getName().equals( app.getName() ) ){
									response.sendRedirect( request.getContextPath()+"/" );
									return;
								}
								
								ac.setRole( r );
								ac.setName( aform.getActionName() );
								ac.setAction( aform.getAction() );
								ac.setRule( aform.getRule() );
								ac.setSort( aform.getSort() );
								ac.setState( aform.getState() );
								ac.setTarget( aform.getTarget() );
								
								em.getTransaction().begin();	
								em.persist( ac );
								em.getTransaction().commit();
								
								HashMap<String, String> parameters = new HashMap<String,String>();
								parameters.put( "role_id", ""+r.getId() );
								parameters.put( "application_id", ""+app.getId() );
								parameters.put( "action", "show" );
								parameters.put( "actionaction", "" );
								String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
								listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
								response.sendRedirect( listpage );
								
								return;
								
							}else{
								
								String roleShow = context.getServletContext().getRealPath( "/template/roleshow.xhtml" );
								File roleShowFile = new File( roleShow );
								WebTemplate roleShowTemp = context.getWebTemplateFactory().templateFor( roleShowFile );
								
								Role r = new Role();
								em.getTransaction().begin();	
								r = em.find( Role.class, new Long (request.getParameter( "role_id" ) ) );
								em.getTransaction().commit();
								
								roleShowTemp.setParameter( "id", r.getId() );
								roleShowTemp.setParameter( "name", r.getName() );
								roleShowTemp.setParameter( "sort", r.getSort() );
								roleShowTemp.setParameter( "appid", app.getId() );
								
								template.setParameter( "role", roleShowTemp );
								
								String Actionitem = context.getServletContext().getRealPath( "/template/ActionListItem.xhtml" );
								File ActionitemFile = new File( Actionitem );
								WebTemplate ActionitemTemp = context.getWebTemplateFactory().templateFor( ActionitemFile );
								
								ActionitemTemp.setParameter( "appid", app.getId() );
								
								List<Action> alist = r.getActionList();
								
								DefaultPager Actionpager = new DefaultPager();
								DefaultSortSelector ActionSortSelector = new DefaultSortSelector();
								ActionListProvider ap = new ActionListProvider( alist );
								ActionList al = new ActionList( request, "actionlist", 10, ap, ActionitemTemp );
								al.initPager( Actionpager );
								al.initSortSelector( ActionSortSelector );
								
								template.setParameter( "actionlist", al );
								template.setParameter( "actionlistpager", Actionpager );
								
								String actionListItem2 = context.getServletContext().getRealPath( "/template/ActionListItem.xhtml" );
								File actionListFile2 = new File( actionListItem2 );
								WebTemplate actionListTemp2 = context.getWebTemplateFactory().templateFor( actionListFile2 );
								actionListTemp2.setParameter( "appid", request.getParameter( "application_id" ) );
								actionListTemp2.setParameter( "roleid", r.getId());
								actionListTemp2.setParameter( "actionform", aform );
							
								template.setParameter( "actionlisthead", head );
								template.setParameter( "emptyaction", actionListTemp2 );
							}
							
						}else{
							
							// wenn del mit kommt dann zu action=delete weiter
							if( request.getParameter( "del" ) != null ){
								
								HashMap<String, String> parameters = new HashMap<String,String>();
								parameters.put( "role_id", ""+request.getParameter( "role_id" ) );
								parameters.put( "application_id", ""+app.getId() );
								parameters.put( "action_id", request.getParameter( "action_id" ) );
								parameters.put( "actionaction", "delete" );
								String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
								String target = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
								
								parameters = new HashMap<String,String>();
								parameters.put( "role_id", ""+request.getParameter( "role_id" ) );
								parameters.put( "application_id", ""+app.getId() );
								parameters.put( "action", "show" );
								parameters.put( "actionaction", "" );
								listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
								String source = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
								
								String target64 = Base64.encode( target );
								String source64 = Base64.encode( source );
					
								
								String url = "Warning.html?target="+
									target64+
									"&source="+
									source64+
									"&info=delete+action";
								E.rr( response.encodeRedirectURL( url ) );
								response.sendRedirect( response.encodeRedirectURL( url ) );
								return;
								
							// normales save mit action_id	
							}else{
						
								ActionForm aform = new ActionForm();
								aform.initialize( request );
								if( aform.isValid() ){
									Action ac = null;
									em.getTransaction().begin();	
									ac = em.find( de.uplinkgmbh.lms.entitys.Action.class, new Long( request.getParameter( "action_id" ) ) );
									em.getTransaction().commit();
									
									ac.setName( aform.getActionName() );
									ac.setAction( aform.getAction() );
									ac.setRule( aform.getRule() );
									ac.setSort( aform.getSort() );
									ac.setState( aform.getState() );
									ac.setTarget( aform.getTarget() );
									
									em.getTransaction().begin();	
									em.merge( ac );
									em.getTransaction().commit();
									
									HashMap<String, String> parameters = new HashMap<String,String>();
									parameters.put( "role_id", request.getParameter( "role_id" ) );
									parameters.put( "application_id", ""+app.getId() );
									parameters.put( "action", "show" );
									parameters.put( "actionaction", "" );
									String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
									listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
									
									response.sendRedirect( listpage );
									return;
									
								// alte action save aber nicht valide
								}else{
									String roleShow = context.getServletContext().getRealPath( "/template/roleshow.xhtml" );
									File roleShowFile = new File( roleShow );
									WebTemplate roleShowTemp = context.getWebTemplateFactory().templateFor( roleShowFile );
									
									Role r = new Role();
									em.getTransaction().begin();	
									r = em.find( Role.class, new Long (request.getParameter( "role_id" ) ) );
									em.getTransaction().commit();
									
									roleShowTemp.setParameter( "id", r.getId() );
									roleShowTemp.setParameter( "name", r.getName() );
									roleShowTemp.setParameter( "sort", r.getSort() );
									roleShowTemp.setParameter( "appid", app.getId() );
									
									template.setParameter( "role", roleShowTemp );
									
									//request.getParameter( "action_id" )
									
									String Actionitem = context.getServletContext().getRealPath( "/template/ActionListItem.xhtml" );
									File ActionitemFile = new File( Actionitem );
									WebTemplate ActionitemTemp = context.getWebTemplateFactory().templateFor( ActionitemFile );
									
									ActionitemTemp.setParameter( "appid", app.getId() );
									
									List<Action> alist = r.getActionList();
									
									for( Action a : alist ){
										if( a.getId() == new Long( request.getParameter( "action_id" ) ).longValue() ){
											a.setAction( request.getParameter( "a_action" ) );
											a.setName( request.getParameter( "a_name" ));
											a.setRule( request.getParameter( "a_rule" ) );
											Long sort = null;
											try{
												sort = new Long( request.getParameter( "a_sort" ) );
											}catch( NumberFormatException e ){
												sort = null;
											}
											if( sort == null ) a.setSort( 0 );
											else a.setSort( sort );
											a.setState( request.getParameter( "a_state" ) );
											a.setTarget( request.getParameter( "a_target" ) );
										}
									}
									
									DefaultPager Actionpager = new DefaultPager();
									ActionListProvider ap = new ActionListProvider( alist );
									ActionList al = new ActionList( request, "actionlist", 10, ap, ActionitemTemp );
									al.initPager( Actionpager );
									
									template.setParameter( "actionlisthead", head );
									template.setParameter( "actionlist", al );
									template.setParameter( "actionlistpager", Actionpager );
								}	
							}
						}
						template.setParameter( "path", app.getName()+" - Role &gt; save" );
						
					}else if( request.getParameter( "actionaction" ).equals( "delete") ){
						
						Role r = null;
						em.getTransaction().begin();	
						r = em.find( Role.class, new Long (request.getParameter( "role_id" ) ) );
						em.getTransaction().commit();
						Action ac = null;
						em.getTransaction().begin();	
						ac = em.find( Action.class, new Long (request.getParameter( "action_id" ) ) );
						em.getTransaction().commit();
					
						r.getActionList().remove( ac );
						
						em.getTransaction().begin();
						em.remove( ac );
						em.getTransaction().commit();
						
						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "role_id", request.getParameter( "role_id" ) );
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "show" );
						parameters.put( "actionaction", "" );
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
						
						response.sendRedirect( listpage );
						
						return;
					}
				}

				if( request.getParameter( "action" ) != null ){
					if( request.getParameter( "action" ).equals( "save") ){
						
						RoleEditForm form = new RoleEditForm();
						form.initialize( request );
						if( form.isValid() ){
							
							Role r = new Role();
							if( request.getParameter( "role_id" ).equals( "" ) ){
								r.setName( form.getRoleName() );
								r.setSort( form.getSort() );
								r.setApplication( app );
								
								em.getTransaction().begin();	
								em.persist( r );
								em.getTransaction().commit();
							}else{
								em.getTransaction().begin();	
								r = em.find( Role.class, new Long( request.getParameter( "role_id" ) ) );
								em.getTransaction().commit();
								r.setName( form.getRoleName() );
								r.setSort( form.getSort() );
								
								em.getTransaction().begin();	
								em.merge( r );
								em.getTransaction().commit();
							}
							
							HashMap<String, String> parameters = new HashMap<String,String>();
							parameters.put( "role_id", ""+r.getId() );
							parameters.put( "application_id", ""+app.getId() );
							parameters.put( "action", "show" );;
							String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
							listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
							
							response.sendRedirect( listpage );
	
							return;
							
						// not valid
						}else{
							
							String roleEdit = context.getServletContext().getRealPath( "/template/roleedit.xhtml" );
							File roleEditFile = new File( roleEdit );
							WebTemplate roleEditTemp = context.getWebTemplateFactory().templateFor( roleEditFile );
							roleEditTemp.setParameter( "appid", app.getId() );
							roleEditTemp.setParameter( "roleid", request.getParameter( "role_id" ) );
							roleEditTemp.setParameter( "userid", token.userId );
							roleEditTemp.setParameter( "action", "save" );
							roleEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
							roleEditTemp.setParameter( "sort", form.getHtmlInput( "sort" ) );
							
							template.setParameter( "role", roleEditTemp );
						}
						
						template.setParameter( "path", app.getName()+" - Role &gt; save" );
						
					}else if( request.getParameter( "action" ).equals( "show") ){
							
						String roleShow = context.getServletContext().getRealPath( "/template/roleshow.xhtml" );
						File roleShowFile = new File( roleShow );
						WebTemplate roleShowTemp = context.getWebTemplateFactory().templateFor( roleShowFile );
						
						Role r = new Role();
						em.getTransaction().begin();	
						r = em.find( Role.class, new Long (request.getParameter( "role_id" ) ) );
						em.getTransaction().commit();
						
						roleShowTemp.setParameter( "id", r.getId() );
						roleShowTemp.setParameter( "name", r.getName() );
						roleShowTemp.setParameter( "sort", r.getSort() );
						roleShowTemp.setParameter( "appid", app.getId() );
						
						String actionListItem = context.getServletContext().getRealPath( "/template/ActionListItem.xhtml" );
						File actionListFile = new File( actionListItem );
						WebTemplate actionListTemp = context.getWebTemplateFactory().templateFor( actionListFile );
						actionListTemp.setParameter( "appid", request.getParameter( "application_id" ) );
						
						DefaultPager ActionPager = new DefaultPager();
						ActionListProvider ap = new ActionListProvider( r.getActionList() );
						ActionList al = new ActionList( request, "actionlist", 10, ap, actionListTemp );
						al.initPager( ActionPager );
						
						template.setParameter( "actionlisthead", head );
						
						template.setParameter( "actionlist", al );
						template.setParameter( "actionlistpager", ActionPager );
						
						template.setParameter( "role", roleShowTemp );
						
						String actionListItem2 = context.getServletContext().getRealPath( "/template/ActionListItem.xhtml" );
						File actionListFile2 = new File( actionListItem2 );
						WebTemplate actionListTemp2 = context.getWebTemplateFactory().templateFor( actionListFile2 );
						actionListTemp2.setParameter( "appid", request.getParameter( "application_id" ) );
						actionListTemp2.setParameter( "roleid", r.getId());
					
						template.setParameter( "emptyaction", actionListTemp2 );
						
						// user list
						String roleuserList1 = context.getServletContext().getRealPath( "/template/RoleUserListItem.xhtml" );
						File roleuserList1File = new File( roleuserList1 );
						WebTemplate roleuserList1Temp = context.getWebTemplateFactory().templateFor( roleuserList1File );
						
						roleuserList1Temp.setParameter( "roleId", r.getId() );
						roleuserList1Temp.setParameter( "applicationId", app.getId() );
						roleuserList1Temp.setParameter( "usertype", "deactivateuser" );
						
						DefaultPager UserPager = new DefaultPager();
						UserListProvider up = new UserListProvider( r.getId() );
						UserList ul = new UserList( request, "roleuserlist1", 10, up, roleuserList1Temp );
						ul.initPager( UserPager );
						
						template.setParameter( "userlist", ul );
						template.setParameter( "userlistpager", UserPager );
						
						// deactive user liste
						String roleuserList2 = context.getServletContext().getRealPath( "/template/RoleUserListItem.xhtml" );
						File roleuserList2File = new File( roleuserList2 );
						WebTemplate roleuserList2Temp = context.getWebTemplateFactory().templateFor( roleuserList2File );
						
						roleuserList2Temp.setParameter( "roleId", r.getId() );
						roleuserList2Temp.setParameter( "applicationId", app.getId() );
						roleuserList2Temp.setParameter( "usertype", "activateuser" );
						
						DefaultPager UserPager1 = new DefaultPager();
						User2ListProvider up1 = new User2ListProvider();
						UserList ul1 = new UserList( request, "roleuserlist2", 10, up1, roleuserList2Temp );
						ul1.initPager( UserPager1 );
						
						template.setParameter( "userlistdeactiv", ul1 );
						template.setParameter( "userlistdeactivpager", UserPager1 );
				
						// group list
						String groupList1 = context.getServletContext().getRealPath( "/template/RoleGroupListItem.xhtml" );
						File groupList1File = new File( groupList1 );
						WebTemplate groupList1Temp = context.getWebTemplateFactory().templateFor( groupList1File );
						
						groupList1Temp.setParameter( "roleId", r.getId() );
						groupList1Temp.setParameter( "applicationId", app.getId() );
						groupList1Temp.setParameter( "usertype", "deactivategroup" );
						
						DefaultPager GroupPager = new DefaultPager();
						GroupListProvider gp = new GroupListProvider( r.getId() );
						GroupList gl = new GroupList( request, "grouplist1", 10, gp, groupList1Temp );
						gl.initPager( GroupPager );
						
						template.setParameter( "groupslist", gl );
						template.setParameter( "groupslistpager", GroupPager );
						
						// deactive group liste
						String groupList2 = context.getServletContext().getRealPath( "/template/RoleGroupListItem.xhtml" );
						File groupList2File = new File( groupList2 );
						WebTemplate groupList2Temp = context.getWebTemplateFactory().templateFor( groupList2File );
						
						groupList2Temp.setParameter( "roleId", r.getId() );
						groupList2Temp.setParameter( "applicationId", app.getId() );
						groupList2Temp.setParameter( "usertype", "activategroup" );
						
						DefaultPager GroupPager1 = new DefaultPager();
						Group2ListProvider gp1 = new Group2ListProvider( app.getId() );
						GroupList gl1 = new GroupList( request, "grouplist2", 10, gp1, groupList2Temp );
						gl1.initPager( GroupPager1 );
						
						template.setParameter( "groupslistdeactiv", gl1 );
						template.setParameter( "groupslistdeactivpager", GroupPager1 );
						
						template.setParameter( "path", app.getName()+" - Role &gt; show" );
						
					}else if( request.getParameter( "action" ).equals( "activategroup" ) ){
						
						Role r = null;
						em.getTransaction().begin();	
						
						r = em.find( de.uplinkgmbh.lms.entitys.Role.class, new Long( request.getParameter( "role_id" ) ) );
						
						Groups g = null;	
						g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
						
						em.getTransaction().commit();
						
						r.getGroupList().add( g );
						
						em.getTransaction().begin();
						em.merge( r );
						em.getTransaction().commit();
						
						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "role_id", ""+request.getParameter( "role_id" ) );
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "show" );;
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
						
						response.sendRedirect( listpage );
						
						return;
					}else if( request.getParameter( "action" ).equals( "deactivategroup" ) ){
						
						Role r = null;
						em.getTransaction().begin();	
						
						r = em.find( de.uplinkgmbh.lms.entitys.Role.class, new Long( request.getParameter( "role_id" ) ) );
						
						Groups g = null;	
						g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
						
						em.getTransaction().commit();
						
						r.getGroupList().remove( g );
						
						em.getTransaction().begin();
						em.merge( r );
						em.getTransaction().commit();
						
						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "role_id", ""+request.getParameter( "role_id" ) );
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "show" );;
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
						
						response.sendRedirect( listpage );
						
						return;	
					}else if( request.getParameter( "action" ).equals( "activateuser" ) ){
						
						Role r = null;
						em.getTransaction().begin();	
						
						r = em.find( de.uplinkgmbh.lms.entitys.Role.class, new Long( request.getParameter( "role_id" ) ) );
						
						User u = null;	
						u = em.find( de.uplinkgmbh.lms.entitys.User.class, new Long( request.getParameter( "user_id" ) ) );
						
						em.getTransaction().commit();
						
						r.getUserList().add( u );
						
						em.getTransaction().begin();
						em.merge( r );
						em.getTransaction().commit();
						
						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "role_id", ""+request.getParameter( "role_id" ) );
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "show" );;
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
						
						response.sendRedirect( listpage );
						
						return;
					}else if( request.getParameter( "action" ).equals( "deactivateuser" ) ){
						
						Role r = null;
						em.getTransaction().begin();	
						
						r = em.find( de.uplinkgmbh.lms.entitys.Role.class, new Long( request.getParameter( "role_id" ) ) );
						
						User u = null;	
						u = em.find( de.uplinkgmbh.lms.entitys.User.class, new Long( request.getParameter( "user_id" ) ) );
						
						em.getTransaction().commit();
						
						r.getUserList().remove( u );
						
						em.getTransaction().begin();
						em.merge( r );
						em.getTransaction().commit();
						
						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "role_id", ""+request.getParameter( "role_id" ) );
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "show" );;
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
						
						response.sendRedirect( listpage );
						
						return;	
					
					}else if( request.getParameter( "action" ).equals( "edit") ){
				
						String roleEdit = context.getServletContext().getRealPath( "/template/roleedit.xhtml" );
						File roleEditFile = new File( roleEdit );
						WebTemplate roleEditTemp = context.getWebTemplateFactory().templateFor( roleEditFile );
						RoleEditForm form = new RoleEditForm();
						
						Role r = new Role();
						em.getTransaction().begin();	
						r = em.find( Role.class, new Long (request.getParameter( "role_id" ) ) );
						em.getTransaction().commit();
						
						form.setRoleName( r.getName() );
						form.setSort( ((Long)r.getSort()).intValue() );
						
						roleEditTemp.setParameter( "appid", app.getId() );
						roleEditTemp.setParameter( "roleid", r.getId() );
						roleEditTemp.setParameter( "userid", token.userId );
						roleEditTemp.setParameter( "action", "save" );
						roleEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
						roleEditTemp.setParameter( "sort", form.getHtmlInput( "sort" ) );
				
						template.setParameter( "role", roleEditTemp );
						
						template.setParameter( "path", app.getName()+" - Role &gt; edit" );
					
					}else if( request.getParameter( "action" ).equals( "new") ){
						
						RoleEditForm form = new RoleEditForm();
						String roleEdit = context.getServletContext().getRealPath( "/template/roleedit.xhtml" );
						File roleEditFile = new File( roleEdit );
						WebTemplate roleEditTemp = context.getWebTemplateFactory().templateFor( roleEditFile );
						roleEditTemp.setParameter( "appid", app.getId() );
						roleEditTemp.setParameter( "roleid", "" );
						roleEditTemp.setParameter( "action", "save" );
						roleEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
						roleEditTemp.setParameter( "sort", form.getHtmlInput( "sort" ) );
				
						template.setParameter( "role", roleEditTemp );
						
						template.setParameter( "path", app.getName()+" - Role &gt; new" );
						
					}else if( request.getParameter( "action" ).equals( "delete") ){

						Role r = new Role();
						em.getTransaction().begin();	
						r = em.find( Role.class, new Long (request.getParameter( "role_id" ) ) );
					
						em.remove( r );
						em.getTransaction().commit();

						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "" );
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Role.html" );
						
						response.sendRedirect( listpage );
					
						return;
					}
				}
				
				User user=null;
				em.getTransaction().begin();	
				user = em.find( User.class, token.userId );
				em.getTransaction().commit();
				
				if( user != null && user.getLanguage() != null ) template.setParameter( "lang", user.getLanguage().getLanguage() );
				else template.setParameter( "lang", STATICS.SYSLANG );
				
				em.clear();

				if( em.isOpen() ) em.close();

				
				String roleList = context.getServletContext().getRealPath( "/template/RoleListItem.xhtml" );
				File roleListFile = new File( roleList );
				WebTemplate RoleListTemp = context.getWebTemplateFactory().templateFor( roleListFile );
				
				DefaultPager RolePager = new DefaultPager();
				RoleListProvider rp = new RoleListProvider( app.getName() );
				RoleList rl = new RoleList( request, "rolelist", 10, rp, RoleListTemp );
				rl.initPager( RolePager );
				
				template.setParameter( "rolelist", rl );
				template.setParameter( "rolelistpager", RolePager );

				template.setParameter( "appname", app.getName() );
				template.setParameter( "appid", app.getId() );
				
				template.render( token, request, response, null );
			}
			
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
	private static final String SORT = "sort";
	
	public static class RoleEditForm extends WebFormImpl {
		
		private FormValue<String> name;
		private FormValue<Integer> sort;
		
		public RoleEditForm() throws WebTemplateException {
			
			name = new FVFactory().createInputTextValue( NAME, 255, false );
			this.addFormValue( NAME, name );
			
			sort = new FVFactory().createInputIntegerValue( SORT, Locale.GERMANY, false );
			this.addFormValue( SORT, sort );
		}
		
		public String getRoleName() throws ConverterException{
			return name.getValue();
		}
		
		public void setRoleName( String name ) throws ConverterException{
			this.name.setValue( name );
		}
	
		public Integer getSort() throws ConverterException{
			return sort.getValue();
		}
		
		public void setSort( Integer sort ) throws ConverterException{
			this.sort.setValue( sort );
		}
	}
	private static class ActionListProvider implements ListProvider<Action> {
		
		private List<Action> list;
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		
		public ActionListProvider( List actionList ){
			list = actionList;
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
		
		protected void finalize() throws Throwable {
			if( em.isOpen() ) em.clear(); em.close();
		}
	}
	
	private static class RoleListProvider implements ListProvider<Role> {
		
		private List<Role> list = new LinkedList<Role>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		private String appname;
		
		public RoleListProvider( String appname ){
			this.appname = appname;
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "RoleFetchByAppnameCount" );
			countQuery.setParameter( "appname", appname );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<Role> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "RoleFetchByAppname" );
			query.setParameter( "appname", appname );
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
		
		protected void finalize() throws Throwable {
			if( em.isOpen() ) em.clear(); em.close();
		}
	}
	
	private static class UserListProvider implements ListProvider<User> {
		
		private List<User> list = new LinkedList<User>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		private long roleId;
		
		public UserListProvider( long roleId ){
			this.roleId = roleId;
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "AllUserByRoleIdCount" );
			countQuery.setParameter( "roleId", roleId );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<User> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllUserByRoleId" );
			query.setParameter( "roleId", roleId );
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
		
		protected void finalize() throws Throwable {
			if( em.isOpen() ) em.clear(); em.close();
		}
	}
	
	private static class User2ListProvider implements ListProvider<User> {
		
		private List<User> list = new LinkedList<User>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		
		public User2ListProvider(){
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
		
		protected void finalize() throws Throwable {
			if( em.isOpen() ) em.clear(); em.close();
		}
	}
	
	private static class GroupListProvider implements ListProvider<Groups> {
		
		private List<Groups> list = new LinkedList<Groups>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		private long roleId;
		
		public GroupListProvider( long roleId ){
			this.roleId = roleId;
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "AllGroupsByRoleIdCount" );
			countQuery.setParameter( "roleId", roleId );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<Groups> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllGroupsByRoleId" );
			query.setParameter( "roleId", roleId );
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
		
		protected void finalize() throws Throwable {
			if( em.isOpen() ) em.clear(); em.close();
		}
	}
	
	private static class Group2ListProvider implements ListProvider<Groups> {
		
		private List<Groups> list = new LinkedList<Groups>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;
		private long appId;
		
		public Group2ListProvider( long appId ){
			this.appId = appId;
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "AllGroupsByApplicationIdCount" );
			countQuery.setParameter( "appId", appId );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<Groups> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllGroupsByApplicationId" );
			query.setParameter( "appId", appId );
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
		
		protected void finalize() throws Throwable {
			if( em.isOpen() ) em.clear(); em.close();
		}
	}
}