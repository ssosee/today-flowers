#!/bin/bash

echo "# jar stop"

SPRING_PID=$(pgrep -f today-flowers-0.0.1-SNAPSHOT.jar)

kill -9 $SPRING_PID

echo “# remove logfile”

rm -rf /usr/local/log/*

echo “# git pull”
cd /home/ec2-user/app/step1/today-flowers
git pull

echo "# start build!"

./gradlew clean build

cd build/libs

nohup java -jar today-flowers-0.0.1-SNAPSHOT.jar &

echo "# finish deployment..."

exit 0