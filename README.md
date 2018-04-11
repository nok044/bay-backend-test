# BAY Backend test

This project is develop for deliver registration service for mobile application. This service develop from Spring Boot

## Prerequisites

- Java 8 or newer
- Maven
- PostgreSQL database

## Installation

Please edit database configuration from src/main/resources/application.properties then
> mvn package

to build jar package, jar file will be place in target/bay-backend-test-1.0.jar

## Running the tests

To run jar package use this command
> java -jar bay-backend-test-1.0.jar

To invoke REST service use curl or other REST client, in this example i'll use curl

### Register service
> curl -d "username=username&password=password&address=address&phone=66848806606&salary=50000" -i -X POST "http://localhost:8080/api/register"

### Current User service with username and password
> curl --user username:password -i -X GET "http://localhost:8080/api/currentUser"

### Current user service with X-JWT-TOKEN
> curl -H "X-JWT-TOKEN: token-from-above-resquest" -i -X GET "http://localhost:8080/api/currentUser"