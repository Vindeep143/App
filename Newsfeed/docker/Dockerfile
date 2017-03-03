FROM base
ADD tweet-1.4.3.RELEASE.jar /opt/

RUN sed -i -- "s/JAR_FILE_NAME/tweet-1.4.3.RELEASE.jar/g" /opt/docker_setenv.sh
EXPOSE 8092

CMD ["/bin/bash", "/opt/docker_setenv.sh"]
