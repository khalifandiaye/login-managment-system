package de.uplinkgmbh.lms.entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@Entity
@NamedQueries( {
    @NamedQuery( 
           name="ActionFetchByName",
           query=          
        	"SELECT a " +
   			"FROM Action a " +
   			"WHERE a.name = :name"
   ),
   @NamedQuery( 
           name="ActionCount",
           query=          
        	"SELECT count(a) " +
   			"FROM Action a "
   )
 
})
public class Action {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private long id;
	private String name;
	private long sort;
	private String target;
	private String action;
	private String rule;
	private String state;
	@ManyToOne
	@JoinColumn( name="role_id" )
	private Role role;
	
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
