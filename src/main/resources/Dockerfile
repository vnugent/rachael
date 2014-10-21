FROM fedora

MAINTAINER Viet Nguyen vnguyen@redhat.com

RUN yum -y install java-1.7.0-openjdk 
ADD ../../../target/rachael-jar-with-dependencies.jar /opt/
CMD ["java", "-jar", "rachael-jar-with-dependencies.jar"]
