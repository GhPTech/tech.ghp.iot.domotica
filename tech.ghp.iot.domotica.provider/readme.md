# TECH GHP IOT DOMOTICA PROVIDER

${Bundle-Description}

## Create provider project

As the API (the service contract) is defined, a provider (an implementation) of the proposed service is required. The *provider* implements the *API* so the users (the clients) can employ the *service*. The *provider projects* have the *.provider* suffix. The root name should be the same as the workspace name. 

To create a *provider* project, from the main menu select

*File/New/Bndtools Project*

select the *OSGi enRoute* template and give the following name:

*tech.ghp.iot.domotica.provider*

The *OSGi enRoute* template creates the *DomoticaImpl.java* file, which includes the *@Component* annotation in order to setup the *Declarative Service (DS)* and the *DomoticaImpl* class.

###Implementation

As the *DomoticaImpl* component class implements the *Domotica* interface, this implementation needs to be registed as an *Domotica* service. This registration is specified as follows:

    @Component(name = "tech.ghp.iot.domotica")
    public class DomoticaImpl implements Domotica { }

####Build Path

As the *Domotica* interface is not a part of the *provider project*, the compilation and the build of the code cannot be carried out. A *build path* to the *api project* has to be provided to the *bnd.bnd* file.

The *buildpath* can be edited with the *Build* tab of the *bnd.bnd* file by adding the  *tech.ghp.iot.domotica.api* bundle. Alternatively, the *buildpath* can be edited with the *Source* tab as 

    -buildpath: \
	    osgi.enroute.base.api;version=1.0,\
	    tech.ghp.iot.domotica.api;version=latest

####Imports

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

####Code

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

####Test the Provider with (Standard) JUnit

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

## Example

## Configuration

	Pid: tech.ghp.iot.domotica
	
	Field					Type				Description
		
	
## References

