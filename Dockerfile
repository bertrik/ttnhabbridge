# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine
MAINTAINER Bertrik Sikken bertrik@gmail.com

ADD ttnhabbridge/build/distributions/ttnhabbridge.tar /opt/

WORKDIR /opt/ttnhabbridge
ENTRYPOINT /opt/ttnhabbridge/bin/ttnhabbridge

