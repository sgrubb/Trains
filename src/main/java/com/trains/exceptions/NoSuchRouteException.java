package com.trains.exceptions;

public class NoSuchRouteException extends RuntimeException {

    public NoSuchRouteException() {
        super("No such route.");
    }
}
