#!/bin/bash

# extract current application version version from properties file
APP_VERSION=$(tail -n 1 application.properties | grep "app.version" | cut -d'=' -f 2)
RELEASE_DIR="releases"

rm -rf "$RELEASE_DIR"
mkdir "$RELEASE_DIR"
grails clean


# build standalone release
echo -e "\n\n1) Let's create a standalone JAR file\n"
grails prod build-standalone revizor.jar

echo -e "\n\n1.1) Let's create the archieve revizor-jar.tar.gz\n"
tar cvfz "$RELEASE_DIR"/revizor-jar-"$APP_VERSION".tar.gz revizor-config.groovy revizor.jar


# build WAR release
echo -e "\n\n2) Let's create a deployable WAR file\n"
grails prod war

echo -e "\n\n2.1) Let's create the archieve revizor-jar.tar.gz\n"
mv target/revizor-*.war ./revizor.war
tar cvfz "$RELEASE_DIR"/revizor-war-"$APP_VERSION".tar.gz revizor-config.groovy revizor.war

rm revizor.war
echo -e "\nDone. ALl the release archives are copied to '$RELEASE_DIR' directory.\n\n"
