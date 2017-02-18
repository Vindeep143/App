#!/bin/bash
DOCKER_HOST_IP=$(ip addr | grep inet | grep docker0 | awk -F " " '{print $2}' | sed -e 's/\/.*$//')

docker run -d --name=consul --net=host gliderlabs/consul-server -bootstrap -bind $DOCKER_HOST_IP

docker run -d --name=registrator --net=host --volume=/var/run/docker.sock:/tmp/docker.sock \
          gliderlabs/registrator:latest -ip $DOCKER_HOST_IP consul://localhost:8500


docker run -d -p 80:80 --name=haproxy cvpn/haproxy


