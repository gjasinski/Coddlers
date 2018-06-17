# Coddlers-backend

## Development

### Backend
```$ gradle build```

```$ java -jar -Dspring.profiles.active=dev build/libs/coddlers-0.0.1-SNAPSHOT.jar```

Server listen at localhost:8080

### Frontend

```$ npm install```

```$ npm start``` 

Server listen at localhost:4200

### Database 

To run docker container with postgres:

```$ sudo ./docker_init.sh```

Postgres should be available at localhost:5432. Database is named ``coddlers``.

### Documentation 

For documenting REST API Swagger is included. 
Documentation UI is available on 
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
For details how to make documentation in swagger please visit [https://github.com/swagger-api/swagger-core/wiki/annotations](https://github.com/swagger-api/swagger-core/wiki/annotations).
 
## Build Fat Jar
```$ gradle clean build```

```$ java -jar -Dspring.profiles.active=prod coddlers-0.0.1-SNAPSHOT.jar```
