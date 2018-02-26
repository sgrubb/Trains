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
                    return Integer.toString(route.calculateDistance());
                case SHORTEST_ROUTE:
                    Route shortestRoute = routeFactory.createShortestRoute(commandInput, routeMap);
                    return Integer.toString(shortestRoute.calculateDistance());
                case NUMBER_OF_ROUTES_WITH_STOPS:
                    List<Route> routesWithStops = routeFactory.createAllRoutesWithExactStops(commandInput, routeMap);
                    return Integer.toString(routesWithStops.size());
                case NUMBER_OF_ROUTES_WITH_MAXIMUM_STOPS:
                    List<Route> routesWithMaxStops = routeFactory.createAllRoutesWithMaxStops(commandInput, routeMap);
                    return Integer.toString(routesWithMaxStops.size());
                case NUMBER_OF_ROUTES_WITH_MAXIMUM_DISTANCE:
                    List<Route> routesWithMaxDistance = routeFactory.createAllRoutesWithMaxDistance(commandInput, routeMap);
                    return Integer.toString(routesWithMaxDistance.size());
                default:
                    return "Command not yet implemented.";
            }
        } catch (CommandLineErrorException e) {
            return e.getMessage();
        }
    }
}
