package com.trains.exceptions;

public class DuplicateRouteException extends CommandLineErrorException {

    public DuplicateRouteException() {
        super("Please do not enter duplicate routes between the same origin and destination.");
    }
}
