FROM openjdk:8u232-jre-slim
ADD build/libs/undersvc-0.0.1-all.jar /undersvc.jar
EXPOSE 8080/tcp
EXPOSE 8081/tcp
ENTRYPOINT [ "java", "-jar", "/undersvc.jar", "server" ]
