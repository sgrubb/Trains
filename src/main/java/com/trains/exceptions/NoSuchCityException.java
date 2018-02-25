package com.trains.exceptions;

public class NoSuchCityException extends IllegalArgumentException {

    public NoSuchCityException() {
        super("No such city.");
    }
}
