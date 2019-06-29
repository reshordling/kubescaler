FROM adoptopenjdk/openjdk12:alpine-slim
VOLUME /tmp
COPY target/processor-1.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9999 -Djava.security.egd=file:/dev/./urandom -jar /app.jar