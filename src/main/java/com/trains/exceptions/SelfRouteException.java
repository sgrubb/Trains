package com.trains.exceptions;

public class SelfRouteException extends CommandLineErrorException {

    public SelfRouteException() {
        super("Please do not enter a route with the same origin and destination.");
    }
}
