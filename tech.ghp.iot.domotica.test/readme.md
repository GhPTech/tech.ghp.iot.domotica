# TECH GHP IOT DOMOTICA TEST

${Bundle-Description}

##Test in OSGi

In order to test the *service contracts*, *OSGi JUnit tests* are run. A sperate project is needed for testing. The contentent of these tests should be available to other projects. 

Select *New/Bndtools Project/*, choose *OSGi enRoute* template and name it *tech.ghp.iot.domotica.test*. 

The *OSGi enRoute* template provides a simple test case with *Bundle Context*, but does not include the *Upper* service.

    package tech.ghp.iot.domotica.test;
    import org.junit.Assert;
    import org.junit.Test;
    import org.osgi.framework.BundleContext;
    import org.osgi.framework.FrameworkUtil;
    public class DomoticaTest {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    
    @Test
    public void testDomotica() throws Exception {
    	Assert.assertNotNull(context);
    	}
    }

In order to include the *Domotica* service the following code is needed:

    <T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T,T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		return st.waitForService(1000);
	}

Assertion of service existence is also needed:

    Assert.assertNotNull(getService(Domotica.class));

As the *Domotica* class is recognised by the current project, the *tech.ghp.iot.domotica.api* project needs to be included on the *BuildPath* of the *bnd.bnd* file (*tech.ghp.iot.domotica.test* project). 
Also *import* of the *Upper* and the *ServiceTracker* classes need to be done into the source code *DomoticaTest* file.

    import org.osgi.util.tracker.ServiceTracker;
    import tech.ghp.upper.api.Domotica;

Finally, the dependencies need to be *resolved* from the *Run* tab of the *bnd.bnd* file. As a result the following bundles will be added into the *Run Bundles* list:

    -runbundles: \
		tech.ghp.iot.domotica.provider;version=snapshot,\
		tech.ghp.iot.domotica.test;version=snapshot,\
		org.apache.felix.configadmin;version='[1.8.6,1.8.7)',\
		org.apache.felix.log;version='[1.0.1,1.0.2)',\
		org.apache.felix.scr;version='[2.0.0,2.0.1)',\
		org.eclipse.equinox.metatype;version='[1.4.100,1.4.101)',\
		org.osgi.service.metatype;version='[1.3.0,1.3.1)',\
		osgi.enroute.hamcrest.wrapper;version='[1.3.0,1.3.1)',\
		osgi.enroute.junit.wrapper;version='[4.12.0,4.12.1)'

To run the test select the *bnd.bnd* file and from the *context menu* (right click) select 

*@/Debug As/Bnd OSGi Test Launcher (JUnit)*

As it is, the test only checks if the service exists. Other tests can be added as necessary. For example, to check if the string *Popovici* is converted to upper case string *POPOVICI*, the following test is added:

    @Test
    public void testText() throws Exception {
    	Assert.assertEquals( new String("POPOVICI"), getService(Domotica.class).upper("Popovici"));

This test case can be run separately from *context menu* of *testText* function as *Debug As/Bnd OSGi Test Launcher (JUnit)*.

Test bundles are marked with the heander *Test-Cases*. This header contains a list with class names that contain *JUnit* tests. The *Source* tab of the *bnd.bnd* file contains the header 

    Test-Cases: ${test-cases}

The *${test-cases}* macro is set by OSGi enRoute template, which either extend the *junit.framework.TestCase* class or use the JUnit 4 annotations like *@Test*. In the present example, the expansion is as follows (*generated/tech.ghp.iot.domotica.test.jar*):

     Test-Cases: tech.ghp.iot.domotica.test.DomoticaTest
     
When a OSGI JUnit test is launched, bnd creates a new framework with the set run bundles. On the class path (for the current framework), bundle *aQute.junit* is added as well as the JARs listed on the *-testpath*. The features available for running a *usual framework* are also available for running a *test framework*, e.g. *-runproprieties*, *-runtrace*.

Once all bundles are started, the *aQute.junit* bundle searches for the header *Test-Cases*, loads the classes and run the specified tests.

If the tests are run from *Eclipse JUnit* framework, the *bnd* sets up a new framework and passes a set of classes/methods that Eclipse has chosen from a given selection. The *aQute.junit* will then execute only those classes/methods and report the results back to Eclipse for the JUnit view.

## Example

## References

