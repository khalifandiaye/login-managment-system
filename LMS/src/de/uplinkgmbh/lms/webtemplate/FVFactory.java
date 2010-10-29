package de.uplinkgmbh.lms.webtemplate;

import java.util.Locale;

import de.axone.webtemplate.element.FormValueFactory;

public class FVFactory extends FormValueFactory{
	
	public FVFactory(){
		super.setLocale( Locale.GERMANY );
	}

}
