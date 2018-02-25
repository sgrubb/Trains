package com.trains.models;

import com.trains.exceptions.DuplicateRouteException;
import com.trains.exceptions.NoSuchRouteException;
import com.trains.exceptions.SelfRouteException;

import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

public class City {

    private final char name;
    private final Set<UnitRoute> unitRoutes;

    public City(char name, Set<UnitRoute> unitRoutes) {
        this.name = name;
        this.unitRoutes = unitRoutes;

        validateUnitRoutes();
    }

    public char getName() {
        return name;
    }

    public Set<UnitRoute> getUnitRoutes() {
        return unitRoutes;
    }

    public void addUnitRoute(UnitRoute unitRoute) {
        unitRoutes.add(unitRoute);

        validateUnitRoutes();
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
                .anyMatch(ur -> ur.getDestinationCity().getName() == name);

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
