package com.trains.exceptions;

public class NoSuchRouteException extends CommandLineErrorException {

    public NoSuchRouteException() {
        super("NO SUCH ROUTE");
    }
}
