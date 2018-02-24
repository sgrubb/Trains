package com.trains.services;

import com.trains.enums.Command;
import com.trains.exceptions.CommandNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class RoutingService {

    private static final int COMMAND_NAME_ARG_INDEX = 0;
    private static final int COMMAND_INPUT_ARG_INDEX = 1;
    private static final int UNIT_ROUTES_ARGS_STARTING_INDEX = 2;
    private static final int MIN_NUM_UNIT_ROUTES = 1;

    public String executeRoutingCommand(List<String> args) {
        if (args.size() < UNIT_ROUTES_ARGS_STARTING_INDEX + MIN_NUM_UNIT_ROUTES) {
            return "Please enter at least 3 arguments.\n" +
                    "Usage: java -jar <path/to/jar> <command> <command input> <route 1> (<route 2> ... <route N>)";
        }

        try {
            Command command = Command.fromAlias(args.get(COMMAND_NAME_ARG_INDEX));
            String commandInput = args.get(COMMAND_INPUT_ARG_INDEX);
            List<String> unitRoutes = args.subList(UNIT_ROUTES_ARGS_STARTING_INDEX, args.size());

            return String.format("%s | %s | %s", command.toString(), commandInput, String.join(", ", unitRoutes));
        } catch (CommandNotFoundException e) {
            List<String> commandAliases = Stream.of(Command.values()).map(Command::getAlias).collect(toList());
            return "Please enter a valid command as the first argument: " + String.join(" | ", commandAliases);
        }
    }

//    private int get
}
