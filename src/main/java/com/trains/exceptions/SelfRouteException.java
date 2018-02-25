package com.trains.exceptions;

public class SelfRouteException extends IllegalArgumentException {

    public SelfRouteException() {
        super("City cannot have route to itself.");
    }
}
