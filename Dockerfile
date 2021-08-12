ARG PORT

FROM adoptopenjdk/openjdk16:alpine
EXPOSE $PORT
WORKDIR /app
COPY . /app/src
RUN cd /app/src && ./gradlew clean build --no-daemon
RUN mv /app/src/build/libs/url-shortener-1.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
