package de.uplinkgmbh.lms.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

@Test( groups="lms.tokenaizer" )
public class TokenazierTest {

	@AfterTest(groups = {"Inv"})
	public void buildToken() throws Exception {
		
		LMSToken token = new LMSToken();
		token.application = "test";
		token.userId = 1;
		
		String t1 = Tokenaizer.buildAESToken( token );
		LMSToken t2 = Tokenaizer.restoreLMSToken( t1.getBytes() );
		
		assertEquals( token.application, t2.application );
		assertEquals( token.genDate, t2.genDate );
		assertEquals( token.magicString, t2.magicString );
		assertTrue( token.userId == t2.userId );
	}
}
