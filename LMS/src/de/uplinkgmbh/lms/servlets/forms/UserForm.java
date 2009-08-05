package de.uplinkgmbh.lms.servlets.forms;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.axone.tools.E;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.elements.impl.HtmlCheckboxElement;
import de.axone.webtemplate.elements.impl.HtmlInputElement;
import de.axone.webtemplate.elements.impl.HtmlSelectElement;
import de.axone.webtemplate.elements.impl.HtmlSelectElement.Option;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.uplinkgmbh.lms.entitys.Organisation;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;

public class UserForm extends WebFormImpl {
	
	public static final String LOGINNAME = "loginname";
	public static final String PASSWORD = "password";
	public static final String FIRSTNAME = "firstname";
	public static final String SURENAME = "surename";
	public static final String EMAIL = "email";
	public static final String PHONEPRIV = "phonepriv";
	public static final String PHONEWORK = "phonework";
	public static final String MOBILE = "mobile";
	public static final String FAX = "fax";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP = "zip";
	public static final String STREET = "street";
	public static final String STREETNR = "streetnr";
	public static final String WASHSTORE = "washstore";
	public static final String COUNTRY = "country";
	public static final String LANGUAGE = "language";
	public static final String ACTIV = "activ";
	public static final String TEMPLATE = "template";
	public static final String ORGANISATION = "organisation";
	
	private FormValue<String> loginname;
	private FormValue<String> password;
	private FormValue<String> firstname;
	private FormValue<String> surename;
	private FormValue<String> email;
	private FormValue<String> phonepriv;
	private FormValue<String> phonework;
	private FormValue<String> mobile;
	private FormValue<String> fax;
	private FormValue<String> city;
	private FormValue<String> state;
	private FormValue<String> zip;
	private FormValue<String> street;
	private FormValue<String> streetnr;
	private FormValue<String> washstore;
	private FormValue<String> country;
	private FormValue<String> language;
	private FormValue<Integer> activ;
	private FormValue<Integer> template;

	private List<HtmlSelectElement.Option> organisationOptions = new LinkedList<HtmlSelectElement.Option>();
	
	public UserForm() throws WebTemplateException {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		em.getTransaction().begin();	
		Query q = em.createNamedQuery( "AllOrganisation" );
		List<Organisation> res = (List<Organisation>) q.getResultList();
		em.getTransaction().commit();
		
		organisationOptions.add( new OptionImpl( "-1", "none" ) );
		organisationOptions.add( new OptionImpl( "-2", "new" ) ); 
		
		if( res != null ){
			for( Organisation o : res ){
				organisationOptions.add( new OptionImpl( String.valueOf( o.getId() ), o.getName() ) );
			}
		}

		loginname = HtmlInputElement.createTextValue( LOGINNAME, 255, false );
		this.addFormValue( LOGINNAME, loginname );
		
		password = HtmlInputElement.createTextValue( PASSWORD, 255, false );
		this.addFormValue( PASSWORD, password );
		
		firstname = HtmlInputElement.createTextValue( FIRSTNAME, 255, true );
		this.addFormValue( FIRSTNAME, firstname );
		
		surename = HtmlInputElement.createTextValue( SURENAME, 255, true );
		this.addFormValue( SURENAME, surename );
		
		email = HtmlInputElement.createEMailValue( EMAIL, 255, true );
		this.addFormValue( EMAIL, email );
		
		phonepriv = HtmlInputElement.createTextValue( PHONEPRIV, 255, true );
		this.addFormValue( PHONEPRIV, phonepriv );
		
		phonework = HtmlInputElement.createTextValue( PHONEWORK, 255, true );
		this.addFormValue( PHONEWORK, phonework );
		
		mobile = HtmlInputElement.createTextValue( MOBILE, 255, true );
		this.addFormValue( MOBILE, mobile );
		
		fax = HtmlInputElement.createTextValue( FAX, 255, true );
		this.addFormValue( FAX, fax );
		
		city = HtmlInputElement.createTextValue( CITY, 255, true );
		this.addFormValue( CITY, city );
		
		state = HtmlInputElement.createTextValue( STATE, 255, true );
		this.addFormValue( STATE, state );
		
		zip = HtmlInputElement.createTextValue( ZIP, 255, true );
		this.addFormValue( ZIP, zip );
		
		street = HtmlInputElement.createTextValue( STREET, 255, true );
		this.addFormValue( STREET, street );
		
		streetnr = HtmlInputElement.createTextValue( STREETNR, 255, true );
		this.addFormValue( STREETNR, streetnr );
		
		washstore = HtmlInputElement.createTextValue( WASHSTORE, 255, true );
		this.addFormValue( WASHSTORE, washstore );
		
		country = HtmlInputElement.createCountryValue( COUNTRY, true );
		this.addFormValue( COUNTRY, country );
		
		language = HtmlInputElement.createLanguageValue( LANGUAGE, true );
		this.addFormValue( LANGUAGE, language );
		
		activ = HtmlCheckboxElement.createBooleanValue( ACTIV );
		this.addFormValue( ACTIV, activ );
		
		template = HtmlCheckboxElement.createBooleanValue( TEMPLATE );
		this.addFormValue( TEMPLATE, template );
	}
	
