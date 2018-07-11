#!/usr/bin/env bash

check=$( sudo docker ps -a -f "name=coddlers-postgres-develop" | tail -n +2 )
if [ -z "$check" ]; then
  docker build -t coddlers/postgres-develop .
  docker run --name coddlers-postgres-develop -d -p 5432:5432 coddlers/postgres-develop
fi

docker start coddlers-postgres-develop
