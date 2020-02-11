#!/usr/bin/env bash
echo "Creating a native executable"
mvn package -Pnative -Dquarkus.native.container-build=true
echo "Building a container that runs the Quarkus application in native (no JVM) mode"
docker build -f src/main/docker/Dockerfile.native -t quarkus/todo-with-quarkus .
echo "Tagging the previously built docker image"
docker tag quarkus/todo-with-quarkus registry.heroku.com/todo-quarkus/web
echo "Pushing the docker image to the Heroku container registry"
docker push registry.heroku.com/todo-quarkus/web
echo "Telling Heroku to run the image"
heroku container:release web -a todo-quarkus


