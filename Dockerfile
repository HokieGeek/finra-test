FROM openjdk:8

RUN mkdir /app
COPY . /app
WORKDIR /app

RUN ./gradlew build

EXPOSE 8080

CMD java -jar build/libs/finra-test-0.1.0.jar
