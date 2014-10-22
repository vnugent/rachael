FROM fedora

MAINTAINER Viet Nguyen vnguyen@redhat.com

RUN yum -y install java-1.7.0-openjdk 
ADD rachael-jar-with-dependencies.jar /opt/
CMD ["java", "-jar", "/opt/rachael-jar-with-dependencies.jar"]
