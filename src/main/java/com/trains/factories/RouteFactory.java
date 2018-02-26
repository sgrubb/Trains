package com.trains.factories;

import com.trains.exceptions.*;
import com.trains.models.City;
import com.trains.models.Route;
import com.trains.models.RouteMap;
import com.trains.models.UnitRoute;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@Component
public class RouteFactory {

    private static final String routeInputRegex = "^[A-Z]{2,}$";
    private static final String originDestinationInputRegex = "^[A-Z]{2}$";
    private static final String originDestinationNumberInputRegex = "^[A-Z]{2}(\\d+)$";

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
        if (!input.matches(originDestinationInputRegex)) {
            throw new InvalidOriginDestinationInputException();
        }

        City originCity = getCityFromRouteMapWithName(routeMap, input.charAt(0));
        City destinationCity = getCityFromRouteMapWithName(routeMap, input.charAt(1));

        calculateShortestRoutes(originCity);

        return getShortestRoute(destinationCity);
    }

    public List<Route> createAllRoutesWithExactStops(String input, RouteMap routeMap) {
        int numStops = validateOriginDestinationStopsInputAndGetNumber(input);

        return createAllRoutes(input, routeMap, numStops, true).stream()
                .filter(r -> r.getCities().size() == numStops + 1)
                .collect(toList());
    }

    public List<Route> createAllRoutesWithMaxStops(String input, RouteMap routeMap) {
        int maxStops = validateOriginDestinationStopsInputAndGetNumber(input);

        return createAllRoutes(input, routeMap, maxStops, true);
    }

    public List<Route> createAllRoutesWithMaxDistance(String input, RouteMap routeMap) {
        int maxDistance = validateOriginDestinationStopsInputAndGetNumber(input);

        return createAllRoutes(input, routeMap, maxDistance, false);
    }

    private List<Route> createAllRoutes(String input, RouteMap routeMap, int max, boolean isMaxStopsNotDistance) {
        City originCity = getCityFromRouteMapWithName(routeMap, input.charAt(0));
        City destinationCity = getCityFromRouteMapWithName(routeMap, input.charAt(1));

        LinkedList<City> visitedCities = new LinkedList<>();
        visitedCities.add(originCity);

        List<Route> routes = new ArrayList<>();
        findAllRoutes(visitedCities, destinationCity, routes, max, isMaxStopsNotDistance);

        return routes;
    }

    private City getCityFromRouteMapWithName(RouteMap routeMap, char name) {
        return routeMap.getCities().stream()
                .filter(c -> c.getName() == name)
                .findAny()
                .orElseThrow(() -> new NoSuchCityException(name));
    }

    private void calculateShortestRoutes(City originCity) {
        // Make a copy so the actual city gets a non-zero min distance
        // for the route looping back to itself.
        City originCityCopy = new City(originCity.getName(), originCity.getUnitRoutes());
        originCityCopy.setMinDistance(0);
        PriorityQueue<City> cityQueue = new PriorityQueue<>();
        cityQueue.add(originCityCopy);

        populateShortestRoutes(cityQueue);
    }

    private void populateShortestRoutes(PriorityQueue<City> cityQueue) {
        if (cityQueue.isEmpty()) {
            return;
        }
        City city = cityQueue.poll();

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

        populateShortestRoutes(cityQueue);
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

    private int validateOriginDestinationStopsInputAndGetNumber(String input) {
        Pattern pattern = Pattern.compile(originDestinationNumberInputRegex);
        Matcher matcher = pattern.matcher(input);

        if (!matcher.matches()) {
            throw new InvalidOriginDestinationStopsInputException();
        }

        return Integer.parseInt(matcher.group(1));
    }

    private void findAllRoutes(
            LinkedList<City> visitedCities,
            City destinationCity,
            List<Route> routes,
            int max,
            boolean isMaxStopsNotDistance
    ) {
        Route route = new Route(new ArrayList<>(visitedCities));
        boolean isOverMaxStops = isMaxStopsNotDistance && visitedCities.size() > max;
        boolean isOverMaxDistance = !isMaxStopsNotDistance && route.calculateDistance() >= max;
        if (isOverMaxStops || isOverMaxDistance) {
            return;
        }

        LinkedList<City> cities = visitedCities.getLast().getUnitRoutes().stream()
                .map(UnitRoute::getDestinationCity)
                .collect(toCollection(LinkedList::new));

        for (City city : cities) {
            visitedCities.addLast(city);
            if (city.equals(destinationCity)) {
                Route completedRoute = new Route(new ArrayList<>(visitedCities));

                if (isMaxStopsNotDistance || completedRoute.calculateDistance() < max) {
                    routes.add(new Route(new ArrayList<>(visitedCities)));
                }
            }

            findAllRoutes(visitedCities, destinationCity, routes, max, isMaxStopsNotDistance);
            visitedCities.removeLast();
        }
    }
}
