#!/bin/sh

# Assumes working directory is the root of the project

docker build --tag fullstackkotlinnextjs .
docker-compose -f .\ops\docker-compose.yml -p fakeprod up -d
docker ps -a
