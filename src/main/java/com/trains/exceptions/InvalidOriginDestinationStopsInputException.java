package com.trains.exceptions;

public class InvalidOriginDestinationStopsInputException extends CommandLineErrorException {

    public InvalidOriginDestinationStopsInputException() {
        super("Please enter a valid origin, destination and number of stops input in the format: AB0\n" +
                "Which is two letters denoting origin and destination city names and an integer number of stops.");
    }
}
