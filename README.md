# SDET Assignment

### Automation Framework
The framework is written using RestAssured with TestNG in Java.
Extent Reports is used to generate reports in the `target/reports` folder.
In order to use the framework, [Java](https://www.java.com/en/download/help/download_options.html) and [Maven](https://maven.apache.org/install.html) will be required.
If using IntelliJ embedded Maven will make do.

## Pre-requisites
1. Ensure that the application is up and running on `localhost:8080`

## Steps:
1. `cd` to root dir of project.
2. Run `mvn install`. This will be create a jar for execution of tests in the `target` folder.
3. Run `cd target` and run `java -jar qa-1.0-SNAPSHOT-jar-with-dependencies.jar`
4. Report will be generated in the `target/reports` folder.
5. Report will be automatically opened post completion of test run.

## Alternatively, if using IntelliJ:
1. Open a new project in IntelliJ.
2. Build using embedded maven to resolve dependencies.
3. Select suites/tests.xml and run using testng plugin.

Test Cases are located [here](https://docs.google.com/spreadsheets/d/1H0ucDBTo52b7WLIZ6gjoJt5DVoyfoS94nrb9MDoTYHM/edit#gid=0). Bugs are highlighted in yellow.
