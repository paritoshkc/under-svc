FROM openjdk:8u232-slim AS builder
VOLUME /tmp
ENV APPDIR=/undersvc
RUN mkdir -p $APPDIR/src/main/java
WORKDIR $APPDIR
COPY build.gradle gradlew gradlew.bat $APPDIR/
COPY gradle $APPDIR/gradle
RUN ./gradlew dependencies
COPY . .
RUN ./gradlew test
RUN ./gradlew shadowJar

FROM openjdk:8u232-jre-slim
COPY --from=builder /undersvc/build/libs/undersvc-0.0.1-all.jar /undersvc.jar
EXPOSE 8080/tcp
EXPOSE 8081/tcp
ENTRYPOINT [ "java", "-jar", "/undersvc.jar", "server" ]
