package de.uplinkgmbh.lms.entitys;

import java.net.URL;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;


@Entity
@NamedQueries( {
    @NamedQuery( 
           name="OrgaFetchByName",
           query=          
        	"SELECT o " +
   			"FROM Organisation o " +
   			"WHERE o.name = :name"
   ),
   @NamedQuery( 
           name="AllOrganisation",
           query=          
        	"SELECT o " +
   			"FROM Organisation AS o "
   ),
   @NamedQuery( 
           name="AllOrganisationCount",
           query=          
        	"SELECT count(o) " +
   			"FROM Organisation AS o "
   )
})

public class Organisation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private long id;
	@Column(length=255, unique=true, nullable=false)
	private String name;
	private URL url;
	private String phone;
	private String fax;
	private String city;
	private String state;
	private Locale country;
	private String zip;
	private String street;
	private String streetnr;
	@Type(type = "text")
	private String washstore;
	@OneToMany( mappedBy="organisation", cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, fetch=FetchType.LAZY )
	private List<User> userList = new LinkedList<User>();
	
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

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Locale getCountry() {
		return country;
	}

	public void setCountry(Locale country) {
		this.country = country;
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


}
