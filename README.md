# ttnhabbridge
Bridge between the-things-network and the habitat network, to receive telemetry from amateur balloons using LoRaWAN

# Development environment
I develop this on Windows 7, in Java 8 in Eclipse, using subversion.

Steps to install everything:
* download a Java JDK and put it in tools/jdk
* download Eclipse (I use Mars) and put in in the tools/eclipse directory (for example)
* install the eclipse SVN plugin, using this update site: https://dl.bintray.com/subclipse/releases/subclipse/4.2.x/

Steps to prepare the Eclipse environment:
* open a command line to the checked out project
* type 'env.bat' to initialise the tool paths etc
* go to directory workspace/gradle
* type 'gradlew eclipse' and watch dependencies being downloaded from the internet
* start Eclipse, using the 'workspace' directory as workspace
* under perspective 'SVN repository exploring' add the URL of this project
* import all sub-projects into the workspace

Steps to create a release:
* edit the version number in @@@TODO
* using gradle: type 'gradlew distTar' or 'gradlew distZip'
* the .tar (for Linux) or .zip (for Windows) file can be found under build/distributions

# Deployment
Steps to deploy the application:
* unzip the .zip or .tar file
* cd into the application directory
* start the .bat or .sh file
* TODO: install a systemd service file
