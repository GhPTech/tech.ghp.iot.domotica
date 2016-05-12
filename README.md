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

The *GitHub workspace* is intended for sharing on [GitHub][17]. The *bnd* template workspace can be obtained from [OSGi workspace repository on GitHub][18] or [GhPTech workspace repository on GitHub][19].

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

##Service structure

The purpose of the *DomoticaApplication* class is to be an interface of the underlying services. It is not recommended to provide functionality to such interfaces. 

*OSGi enRoute* has a special templates for various projects types:

* Application projects *.application*, bind together a set of components and parametrizes them
* API projects *.api*, defines the service
* Provider projects *.provider*, *.adapter*, implements a project
* Test Projects *.test*, runs tests inside the framework


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

Once logged in, select the *Repositories* tab. The *Sync Now* button should be pushed in order to update the latests changes from *GitHub*. Find the *tech.ghp.iot.domotica* repository and push the *ON* button. Every push will now be automatically build the repository *tech.ghp.iot.domotica* on the *Travis IC* server. 

In order to trigger the build on *Travis IC* server, a change in the *tech.ghp.upper* repository is made. The warning from the *bnd.bnd* file of the provider project is commented as follows:

    Bundle-Version:					1.0.0.${tstamp}
    Bundle-Description: 				\
			A bundle with a provider. Notice that this provider exports the API package. \
			It also provides a JUnit test and it can be run standalone. \
			\
			#${warning;Please update this Bundle-Description in tech.ghp.iot.domotica.provider/bnd.bnd}

The change is *saved* and *pushed* to the *GitHub* server. The *Travis IC* notices the difference and launches a new automatic build of the repository.

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