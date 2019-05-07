# ttnhabbridge
Bridge between the-things-network and the habitat network, to receive telemetry from amateur balloons using LoRaWAN

See https://revspace.nl/TTNHABBridge for a more detailed description of this project.
See https://travis-ci.org/bertrik/ttnhabbridge for the Travis CI status

# Development environment
I develop this on Windows 7, in Java 8 in Eclipse.

Steps to install everything:
* download a Java 11 JDK and put it in tools/jdk
* download Eclipse and put in in the tools/eclipse directory (for example)
* install the eclipse SVN plugin, using this update site: https://dl.bintray.com/subclipse/releases/subclipse/4.2.x/

Steps to prepare the Eclipse environment:
* open a command line to the checked out project, enter the 'ttnhabbridge' directory
* type 'env.bat' to initialise the tool paths etc
* enter directory gradle
* type 'gradlew eclipse' and watch dependencies being downloaded from the internet
* start Eclipse, using the 'workspace' directory as workspace
* import sub-projects (ttnhabbridge, cayenne) into the workspace

Steps to create a release:
* edit the version number in @@@TODO
* using gradle: type 'gradlew distTar' or 'gradlew distZip'
* the .tar (for Linux) or .zip (for Windows) file can be found under ttnhabbridge/build/distributions

# Deployment
Steps to deploy the application:
* unzip the .zip or .tar file
* cd into the application directory
* start the .bat or .sh file
* install the systemd service file, if desired (instructions inside the .service file)
