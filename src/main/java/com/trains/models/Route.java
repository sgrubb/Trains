package com.trains.models;

import java.util.List;

public class Route {

    private final List<City> cities;

    public Route(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {
        return cities;
    }

    public int calculateDistance() {
        // Is there a neater way to do this??
        int distance = 0;
        for (int i = 0; i < cities.size() - 1; i++) {
            distance += cities.get(i).getDistanceTo(cities.get(i + 1));
        }
        return distance;
    }
}
