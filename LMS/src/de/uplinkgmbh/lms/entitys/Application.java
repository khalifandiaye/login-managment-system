package de.uplinkgmbh.lms.entitys;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;



@Entity
@NamedQueries( {
    @NamedQuery( 
           name="ApplicationFetchByName",
           query=          
        	"SELECT a " +
   			"FROM Application a " +
   			"WHERE a.name = :name"
   ),
   @NamedQuery( 
           name="AllApplicationCount",
           query=          
        	"SELECT count(a) " +
   			"FROM Application a "
   ),
   @NamedQuery( 
           name="AllApplication",
           query=          
        	"SELECT a " +
   			"FROM Application a "
   )
})
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private long id;
	private String name;
	@OneToMany( mappedBy="application", cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH } )
	private List<Groups> groupList = new LinkedList<Groups>();
	@OneToMany( mappedBy="application", cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH } )
	@OrderBy("sort") 
	private List<Role> roleList = new LinkedList<Role>();
	
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

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Groups> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Groups> groupList) {
		this.groupList = groupList;
	}
}
