package com.trains.services;

import com.trains.enums.Command;
import com.trains.exceptions.*;
import com.trains.factories.RouteFactory;
import com.trains.factories.RouteMapFactory;
import com.trains.models.Route;
import com.trains.models.RouteMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class RoutingService {

    private static final int COMMAND_NAME_ARG_INDEX = 0;
    private static final int COMMAND_INPUT_ARG_INDEX = 1;
    private static final int ROUTES_ARGS_STARTING_INDEX = 2;
    private static final int MIN_NUM_ROUTES = 1;

    private final RouteMapFactory routeMapFactory;
    private final RouteFactory routeFactory;

    @Autowired
    public RoutingService(
            RouteMapFactory routeMapFactory,
            RouteFactory routeFactory
    ) {
        this.routeMapFactory = routeMapFactory;
        this.routeFactory = routeFactory;
    }

    public String executeRoutingCommand(List<String> args) {
        if (args.size() < ROUTES_ARGS_STARTING_INDEX + MIN_NUM_ROUTES) {
            return "Please enter at least 3 arguments.\n" +
                    "Usage: java -jar <path/to/jar> <command> <command input> <route 1> (<route 2> ... <route N>)";
        }

        try {
            Command command = Command.fromAlias(args.get(COMMAND_NAME_ARG_INDEX));
            String commandInput = args.get(COMMAND_INPUT_ARG_INDEX);

            List<String> routes = args.subList(ROUTES_ARGS_STARTING_INDEX, args.size());
            RouteMap routeMap = routeMapFactory.createRouteMap(routes);

            switch (command) {
                case DISTANCE_OF_ROUTE:
                    Route route = routeFactory.createRoute(commandInput, routeMap);
                    return route.calculateDistance().toString();
                case SHORTEST_ROUTE:
                    return "";
                case NUMBER_OF_TRIPS_WITH_MAXIMUM_STOPS:
                    return "";
                default:
                    return "Command not yet implemented.";
            }
        } catch (CommandNotFoundException e) {
            List<String> commandAliases = Stream.of(Command.values()).map(Command::getAlias).collect(toList());
            return "Please enter a valid command as the first argument: " + String.join(" | ", commandAliases);
        } catch (InvalidRouteException e) {
            return "Please enter a valid route in the format: AB0.\n" +
                    "Where A and B are single capital letters denoting city names  +" +
                    "and 0 is an integer denoting the distance of the route between the two.";
        } catch (InvalidDistanceInputException e) {
            return "Please enter a valid distance command input in the format: ABC...\n" +
                    "Which can be any number of letters denoting city names.";
        } catch (DuplicateRouteException e) {
            return "Please do not enter duplicate routes between the same origin and destination.";
        } catch (SelfRouteException e) {
            return "Please do not enter a route with the same origin and destination";
        } catch (NoSuchCityException e) {
            return "Please do not enter a distance command input that includes a city not present in any provided routes.";
        } catch (NoSuchRouteException e) {
            return "NO SUCH ROUTE";
        }
    }
}
