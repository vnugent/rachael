FROM java:8-jre-alpine

ENV APP_HOME=/home/rachael
WORKDIR ${APP_HOME}

RUN mkdir -p ${APP_HOME} && \
    chgrp -R 0 ${APP_HOME} && \
    chmod -R g+rw ${APP_HOME}

ADD target/rachael.jar docker-start.sh ${APP_HOME}/

CMD ["sh", "-c", "${APP_HOME}/docker-start.sh"]
