#!/usr/bin/env bash

check=$( sudo docker ps -a -f "name=coddlers-postgresql" | tail -n +2 )
if [ -z "$check" ]; then
  docker build -t skysurferone/postgres_coddlers .
  docker run --name coddlers-postgresql -d -p 5432:5432 skysurferone/postgres_coddlers
fi

docker start coddlers-postgresql
