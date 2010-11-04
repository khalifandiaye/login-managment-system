window.addEvent('domready', function(){
	
	
	$$('.formcheck').each( function( self ){
		
		var id = self.get('id'); 
		
		new FormCheck( id, {
			'display' : {
				'errorsLocation' : 1,
				'indicateErrors' : 2,
				'keepFocusOnError' : 0,
				'closeTipsButton' : 1,
				'showErrors' : 1,
				'addClassErrorToField' : 1
			},
			'optional' : {
				'submit' : false,
				'fieldErrorClass' : 'error',
				'listErrorsAtTop' : true
			}
		} );
	} );
	
});
