package de.uplinkgmbh.lms.servlets;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.axone.wash.DefaultWash;
import de.axone.wash.Wash;
import de.axone.wash.WashTools;
import de.axone.wash.Wash.Type;
import de.axone.wash.Wash.WashException;
import de.axone.wash.WashTools.TypeValue;
import de.axone.wash.configuration.Configuration;

/**
 * Servlet implementation class for Servlet: WashServices
 *
 */
 public class WashServices extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( WashServices.class.getName() );
	private Configuration configuration;
   
	public WashServices() {
		super();
	}   	
	
	@Override
	public void init( javax.servlet.ServletConfig config ) throws ServletException {
		
		super.init( config );

		String pathToConfig = getServletContext().getRealPath( "WEB-INF/service.wash" );
		System.err.println( pathToConfig );
		configuration = new Configuration( pathToConfig );
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Do all the work
		Wash result = process( request );
		
		// Get Result
		String rawResult = result.serialize();
		
		
		// Return result
		response.setContentType( "text/plain" );
		response.setContentLength( rawResult.length() );
		
		PrintStream out = new PrintStream( response.getOutputStream() );
		out.print( rawResult );
	}  	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet( request, response );
	}
	
	private Wash process( HttpServletRequest request ) {
		
		// Build request Wash
		Wash requestWash=null;
		try {
			requestWash = buildWash( request );
		} catch( WashException e1 ) {
			log.log( Level.ALL, "Cannot read input", e1 );
			return new ErrorWash( requestWash, "Cannot read input", e1 );
		}
		
		log.log( Level.FINE, "Wash-In:\n" + requestWash.serialize() );
		
		
		// Handle operation
		Wash resultWash;
		try {
			
			resultWash = configuration.getHandler().handle( requestWash, request );
			
		} catch( Exception e ) {
			log.log( Level.ALL, "Exception handling", e );
			return new ErrorWash( requestWash, "Exception handling", e );
		} catch( Error er ) {
			log.log( Level.ALL, "Error handling", er );
			return new ErrorWash( requestWash, "Error handling", er );
		}
		
		
		
		// Log result
		log.log( Level.FINE, "Wash-Out:\n" + resultWash.serialize() );
		
		return resultWash;
	}

	protected Wash buildWash( HttpServletRequest request ) throws WashException {
	
		Wash wash = new DefaultWash();
		
		// First look for a parameter "wash.q"
		String query = request.getParameter( "wash.q" );
		if( query != null ) {
			
			log.log( Level.FINE,"Got wash query: " + query );
			
			wash.deserialize( query );
			
			log.log( Level.FINE,"Deserialized to: " + wash.serialize() );
		}

		// Then process all other parametes 
		// and extend the rquest-wash with it.
		Map parameters = request.getParameterMap();
		for( Object parameterName : parameters.keySet() ) {
			
			String name = (String)parameterName;
			String value = request.getParameter( (String)parameterName );
			
			log.log( Level.FINE, "parse " + name + " = " + value );
				
			if( ! parameterName.equals( "wash.q" ) ) {
				
				TypeValue tv = WashTools.decodeValue( value );
				
				wash.addField( name, tv.type, tv.value );
			} else {
				wash.addField( "wash.q", Type.STRING, value );
			}
		}
		
		return wash;
	}
	
	private class ErrorWash extends DefaultWash {
		
		public ErrorWash( Wash request, String message, Throwable cause ) {
			
			try {
				addField( "wash.error", Type.STRING, message );
				addField( "wash.exception", Type.STRING, cause.getClass().getName() );
				addField( "wash.message", Type.STRING, cause.getMessage() );
				
				int i = 0;
				for( StackTraceElement trace : cause.getStackTrace() ) {
					
					addField( "wash.trace." + i, Type.STRING, trace.toString() );
					
					i++;
				}
				
				if( request != null )
					addField( "wash.request", Type.STRING, request.serialize() );
				
			} catch( WashException e ) {}
		}
	}

}