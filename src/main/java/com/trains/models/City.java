package com.trains.models;

import com.trains.exceptions.DuplicateRouteException;
import com.trains.exceptions.NoSuchRouteException;
import com.trains.exceptions.SelfRouteException;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

public class City implements Comparable<City> {

    private final char name;
    private final Set<UnitRoute> unitRoutes;
    private int minDistance;
    private City previousCity;

    public City(char name, Set<UnitRoute> unitRoutes) {
        this.name = name;
        this.unitRoutes = unitRoutes;

        minDistance = Integer.MAX_VALUE;

        validateUnitRoutes();
    }

    public char getName() {
        return name;
    }

    public Set<UnitRoute> getUnitRoutes() {
        return unitRoutes;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public City getPreviousCity() {
        return previousCity;
    }

    public void setPreviousCity(City previousCity) {
        this.previousCity = previousCity;
    }

    public void addUnitRoute(UnitRoute unitRoute) {
        unitRoutes.add(unitRoute);

        validateUnitRoutes();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        City city = (City) obj;
        return name == city.name;
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    public boolean equals(City city) {
        return city.getName() == name;
    }

    public int compareTo(City city)
    {
        return Integer.compare(minDistance, city.getMinDistance());
    }

    public int getDistanceTo(City city) {
        return unitRoutes.stream()
                .filter(c -> c.getDestinationCity().equals(city))
                .map(UnitRoute::getDistance)
                .findAny()
                .orElseThrow(NoSuchRouteException::new);
    }

    private void validateUnitRoutes() {
        boolean hasSelfRoute = unitRoutes.stream()
                .anyMatch(ur -> ur.getDestinationCity().equals(this));

        if (hasSelfRoute) {
            throw new SelfRouteException();
        }

        boolean isDuplicate = unitRoutes.stream()
                .collect(groupingBy(ur -> ur.getDestinationCity().getName()))
                .values().stream()
                .anyMatch(urs -> urs.size() > 1);

        if (isDuplicate) {
            throw new DuplicateRouteException();
        }
    }
}
