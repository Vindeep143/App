FROM ubuntu:14.04
ADD envconsul.cnf /opt/
ADD docker_setenv.sh /opt/


RUN sudo -E apt-get update
RUN sudo -E apt-get install -y python-software-properties software-properties-common
RUN sudo -E add-apt-repository -y ppa:webupd8team/java
RUN sudo -E apt-get update
RUN sudo -E apt-get install -y unzip
RUN sudo -E apt-get install -y wget
RUN sudo -E apt-get install -y make

RUN echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections
RUN sudo -E apt-get -y install oracle-java8-installer


RUN wget https://releases.hashicorp.com/envconsul/0.6.1/envconsul_0.6.1_linux_amd64.zip -O /envconsul.zip
RUN unzip /envconsul.zip -d /usr/local/bin
RUN rm /envconsul.zip


RUN rm -rf /var/lib/{apt,dpkg,cache,log}/
RUN rm -rf /var/cache

