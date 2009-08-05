package de.uplinkgmbh.lms.entitys;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;


@Entity
@NamedQueries( {
    @NamedQuery( 
           name="RoleFetchByName",
           query=          
        	"SELECT r " +
   			"FROM Role r " +
   			"WHERE r.name = :name"
   ),
   @NamedQuery( 
           name="RoleCount",
           query=          
        	"SELECT count(r) " +
   			"FROM Role r "
   ),
   @NamedQuery( 
           name="RoleFetchByAppname",
           query=          
        	   "SELECT r " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"WHERE a.name = :appname " +
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByAppnameCount",
           query=          
        	   "SELECT count(r) " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"WHERE a.name = :appname " +
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByUserId",
           query=          
        	   "SELECT r " +
      			"FROM Role as r " +
      			"LEFT JOIN r.userList as u " +
      			"LEFT JOIN r.groupList as g " +
     			"LEFT JOIN g.userList as gu " +
      			"WHERE u.id = :userId " +
      			"OR gu.id = :userId " +
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByUserIdCount",
           query=          
        	   "SELECT count(r) " +
        	   "FROM Role as r " +
     			"LEFT JOIN r.userList as u " +
     			"LEFT JOIN r.groupList as g " +
     			"LEFT JOIN g.userList as gu " +
     			"WHERE u.id = :userId " +
     			"OR gu.id = :userId " +
     			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByAppnameAndUserId",
           query=          
        	   "SELECT r " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"LEFT JOIN r.userList as u " +
      			"LEFT JOIN r.groupList as g " +
      			"LEFT JOIN g.userList as gu " +
      			"WHERE a.name = :appname " +
      			"AND (u.id = :userId "+
      			"OR gu.id = :userId) "+
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByAppnameAndUserIdCount",
           query=          
        	   "SELECT count(r) " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"LEFT JOIN r.userList as u " +
      			"LEFT JOIN r.groupList as g " +
      			"LEFT JOIN g.userList as gu " +
      			"WHERE a.name = :appname " +
      			"AND (u.id = :userId "+
      	      	"OR gu.id = :userId) "+
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByAppnameAndGroups",
           query=          
        	   "SELECT r " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"LEFT JOIN r.groupList as g " +
      			"WHERE a.name = :appname " +
      			"AND g.id = :groupId " +
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByAppnameAndGroupsCount",
           query=          
        	   "SELECT count(r) " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"LEFT JOIN r.groupList as g " +
      			"WHERE a.name = :appname " +
      			"AND g.id = :groupId " +
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByAppnameAndNotGroups",
           query=          
        	   "SELECT r " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"WHERE a.name = :appname " +
      			"ORDER BY sort ASC"
   ),
   @NamedQuery( 
           name="RoleFetchByAppnameAndNotGroupsCount",
           query=          
        	   "SELECT count(r) " +
      			"FROM Role as r " +
      			"LEFT JOIN r.application as a " +
      			"WHERE a.name = :appname " +
      			"ORDER BY sort ASC"
   )
})


public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private long id;
	private String name;
	private long sort;
	@OneToMany( mappedBy="role", cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, fetch=FetchType.EAGER )
	@OrderBy("sort") 
	private List<Action> actionList = new LinkedList<Action>();
	@ManyToMany( targetEntity=Groups.class, cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH } )
	@JoinTable(name="groups_role", joinColumns = {@JoinColumn(name="role_id")}, inverseJoinColumns = {@JoinColumn(name="groups_id")})
	private List<Groups> groupList = new LinkedList<Groups>();
	@ManyToMany( targetEntity=User.class, cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH } )
	@JoinTable(name="role_user", joinColumns = {@JoinColumn(name="role_id")}, inverseJoinColumns = {@JoinColumn(name="user_id")})
	private List<User> userList = new LinkedList<User>();
	@ManyToOne
	@JoinColumn( name="application_id" )
	private Application application;
	
	public long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Groups> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Groups> groupList) {
		this.groupList = groupList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public List<Action> getActionList() {
		return actionList;
	}

	public void setActionList(List<Action> actionList) {
		this.actionList = actionList;
	}


}
