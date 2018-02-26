# Trains

## Building the JAR

To build the jar simply run:

`mvn package`

from the command line in the project directory. You will need to have Maven installed and on your path.

This will create a .jar file in the target directory and it will also run the tests.

## Running the app

Having built the jar it can be run by executing:

`java -jar target/<jar name>.jar <command> <command args> <route 1> (<route 2> <route 3> ... <route N>)`

from the command line. There may be as many route arguments as desired and they must be of the form:
`<city 1><city 2><distance>` eg. `AB5`.

### Commands

- `dist` - Calculate the distance of a given route, the argument must be of the form:
`<city 1><city 2>(<city 3>...<city N>)` eg. `ABCD`.
- `short` - Calculate the shortest route between two cities, the argument must be of the form:
`<origin city><destination city>` eg. `AD`.
- `stops` - Calculate the number of routes of a given number of stops that exist between two cities, the argument must be of the form:
`<origin city><destination city><number of stops>` eg. `AD3`.
- `maxstops` - Calculate the number of routes of a given number of stops or fewer that exist between two cities, the argument must be of the form:
`<origin city><destination city><maximum number of stops>` eg. `AD3`.
- `maxdist` - Calculate the number of routes of a given distance or less between two cities, the argument must be of the form:
`<origin city><destination city><maximum distance>` eg. `AD15`.

## Running the tests

To run the tests simply run:

`mvn test`

from the command line in the project directory.

The `AppTest` class contains the full app test suite using the test cases provided in the original question.

## About

This is a Spring console application for calculating distances and routes between cities.

The Spring framework is possibly overkill and not best suited for a console application, but I have used it to demonstrate experience with the framework.

