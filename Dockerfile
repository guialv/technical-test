FROM openjdk:11

ARG APP_FILE_PATH_ARG

ARG APP_FILE_PATH=APP_FILE_PATH_ARG
ENV CUSTOM_USER=dockeruser

RUN mkdir -p /app/bin && \
    addgroup --system $CUSTOM_USER && \
    adduser --system --no-create-home --shell /bin/false --ingroup $CUSTOM_USER $CUSTOM_USER

ADD $APP_FILE_PATH /app/bin/app.jar

RUN chown -R $CUSTOM_USER:$CUSTOM_USER /app && \
    echo "java -Xmx512m -Xms512m -jar /app/bin/app.jar" > container-entrypoint.sh && \
    chmod +x container-entrypoint.sh && \
    mv container-entrypoint.sh /app/bin/

USER $CUSTOM_USER:$CUSTOM_USER

WORKDIR /app/bin

ENTRYPOINT ["/bin/sh", "container-entrypoint.sh"]