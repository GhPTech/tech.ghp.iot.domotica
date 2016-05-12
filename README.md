<h1><img src="http://www.ghp.tech/images/GhP_brand.png" witdh=20px style="float:left;margin: 0 1em 1em 0;width:40px">
IoT Domotica application</h1>

This repository is a typical Internet of Things (IoT) application based on [OSGi][1] framework. The application is developed in the [Eclipse][2] environment with [Bndtools][3] templates following [OSGi enRoute][4] tutorials. The workspace has support for [continuous integration][5] with [gradle][6].

The application is based on [OSGi enRoute IoT tutorial][7]. Its purpose is to control a hardware with the help of a [Raspbery Pi][8]. In this tutorial a LED is turned on and off based on an event triggered by a physical push-button and a programmed schedule.

The chip [BCM2835][9] controls and gets input from hardware. The pins of this chip are assigned and registered with the [Pi4J GpioController][11]. The bundle [OSGi enRoute][12] bundle *osgi.enroute.iot.pi.provider* (a version of the [Pi4j][10] library) is employed for this purpose.

##Prerequisites

The following software is needed:
* [Java 8][12]
* [Eclipse Mars][13]
* [Git][14]
* [Bndtools][15] version 3.1.0 or later.

The necessary hardware consist of:

* Raspberry Pi 2 model B setup with [NOOBS][16]
* LEDs 
* resistors \pm 220\Omega 
* breadboard
* push-buttons
* male-female wires

##The Bnd Workspace

A *Bnd workspace* is a directory with a *cnf* folder and a number of projects. The *cnf* folder contains a *build.bnd* file and a *ext* directory which together define the workspace properties. The *Bnd workspace* is imports bundles (and JARs) from repositories and it exports bundles to the same or another repository. Within this folder, the projects are private and cohesive so that they can share information via the *cnf* project.

###The GitHub workspace

The *GitHub workspace* is intended for sharing on [GitHub][17]. The *bnd* template workspace can be obtained from [OSGi worksapce repository on GitHub][18] or [GhPTech worksapce repository on GitHub][19].

	 $ cd  ~/git
	 $ git init tech.ghp.iot.domotica
	 $ cd tech.ghp.iot.domotica
	 $ git fetch --depth=1 https://github.com/osgi/workspace master
	 $ git checkout FETCH_HEAD -- .

###The Eclipse workspace

The *Eclipse workspace* stores *metadata* with personal preferences and history, which should not be shared through GitHub. *Bnd* is able to build independent of Eclipse preferences.

In order to create/select an *Eclipse workspace*, select _File/Switch Workspace/Other ..._ fill in the path accordingly. For the present tutorial the *Eclipse Workspace* should be as follows:

*~/eclipse/tech.ghp.iot.domotica*

###Workbench view

In order to get the *Workbench* view, push the *Workbench* button from On the top right corner of *Eclipse* window.

###Import the *Bnd Workspace*

The recommended way to work with projects in Eclipse is to not store them in the Eclipse workspace folder but import them from the *~\git* workspace.

From the context menu (right click) select 
	 
*@/Import -> General/Existing Projects into Workspace*

then select the corresponding *Bnd Workspace* from *GitHub*. For the present tutorial, the following path:

*~/git/tech.ghp.iot.domotica*

This will import the cnf project into your Eclipse workspace.

###The *Bndtools* perspective

The *Bndtools* perspective gives access to specific *Bnd* tools. For this perspective, from the main menu, select 

*Window/Perspective/OpenPerspective/Bndtools*

##Application project

From main menu select *File/New/Bndtools Projects*. Select the *OSGI enRoute* template and give the name *tech.ghp.iot.domotica.application*.


###Code

