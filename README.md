finra-test
==========

Simple REST service for storing files and associated metadata using Spring and MongoDB.

## Execution

### Starting
To start the service, simply issue the following command: ```docker-compose up```.

This will start the MongoDB instance as well as build and run the Spring Boot service.

To start it in daemon mode, pass the option ```-d``` to the compose call.

### Stopping
To stop the service, simply Ctrl-C or, if started in daemon mode, then issue the ```docker-compose down``` command.

## API

#### Upload file
http://localhost:8080/upload
http://localhost:8080/v1/upload

Expects multipart post where the file is parameter ```file``` and all other parameters are assumed to be metadata.
Returns the ID generated for the file.

#### Retrieve file metadata
http://localhost:8080/metadata/{id}
http://localhost:8080/v1/metadata/{id}

Given a file ID, it returns all metadata associated with the file.

#### Retrieve file
http://localhost:8080/file/{id}
http://localhost:8080/v1/file/{id}

Given a file ID, it initiates a content stream of the file.

#### Retrieve IDs of files matching metadata
http://localhost:8080/search
http://localhost:8080/v1/search

A get request where the query parameters are the search criteria matching the metadata.
Returns a list of matching file IDs.

## Configuration

All configuration is located in the ```application.properties``` file.

Available configuration options:

|Property|Default value|Description|
|--------|-------------|-----------|
|upload.location|/var/cache/finra-test|Location on disk where the files will be stored **This location must exist and be writable!!**|
|metadata.database|mongodb|The type of database to connect to. Only ```mongodb``` is supported|
|spring.data.mongodb.host|mongodb|Hostname to use to connect to the mongodb instance|
|alert.new-item.poll-rate.mins|3600000|The rate in which it should poll for new items in minutes|
|alert.new-item.email|<none>|The email address to send new item alerts|
|spring.mail.host|<none>|Must be filled in for email to work|
|spring.mail.port|<none>|Must be filled in for email to work|
|spring.mail.username|<none>|Must be filled in for email to work|
|spring.mail.password|<none>|Must be filled in for email to work|
|spring.mail.properties.mail.smtp.auth|<none>|Must be filled in for email to work|
|spring.mail.properties.mail.smtp.starttls.enable|<none>|Must be filled in for email to work|

## Testing

To run very basic unit tests, issue a ```./gradlew test``` command.

To run a very basic end-to-end integration test, execute the command ```e2e/test```.
