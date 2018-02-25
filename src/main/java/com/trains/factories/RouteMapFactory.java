package com.trains.factories;

import com.trains.exceptions.InvalidRouteException;
import com.trains.models.City;
import com.trains.models.RouteMap;
import com.trains.models.UnitRoute;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RouteMapFactory {

    private static final String routeRegex = "^([A-Z])([A-Z])(\\d+)$";

    public RouteMap createRouteMap(List<String> routes) {
        Pattern routePattern = Pattern.compile(routeRegex);

        Set<City> cities = new HashSet<>();
        RouteMap routeMap = new RouteMap(cities);

        for (String route : routes) {
            Matcher routeMatcher = routePattern.matcher(route);

            if (!routeMatcher.matches()) {
                throw new InvalidRouteException();
            }

            char originCityName = routeMatcher.group(1).charAt(0);
            char destinationCityName = routeMatcher.group(2).charAt(0);
            int distance = Integer.parseInt(routeMatcher.group(3));

            City originCity = getCityByName(cities, originCityName);
            City destinationCity = getCityByName(cities, destinationCityName);

            originCity.addUnitRoute(new UnitRoute(destinationCity, distance));

            routeMap.addCityIfMissing(originCity);
            routeMap.addCityIfMissing(destinationCity);
        }

        return routeMap;
    }

    private City getCityByName(Set<City> cities, char cityName) {
        return cities.stream()
                .filter(c -> c.getName() == cityName)
                .findAny()
                .orElse(new City(cityName, new HashSet<>()));
    }
}
