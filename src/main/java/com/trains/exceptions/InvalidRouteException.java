package com.trains.exceptions;

public class InvalidRouteException extends IllegalArgumentException {

    public InvalidRouteException() {
        super("Invalid route format.");
    }
}
