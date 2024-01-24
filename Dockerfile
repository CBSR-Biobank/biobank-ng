FROM eclipse-temurin:21-jdk-jammy

#RUN useradd app -s /bin/bash -u 1001 -G 100
RUN addgroup --gid 1000 app \
    && adduser --uid 1000 --gid 1000 app
USER app

WORKDIR /app

ENV HOME /app
COPY --chown=app:app .mvn/ .mvn
COPY --chown=app:app mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY --chown=app:app src/ src/

CMD ["./mvnw", "spring-boot:run"]
