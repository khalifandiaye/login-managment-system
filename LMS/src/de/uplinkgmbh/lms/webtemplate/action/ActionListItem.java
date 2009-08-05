package de.uplinkgmbh.lms.webtemplate.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.tools.E;
import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.servlets.forms.ActionForm;

public class ActionListItem extends AbstractFileWebTemplate{

	public ActionListItem() throws KeyException, IOException {
		super();
	}

	@Override
	protected void doRender(Object object, HttpServletRequest request,
			HttpServletResponse response, Translator translator) throws IOException,
			WebTemplateException, Exception {
		
		ActionForm aform = new ActionForm();
		
		getHolder().setValue( "validatorname", "valid" );
		getHolder().setValue( "validatorsort", "valid" );
		getHolder().setValue( "validatorstate", "valid" );
		getHolder().setValue( "validatoraction", "valid" );
		getHolder().setValue( "validatortarget", "valid" );
		getHolder().setValue( "validatorrule", "valid" );

		if( object instanceof Action ){
		
			Action a = (Action)object;
			
			aform.setAction( a.getAction() );
			aform.setName( a.getName() );
			aform.setRule( a.getRule() );
			aform.setSort( ((Long)a.getSort()).intValue() );
			aform.setState( a.getState() );
			aform.setTarget( a.getTarget() );
			
			getHolder().setValue( "appid", this.getParameter( "appid" ) );
			getHolder().setValue( "roleid", ((Long)a.getRole().getId()).toString() );
			getHolder().setValue( "actionid", ((Long)a.getId()).toString() );
			
			if( aform.isValid() ){
				
				getHolder().setValue( "elementname", aform.getHtmlInput( "a_name" ) );
				getHolder().setValue( "elementsort", aform.getHtmlInput( "a_sort" ) );
				getHolder().setValue( "elementstate", aform.getHtmlInput( "a_state" ) );
				getHolder().setValue( "elementaction", aform.getHtmlInput( "a_action" ) );
				getHolder().setValue( "elementtarget", aform.getHtmlInput( "a_target" ) );
				getHolder().setValue( "elementrule", aform.getHtmlInput( "a_rule" ) );
				getHolder().setValue( "del","<td><input type=\"submit\" name=\"del\" value=\"Del\"></input></td>" );
			}else{
				
				if( aform.getFormValue( "a_name" ).isValid() ){
					getHolder().setValue( "elementname", aform.getHtmlInput( "a_name" ) );
					getHolder().setValue( "validatorname", "valid" );
				}else{
					getHolder().setValue( "elementname", aform.getHtmlInput( "a_name" ) );
					getHolder().setValue( "validatorname", "invalid" );
				}
				if( aform.getFormValue( "a_sort" ).isValid() ){
					getHolder().setValue( "elementsort", aform.getHtmlInput( "a_sort" ) );
					getHolder().setValue( "validatorsort", "valid" );
				}else{
					getHolder().setValue( "elementsort", aform.getHtmlInput( "a_sort" ) );
					getHolder().setValue( "validatorsort", "invalid" );
				}
				if( aform.getFormValue( "a_state" ).isValid() ){
					getHolder().setValue( "elementstate", aform.getHtmlInput( "a_state" ) );
					getHolder().setValue( "validatorstate", "valid" );
				}else{
					getHolder().setValue( "elementstate", aform.getHtmlInput( "a_state" ) );
					getHolder().setValue( "validatorstate", "invalid" );
				}
				if( aform.getFormValue( "a_action" ).isValid() ){
					getHolder().setValue( "elementaction", aform.getHtmlInput( "a_action" ) );
					getHolder().setValue( "validatoraction", "valid" );
				}else{
					getHolder().setValue( "elementaction", aform.getHtmlInput( "a_action" ) );
					getHolder().setValue( "validatoraction", "invalid" );
				}
				if( aform.getFormValue( "a_target" ).isValid() ){
					getHolder().setValue( "elementtarget", aform.getHtmlInput( "a_target" ) );
					getHolder().setValue( "validatortarget", "valid" );
				}else{
					getHolder().setValue( "elementtarget", aform.getHtmlInput( "a_target" ) );
					getHolder().setValue( "validatortarget", "invalid" );
				}
				if( aform.getFormValue( "a_rule" ).isValid() ){
					getHolder().setValue( "elementrule", aform.getHtmlInput( "a_rule" ) );
					getHolder().setValue( "validatorrule", "valid" );
				}else{
					getHolder().setValue( "elementrule", aform.getHtmlInput( "a_rule" ) );
					getHolder().setValue( "validatorrule", "invalid" );
				}
				getHolder().setValue( "del","<td><input type=\"submit\" name=\"del\" value=\"Del\"></input></td>" );
			}
		}else{
			getHolder().setValue( "roleid", ""+this.getParameter( "roleid" ) );
			getHolder().setValue( "appid", this.getParameter( "appid" ) );
			getHolder().setValue( "actionid", "" );
			
			if( this.getParameter( "actionform" ) != null ){
				aform = (ActionForm) this.getParameter( "actionform" );
				
				if( aform.getFormValue( "a_name" ).isValid() ){
					getHolder().setValue( "elementname", aform.getHtmlInput( "a_name" ) );
					getHolder().setValue( "validatorname", "valid" );
				}else{
					getHolder().setValue( "elementname", aform.getHtmlInput( "a_name" ) );
					getHolder().setValue( "validatorname", "invalid" );
				}
				if( aform.getFormValue( "a_sort" ).isValid() ){
					getHolder().setValue( "elementsort", aform.getHtmlInput( "a_sort" ) );
					getHolder().setValue( "validatorsort", "valid" );
				}else{
					getHolder().setValue( "elementsort", aform.getHtmlInput( "a_sort" ) );
					getHolder().setValue( "validatorsort", "invalid" );
				}
				if( aform.getFormValue( "a_state" ).isValid() ){
					getHolder().setValue( "elementstate", aform.getHtmlInput( "a_state" ) );
					getHolder().setValue( "validatorstate", "valid" );
				}else{
					getHolder().setValue( "elementstate", aform.getHtmlInput( "a_state" ) );
					getHolder().setValue( "validatorstate", "invalid" );
				}
				if( aform.getFormValue( "a_action" ).isValid() ){
					getHolder().setValue( "elementaction", aform.getHtmlInput( "a_action" ) );
					getHolder().setValue( "validatoraction", "valid" );
				}else{
					getHolder().setValue( "elementaction", aform.getHtmlInput( "a_action" ) );
					getHolder().setValue( "validatoraction", "invalid" );
				}
				if( aform.getFormValue( "a_target" ).isValid() ){
					getHolder().setValue( "elementtarget", aform.getHtmlInput( "a_target" ) );
					getHolder().setValue( "validatortarget", "valid" );
				}else{
					getHolder().setValue( "elementtarget", aform.getHtmlInput( "a_target" ) );
					getHolder().setValue( "validatortarget", "invalid" );
				}
				if( aform.getFormValue( "a_rule" ).isValid() ){
					getHolder().setValue( "elementrule", aform.getHtmlInput( "a_rule" ) );
					getHolder().setValue( "validatorrule", "valid" );
				}else{
					getHolder().setValue( "elementrule", aform.getHtmlInput( "a_rule" ) );
					getHolder().setValue( "validatorrule", "invalid" );
				}
				getHolder().setValue( "del","<td><input type=\"submit\" name=\"del\" value=\"Del\"></input></td>" );
				
			}else{
				getHolder().setValue( "elementname", aform.getHtmlInput( "a_name" ) );
				getHolder().setValue( "elementsort", aform.getHtmlInput( "a_sort" ) );
				getHolder().setValue( "elementstate", aform.getHtmlInput( "a_state" ) );
				getHolder().setValue( "elementaction", aform.getHtmlInput( "a_action" ) );
				getHolder().setValue( "elementtarget", aform.getHtmlInput( "a_target" ) );
				getHolder().setValue( "elementrule", aform.getHtmlInput( "a_rule" ) );
				getHolder().setValue( "del","<td><input type=\"submit\" name=\"del\" value=\"Del\"></input></td>" );
			}
		}
	
		getHolder().render( object, request, response, null );
	}
	
}
