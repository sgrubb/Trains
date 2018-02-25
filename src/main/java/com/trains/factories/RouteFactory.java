package com.trains.factories;

import com.trains.exceptions.InvalidDistanceInputException;
import com.trains.exceptions.InvalidShortestInputException;
import com.trains.exceptions.NoSuchCityException;
import com.trains.exceptions.NoSuchRouteException;
import com.trains.models.City;
import com.trains.models.Route;
import com.trains.models.RouteMap;
import com.trains.models.UnitRoute;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import static java.util.stream.Collectors.toList;

@Component
public class RouteFactory {

    private static final String routeInputRegex = "^[A-Z]{2,}$";
    private static final String shortestRouteInputRegex = "^[A-Z]{2}$";

    public Route createRoute(String input, RouteMap routeMap) {
        if (!input.matches(routeInputRegex)) {
            throw new InvalidDistanceInputException();
        }

        List<City> cities = input.chars()
                .mapToObj(c -> (char) c)
                .map(cn -> routeMap.getCities().stream()
                        .filter(c -> c.getName() == cn)
                        .findAny()
                        .orElseThrow(() -> new NoSuchCityException(cn)))
                .collect(toList());

        return new Route(cities);
    }

    public Route createShortestRoute(String input, RouteMap routeMap) {
        if (!input.matches(shortestRouteInputRegex)) {
            throw new InvalidShortestInputException();
        }

        City originCity = getCityFromRouteMapWithName(routeMap, input.charAt(0));
        City destinationCity = getCityFromRouteMapWithName(routeMap, input.charAt(1));

        calculateRoutes(originCity);

        return getShortestRoute(destinationCity);
    }

    private City getCityFromRouteMapWithName(RouteMap routeMap, char name) {
        return routeMap.getCities().stream()
                .filter(c -> c.getName() == name)
                .findAny()
                .orElseThrow(() -> new NoSuchCityException(name));
    }

    private void calculateRoutes(City originCity) {
        originCity.setMinDistance(0);
        PriorityQueue<City> cityQueue = new PriorityQueue<>();
        cityQueue.add(originCity);

        while (!cityQueue.isEmpty()) {
            City city = cityQueue.poll();

            // Visit each route exiting city
            for (UnitRoute unitRoute : city.getUnitRoutes()) {
                City destinationCity = unitRoute.getDestinationCity();
                int distanceThroughCity = city.getMinDistance() + unitRoute.getDistance();

                if (distanceThroughCity < destinationCity.getMinDistance()) {
                    cityQueue.remove(destinationCity);

                    destinationCity.setMinDistance(distanceThroughCity);
                    destinationCity.setPreviousCity(city);
                    cityQueue.add(destinationCity);
                }
            }
        }
    }

    private Route getShortestRoute(City destinationCity) {
        if (destinationCity.getPreviousCity() == null) {
            throw new NoSuchRouteException();
        }

        List<City> cities = new ArrayList<>();
        for (City city = destinationCity; city != null; city = city.getPreviousCity()) {
            cities.add(city);
        }

        Collections.reverse(cities);
        return new Route(cities);
    }
}
