package com.trains.exceptions;

public class NoSuchCityException extends CommandLineErrorException {

    public NoSuchCityException(char cityName) {
        super("No such city " + cityName + ". " +
                "Please do not enter a distance command input that includes a city not present in any provided routes.");
    }
}
