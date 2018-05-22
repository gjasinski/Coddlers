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

```$ sudo ./docker_init.sh```

## Build Fat Jar
```$ gradle clean build```

```$ java -jar -Dspring.profiles.active=prod coddlers-0.0.1-SNAPSHOT.jar```
