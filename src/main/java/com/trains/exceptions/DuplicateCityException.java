package com.trains.exceptions;

public class DuplicateCityException extends CommandLineErrorException {

    public DuplicateCityException() {
        super("Cannot have two cities of the same name in route map.");
    }
}
