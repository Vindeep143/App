FROM base
ADD login-1.4.3.RELEASE.jar /opt/

RUN sed -i -- "s/JAR_FILE_NAME/login-1.4.3.RELEASE.jar/g" /opt/docker_setenv.sh
EXPOSE 8091

CMD ["/bin/bash", "/opt/docker_setenv.sh"]
