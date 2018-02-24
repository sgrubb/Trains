package com.trains.exceptions;

public class CommandNotFoundException extends IllegalArgumentException {

    private static final String message = "Command not found.";

    public CommandNotFoundException() {
        super(message);
    }
}
