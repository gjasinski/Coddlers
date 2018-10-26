# Coddlers-backend

## Development

### Environment setup
* Install Java 8
* Install Node.js

Using Ubuntu based OS:
```
curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash -
sudo apt-get install -y nodejs
```
Or see [https://nodejs.org/en/download/](https://nodejs.org/en/download/)

* Install Angular CLI
```
npm install -g @angular/cli
```
* Install docker 

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

## Troubleshooting

* I want to use Windows for development and I cannot run sh scripts to run db script
    
    Just run once scripts inside "if" condition to download image and initialize container
    then use ```docker start <container-name>```. Container should be in up state after os restart. 
    If not, write the command to start container again.
   
* I have an errors connected with missing getters & setters in Intellij

    You should Install Lombok plugin. Then go to settings ```Annotation processors``` and enable ```annotation processing```.
    
* Intellij don't see repository beans

    You should enable ```Sping Data``` plugin in ```Plugins```.  
    
* I have a problem with node-sass package

    Try ```sudo npm rebuild node-sass```. You have to be sure that you are using node with version >=v10.0.0 and npm >=v5.6.0.
    
* I cannot use ng cli inside Intellij

    You have to update your angular cli to v6 or greater.
