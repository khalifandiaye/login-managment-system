package de.uplinkgmbh.lms.servlets;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.web.HttpLinkBuilder;
import de.axone.webtemplate.WebTemplate;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.WebTemplateFactory;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.axone.webtemplate.list.DefaultPager;
import de.axone.webtemplate.list.ListProvider;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.user.AuthorizationsChecker;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.UserStatus;
import de.uplinkgmbh.lms.webtemplate.Context;
import de.uplinkgmbh.lms.webtemplate.FVFactory;
import de.uplinkgmbh.lms.webtemplate.groups.GroupList;
import de.uplinkgmbh.lms.webtemplate.role.RoleList;
import de.uplinkgmbh.lms.webtemplate.user.UserList;


/**
 * Servlet implementation class for Servlet: TestServlet
 *
 */
 public class GroupsServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	//private static Log log = LogFactory.getLog( TestSite.class );
	private Context context = Context.getSingelton();
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public GroupsServlet() {
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
		@SuppressWarnings("unused")
		UserStatus userstatus = null;
		
		if( AuthorizationsChecker.isAllowed( token, "SYSTEMADMIN", "DOALL", "Application" ) ) userstatus = UserStatus.SYSTEMADMIN;
		else if( AuthorizationsChecker.isAllowed( token, "APPADMIN", "SHOW", "Application" ) ) userstatus = UserStatus.APPADMIN;
		else{
			response.sendRedirect( request.getContextPath()+"/" );
			return;
		}
		
		
		try {
			
			String templatePath = context.getServletContext().getRealPath( "/template/GroupPage.xhtml" );
			File templateFile = new File( templatePath );
			
			WebTemplate template = context.getWebTemplateFactory().templateFor( templateFile );
			template.setParameter( "path", "Groups" );
			
			template.setParameter( "userstatus", userstatus );
			
			
			if( request.getParameter( "application_id" ) != null  ){
				
				template.setParameter( "appid", request.getParameter( "application_id" ) );
				
				de.uplinkgmbh.lms.entitys.Application app = null;
				MyPersistenceManager pm = MyPersistenceManager.getInstance();
				EntityManager em = pm.getEntityManager();

				em.getTransaction().begin();	
				app = em.find( de.uplinkgmbh.lms.entitys.Application.class, new Long( request.getParameter( "application_id" ) ) );
				em.getTransaction().commit();
				
				template.setParameter( "path", app.getName()+" - Group" );
				
				if( ! AuthorizationsChecker.isAllowed( token, "ADMIN", "DOALL", app.getName() ) ){
					response.sendRedirect( request.getContextPath()+"/Login.html" );
					return;
				}
				
				if( request.getSession().getAttribute( "group1_appid" ) == null ){
					request.getSession().setAttribute( "group1_appid", app.getId() );
				}else{
					if( ((Long)request.getSession().getAttribute( "group1_appid" )).longValue() != ((Long)app.getId()).longValue() ){

						request.getSession().setAttribute( "group1_appid", app.getId() );
					}
				}
				
				if( request.getParameter( "group_id" ) != null && !request.getParameter( "group_id" ).equals( "" ) ){
					long groupId = new Long( request.getParameter( "group_id" ) );
					if( request.getSession().getAttribute( "role2_groupid" ) == null ){
						request.getSession().setAttribute( "role2_groupid", groupId );
					}else{
						if( ((Long)request.getSession().getAttribute( "role2_groupid" )).longValue() != groupId ){

							request.getSession().setAttribute( "role2_groupid", groupId );
						}
					}
					
				}

				if( request.getParameter( "action" ) != null ){
					if( request.getParameter( "action" ).equals( "save") ){
						
						GroupsEditForm form = new GroupsEditForm();
						form.initialize( request );
						if( form.isValid() ){
							Groups g = new Groups();
							if( request.getParameter( "group_id" ).equals( "" ) ){
								g.setApplication( app );
								g.setName( request.getParameter( "name" ) );
								em.getTransaction().begin();	
								em.persist( g );
								em.getTransaction().commit();
							}else{
								em.getTransaction().begin();	
								g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
								em.getTransaction().commit();
								g.setName( request.getParameter( "name" ) );
								em.getTransaction().begin();	
								em.merge( g );
								em.getTransaction().commit();
							}
							
							HashMap<String, String> parameters = new HashMap<String,String>();
							parameters.put( "group_id", ""+g.getId() );
							parameters.put( "application_id", ""+app.getId() );
							parameters.put( "action", "show" );
							String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
							listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
							response.sendRedirect( listpage );
							return;
						}else{
							String groupsEdit = context.getServletContext().getRealPath( "/template/groupsedit.xhtml" );
							File groupsEditFile = new File( groupsEdit );
							WebTemplate groupsEditTemp = context.getWebTemplateFactory().templateFor( groupsEditFile );
							groupsEditTemp.setParameter( "appid", app.getId() );
							groupsEditTemp.setParameter( "groupid", request.getParameter( "group_id" ) );
							groupsEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
							template.setParameter( "group", groupsEditTemp );
							template.setParameter( "path", app.getName()+" - Group &gt; edit" );
						}
						
					}else if( request.getParameter( "action" ).equals( "show" ) ){
							
						String groupsShow = context.getServletContext().getRealPath( "/template/groupsshow.xhtml" );
						File groupsShowFile = new File( groupsShow );
						WebTemplate groupsShowTemp = context.getWebTemplateFactory().templateFor( groupsShowFile );
						groupsShowTemp.setParameter( "appid", app.getId() );
						groupsShowTemp.setParameter( "groupid", request.getParameter( "group_id" ) );
						Groups g = null;
						em.getTransaction().begin();	
						g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
						em.getTransaction().commit();
						groupsShowTemp.setParameter( "name", g.getName() );
						
						template.setParameter( "group", groupsShowTemp );
						
						String roleList = context.getServletContext().getRealPath( "/template/GroupRoleListItem.xhtml" );
						File roleListFile = new File( roleList );
						WebTemplate roleListTemp = context.getWebTemplateFactory().templateFor( roleListFile );
						DefaultPager Rolepager = new DefaultPager();
						RoleListProvider rp = new RoleListProvider( app.getName(), g.getId() );
						RoleList rl = new RoleList( request, "rolelist", 10, rp, roleListTemp );
						rl.initPager( Rolepager );
						
						template.setParameter( "rolelist", rl );
						template.setParameter( "rolelistpager", Rolepager );
						
						// user list
						String groupsuserList1 = context.getServletContext().getRealPath( "/template/GroupUserListItem.xhtml" );
						File groupsuserList1File = new File( groupsuserList1 );
						WebTemplate groupsuserList1Temp = context.getWebTemplateFactory().templateFor( groupsuserList1File );
						
						groupsuserList1Temp.setParameter( "groupId", g.getId() );
						groupsuserList1Temp.setParameter( "applicationId", app.getId() );
						groupsuserList1Temp.setParameter( "usertype", "deactivateuser" );
						
						DefaultPager UserPager = new DefaultPager();
						UserListProvider up = new UserListProvider( g.getId() );
						UserList ul = new UserList( request, "groupuserlist1", 10, up, groupsuserList1Temp );
						ul.initPager( UserPager );
						
						template.setParameter( "userlist", ul );
						template.setParameter( "userlistpager", UserPager );
						
						// deactive user liste
						String groupsuserList2 = context.getServletContext().getRealPath( "/template/GroupUserListItem.xhtml" );
						File groupsuserList2File = new File( groupsuserList2 );
						WebTemplate groupsuserList2Temp = context.getWebTemplateFactory().templateFor( groupsuserList2File );
						
						groupsuserList2Temp.setParameter( "groupId", g.getId() );
						groupsuserList2Temp.setParameter( "applicationId", app.getId() );
						groupsuserList2Temp.setParameter( "usertype", "activateuser" );
						
						DefaultPager UserPager1 = new DefaultPager();
						User2ListProvider up1 = new User2ListProvider();
						UserList ul1 = new UserList( request, "groupuserlist2", 10, up1, groupsuserList2Temp );
						ul1.initPager( UserPager1 );
						
						template.setParameter( "userlistdeactiv", ul1 );
						template.setParameter( "userlistdeactivpager", UserPager1 );
				
						template.setParameter( "path", app.getName()+" - Group &gt; show" );
						
					}else if( request.getParameter( "action" ).equals( "activateuser" ) ){
						
						Groups g = null;
						em.getTransaction().begin();	
						
						g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
						
						User u = null;	
						u = em.find( de.uplinkgmbh.lms.entitys.User.class, new Long( request.getParameter( "user_id" ) ) );
						
						em.getTransaction().commit();
						
						g.getUserList().add( u );
						
						em.getTransaction().begin();
						em.merge( g );
						em.getTransaction().commit();
						
						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "user_id", ""+u.getId() );
						parameters.put( "group_id", ""+g.getId() );
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "show" );
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
						response.sendRedirect( listpage );
						return;
					}else if( request.getParameter( "action" ).equals( "deactivateuser" ) ){
						
						Groups g = null;
						em.getTransaction().begin();	
						
						g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
						
						User u = null;	
						u = em.find( de.uplinkgmbh.lms.entitys.User.class, new Long( request.getParameter( "user_id" ) ) );
						
						em.getTransaction().commit();
						
						g.getUserList().remove( u );
						
						em.getTransaction().begin();
						em.merge( g );
						em.getTransaction().commit();
						
						HashMap<String, String> parameters = new HashMap<String,String>();
						parameters.put( "user_id", ""+u.getId() );
						parameters.put( "group_id", ""+g.getId() );
						parameters.put( "application_id", ""+app.getId() );
						parameters.put( "action", "show" );
						String listpage = HttpLinkBuilder.makeLink( request, true, false, parameters );
						listpage = listpage.replaceFirst( "[a-zA-Z_0-9]*\\.html", "Groups.html" );
						response.sendRedirect( listpage );
						return;
					}else if( request.getParameter( "action" ).equals( "delete" ) ){
						
						Groups g = null;
						em.getTransaction().begin();	
						g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
						em.getTransaction().commit();
						if( g.getRoleList() != null ) g.getRoleList().clear();
						if( g.getUserList() != null ) g.getUserList().clear();
						em.getTransaction().begin();
						em.remove( g );
						em.getTransaction().commit();
						
					}else if( request.getParameter( "action" ).equals( "edit" ) ){
						
						GroupsEditForm form = new GroupsEditForm();
						// wenn ohne group id dann neue group
						if( request.getParameter( "group_id" ) == null ){
							String groupsEdit = context.getServletContext().getRealPath( "/template/groupsedit.xhtml" );
							File groupsEditFile = new File( groupsEdit );
							WebTemplate groupsEditTemp = context.getWebTemplateFactory().templateFor( groupsEditFile );
							groupsEditTemp.setParameter( "appid", app.getId() );
							groupsEditTemp.setParameter( "groupid", "" );
							groupsEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
							template.setParameter( "group", groupsEditTemp );
							template.setParameter( "path", app.getName()+" - Group &gt; edit" );
						// wenn mit group id
						}else{
							String groupsEdit = context.getServletContext().getRealPath( "/template/groupsedit.xhtml" );
							File groupsEditFile = new File( groupsEdit );
							WebTemplate groupsEditTemp = context.getWebTemplateFactory().templateFor( groupsEditFile );
							groupsEditTemp.setParameter( "appid", app.getId() );
							Groups g = null;
							em.getTransaction().begin();	
							g = em.find( de.uplinkgmbh.lms.entitys.Groups.class, new Long( request.getParameter( "group_id" ) ) );
							em.getTransaction().commit();
							form.setGroupsName( g.getName() );
							groupsEditTemp.setParameter( "groupid", g.getId() );
							groupsEditTemp.setParameter( "name", form.getHtmlInput( "name" ) );
							template.setParameter( "group", groupsEditTemp );
							template.setParameter( "path", app.getName()+" - Group &gt; edit" );
						}
					}
					em.clear();
				}
				
				
				String groupsList = context.getServletContext().getRealPath( "/template/GroupListItem.xhtml" );
				File groupsListFile = new File( groupsList );
				WebTemplate groupsListTemp = context.getWebTemplateFactory().templateFor( groupsListFile );
				
				DefaultPager GroupPager = new DefaultPager();
				GroupListProvider gp = new GroupListProvider( app.getId() );
				GroupList gl = new GroupList( request, "grouplist2", 10, gp, groupsListTemp );
				gl.initPager( GroupPager );
				
				template.setParameter( "grouplist", gl );
				template.setParameter( "grouplistpager", GroupPager );
				
				
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
	
	public static class GroupsEditForm extends WebFormImpl {
		
		private FormValue<String> name;
		
		public GroupsEditForm() throws WebTemplateException {
			
			name = new FVFactory().createInputTextValue( NAME, 255, false );
			this.addFormValue( NAME, name );
		}
		
		public String getGroupsName() throws ConverterException{
			return name.getValue();
		}
		
		public void setGroupsName( String name ) throws ConverterException{
			this.name.setValue( name );
		}
	
	}
	
	private static class GroupListProvider implements ListProvider<Groups> {
		
		private List<Groups> list = new LinkedList<Groups>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		private long appId;
		
		public GroupListProvider( long appId ){
			this.appId = appId;
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "GroupFetchByApplicationIdCount" );
			countQuery.setParameter( "appId", appId );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<Groups> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "GroupFetchByApplicationId" );
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
		
	}

	
	private static class RoleListProvider implements ListProvider<Role> {
		
		private List<Role> list = new LinkedList<Role>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;
		private String appname;
		private long groupId;
		
		public RoleListProvider( String appname, long groupId ){
			
			this.appname = appname;
			this.groupId = groupId;
			
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "RoleFetchByAppnameAndGroupsCount" );
			countQuery.setParameter( "appname", appname);
			countQuery.setParameter( "groupId", groupId );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<Role> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "RoleFetchByAppnameAndGroups" );
			query.setParameter( "appname", appname);
			query.setParameter( "groupId", groupId );
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
	
	private static class UserListProvider implements ListProvider<User> {
		
		private List<User> list = new LinkedList<User>();
		private MyPersistenceManager pm = MyPersistenceManager.getInstance();
		private EntityManager em = pm.getEntityManager();
		private Query countQuery = null;
		private Query query = null;
		private Long maxResults = 0L;

		private long groupId;
		
		public UserListProvider( long groupId ){
			this.groupId = groupId;
			em.getTransaction().begin();
			countQuery = em.createNamedQuery( "AllUserByGroupIdCount" );
			countQuery.setParameter( "groupId", groupId );
			maxResults = (Long) countQuery.getSingleResult();
			em.getTransaction().commit();
		}

		@Override
		public Iterable<User> getList(int beginIndex, int count,
				String sort) {
			
			em.getTransaction().begin();
			query = em.createNamedQuery( "AllUserByGroupId" );
			query.setParameter( "groupId", groupId );
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
		
	}
	

}