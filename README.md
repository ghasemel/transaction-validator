# Transaction Validation Service

Given a file with bank transaction records, create a validation service that reads the file and gives a report with the invalid records.  
The file could either be in CSV or JSON format. An example of each is included.

## How to run tests
`mvn clean test`

## How to build
`mvn clean install`

## How to run app
`java -jar transaction-validation.jar`

Configuration parameters that can be adjusted in config.properties file:
- app.input.dir=input/
- app.report.dir=report/
- app.processed.dir=processed/
- app.error.dir=error/
- app.input.interval=1000

### Tools/Technologies
* Java 11
* JUnit 5
* Maven

### Github Repository Link
[https://github.com/ghasemel/transaction-validator](https://github.com/ghasemel/transaction-validator)