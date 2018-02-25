package com.trains.models;

public class UnitRoute {

    private final City destinationCity;
    private final int distance;

    public UnitRoute(City destinationCity, int distance) {
        this.destinationCity = destinationCity;
        this.distance = distance;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public int getDistance() {
        return distance;
    }
}
