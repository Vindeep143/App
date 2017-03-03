FROM base
ADD register-1.4.3.RELEASE.jar /opt/

RUN sed -i -- "s/JAR_FILE_NAME/register-1.4.3.RELEASE.jar/g" /opt/docker_setenv.sh
EXPOSE 8090

CMD ["/bin/bash", "/opt/docker_setenv.sh"]
