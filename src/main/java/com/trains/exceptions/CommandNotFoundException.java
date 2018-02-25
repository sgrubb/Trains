package com.trains.exceptions;

public class CommandNotFoundException extends IllegalArgumentException {

    public CommandNotFoundException() {
        super("Command not found.");
    }
}
