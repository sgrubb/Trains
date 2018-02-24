package com.trains.enums;

import com.trains.exceptions.CommandNotFoundException;

import java.util.stream.Stream;

public enum Command {
    DISTANCE_OF_ROUTE("dist"),
    SHORTEST_ROUTE("short"),
    NUMBER_OF_TRIPS_WITH_MAXIMUM_STOPS("ntwms");

    private final String alias;

    Command(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public static Command fromAlias(final String alias) {
        return Stream.of(Command.values())
                .filter(a -> a.getAlias().equals(alias))
                .findAny()
                .orElseThrow(CommandNotFoundException::new);
    }
}
