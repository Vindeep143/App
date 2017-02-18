HOST_IP=$(/sbin/ip route|awk '/default/ { print $3 }')

sed -i -- "s/VM_HOST/$HOST_IP/g" /opt/envconsul.cnf

echo HOST_IP=$HOST_IP >> /opt/docker_run.sh
envconsul -config=/opt/envconsul.cnf -pristine -prefix vars env >> /opt/docker_run.sh
sed 's/^/export /' -i /opt/docker_run.sh
sed -i '1 i\#!/bin/bash' /opt/docker_run.sh

echo "exec java -jar /opt/JAR_FILE_NAME" >> /opt/docker_run.sh
cat /opt/docker_run.sh
/bin/bash /opt/docker_run.sh

