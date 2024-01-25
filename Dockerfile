FROM eclipse-temurin:21-jdk-jammy

RUN addgroup --gid 1000 app \
   && adduser --uid 1000 --gid 1000 app
USER app
WORKDIR /app

#ENV HOME /app
#COPY --chown=app:app .mvn/ .mvn
#COPY --chown=app:app mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#
#COPY --chown=app:app src/ src/
#
#CMD ["./mvnw", "clean spring-boot:run"]

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar" "/app.jar"]
