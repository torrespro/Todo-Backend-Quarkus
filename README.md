# ToDo-with-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework to build an API for a ToDo application adhering to the
spec described at [https://www.todobackend.com/](https://www.todobackend.com/).
The project is deployed on [Render](https://todo-quarkus-ep2a.onrender.com/todos)


You can see it working with the provided UI [Here](https://www.todobackend.com/client/index.html?https://todo-quarkus-ep2a.onrender.com/todos) 
You can verify the test are passing here [API tests](https://www.todobackend.com/specs/index.html?https://todo-quarkus-ep2a.onrender.com/todos)

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Requirements

To compile and run this demo you will need:
- GraalVM - see [our Building native image guide](https://quarkus.io/guides/building-native-image-guide)
- Apache Maven `3.5.3+`

In addition, you will need either a PostgreSQL database, or Docker to run one.

If you don't have GraalVM installed, you can download it here:

<https://github.com/oracle/graal/releases>

Installing GraalVM is very similar to installing any other JDK:
just unpack it, add it to your path, and point the `JAVA_HOME`
and `GRAALVM_HOME` environment variables to it.

You should then use this JDK to run the Maven build.


## Building the demo

After having set GraalVM as your JVM, launch the Maven build on
the checked out sources of this demo:

> mvn package

## Running the demo

### Using DevServices

If you want to use DevServices then all you need to do is include the relevant extension for the type of database you want (either reactive or JDBC, or both), and don’t configure a database URL, username and password, Quarkus will provide the database and you can just start coding without worrying about config.

https://quarkus.io/guides/datasource#dev-services

### Prepare a PostgreSQL instance

First we will need a PostgreSQL database; you can launch one easily if you have Docker installed:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:11.5

Alternatively you can set up a PostgreSQL instance in any another way.

The connection properties of the Agroal datasource are configured in the standard Quarkus configuration file, which you will find in
`src/main/resources/application.properties`.


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw quarkus:dev
```

## Running the test harness 

With the app running locally, navigate to [this
  link](https://www.todobackend.com/specs/index.html?http://localhost:8080/todos)
to run the tests for the ToDo API

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `todo-with-quarkus-1.0.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/todo-with-quarkus-1.0.0-SNAPSHOT-runner.jar`.

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.
