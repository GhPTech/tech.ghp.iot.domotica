package tech.ghp.iot.domotica.provider;

import junit.framework.TestCase;

/*
 * 
 * 
 * 
 */

public class DomoticaImplTest extends TestCase {
	
	/*
	 * 
	 * 
	 * 
	 */
	public void testSimple() throws Exception {
			DomoticaImpl text = new DomoticaImpl();
		
			String str1 = new String("POPOVICI");
			String str2 = new String(text.upper("Popovici"));
			assertEquals(str1,str2);
	}
}
