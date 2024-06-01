#!/bin/bash

# This will start all the services required for tests or running the service.
# All services will have their output prefixed with the name of the service.

ctrl_c() {
    docker compose down

    echo "===> Shutting down Labamap Service"
    kill "$labamap_pid"


    lsof -ti tcp:8080 | xargs kill
    lsof -ti tcp:8081 | xargs kill
    lsof -ti tcp:9000 | xargs kill
    lsof -ti tcp:9001 | xargs kill
    lsof -ti tcp:8085 | xargs kill

}


lsof -ti tcp:9000 | xargs kill
lsof -ti tcp:9001 | xargs kill
lsof -ti tcp:8085 | xargs kill
lsof -ti tcp:8080 | xargs kill
lsof -ti tcp:8081 | xargs kill


cd /Users/admin/MyKalix/labamap && docker compose down &

echo "===> Starting Docker Compose Up"
cd /Users/admin/MyKalix/labamap && docker compose up &

sleep 30

echo "===> Starting Labamap Service"
cd /Users/admin/MyKalix/labamap && mvn compile exec:exec -Dlogback.configurationFile=src/main/resources/logback.xml &
labamap_pid=$!



sleep 30

bash set_attributes.sh

bash set_channel_attributes_mapping_for-shopify.sh

# sleep 30

# bash set_channel_metadata_for-shopify.sh

# sleep 30
# open -n -a /Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --args --user-data-dir="/tmp/chrome_dev_test" --disable-web-security


trap ctrl_c INT

wait $labamap_pid 

echo "===> All services stopped"


