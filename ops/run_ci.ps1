# Assumes working directory is the root of the project

docker-compose --progress tty --file .\ops\docker-compose.ci.yml --project-name fullstackci up --remove-orphans --force-recreate --abort-on-container-exit
docker-compose --progress tty --file .\ops\docker-compose.ci.yml --project-name fullstackci down