The OSGI enRoute template provides a template source code for the application in the file */src/DomoticaApplication.java*.

	 package tech.ghp.iot.domotica.application;
	 
	 import org.osgi.service.component.annotations.Component;
	 
	 import osgi.enroute.configurer.api.RequireConfigurerExtender;
	 import osgi.enroute.google.angular.capabilities.RequireAngularWebResource;
	 import osgi.enroute.rest.api.REST;
	 import osgi.enroute.twitter.bootstrap.capabilities.RequireBootstrapWebResource;
	 import osgi.enroute.webserver.capabilities.RequireWebServerExtender;
	 

	 @RequireAngularWebResource(resource={"angular.js","angular-resource.js", "angular-route.js"}, priority=1000)
	 @RequireBootstrapWebResource(resource="css/bootstrap.css")
	 @RequireWebServerExtender
	 @RequireConfigurerExtender
	 @Component(name="tech.ghp.upper")
	 public class DomoticaApplication implements REST {

			public String getUpper(RESTRequest req, String string) throws Exception{
			return string.toUpperCase();
			}

It includes web resources for our application like Angular, Bootstrap, and the web extender that serves the static pages. 
The component annotation that makes this object a *Declarative Services service component*. A service component is automatically registered as a service when it implements an interface and it can depend on other services.

The *UpperApplication* component implements the *REST* interface and is thus registered as a *REST service*. The contract of this service indicates that any public method in this class becomes available as a *REST end-point*. 

The *getUpper* method is for the *GET* method and it is mapped from the /rest/upper URI. Since it accepts a single argument, we can specify the word we want to upper case as /rest/upper/<word>. More information about the *REST API* can be found in the [service catalog](http://enroute.osgi.org/services/osgi.enroute.rest.api.html).

###HTML resources

Some static resources for the *Javascript code* and *CSS* are also provided for the web application, stored in the directory *~/static*. These resources are directly mapped to the root, i.e. a resource with the path *static/abc/def* will be available as */abc/def*. 

The recommendation is to create a static direction with the application PID name in static.

	 static/
	 			tech.ghp.iot.domotica
				index.html
				...


The *static/tech.ghp.iot.domotica/index.html* directroy contains the single page HTML root. It defines a header, view area, and a footer. 

The *tech.ghp.iot.domotica/main/htm* directory contains html fragments that are inserted in the main page depending on the URI. 

These resources use macros from the build environment.

###Automatic Resources

The file *index.html* file contains the following entries:

	 <link 
	 		rel="stylesheet" 
	 		type="text/css"
			href="/osgi.enroute.webresource/${bsn}/${Bundle-Version}/*.css">

	 <script 
			src="/osgi.enroute.webresource/${bsn}/${Bundle-Version}/*.js">
	 </script>

OSGi enRoute automatically insert any CSS or Javascript code required (through annotation) for the bundle. Additionally, at the end it will add code in bundle’s web directory.

###Runtime

The requirements of the *runtime* need to be defined in the *tech.ghp.iot.domotica.bndrun* file. These can be defined from the *Run* tab. By default, the requirements are specified via annotations, the application *tech.ghp.iot.domotica.application* is listed in the initial requirements. Other bundles can be added from the *Browse Repos* list (the left side of the *Run* tab). 

As the *Run Requirements* list is complete, the *Resolve* button needs to be pushed in order to get the bundles required by the runtime. This will set the *Run Bundles* list. The *-runbundles* section of the script is overwritten every time the *Resolve* button is pushed (manual editing of the *-runbundles* section would be lost).

Save the file *tech.ghp.iot.domotica.bndrun*. 

Push the button *Debug OSGi* at the right top of the window.

The application is running at the address 

    http://192.168.x.xx:8080/tech.ghp.iot.domotica
    
By pushing the button *To Upper* will transform the string inserted in the dialog window to upper case.

###Executable

In order to run the application on an arbitrary environment, an executable JAR can be 
created. The JAR include all the dependencies of the launcher and the framework. 

The executable JAR is created by selecting the *project_name.bndrun* file with the *Run* tab and by pushing the *Export* button form the right top side of the window. This will initiate an *Export Wizard Selection* in which, the name and the path of the executable JAR file are chosen. 

The created executable JAR can be run from the command line at the chosen location:

    cd ~/[chosen_path]

The Java version at the target environment should be verified (Java version 1.8.0 is required):

    java -version
 
Make sure that no other framework is running on the target environment.

The executable JAR can be run with the command:

    java -jar tech.ghp.iot.domotica.jar
    
The application can be used/accessed from the address:

    http://192.168.x.xx:8080/tech.ghp.iot.domotica

To stop the application do *Control-C* at the command line.

    ^C

##Service structure

The purpose of the *DomoticaApplication* class is to be an interface of the underlying services. It is not recommended to provide functionality to such interfaces. 

*OSGi enRoute* has a special templates for various projects types:

* Application projects *.application*, bind together a set of components and parametrizes them
* API projects *.api*, defines the service
* Provider projects *.provider*, *.adapter*, implements a project
* Test Projects *.test*, runs tests inside the framework

###API Project

An Application Programming Interface (API) is recommended in order to separate interface code from implementation code. The API  is like a service contract. The best practice is to give service contracts their own projects so they can be easily shared. For API projects, the sufix of the name should be *.api*. OSGi enRoute recognises this suffix and will select the appropriate layout for the API project.

To create the API project, from the main menu select

*File/New/Bndtools Project*

select the *OSGi enRoute* template and give the following name:

*tech.ghp.iot.domotica.api*

The *OSGI enRoute* template creates a project with all the options set to treat it as an API. The templates creates the *Domotica* interface in the right package. The *Domotica* class should specify that the *Domotica* interface converts an input string to upper case as follows:

     package tech.ghp.iot.domotica.api;
	
	 import org.osgi.annotation.versioning.ProviderType;

	 /**
 	 * A service that turns a string to uppercase
 	 */
	 @ProviderType
	 public interface Domotica {
	 	/**
	 	* Turn a string into upper case
	 	*/
	
	 	String upper(String input);
	}

In the *generated* folder of the project, the file *tech.ghp.iot.domotica.api.jar* is created. This file contains the *Manifest*, which was generated by *bnd*. 
The *Manifest* shows that the package *tech.ghp.iot.domotica.api* is exported. This means that this package is available for other bundles. The *Contents* tab of the *bnd.bnd* file shows the *Export packages* list.  

![tech.ghp.iot.domotica.api](http://enroute.osgi.org/img/tutorial_base/project-create-5.png "The box with rounded corners represents a bundle; the inside black box represents an exported package. The package 'tech.ghp.upper.api' is exported and made available to other bundles. ")

The *version* of this package is defined in the file *package-info.java*. 

    @org.osgi.annotation.versioning.Version("1.0.0")
    package tech.ghp.iot.domotica.api;

The modifications of this package should be reflected in the specified version. Any modification that affects the public API must result in a rebuild of the bundle of the *provider*(the party that implements the *Domotica* interface). For example, if the version goes to *1.1*, the bundels that implemented *1.0* should no longer be compatible. In order to assure that the proper version ranges are used, the following annotation should be added:

    @ProviderType
    public interface Domotica { ... }
    
By making this annotation, the *bndtool* will automatically ensure that the *providers/implementers* of the respective interface  use semantic versioning to import the package with a minor range, *1.0, 1.0*.

The default implemented interfaces of the API (usually listener like interfaces) are *consumer* type and could be annotated as

    @ConsumerType
    
The *bnd* project is driven by the *bnd.bnd* file, which defines the *version*, the *build path* and which packages should go into the bundle. For example, the source of the *bnd.bnd* file is as follows:

    Bundle-Version:1.0.0.${tstamp}
    Bundle-Description: 				\
	    This is  project. An API project should in general not contain any \
	    implementation code.\
	    \
	    ${warning;Please update this Bundle-Description in tech.ghp.iot.domotica.api/bnd.bnd}

     Export-Package:  \
	     tech.ghp.iot.domotica.api;provide:=true


     Require-Capability: \
	     compile-only

    -buildpath: \
	     osgi.enroute.base.api;version=1.0

    -testpath: \
	    osgi.enroute.junit.wrapper;version=4.12


###Provider Project

As the API (the service contract) is defined, a provider (an implementation) of the proposed service is required. The *provider* implements the *API* so the users (the clients) can employ the *service*. The *provider projects* have the *.provider* suffix. The root name should be the same as the workspace name. 

To create a *provider* project, from the main menu select

*File/New/Bndtools Project*

select the *OSGi enRoute* template and give the following name:

*tech.ghp.iot.domotica.provider*

The *OSGi enRoute* template creates the *DomoticaImpl.java* file, which includes the *@Component* annotation in order to setup the *Declarative Service (DS)* and the *DomoticaImpl* class.

####Implementation

As the *DomoticaImpl* component class implements the *Domotica* interface, this implementation needs to be registed as an *Domotica* service. This registration is specified as follows:

    @Component(name = "tech.ghp.iot.domotica")
    public class DomoticaImpl implements Domotica { }

#####Build Path

As the *Domotica* interface is not a part of the *provider project*, the compilation and the build of the code cannot be carried out. A *build path* to the *api project* has to be provided to the *bnd.bnd* file.

The *buildpath* can be edited with the *Build* tab of the *bnd.bnd* file by adding the  *tech.ghp.iot.domotica.api* bundle. Alternatively, the *buildpath* can be edited with the *Source* tab as 

    -buildpath: \
	    osgi.enroute.base.api;version=1.0,\
	    tech.ghp.iot.domotica.api;version=latest

#####Imports

The *bnd.bnd* files defines the dependencies of the bundle. It consists of 

* *Private packages*: packages only available the current bundle;
* *Exported packages*: packages exported to other bundles;
* *Imported packages*: packages provided to the current bundle;

In order to recognise the *Domotica* type, the class *DomoticaImpl* has to import the package *tech.ghp.iot.domotica.api* to get the *Domotica* interface.

    import tech.ghp.iot.domotica.api.Domotica;

In this case, the user of the *Domotica* service would depend on two bundles: *tech.ghp.iot.domotica.api* and *tech.ghp.iot.domotica.provider*. 

![tech.ghp.iot.domotica.api/tech.ghp.iot.domotica.provider](http://enroute.osgi.org/img/tutorial_base/provider-imports-1.png "The box with rounded corners represents a bundle; the inside black box represents an exported package. The bundle is importing the *tech.ghp.iot.domotica.api* package to get the *Domotica* interface.")

To avoid this double dependency, the *API* bundle can be exported to the *provider* bundle. In order to export the *API* bundle, add the package *tech.ghp.iot.domotica.api* to the *Export Packages*. 

![tech.ghp.iot.domotica.api/tech.ghp.iot.domotica.provider](http://enroute.osgi.org/img/tutorial_base/provider-imports-3.png "The box with rounded corners represents a bundle; the inside black box represents an exported package. By exporting the *com.acme.prime.eval.api*, the bundle *com.acme.prime.eval.provider* is independent.")

#####Code

The *Domotica* interface specifies the *upper* method as follows

    public interface Domotica {
	     String upper(String input);
    }

This method needs to be provided by the *DomoticaImpl* class.

For this trivial example, the provider only has to return the input and convert it to uppercase with the function *toUpperCase* (already available in the Java library of the method *input*):

    @Component(name = "tech.ghp.iot.domotica")
    public class DomoticaImpl implements Domotica {
    	public String upper(String input){
				return input.toUpperCase();
			}
	}

The *provider* can both implement and/or be a *client* of the interfaces in a *service* package. The *provider* and the *client* roles have consequences on the versioning of the packages. Any change in the *service* contract (API) require an update of the *privider* (implementation).

The best practice in OSGi for a *provider* is to include *service* (API) codes and export it. However, the *provider* code and the *api* codes should not be part of the same project because the compilation of the *api* would expose the *provider* (implementation) code.

#####Test the Provider with (Standard) JUnit

The implementation (the provider) of the service should be tested before it is released. Testing saves time and resources in the later phases of the development process.  

A *provider* should have *JUnit* tests. These tests have access to private information about the implementation details and proprieties of the components that are not part of the public API. When these tests fail they prohibit the release of the project. 

The *OSGi enRoute template* includes a test case in the *test* directory. The *DomoticaImplTest* test is placed in the same package as the *DomoticaImpl* class, but is not part of the bundle. As it is in the same package, it has access at the private information of the *DomoticaImpl* class. 

For the present template a simple test will be run as follows:

    package tech.ghp.iot.domotica.provider;
    import junit.framework.TestCase;

    public class DomoticaImplTest extends TestCase {
    
    		public void testSimple() throws Exception {
					DomoticaImpl text = new DomoticaImpl();
					
					String str1 = new String("POPOVICI");
					String str2 = new String(text.upper("Popovici"));
					assertEquals(str1,str2);
			}
	}

To run the test from the *context menu* (right click) select

*@Run As/JUnit Test*    

For debugging, *breakpoints* can be inserted at the code lines where the running programe should stop. Tu run a test in *debug* mode, select

*@Debug As/JUnit Tests*

The *JUnit* runner creates a new *VM* with the same *build-path* as the class it tests. All the tests dependencies must be on the *build-path* in order to run the tests. 

* *JUnit 4.x* requires annotations on the test class. Extensions of *TestCase* in the class are not necessary (annotations are ignored).
* *JUnit 3.x* must extend *TestCase* (usually the easiest way to write tests)

###Update the Application project

As initially drafted, the *application* bundle had no dependencies on the *Domotica* service defined by the *api* bundle (and implemented by the *provider* bundle).

In order to refer to the defined service, the *application* bundle should include the *api* bundle to the build path. 

Convention is to place references at the end of the *application* class. Reference to the *Domotica* service can be made as follows:

    @Reference
    Domotica 		upper;

The *@Reference* annotation creates a dependency on this service. The *DomoticaApplication* component is not initiated until the service registry contains an *Domotica* service.

Now the initial instruction to return a string to upper case,

    return string.toUpperCase();
    
is replaced by the instruction to return *upper* string as declared in *Domotica* class implemented in *DomoticaImpl* class.

Finally, the reference to  *tech.ghp.iot.domotica.api* should be provided to the *application* bundle. This can be done by pushing the *Resolve* button of the *tech.ghp.upper.bndrun* file of the *tech.ghp.iot.domotica.application* project. 

Check if the bundle *tech.ghp.iot.domotica.provider* was added to the list of *Run Bundles*. 

###Test in OSGi

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

##Continous Integration

The current application *tech.ghp.iot.domotica* is build by Eclipse IDE. This is useful for local usage and development, however the philosophy of *open source* and *collaborative development* should be considered. Moreover, when application building is carried out on the personal system, many dependencies accumulate. These dependencies are not suitable for *collaborative development* and *open source*. Automatic building from the command line should also be carried out.

###Automatic building

*OSGi enRoute* includes a full *Gradle* build. The *gradlew* script downloads the appropriate *gradle* version (Java version 1.8 should be available):

    $ cd ~/git/tech.ghp.iot.domotica
    $ java -version
    java version "1.8.0"
    Java(TM) SE Runtime Environment ...
    $ ./gradlew
    Downloading https://b../biz.aQute.bnd-latest.jar to 
    /Users/aqute/git/tech.ghp.iot.domotica/cnf/cache/biz.aQute.bnd-latest.jar ... 
    :help

The possible *gradle tasks* can be obtained with the command
    
    $./gradlew tasks

To automatically builds the application *tech.ghp.upper* from the command line, the following command is used:

    $./gradlew export.tech.ghp.iot.domotica
    
This will create and store the JAR *tech.ghp.iot.domotica.jar* on the path 
*tech.ghp.iot.domotica.api/generated/distributions/executable/*

###Sharing

In order share the development, the project can be pushed to the *GitHub*. 

On the GitHub home page [GitHub](https://github.com/GhPTech), a new repository has to be created. Name this repository *tech.ghp.upper*, add a short description 'An example workspace fro the osgi.enroute base tutorial' and set the repository as 'Public' (do not set initialise the 'README' file, do no add 'gitnore' and license).

[!GitHub Repository](http://enroute.osgi.org/img/tutorial_base/ci-github-1.png "Create a New Repository dialog")


In order to connect to the local repository *!/git/tech.ghp.iot.domotica*, the 'SSH URI' *git@github.com:GhPTech/tech.ghp.iot.domotica.git* needs to be copied, then on the command line the following commands should be launched:

    $ cd ~/git/tech.ghp.iot.domotica
	$ git add .
	$ git commit -m "message associated with the commit"
	$ git remote add origin https://github.com/GhPTech/tech.ghp.iot.domotica.git
	$ git push -u origin master

For further commits and pushes the following commands are used:

    $ git add .
	$ git commit -m "[commit message]"
	$ git push -u origin master

###Continous Integration

The *bnd workspace* is setup to be built continuously with *Travis CI*. 

From the *Travis CI* website [!https://travis-ci.org](https://travis-ci.org "Travis CI website") *create an account* based on GitHub credentials (or *login*). 

Once logged in, select the *Repositories* tab. The *Sync Now* button should be pushed in order to update the latests changes from *GitHub*. Find the 'com.acme.prime' repository and push the *ON* button. Every push will now be automatically build the repository 'com.acme.prime' on the *Travis IC* server. 

In order to trigger the build on *Travis IC* server, a change in the *tech.ghp.upper* repository is made. The warning from the *bnd.bnd* file of the provider project is commented as follows:

    Bundle-Version:					1.0.0.${tstamp}
    Bundle-Description: 				\
			A bundle with a provider. Notice that this provider exports the API package. \
			It also provides a JUnit test and it can be run standalone. \
			\
			#${warning;Please update this Bundle-Description in tech.ghp.iot.domotica.provider/bnd.bnd}

The change is *saved* and *pushed* to the *GitHub* server. The *Travis IC* notices the difference and launches a new automatic build of the repository.

##Debugging

Debugging can be done by setting *breakpoints* and *single step*. A new bundle is generated and gets deployed with every change of the bundle. If changes are made to the code (and saved), a new bundle is generated and gets deployed. If more requirements are defined (or removed) in the *bndrun* file, the respective bundels get deployed (or stopped). 

For example if a change is made to the code to return *lower case* string instead of *upper case*

	 public String getUpper(RESTRequest req, String string) throws Exception {
			return string.toLowerCase();
	}

the running framework will include this change.

N.B.
A warning from Eclipse informs about the changes. The *Continue* button should be pushed.

If there are Javascript or html fragment changes, the page in the browser needs to be refreshed to reload the changes.

Every application project has a *[project_name].bndrun* file and a *debug.bndrun* file.

The principle 'Don't Repeat Yourself (DRY)' is applied. The *debug.bndrun* file inherits from the *[project_name].bndrun* file (information already available in the later file is aslo available in the former file) and it includes additional bundles supporting the debugging process. 

As *debug.bndrun* inherits from *[project_name].bndrun*, it has no specified requirements in *Run Requirements*. However, the listed bundels to be added to the debug mode runtime  (*Run Bundles*) should be obtained by pushing *Resolve* button. This will add bundels like Web Console, XRay, etc.

Once the *debug.bndrun* file is saved, the application can be run in debug mode by pushing the button *Debug OSGi*. This run in trace mode, which provides detailed information about the launch process and the ongoing updates. This is triggered by the *-runtrace* flag. This can be set to *false* or removed, if this information is not required.

The *Web Console* can be accessed at the address:

    http://192.168.x.xx:8080/system/console

The *ID* and the *password* are defined by Apache Felix as

    User id: admin
    Password: admin
    
These debugging tools provide valuable insights about the running application. For example, a visualisation of the service layer can be obtained with the *XRay* web console plugin. The running system can be inspected on a browser at the address:

	 http://192.168.x.xx:8080/system/console/xray
	 
![XRay](http://enroute.osgi.org/img/tutorial_base/debug-xray-1.png "A visualisation of the service layer can be obtained with the *XRay* web console plugin. ")

The *triangle* represents the *service*. Connections to the *point* of the triangle indicate the *registration* (points to the object that receives the method calls from the service users). The *base* of the triangle indicate the *client(s)* (call the methods of the service). The *sides* of the triangle indicate *listeners* of the service.

![service symbols](http://enroute.osgi.org/img/tutorial_base/debug-service-0.png "Graphical representation of services in XRay")

The following color codes are used:

* yellow: service is registered and in use
* white, solid border: service is registered and not in use
* white, dashed border: service is not registered and needed by other bundles
* red: serious failure of the service
* orange: service is exported or imported  

The *triangles* represent service *types*, not *instances*.

*XRay* also tracks eventual errors logged by the bundles. It marks the bundle with a warning sign and the log message.

Various *tabs* and *subtabs* can be selected in order to get an overview of the running framework:

* Main
	* HTTP Service
	* X-Ray
* OSGi
	* Bundles
	* Configurations
	* Log Service
	* Services
* Web Console 
	* Licenses
	* System Information
	
The *XRay* plugin returns a page with *Javascript* that pulls the server at given time intervals. The server returns the data which is rendered by a javascript library (d3.js).

Each service type is represented by a rounded corner yellow box.

##Connecting to Raspberry Pi

To connect to the *Raspebery Pi* the following command is used:

    $ ssh pi@192.168.x.xx
    
To check the Java version (1.8.0 required) use the command:    
    
    pi@raspberry ! $java -version

[Just Another Package for Java](https://jpm4j.org/#!/) needs to be installed on the *Raspberry Pi*:
    
    pi@raspberrypi ~ $ curl https://bndtools.ci.cloudbees.com/job/bnd.master/719/artifact/dist/bundles/biz.aQute.jpm.run/biz.aQute.jpm.run-3.0.0.jar >jpm.jar    
    pi@raspberrypi ~ $ sudo java -jar jpm.jar init
    pi@raspberry ~ $ jpm version

*Remote debugging* from *bnd tools* requires and agent running on *Raspbery Pi*. For this purpose the package *biz.aQute.remote.main* needs to be installed:

    pi@raspberrypi ~ $ sudo jpm install -f biz.aQute.remote.main@*
    
To launch the *bndremote* program in Java debug mode and listen to port 1044, the following command is used:
    
    pi@raspberrypi ~ $ sudo bndremote -n 192.168.x.xx
    
The *-n* option indicates the host on which the agent is run on. Alternatively the *-a* command can be employed:

	 pi@raspberrypi ~ $ sudo bndremote -a
	 
The *-a* option indicates that *bndremote* should listen to all interfaces, not just localhost.

If the interfaces to listen to are not specified, only the agent from the localhost (same machine) is used.

##Remote Debugging

The *OSGi enRoute Application* creates also the files *tech.ghp.iot.bndrun* and *debug.bndrun*. The later inherits the former and adds debug bundles. The default (OSGi) port for *Http Server* is *8080*. However this can be changed in the *tech.ghp.iot.bndrun* file. For example to use the port *9090* for the *Http Server*, the following line needs to be added from the *Source* tab of the *tech.ghp.iot.bndrun*:

    -runproperty: or.osgi.service.http.port = 9090 

Additionally *-runpath* and *-runremote*  need to be specified:

    -runpath: biz.aQute.remote.launcher
    -runremote: \
				pi; \
				jdb=1044; \
				host=192.168.x.xx; \
				shell=-1
				 
N.B. The remote debugging specifications such as *-runpath: biz.aQute.remote.launcher* is compatible only with the *Debug As: Bnd Native Launcher* mode. The options *Run As* mode, *Run OSGi*, *Debug OSGi* *Export* or *Export* modes generate errors at the runtime such as     

    Error: Could not find or load main class.  

##Command Package

The *OSGi enRoute* template creates a class *DomoticaApplication* within the package *tech.ghp.iot.domotica.application*, which is a web application with REST. However, another component is created to communicate with the hardware (Rapberry Pi). 

From the main menu select 

*File/New/Package*

and name the package *tech.ghp.iot.domotica.command*

Within this package create a new Java class *DomoticaCommand*. The newly created class has the following code by default:

    package tech.ghp.iot.domotica.command;

    public class DomoticaCommand {

	 }

The *DomoticaCommand* package has to be added to the *Private Packages* list of the *bnd.bnd* file.

###Component annotation

The annotation *@Component* is necessary befor the declaration of the class

	 @Component
	 public class DomoticaCommand {
	 ...

The annotation *Component* need to be imported from *org.osgi.service.component.annotations*
	 
	 import org.osgi.service.component.annotations.Component;

###Sending a message from Raspberry Pi

A simple way to interact with hardware is to send a message from the remote agent. 
    
    @Component
    public class DomoticaCommand {
    
    		@Activate
			void activate() {
					System.out.println("Hello!");
			}

			@Deactivate
	 		void deactivate() {
					System.out.println("Goodbye!");
			}

	 }

The annotations *Activate* and *Deactivate* needs to be imported from *org.osgi.service.component.annotations*.

	 import org.osgi.service.component.annotations.Activate;
	 import org.osgi.service.component.annotations.Deactivate;


The dependencies of this application to be *Resolved* from the *Run* tab of the  *tech.ghp.iot.domotica.bndrun* and *debug.bndrun* specifications. 

To launch the application, from the context menu (right click) of the *debug.bndrun* file, select

*@Debug As/Bnd Native Launcher*

This cause *bnd* to start the debugger and look for a remote agent on the specified host.

N.B. The remote agent (192.168.x.xx) should be running *bndremote* and display the message: 

    Listening for transport dt_socket at address: 1044

    ____________________________
	Welcome to Apache Felix Gogo

	 g! 2016-04-12 10:52:43.887:INFO::pool-3-thread-2: Logging initialized @2713135ms
	2016-04-12 10:52:44.695:INFO:oejs.Server:pool-3-thread-2: jetty-9.2.12.v20150709
	2016-04-12 10:52:45.937:INFO:oejsh.ContextHandler:pool-3-thread-2: Started o.e.j.s.ServletContextHandler@1b77ed6{/,null,AVAILABLE}
	2016-04-12 10:52:45.945:INFO:oejs.Server:pool-3-thread-2: Started @2715197ms
	2016-04-12 10:52:46.286:INFO:oejs.ServerConnector:pool-3-thread-2: Started ServerConnector@782931{HTTP/1.1}{0.0.0.0:8080}
	[INFO] Started Jetty 9.2.12.v20150709 at port(s) HTTP:8080 on context path /
	Hello!

###Continous update

While the remote agent is running, changes can be made to the application. For example we can modify the message sent to Raspberry Pi.

	 @Activate
	 void activate() {
	 		System.out.println("Hello! ");
	 		System.out.println("I am a Raspberry Pi ");
	 }
			
As the changes are saved, the launcher reloads and calculates a *delta* of the proprieties file (bnd.bnd) and applies it to the running framework and the updated messages are update on the command line of the remote agent.

##Getting information from Raspberry Pi

###The GpioController

In order to get the hardware information (such as serial number, model, etc.), the bundle *osgi.enroute.iot.pi.provider* is used. This bundle contains the [Pi4j](http://pi4j.com) library, which is based on [WiringPi](http://wiringpi.com), a native code library to control the *BCM2835* chip from Raspberry Pi.

The bundle *osgi.enroute.iot.pi.provider* can be found in the *Respositories* list from the *Run* tab of the *bnd.bnd* file. To add it on the *Run Requirements* list, the *drag and drop* function of Eclipse can be used. Alternatively, it can be added manually from the *Source* tab:

    -runrequires:\
    osgi.identity;filter:='(osgi.identity=tech.ghp.iot.domotica.application)',\
    osgi.identity;filter:='(osgi.identity=osgi.enroute.iot.pi.provider)'

Once this modification is saved to the file *bnd.bnd*, from the *debug.bndrun* file, the dependencies need to be *Resolved*.

###Access to GpioController types

The *GpioController* types also need to be added to the *buidpath* of the 'bnd.bnd' file.

	 -buildpath: \
			osgi.enroute.base.api,\
			osgi.enroute.logger.simple.provider,\
			osgi.enroute.web.simple.provider;version=1.2,\
			osgi.enroute.iot.circuit.provider,\
			osgi.enroute.iot.pi.provider,\
			tech.ghp.iot.domotica.api
			
			
###Throwing exceptions

If an event that disturbs the execution of a method occurs, the user should be notified. 

![Throwing Exception](http://i.stack.imgur.com/4ppVR.gif "Throwing Exceptions")

Notification of these exceptions can be coded as:

    void activate() throws Exception {
    ...
    }
    
###Getting the board type
    
The board type can be obtained by the following code line:
	 
    ...
	System.out.println("I am a Rasberry Pi "+ SystemInfo.getBoardType().name());	
	...		
	
###Getting serial number
    
The model can be obtained by the following code line:
	 
    ...
	System.out.println("Serial " + SystemInfo.getSerial());
	...		

###Blinking a LED

Blinking a LED is a trivial example of hardware control. The schema for this scenario is shown here below:

![Blinking LED schema](http://enroute.osgi.org/img/tutorial_iot/exploring-led-schema-1.png "Blinking LED schema (with Fritzing)")

The positive side of the LED is connected to the *GPIO0* pin of the Raspberry Pi. To protect this pin (and the LED), a *220\Omega* resistor is connected to the negative side of the LED, then connected to the *Ground* of the Raspberry Pi. As the voltage supplied by pin *GPIO0* is *3.3V*, the current passing through the circuit is limited to *15mA*.

The *Pi4J* map of *Raspbery Pi model B+* *GPIOs* is shown here below:

![Raspberry Pi GPIO ](http://pi4j.com/images/j8header-b-plus.png "Raspberry Pi Model B+ GPIO")

Note that *Pi4j* GPIOs numbering differs from that of *Raspberry*.

The experimental model of the circuit above can be realised with a breadboard as shown in the image below. 

![Blinking LED breadboard](http://enroute.osgi.org/img/tutorial_iot/exploring-led-breadboard-1.png "Blinking LED breadboard")

Note that LEDs (Light Emitting Diode) are unidirectional (pass electricity only in one direction). The positive (+) side of the LEDs is the longer leg. Another way to identify the positive and the negative side of is to look at the sides of the LED and identify a flat edge. The flat edge corresponds to the negative side of the LED.

The code necessary to blink the LED is described here below.

The code that might throw and exception is enclosed within a *try* block. The exception handlers are associated by providing a *catch* block after the *try* block.

     try {
     		'try code'
     } catch (ExceptionType name){'catch code'}
     
Each *catch* block handles the type of exception indicated by its argument. The argument *ExceptionType* must be the name of the class that inherits from the *Throwable* class. The handler can refer to the exception with *name*. The *catch* block contains code that is executed when the exception handler is invoked. 

The following 'try code' and 'catch code' is used for the *activate* method:

	 try {
     		GpioController gpioController = GpioFactory.getInstance();
						
				while (!gpioController.getProvisionedPins().isEmpty()) 
							gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());
				
				GpioPinDigitalOutput out = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED1",PinState.LOW);
				
				System.out.println("'LED1' is connected to the GPIO_00 and it's blinking every 500ms");
		} catch (Exception e) {e.printStackTrace();}

Exception handlers can do more then just print error messages or halt the program. They can do error recovery, prompt the user to make decision, or propagate the error up to a higher-level handler using [chained exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/chained.html).

The *activate* method is a static API. This loop is needed to clear out any pins that were already provisioned before, then the pin needs to be created while a specific GPIO is chosen (*GPIO00*). However, this code design has major issues in a shared environment. As a specific pin was chosen to control the LED, it is possible that other bundles assigns the same pin (*GPIO00*) for other purposes. In that case the new bundle clears the pin already claimed by the current bundle. 

A *schedule* and a *scheduler* are used to set the state of the pin *GPI_00*. The state is set to *true* and *false* every 1000 milliseconds.

	 private Scheduler scheduler;
	 private Closeable schedule;

	 ...

	 schedule = scheduler.schedule(() -> {
			boolean high = out.getState().isHigh();
			out.setState(!high);
			}, 1000);
			
	 ...		

In the *deactivate* method, the open *schedule* should be closed.

	 @Deactivate
	 void deactivate() throws IOException {
			 schedule.close();
		     System.out.println("Goodbye World!");
	 }

The reference to *scheduler* should also be made

	 @Reference
	 void setScheduler(Scheduler scheduler) {
			 this.scheduler = scheduler;
	 }	 

[1]:  http://osgi.org/
[2]:  https://eclipse.org/
[3]:  http://bndtools.org/
[4]:  http://enroute.osgi.org/book/150-tutorials.html
[5]:  http://enroute.osgi.org/tutorial_base/800-ci.html
[6]:  https://www.gradle.org/
[7]:  http://enroute.osgi.org/tutorial_iot/050-start.html
[8]:  https://www.raspberrypi.org/products/raspberry-pi-2-model-b/
[9]:  https://www.raspberrypi.org/documentation/hardware/raspberrypi/bcm2835/README.md
[10]: http://pi4j.com
[11]: http://pi4j.com/example/control.html
[12]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[13]: https://www.eclipse.org/downloads/
[14]: https://git-scm.com/book/en/v2/Getting-Started-Installing-Git
[15]: http://bndtools.org/installation.html#update-site
[16]: https://www.raspberrypi.org/blog/new-raspbian-and-noobs-releases/
[17]: https://github.com
[18]: https://github.com/osgi/workspace
[19]: https://github.com/GhPTech/workspace