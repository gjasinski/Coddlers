#!/bin/bash
echo "Deploy coddlers application"
PID=$(jps -lV | grep "coddlers-master" | awk '{print $1;}')
echo "PID: $PID"
if [[ "$PID" = "" ]]
then
	echo "Not found any coddlers application running"
else
	echo "Found coddlers application runnning with PID: $PID"
	kill $PID
	echo "Coddlers application killed"
fi


echo "Run coddlers application"
sudo -H -u production bash -c 'java -jar -Dserver.port=8000 -Dspring.profiles.active=prod /home/master/coddlers-master-0.0.1-SNAPSHOT.jar > /home/master/logs/logs$(date +%F_%H-%M-%S).txt &'
