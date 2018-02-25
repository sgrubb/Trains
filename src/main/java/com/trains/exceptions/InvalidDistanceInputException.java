package com.trains.exceptions;

public class InvalidDistanceInputException extends CommandLineErrorException {

    public InvalidDistanceInputException() {
        super("Please enter a valid distance command input in the format: ABC...\n" +
                "Which can be any number of letters denoting city names.");
    }
}
