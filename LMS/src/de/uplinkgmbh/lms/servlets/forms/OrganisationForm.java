package de.uplinkgmbh.lms.servlets.forms;

import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.uplinkgmbh.lms.webtemplate.FVFactory;

public class OrganisationForm extends WebFormImpl {
	
	public static final String NAME = "name";
	public static final String URL = "url";
	public static final String PHONE = "phone";
	public static final String FAX = "fax";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP = "zip";
	public static final String STREET = "street";
	public static final String STREETNR = "streetnr";
	public static final String WASHSTORE = "washstore";
	public static final String COUNTRY = "country";
	
	private FormValue<String> name;
	private FormValue<String> url;
	private FormValue<String> phone;
	private FormValue<String> fax;
	private FormValue<String> city;
	private FormValue<String> state;
	private FormValue<String> zip;
	private FormValue<String> street;
	private FormValue<String> streetnr;
	private FormValue<String> washstore;
	private FormValue<String> country;
	
	public OrganisationForm() throws WebTemplateException {
	
		name = new FVFactory().createInputTextValue( NAME, 255, false );
		this.addFormValue( NAME, name );
		url =new FVFactory().createInputUrlValue( URL, 255, true );
		this.addFormValue( URL, url );
		phone = new FVFactory().createInputTextValue( PHONE, 255, true );
		this.addFormValue( PHONE, phone );
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

	}
	
	public String getOrgaName() throws ConverterException{
		return name.getValue();
	}
	public void setOrgaName( String name ) throws ConverterException{
		this.name.setValue( name );
	}

	public String getUrl() throws ConverterException{
		return url.getValue();
	}
	public void setUrl( String url ) throws ConverterException{
		this.url.setValue( url );
	}
	
	public String getPhone() throws ConverterException{
		return phone.getValue();
	}
	public void setPhone( String phone ) throws ConverterException{
		this.phone.setValue( phone );
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
	
}
