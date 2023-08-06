#!/bin/bash
go run main.go &
cd qa/
mvn clean install
java -jar target/qa-1.0-SNAPSHOT-jar-with-dependencies.jar