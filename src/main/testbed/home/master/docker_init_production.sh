#!/usr/bin/env bash

check=$( sudo docker ps -a -f "name=coddlers-postgres-master" | tail -n +2 )
if [ -z "$check" ]; then
  docker build -t coddlers/postgres-master .
  docker run --name coddlers-postgres-master -d -p 5431:5432 coddlers/postgres-master
fi

docker start coddlers-postgres-master