	public HtmlSelectElement getOrganisationHtmlSelectElement( ){
		return getOrganisationHtmlSelectElement( null );
	}
	
	public HtmlSelectElement getOrganisationHtmlSelectElement( String selectedkey ){
		if( selectedkey == null )
			return new HtmlSelectElement( ORGANISATION, organisationOptions );
		else
			return new HtmlSelectElement( ORGANISATION, selectedkey, organisationOptions );
	}
	
	public String getLoginname() throws ConverterException{
		return loginname.getValue();
	}
	public void setLoginname( String loginname ) throws ConverterException{
		this.loginname.setValue( loginname );
	}

	public String getPassword() throws ConverterException{
		return password.getValue();
	}
	public void setPassword( String password ) throws ConverterException{
		this.password.setValue( password );
	}
	
	public String getFirstname() throws ConverterException{
		return firstname.getValue();
	}
	public void setFirstname( String firstname ) throws ConverterException{
		this.firstname.setValue( firstname );
	}
	
	public String getSurename() throws ConverterException{
		return surename.getValue();
	}
	public void setSurename( String surename ) throws ConverterException{
		this.surename.setValue( surename );
	}
	
	public String getEmail() throws ConverterException{
		return email.getValue();
	}
	public void setEmail( String email ) throws ConverterException{
		this.email.setValue( email );
	}
	
	public String getPhonepriv() throws ConverterException{
		return phonepriv.getValue();
	}
	public void setPhonepriv( String phonepriv ) throws ConverterException{
		this.phonepriv.setValue( phonepriv );
	}
	
	public String getPhonework() throws ConverterException{
		return phonework.getValue();
	}
	public void setPhonework( String phonework ) throws ConverterException{
		this.phonework.setValue( phonework );
	}
	
	public String getMobile() throws ConverterException{
		return mobile.getValue();
	}
	public void setMobile( String mobile ) throws ConverterException{
		this.mobile.setValue( mobile );
	}
	
	public String getFax() throws ConverterException{
		return fax.getValue();
	}
	public void setFax( String fax ) throws ConverterException{
		this.fax.setValue( fax );
	}
	
	public String getCity() throws ConverterException{
		return city.getValue();
	}
	public void setCity( String city ) throws ConverterException{
		this.city.setValue( city );
	}
	
	public String getState() throws ConverterException{
		return state.getValue();
	}
	public void setState( String state ) throws ConverterException{
		this.state.setValue( state );
	}

	public String getZip() throws ConverterException{
		return zip.getValue();
	}
	public void setZip( String zip ) throws ConverterException{
		this.zip.setValue( zip );
	}
	
	public String getStreet() throws ConverterException{
		return street.getValue();
	}
	public void setStreet( String street ) throws ConverterException{
		this.street.setValue( street );
	}
	
	public String getStreetnr() throws ConverterException{
		return streetnr.getValue();
	}
	public void setStreetnr( String streetnr ) throws ConverterException{
		this.streetnr.setValue( streetnr );
	}
	
	public String getWashstore() throws ConverterException{
		return washstore.getValue();
	}
	public void setWashstore( String washstore ) throws ConverterException{
		this.washstore.setValue( washstore );
	}
	
	public String getCountry() throws ConverterException{
		return country.getValue();
	}
	public void setCountry( String country ) throws ConverterException{
		this.country.setValue( country );
	}

	public String getLanguage() throws ConverterException{
		return language.getValue();
	}
	public void setLanguage( String language ) throws ConverterException{
		this.language.setValue( language );
	}
	
	public Boolean getActiv() throws ConverterException{
		
		if( activ.getValue() == 0 ){
			return false;
		}else{
			return true;
		}
	}
	public void setActiv( Boolean activ ) throws ConverterException{
		if( activ )
			this.activ.setValue( 1 );
		else
			this.activ.setValue( 0 );
	}
	
	public Boolean getTemplate() throws ConverterException{
		if( template.getValue() == 0 ){
			return false;
		}else{
			return true;
		}
	}
	public void setTemplate( Boolean template ) throws ConverterException{
		if( template )
			this.template.setValue( 1 );
		else
			this.template.setValue( 0 );
	}
	
	public static class OptionImpl implements Option {
		
		private String key;
		private String text;
		
		public OptionImpl( String key, String text ){
			this.key = key;
			this.text = text;
		}
		
		public String getValue() { return key; }
		public void setValue( String value ) { this.key = value; }
		
		public String getText() { return text; }
		public void setText( String text ) { this.text = text; }
		
	}
}
