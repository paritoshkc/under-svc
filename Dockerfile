FROM openjdk:8u232-jre-slim
RUN mkdir -p /app/conf
ADD build/libs/undersvc-0.0.1-all.jar /app/undersvc.jar
ADD config.yml /app/conf/config.yml
EXPOSE 8080/tcp
EXPOSE 8081/tcp
ENTRYPOINT [ "java", "-jar", "/app/undersvc.jar", "server", "/app/conf/config.yml"]
