package com.trains.factories;

import com.trains.exceptions.InvalidDistanceInputException;
import com.trains.exceptions.NoSuchCityException;
import com.trains.models.City;
import com.trains.models.Route;
import com.trains.models.RouteMap;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class RouteFactory {

    private static final String inputRegex = "^[A-Z]{2,}$";

    public Route createRoute(String input, RouteMap routeMap) {
        if (!input.matches(inputRegex)) {
            throw new InvalidDistanceInputException();
        }

        List<City> cities = input.chars()
                .mapToObj(c -> (char) c)
                .map(cn -> routeMap.getCities().stream()
                        .filter(c -> c.getName() == cn)
                        .findAny()
                        .orElseThrow(NoSuchCityException::new))
                .collect(toList());

        return new Route(cities);
    }
}
