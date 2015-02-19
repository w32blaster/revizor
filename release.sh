#!/bin/bash

rm -rf release
mkdir release
grails clean

echo -e "\n1) Let's create a standalone JAR file\n"
grails prod build-standalone revizor.jar

echo -e "\n1.1) Let's create the archieve revizor-jar.tar.gz\n"
tar cvfz release/revizor-jar.tar.gz revizor-config.groovy revizor.jar

echo -e "\n2) Let's create a deployable WAR file\n"
grails war

echo -e "\n2.1) Let's create the archieve revizor-jar.tar.gz\n"
mv target/revizor-*.war ./
tar cvfz release/revizor-war.tar.gz revizor-config.groovy revizor-*.war

echo -e "\nDone. ALl the release archives are copied to 'release' directory."
