package com.trains.exceptions;

public class InvalidOriginDestinationInputException extends CommandLineErrorException {

    public InvalidOriginDestinationInputException() {
        super("Please enter a valid origin and destination input in the format: AB\n" +
                "Which is two letters denoting origin and destination city names.");
    }
}
