FROM adoptopenjdk/openjdk16:alpine
WORKDIR /app
COPY . /app/src
RUN cd /app/src && ./gradlew clean build --no-daemon
RUN mv /app/src/build/libs/url-shortener-1.0.jar /app/app.jar
ENTRYPOINT ["java","-Xmx512M", "-jar", "/app/app.jar"]
