# ttnhabbridge
Bridge between TheThingsNetwork/Helium and the habitat network,
to receive telemetry from amateur balloons using LoRaWAN

See https://revspace.nl/TTNHABBridge for a more detailed description of this project.
See https://travis-ci.org/bertrik/ttnhabbridge for the Travis CI status

# Development environment
I develop this on in Java 11 in Eclipse.

Steps to install everything:
* download a Java 11 JDK and put it in tools/jdk
* download Eclipse and put in in the tools/eclipse directory (for example)

Steps to prepare the Eclipse environment:
* open a command line to the checked out project, enter the 'ttnhabbridge' directory
* type 'env.bat' to initialise the tool paths etc
* type 'gradlew eclipse' and watch dependencies being downloaded from the internet
* start Eclipse, using the 'workspace' directory as workspace
* import sub-projects (ttnhabbridge, cayenne) into the workspace

Steps to update source code from github:
* in the top-level 'ttnhabbridge' directory, enter 'git pull'

Steps to create the executable package:
* in the root directory of the project,  type './gradlew assemble' (or just 'gradlew assemble' on Windows)
* the .tar (for Linux) and .zip (for Windows) file can be found under ttnhabbridge/build/distributions

# Deployment
Steps to deploy the application:
* unzip the .zip or .tar file
* cd into the application directory
* start the .bat or .sh file, if no .yaml configuration file exists, a default one is generated
* edit and install the systemd service file, if desired (instructions inside the .service file)
