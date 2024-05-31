# Spring Telegram Bot for NasaAPOD API
This Telegram bot was created as a pet-project to familiarize with Spring Boot and can be used to get the APODs for desired date/range/count(less than/or equal 100) from https://api.nasa.gov/planetary/apod.

## Technologies:
* Spring Boot 3.2.5
* Lombok
* Spring Data Jpa
* Liquibase
* Eureka discovering (with using Feign client)
* Opendocs with Swagger-UI

## Simple getting started:
>1. Start up your database (docker, native, etc...)
>2. Configure datasource properties, telegram-bot name and telegram-bot token (telegram-bot-service.yaml) in config-service module 
>3. Run config-server
>4. Run eureka-discovery-server
>5. Run microservices (telegram-bot-service and nasa-api-service)
>6. Profit!
>7. Optionally run api-gateway for using Swagger-UI

## Default ports:
8761 - eureka-discovery-server <p>
8765 - api-gateway <p>
8888 - config-server <p>

## Swagger-UI:
http://localhost:8765/webjar/swagger-ui/index.html
