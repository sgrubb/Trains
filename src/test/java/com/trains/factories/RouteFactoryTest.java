package com.trains.factories;

import com.trains.exceptions.InvalidDistanceInputException;
import com.trains.exceptions.NoSuchCityException;
import com.trains.exceptions.NoSuchRouteException;
import com.trains.models.City;
import com.trains.models.Route;
import com.trains.models.RouteMap;
import com.trains.models.UnitRoute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class RouteFactoryTest {

    private final RouteFactory routeFactory;

    public RouteFactoryTest() {
        this.routeFactory = new RouteFactory();
    }

    @Test(expected = InvalidDistanceInputException.class)
    public void createRouteShouldThrowInvalidDistanceInputExceptionIfInvalidInput() {
        // given
        String input = "AB2";
        RouteMap routeMap = new RouteMap(new HashSet<>());

        // when
        routeFactory.createRoute(input, routeMap);
    }

    @Test(expected = NoSuchCityException.class)
    public void createRouteShouldThrowNoSuchCityExceptionIfMissingCity() {
        // given
        String input = "ABC";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB)));

        // when
        routeFactory.createRoute(input, routeMap);
    }

    @Test
    public void createRouteShouldCreateExistentRoute() {
        // given
        String input = "ABC";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC)));

        // when
        Route result = routeFactory.createRoute(input, routeMap);

        // then
        assertThat(result.calculateDistance()).isEqualTo(8);
    }

    @Test(expected = NoSuchRouteException.class)
    public void createRouteShouldCreateNonexistentRoute() {
        // given
        String input = "ABC";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityC.addUnitRoute(new UnitRoute(cityB, 3));
        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC)));

        // when
        Route result = routeFactory.createRoute(input, routeMap);
        result.calculateDistance();
    }
}
