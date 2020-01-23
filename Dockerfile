FROM openjdk:8u232-slim
VOLUME /tmp
ADD target/undersvc-0.0.1.jar /undersvc.jar
EXPOSE 8080/tcp
EXPOSE 8081/tcp
ENTRYPOINT [ "java", "-jar", "/undersvc.jar" ]
