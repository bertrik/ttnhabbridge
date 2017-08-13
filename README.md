# ttnhabbridge
Bridge between the-things-network and the habitat network, to receive amateur balloons using LoRaWAN

== Development environment ==
I develop this on Windows 7.

Steps to install everything:
* check out this archive, e.g. using tortoisegit
* download a Java JDK and put it in tools/jdk
* download Eclipse (I use Mars) and put in in the tools/eclipse directory (for example)
* install the egit plugin in eclipse, using this update site: http://download.eclipse.org/egit/updates/

Steps to prepare the Eclipse environment:
* open a command line to the checked out project
* type 'env.bat' to initialise the tool paths etc
* go to directory workspace/gradle
* type 'gradlew eclipse' and watch dependencies being downloaded from the internet
* start Eclipse, using the 'workspace' directory as workspace
* import all sub-projects into the workspace

Steps to create a release:
* edit the version number in @@@TODO
* using gradle: type 'gradlew distTar' or 'gradlew distZip'
* the .tar or .zip file can be found under build/distributions
