package de.uplinkgmbh.lms.entitys;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;


@Entity
@NamedQueries( {
    @NamedQuery( 
           name="GroupFetchByName",
           query=          
        	"SELECT g " +
   			"FROM Groups g " +
   			"WHERE g.name = :name"
   ),
   @NamedQuery( 
           name="GroupCount",
           query=          
        	"SELECT count(g) " +
   			"FROM Groups g "
   ),
   @NamedQuery( 
           name="AllGroupsByApplicationId",
           query=          
        	"SELECT g " +
   			"FROM Groups g "+
   			"WHERE g.application.id = :appId"
   ),
   @NamedQuery( 
           name="AllGroupsByApplicationIdCount",
           query=          
        	"SELECT count(g) " +
   			"FROM Groups g "+
   			"WHERE g.application.id = :appId"
   ),
   @NamedQuery( 
           name="GroupFetchByApplicationId",
           query=          
        	"SELECT g " +
   			"FROM Groups g " +
   			"WHERE g.application.id = :appId"
   ),
   @NamedQuery( 
           name="GroupFetchByApplicationIdCount",
           query=          
        	"SELECT count(g) " +
   			"FROM Groups g " +
   			"WHERE g.application.id = :appId"
   ),
   @NamedQuery( 
           name="AllGroupsByRoleId",
           query=          
        	"SELECT g " +
   			"FROM Groups g " +
   			"LEFT JOIN g.roleList AS r "+
   			"WHERE r.id = :roleId"
   ),
   @NamedQuery( 
           name="AllGroupsByRoleIdCount",
           query=          
        	"SELECT count(g) " +
   			"FROM Groups g " +
   			"LEFT JOIN g.roleList AS r "+
   			"WHERE r.id = :roleId"
   ),
   @NamedQuery( 
           name="AllGroupsByUserId",
           query=          
        	"SELECT g " +
   			"FROM Groups g " +
   			"LEFT JOIN g.userList AS u "+
   			"WHERE u.id = :userId"
   ),
   @NamedQuery( 
           name="AllGroupsByUserIdCount",
           query=          
        	"SELECT count(g) " +
   			"FROM Groups g " +
   			"LEFT JOIN g.userList AS u "+
   			"WHERE u.id = :userId"
   )
})

public class Groups {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private long id;
	private String name;
	@ManyToMany( targetEntity=User.class, cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH } )
	@JoinTable(name="groups_user", joinColumns = {@JoinColumn(name="groups_id")}, inverseJoinColumns = {@JoinColumn(name="user_id")})
	private List<User> userList = new LinkedList<User>();
	@ManyToMany( targetEntity=Role.class, cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH } )
	@JoinTable(name="groups_role", joinColumns = {@JoinColumn(name="groups_id")}, inverseJoinColumns = {@JoinColumn(name="role_id")})
	@OrderBy("sort asc") 
	private List<Role> roleList = new LinkedList<Role>();
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

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> user) {
		this.userList = user;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}
