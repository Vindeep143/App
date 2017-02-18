#!/bin/bash

HOST_IP=$(/sbin/ip route|awk '/default/ { print $3 }')

sed -i -- "s/VM_HOST/$HOST_IP/g" /opt/consul-files/haproxy.json

service rsyslog start

exec consul-template -config=/opt/consul-files/haproxy.json