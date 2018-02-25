package com.trains.exceptions;

public class CommandLineErrorException extends IllegalArgumentException {

    public CommandLineErrorException(String errorMessage) {
        super(errorMessage);
    }
}
