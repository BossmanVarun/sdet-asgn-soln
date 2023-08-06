# SDET Assignment

### Automation Framework
The framework is written using RestAssured with TestNG in Java.
Extent Reports is used to generate reports in the `reports` folder.
In order to use the framework, Java and Maven will be required.
If using IntelliJ embedded Maven will do the trick.

## Steps:
1. Open a new project in IntelliJ.
2. Build using embedded maven to resolve dependencies.
3. Select suites/tests.xml and run using testng plugin.

## Alternatively, if mvn is available on the CLI:
1. `cd` to root dir of project.
2. run `mvn clean test -DsuiteFile=suites/tests.xml`.
3. Report will be generated in the `reports` folder.

Test Cases are located [here](https://docs.google.com/spreadsheets/d/1H0ucDBTo52b7WLIZ6gjoJt5DVoyfoS94nrb9MDoTYHM/edit#gid=0)
