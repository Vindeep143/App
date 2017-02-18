FROM ubuntu:14.04


RUN apt-get update && apt-get install -y haproxy wget unzip
RUN wget https://releases.hashicorp.com/consul-template/0.11.1/consul-template_0.11.1_linux_amd64.zip -O ct.zip
#ADD ct.zip /
RUN unzip ct.zip -d /usr/local/bin
RUN rm /ct.zip

RUN mkdir -p /opt/consul-files/
ADD haproxy.cfg /opt/consul-files/
ADD haproxy.json /opt/consul-files/
ADD help.sh /opt/consul-files/

EXPOSE 80
EXPOSE 1936

RUN chmod +x /opt/consul-files/help.sh

CMD ["/bin/bash", "-c" ,"/opt/consul-files/help.sh"]
