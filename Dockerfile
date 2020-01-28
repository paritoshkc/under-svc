FROM openjdk:8u232-slim AS builder
VOLUME /tmp
WORKDIR /app
ADD build.gradle gradlew* /app/
ADD gradle/wrapper /app/gradle/wrapper
RUN chmod +x ./gradlew
RUN ./gradlew dependencies
COPY . .
RUN ./gradlew test
RUN ./gradlew shadowJar

FROM openjdk:8u232-jre-slim
COPY --from=builder /app/build/libs/undersvc-0.0.1-all.jar /undersvc.jar
EXPOSE 8080/tcp
EXPOSE 8081/tcp
ENTRYPOINT [ "java", "-jar", "/undersvc.jar", "server" ]
