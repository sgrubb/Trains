package com.trains.exceptions;

public class InvalidDistanceInputException extends IllegalArgumentException {

    public InvalidDistanceInputException() {
        super("Invalid distance command input.");
    }
}
