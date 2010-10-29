package de.uplinkgmbh.lms.servlets.forms;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.elements.impl.HtmlSelectElement;
import de.axone.webtemplate.elements.impl.HtmlSelectElement.Option;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.uplinkgmbh.lms.entitys.Organisation;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.webtemplate.FVFactory;

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
	private FormValue<Boolean> activ;
	private FormValue<Boolean> template;

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

		loginname = new FVFactory().createInputTextValue( LOGINNAME, 255, false );
		this.addFormValue( LOGINNAME, loginname );
		
		password = new FVFactory().createInputTextValue( PASSWORD, 255, false );
		this.addFormValue( PASSWORD, password );
		
		firstname = new FVFactory().createInputTextValue( FIRSTNAME, 255, true );
		this.addFormValue( FIRSTNAME, firstname );
		
		surename = new FVFactory().createInputTextValue( SURENAME, 255, true );
		this.addFormValue( SURENAME, surename );
		
		email = new FVFactory().createInputEMailValue( EMAIL, 255, true );
		this.addFormValue( EMAIL, email );
		
		phonepriv = new FVFactory().createInputTextValue( PHONEPRIV, 255, true );
		this.addFormValue( PHONEPRIV, phonepriv );
		
		phonework = new FVFactory().createInputTextValue( PHONEWORK, 255, true );
		this.addFormValue( PHONEWORK, phonework );
		
		mobile = new FVFactory().createInputTextValue( MOBILE, 255, true );
		this.addFormValue( MOBILE, mobile );
		
		fax = new FVFactory().createInputTextValue( FAX, 255, true );
		this.addFormValue( FAX, fax );
		
		city = new FVFactory().createInputTextValue( CITY, 255, true );
		this.addFormValue( CITY, city );
		
		state = new FVFactory().createInputTextValue( STATE, 255, true );
		this.addFormValue( STATE, state );
		
		zip = new FVFactory().createInputTextValue( ZIP, 255, true );
		this.addFormValue( ZIP, zip );
		
		street = new FVFactory().createInputTextValue( STREET, 255, true );
		this.addFormValue( STREET, street );
		
		streetnr = new FVFactory().createInputTextValue( STREETNR, 255, true );
		this.addFormValue( STREETNR, streetnr );
		
		washstore = new FVFactory().createInputTextValue( WASHSTORE, 255, true );
		this.addFormValue( WASHSTORE, washstore );
		
		country = new FVFactory().createInputCountryValue( COUNTRY, true );
		this.addFormValue( COUNTRY, country );
		
		language = new FVFactory().createInputLanguageValue( LANGUAGE, true );
		this.addFormValue( LANGUAGE, language );
		
		activ = new FVFactory().createCheckboxBooleanValue( ACTIV );
		this.addFormValue( ACTIV, activ );
		
		template = new FVFactory().createCheckboxBooleanValue( TEMPLATE );
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
		
		if( activ.getValue() == false ){
			return false;
		}else{
			return true;
		}
	}
	public void setActiv( Boolean activ ) throws ConverterException{
		if( activ )
			this.activ.setValue( true );
		else
			this.activ.setValue( false );
	}
	
	public Boolean getTemplate() throws ConverterException{
		if( template.getValue() == false ){
			return false;
		}else{
			return true;
		}
	}
	public void setTemplate( Boolean template ) throws ConverterException{
		if( template )
			this.template.setValue( true );
		else
			this.template.setValue( false );
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
