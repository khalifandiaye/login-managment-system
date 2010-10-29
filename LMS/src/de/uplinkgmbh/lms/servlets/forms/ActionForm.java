package de.uplinkgmbh.lms.servlets.forms;

import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.converter.ConverterException;
import de.axone.webtemplate.form.FormValue;
import de.axone.webtemplate.form.WebFormImpl;
import de.uplinkgmbh.lms.webtemplate.FVFactory;

public class ActionForm extends WebFormImpl {
	

	public static final String ACTIONNAME = "a_name";
	public static final String ACTIONTARGET = "a_target";
	public static final String ACTIONSTATE = "a_state";
	public static final String ACTIONACTION = "a_action";
	public static final String ACTIONRULE = "a_rule";
	public static final String ACTIONSORT = "a_sort";
	
	private FormValue<String> a_name;
	private FormValue<String> a_target;
	private FormValue<String> a_state;
	private FormValue<String> a_action;
	private FormValue<String> a_rule;
	private FormValue<Integer> a_sort;
	
	public ActionForm() throws WebTemplateException {
		
		a_name = new FVFactory().createInputTextValue( ACTIONNAME, 255, false );
		this.addFormValue( ACTIONNAME, a_name );
		a_target = new FVFactory().createInputTextValue( ACTIONTARGET, 255, false );
		this.addFormValue( ACTIONTARGET, a_target );
		a_state = new FVFactory().createInputTextValue( ACTIONSTATE, 255, false );
		this.addFormValue( ACTIONSTATE, a_state );
		a_action = new FVFactory().createInputTextValue( ACTIONACTION, 255, false );
		this.addFormValue( ACTIONACTION, a_action );
		a_rule = new FVFactory().createInputTextValue( ACTIONRULE, 255, false );
		this.addFormValue( ACTIONRULE, a_rule );
		a_sort = new FVFactory().createInputIntegerValue( ACTIONSORT, false );
		this.addFormValue( ACTIONSORT, a_sort );
	}
	
	public String getActionName() throws ConverterException{
		return a_name.getValue();
	}
	
	public void setActionName( String a_name ) throws ConverterException{
		this.a_name.setValue( a_name );
	}

	public String getTarget() throws ConverterException{
		return a_target.getValue();
	}
	
	public void setTarget( String a_target ) throws ConverterException{
		this.a_target.setValue( a_target );
	}
	
	public String getState() throws ConverterException{
		return a_state.getValue();
	}
	
	public void setState( String a_state ) throws ConverterException{
		this.a_state.setValue( a_state );
	}
	
	public String getAction() throws ConverterException{
		return a_action.getValue();
	}
	
	public void setAction( String a_action ) throws ConverterException{
		this.a_action.setValue( a_action );
	}
	
	public String getRule() throws ConverterException{
		return a_rule.getValue();
	}
	
	public void setRule( String a_rule ) throws ConverterException{
		this.a_rule.setValue( a_rule );
	}
	
	public Integer getSort() throws ConverterException{
		return a_sort.getValue();
	}
	
	public void setSort( Integer a_sort ) throws ConverterException{
		this.a_sort.setValue( a_sort );
	}
}
