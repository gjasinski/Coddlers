#!/bin/bash
echo "Deploy coddlers application"
PID=$(jps -lV | grep "coddlers-develop" | awk '{print $1;}')
echo "PID: $PID"
if [[ "$PID" = "" ]]
then
	echo "Not found any coddlers application running"
else
	echo "Found coddlers application runnning with PID: $PID"
	kill $PID
	echo "Coddlers application killed"
fi

echo "Copy aplication to /home/develop"

cp build/libs/coddlers-0.0.1-SNAPSHOT.jar /home/develop/coddlers-develop-0.0.1-SNAPSHOT.jar

echo "Run coddlers application"
chown production /home/develop/coddlers-develop-0.0.1-SNAPSHOT.jar
java -jar -Dspring.profiles.active=dev-tb -Dserver.port=8080 /home/develop/coddlers-develop-0.0.1-SNAPSHOT.jar > /home/develop/logs/logs$(date +%F_%H-%M-%S).txt &

