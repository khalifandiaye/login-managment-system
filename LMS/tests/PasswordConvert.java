
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.testng.annotations.Test;

import de.axone.tools.PasswordBuilder;

@Test( groups="passwordConvert" )
public class PasswordConvert {
	
	
	public PasswordConvert(){
		
       PasswordBuilder pb = new PasswordBuilder();
       
       System.out.println( pb.hashPassword( "BzMwVrYN6avpY54E" ) );
       System.out.println( pb.hashPassword( "NrktcYjAAK07o" ) );
       System.out.println( pb.hashPassword( "XsYkiIy9YpQwk" ) );
       System.out.println( pb.hashPassword( "zksCpWLWaMl32" ) );
	}
}
