package de.uplinkgmbh.lms.entitys;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import org.hibernate.annotations.Type;


@Entity
@NamedQueries( {
    @NamedQuery( 
           name="UserFetchByLoginname",
           query=          
        	"SELECT u " +
   			"FROM User u " +
   			"WHERE u.loginname = :loginname"
   ),
   @NamedQuery( 
           name="AllUser",
           query=          
        	"SELECT u " +
   			"FROM User AS u "
   ),
   @NamedQuery( 
           name="AllUserCount",
           query=          
        	"SELECT count(u) " +
   			"FROM User AS u "
   ),
   @NamedQuery( 
           name="AllUserByGroupId",
           query=          
        	"SELECT u " +
   			"FROM User AS u "+
   			"LEFT JOIN u.groupList AS g "+
   			"WHERE g.id = :groupId "
   ),
   @NamedQuery( 
           name="AllUserByGroupIdCount",
           query=          
        	"SELECT count(u) " +
   			"FROM User AS u "+
        	"LEFT JOIN u.groupList AS g "+
   			"WHERE g.id = :groupId "
   ),
   @NamedQuery( 
           name="AllUserByRoleId",
           query=          
        	"SELECT u " +
   			"FROM User AS u "+
   			"LEFT JOIN u.roleList AS r "+
   			"WHERE r.id = :roleId "
   ),
   @NamedQuery( 
           name="AllUserByRoleIdCount",
           query=          
        	"SELECT count(u) " +
   			"FROM User AS u "+
        	"LEFT JOIN u.roleList AS r "+
   			"WHERE r.id = :roleId "
   ),
   @NamedQuery( 
           name="AllTemplateUser",
           query=          
        	"SELECT u " +
   			"FROM User AS u "+
   			"WHERE u.template = true "
   ),
   @NamedQuery( 
           name="AllTemplateUserCount",
           query=          
        	"SELECT count(u) " +
   			"FROM User AS u "+
   			"WHERE u.template = true "
   ),
   @NamedQuery( 
           name="AllUserByApplication",
           query=          
        	"SELECT u " +
   			"FROM User AS u "+
   			"LEFT JOIN u.roleList AS r "+
   			"LEFT JOIN u.groupList AS g "+
   			"WHERE r.application.name = :appName "+
   			"OR g.application.name = :appName "
   ),
   @NamedQuery( 
           name="UserFetchByLoginnameAndPassword",
           query=          
        	"SELECT u " +
   			"FROM User u " +
   			"WHERE u.loginname = :loginname " +
   			"AND u.password = :password "
   )
})


public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private long id;
	@Column(length=255, unique=true, nullable=false)
	private String loginname;
	@Column(length=255, unique=false, nullable=false)
	private String password;
	private String firstname;
	private String surename;
	private String email;
	private String phonepriv;
	private String phonework;
	private String mobile;
	private String fax;
	private String city;
	private String state;
	private String zip;
	private String street;
	private String streetnr;
	@Type(type = "text")
	private String washstore;
	private Locale country;
	private Locale language;
	private boolean activ;
	private boolean template;
	private Date lastlogin;
	private Date lastlogout;
	private long logincounter;
	@ManyToMany( targetEntity=Groups.class, cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name="groups_user", joinColumns = {@JoinColumn(name="user_id")}, inverseJoinColumns = {@JoinColumn(name="groups_id")})
	private List<Groups> groupList = new LinkedList<Groups>();
	@ManyToMany( targetEntity=Role.class, cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name="role_user", joinColumns = {@JoinColumn(name="user_id")}, inverseJoinColumns = {@JoinColumn(name="role_id")})
	private List<Role> roleList = new LinkedList<Role>();
	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn( name="organisation_id" )
	private Organisation organisation;
	
	public long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(long id) {
		this.id = id;
	}


	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public List<Groups> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Groups> groupList) {
		this.groupList = groupList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurename() {
		return surename;
	}

	public void setSurename(String surename) {
		this.surename = surename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonepriv() {
		return phonepriv;
	}

	public void setPhonepriv(String phonepriv) {
		this.phonepriv = phonepriv;
	}

	public String getPhonework() {
		return phonework;
	}

	public void setPhonework(String phonework) {
		this.phonework = phonework;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetnr() {
		return streetnr;
	}

	public void setStreetnr(String streetnr) {
		this.streetnr = streetnr;
	}

	public String getWashstore() {
		return washstore;
	}

	public void setWashstore(String washstore) {
		this.washstore = washstore;
	}

	public Locale getCountry() {
		return country;
	}

	public void setCountry(Locale country) {
		this.country = country;
	}

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

	public boolean isActiv() {
		return activ;
	}

	public void setActiv(boolean activ) {
		this.activ = activ;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	public Date getLastlogout() {
		return lastlogout;
	}

	public void setLastlogout(Date lastlogout) {
		this.lastlogout = lastlogout;
	}

	public long getLogincounter() {
		return logincounter;
	}

	public void setLogincounter(long logincounter) {
		this.logincounter = logincounter;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

}
