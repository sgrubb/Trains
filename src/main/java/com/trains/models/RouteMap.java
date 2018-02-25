package com.trains.models;

import com.trains.exceptions.DuplicateCityException;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class RouteMap {

    private final Set<City> cities;

    public RouteMap(Set<City> cities) {
        boolean isDuplicate = cities.stream()
                .collect(groupingBy(City::getName))
                .values().stream()
                .anyMatch(cs -> cs.size() > 1);

        if (isDuplicate) {
            throw new DuplicateCityException();
        }

        this.cities = cities;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void addCityIfMissing(City city) {
        if (cities.stream().noneMatch(c -> c.getName() == city.getName())) {
            cities.add(city);
        }
    }
}
