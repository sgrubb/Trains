package com.trains.exceptions;

import com.trains.enums.Command;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CommandNotFoundException extends CommandLineErrorException {

    public CommandNotFoundException() {
        super("Please enter a valid command as the first argument: " +
                String.join(" | ", Stream.of(Command.values()).map(Command::getAlias).collect(toList())));
    }
}
