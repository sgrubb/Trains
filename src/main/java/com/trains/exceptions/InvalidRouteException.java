package com.trains.exceptions;

public class InvalidRouteException extends CommandLineErrorException {

    public InvalidRouteException() {
        super("Please enter a valid route in the format: AB0.\n" +
                "Where A and B are single capital letters denoting city names  +" +
                "and 0 is an integer denoting the distance of the route between the two.");
    }
}
