FROM openjdk:8

EXPOSE 8080

RUN mkdir /app
WORKDIR /app

COPY . /app

# read in application.properties and setup upload-location properly
RUN mkdir -p $(awk -F= '$1 ~ /upload.location/ { print $2 }' application.properties)
RUN chmod 777 $(awk -F= '$1 ~ /upload.location/ { print $2 }' application.properties)

RUN ./gradlew build

CMD java -jar build/libs/finra-test-0.1.0.jar
