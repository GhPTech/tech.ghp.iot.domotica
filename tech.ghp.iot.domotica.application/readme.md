# TECH GHP IOT DOMOTICA APPLICATION

${Bundle-Description}

##Create application project

From main menu select *File/New/Bndtools Projects*. Select the *OSGI enRoute* template and give the name *tech.ghp.iot.domotica.application*.

##Code

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

##HTML resources

Some static resources for the *Javascript code* and *CSS* are also provided for the web application, stored in the directory *~/static*. These resources are directly mapped to the root, i.e. a resource with the path *static/abc/def* will be available as */abc/def*. 

The recommendation is to create a static direction with the application PID name in static.

	 static/
	 			tech.ghp.iot.domotica
				index.html
				...


The *static/tech.ghp.iot.domotica/index.html* directroy contains the single page HTML root. It defines a header, view area, and a footer. 

The *tech.ghp.iot.domotica/main/htm* directory contains html fragments that are inserted in the main page depending on the URI. 

These resources use macros from the build environment.

##Automatic Resources

The file *index.html* file contains the following entries:

	 <link 
	 		rel="stylesheet" 
	 		type="text/css"
			href="/osgi.enroute.webresource/${bsn}/${Bundle-Version}/*.css">

	 <script 
			src="/osgi.enroute.webresource/${bsn}/${Bundle-Version}/*.js">
	 </script>

OSGi enRoute automatically insert any CSS or Javascript code required (through annotation) for the bundle. Additionally, at the end it will add code in bundle’s web directory.

##Runtime

The requirements of the *runtime* need to be defined in the *tech.ghp.iot.domotica.bndrun* file. These can be defined from the *Run* tab. By default, the requirements are specified via annotations, the application *tech.ghp.iot.domotica.application* is listed in the initial requirements. Other bundles can be added from the *Browse Repos* list (the left side of the *Run* tab). 

As the *Run Requirements* list is complete, the *Resolve* button needs to be pushed in order to get the bundles required by the runtime. This will set the *Run Bundles* list. The *-runbundles* section of the script is overwritten every time the *Resolve* button is pushed (manual editing of the *-runbundles* section would be lost).

Save the file *tech.ghp.iot.domotica.bndrun*. 

Push the button *Debug OSGi* at the right top of the window.

The application is running at the address 

    http://192.168.x.xx:8080/tech.ghp.iot.domotica
    
By pushing the button *To Upper* will transform the string inserted in the dialog window to upper case.

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

##Executable

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
    
##Update the Application project

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

## References

