package com.trains.exceptions;

public class DuplicateRouteException extends IllegalArgumentException {

    public DuplicateRouteException() {
        super("Cannot have two routes to the same destination from one city.");
    }
}
