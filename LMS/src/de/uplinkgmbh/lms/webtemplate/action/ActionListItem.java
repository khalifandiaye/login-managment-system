package de.uplinkgmbh.lms.webtemplate.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.webtemplate.AbstractFileWebTemplate;
import de.axone.webtemplate.KeyException;
import de.axone.webtemplate.WebTemplateException;
import de.axone.webtemplate.elements.impl.HtmlInputElement;
import de.axone.webtemplate.form.Translator;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Application;
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
			Application app;
			
			aform.setAction( a.getAction() );
			aform.setActionName( a.getName() );
			aform.setRule( a.getRule() );
			aform.setSort( ((Long)a.getSort()).intValue() );
			aform.setState( a.getState() );
			aform.setTarget( a.getTarget() );
			
			getHolder().setValue( "appid", this.getParameter( "appid" ) );
			getHolder().setValue( "roleid", ((Long)a.getRole().getId()).toString() );
			getHolder().setValue( "actionid", ((Long)a.getId()).toString() );
			
			getHolder().setValue( "rand", "_"+(int)(Math.random()*10000000) );
			
			if( aform.isValid() ){
				HtmlInputElement hie = new HtmlInputElement( HtmlInputElement.InputType.TEXT, "application" );
				hie.setValue( a.getRole().getApplication().getName() );
				hie.addAttribute( "id", "input_a_name" );
				getHolder().setValue( "elementapp", hie );				
				getHolder().setValue( "elementname", aform.getHtmlInput( ActionForm.ACTIONNAME ) );
				getHolder().setValue( "elementsort", aform.getHtmlInput( ActionForm.ACTIONSORT) );
				getHolder().setValue( "elementstate", aform.getHtmlInput( ActionForm.ACTIONSTATE ) );
				getHolder().setValue( "elementaction", aform.getHtmlInput( ActionForm.ACTIONACTION ) );
				getHolder().setValue( "elementtarget", aform.getHtmlInput( ActionForm.ACTIONTARGET ) );
				getHolder().setValue( "elementrule", aform.getHtmlInput( ActionForm.ACTIONRULE ) );
				getHolder().setValue( "del","<td><input type=\"submit\" name=\"del\" value=\"Del\"></input></td>" );
			}else{
				
				if( aform.getFormValue( ActionForm.ACTIONNAME ).isValid() ){
					getHolder().setValue( "elementname", aform.getHtmlInput( ActionForm.ACTIONNAME ) );
					getHolder().setValue( "validatorname", "valid" );
				}else{
					getHolder().setValue( "elementname", aform.getHtmlInput( ActionForm.ACTIONNAME ) );
					getHolder().setValue( "validatorname", "invalid" );
				}
				if( aform.getFormValue( ActionForm.ACTIONSORT ).isValid() ){
					getHolder().setValue( "elementsort", aform.getHtmlInput( ActionForm.ACTIONSORT ) );
					getHolder().setValue( "validatorsort", "valid" );
				}else{
					getHolder().setValue( "elementsort", aform.getHtmlInput( ActionForm.ACTIONSORT ) );
					getHolder().setValue( "validatorsort", "invalid" );
				}
				if( aform.getFormValue( ActionForm.ACTIONSTATE ).isValid() ){
					getHolder().setValue( "elementstate", aform.getHtmlInput( ActionForm.ACTIONSTATE ) );
					getHolder().setValue( "validatorstate", "valid" );
				}else{
					getHolder().setValue( "elementstate", aform.getHtmlInput( ActionForm.ACTIONSTATE ) );
					getHolder().setValue( "validatorstate", "invalid" );
				}
				if( aform.getFormValue( ActionForm.ACTIONACTION ).isValid() ){
					getHolder().setValue( "elementaction", aform.getHtmlInput( ActionForm.ACTIONACTION ) );
					getHolder().setValue( "validatoraction", "valid" );
				}else{
					getHolder().setValue( "elementaction", aform.getHtmlInput( ActionForm.ACTIONACTION ) );
					getHolder().setValue( "validatoraction", "invalid" );
				}
				if( aform.getFormValue( ActionForm.ACTIONTARGET ).isValid() ){
					getHolder().setValue( "elementtarget", aform.getHtmlInput( ActionForm.ACTIONTARGET ) );
					getHolder().setValue( "validatortarget", "valid" );
				}else{
					getHolder().setValue( "elementtarget", aform.getHtmlInput( ActionForm.ACTIONTARGET ) );
					getHolder().setValue( "validatortarget", "invalid" );
				}
				if( aform.getFormValue( ActionForm.ACTIONRULE ).isValid() ){
					getHolder().setValue( "elementrule", aform.getHtmlInput( ActionForm.ACTIONRULE ) );
					getHolder().setValue( "validatorrule", "valid" );
				}else{
					getHolder().setValue( "elementrule", aform.getHtmlInput( ActionForm.ACTIONRULE ) );
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
					getHolder().setValue( "elementname", aform.getHtmlInput( ActionForm.ACTIONNAME ) );
					getHolder().setValue( "validatorname", "valid" );
				}else{
					getHolder().setValue( "elementname", aform.getHtmlInput( ActionForm.ACTIONNAME ) );
					getHolder().setValue( "validatorname", "invalid" );
				}
				if( aform.getFormValue( "a_sort" ).isValid() ){
					getHolder().setValue( "elementsort", aform.getHtmlInput( ActionForm.ACTIONSORT ) );
					getHolder().setValue( "validatorsort", "valid" );
				}else{
					getHolder().setValue( "elementsort", aform.getHtmlInput( ActionForm.ACTIONSORT ) );
					getHolder().setValue( "validatorsort", "invalid" );
				}
				if( aform.getFormValue( "a_state" ).isValid() ){
					getHolder().setValue( "elementstate", aform.getHtmlInput( ActionForm.ACTIONSTATE ) );
					getHolder().setValue( "validatorstate", "valid" );
				}else{
					getHolder().setValue( "elementstate", aform.getHtmlInput( ActionForm.ACTIONSTATE ) );
					getHolder().setValue( "validatorstate", "invalid" );
				}
				if( aform.getFormValue( "a_action" ).isValid() ){
					getHolder().setValue( "elementaction", aform.getHtmlInput( ActionForm.ACTIONACTION ) );
					getHolder().setValue( "validatoraction", "valid" );
				}else{
					getHolder().setValue( "elementaction", aform.getHtmlInput( ActionForm.ACTIONACTION ) );
					getHolder().setValue( "validatoraction", "invalid" );
				}
				if( aform.getFormValue( "a_target" ).isValid() ){
					getHolder().setValue( "elementtarget", aform.getHtmlInput( ActionForm.ACTIONTARGET ) );
					getHolder().setValue( "validatortarget", "valid" );
				}else{
					getHolder().setValue( "elementtarget", aform.getHtmlInput( ActionForm.ACTIONTARGET ) );
					getHolder().setValue( "validatortarget", "invalid" );
				}
				if( aform.getFormValue( "a_rule" ).isValid() ){
					getHolder().setValue( "elementrule", aform.getHtmlInput( ActionForm.ACTIONRULE ) );
					getHolder().setValue( "validatorrule", "valid" );
				}else{
					getHolder().setValue( "elementrule", aform.getHtmlInput( ActionForm.ACTIONRULE ) );
					getHolder().setValue( "validatorrule", "invalid" );
				}
				getHolder().setValue( "del","<td><input type=\"submit\" name=\"del\" value=\"Del\"></input></td>" );
				
			}else{
				getHolder().setValue( "elementname", aform.getHtmlInput( ActionForm.ACTIONNAME ) );
				getHolder().setValue( "elementsort", aform.getHtmlInput( ActionForm.ACTIONSORT ) );
				getHolder().setValue( "elementstate", aform.getHtmlInput( ActionForm.ACTIONSTATE ) );
				getHolder().setValue( "elementaction", aform.getHtmlInput( ActionForm.ACTIONACTION ) );
				getHolder().setValue( "elementtarget", aform.getHtmlInput( ActionForm.ACTIONTARGET ) );
				getHolder().setValue( "elementrule", aform.getHtmlInput( ActionForm.ACTIONRULE ) );
				getHolder().setValue( "del","<td><input type=\"submit\" name=\"del\" value=\"Del\"></input></td>" );
			}
		}
	
		getHolder().render( object, request, response, null );
	}
	
}
