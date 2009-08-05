package de.uplinkgmbh.lms.business;

import static org.junit.Assert.assertTrue;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import de.axone.tools.E;

@Test( groups="lms.business.FolderList" )
public class FolderListTest {

	@AfterTest(groups = {"Inv"})
	public void folderListTest() throws Exception {
		
		String res = FolderList.generate( 377, 25, 4, "testtarget", "?testlist" );
		String testres = "<div class=\"folder\" ><a class=\"folder_arrows\" href=\"testtarget?testlist_pos=3\" >&lt;&lt;</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=1\" >1</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=2\" >2</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=3\" >3</a>\n"+
		"&nbsp;[4]\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=5\" >5</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=6\" >6</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=7\" >7</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=8\" >8</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=9\" >9</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=10\" >10</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=11\" >11</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=12\" >12</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=13\" >13</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=14\" >14</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=15\" >15</a>\n"+
		"&nbsp;<a href=\"testtarget?testlist_pos=16\" >16</a>\n"+
		"&nbsp;<a class=\"folder_arrows\" href=\"testtarget?testlist_pos=5\" >&gt;&gt;</a></div>\n";

		//E.rr( res );
		//E.rr( testres );
		
		assertTrue( res.equals( testres ) );
	}
}
