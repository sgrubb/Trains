package com.trains.exceptions;

public class InvalidShortestInputException extends CommandLineErrorException {

    public InvalidShortestInputException() {
        super("Please enter a valid shorted command input in the format: AB\n" +
                "Which is two letters denoting origin and destination city names.");
    }
}
