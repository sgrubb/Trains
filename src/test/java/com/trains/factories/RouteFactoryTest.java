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
import java.util.List;

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

    @Test
    public void createRouteShouldCreateShortestRouteIfExists() {
        // given
        String input = "AD";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityC.addUnitRoute(new UnitRoute(cityD, 2));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        Route result = routeFactory.createShortestRoute(input, routeMap);

        // then
        assertThat(result.calculateDistance()).isEqualTo(10);
    }

    @Test(expected = NoSuchRouteException.class)
    public void createRouteShouldThrowNoSuchRouteExceptionIfNoRouteExists() {
        // given
        String input = "AD";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityA, 4));
        cityD.addUnitRoute(new UnitRoute(cityA, 6));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        routeFactory.createShortestRoute(input, routeMap);
    }

    @Test
    public void createAllRoutesWithMaxStopsShouldReturnNoRoutesIfNoRoutes() {
        // given
        String input = "AD5";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityA, 4));
        cityD.addUnitRoute(new UnitRoute(cityA, 6));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        List<Route> result = routeFactory.createAllRoutesWithMaxStops(input, routeMap);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void createAllRoutesWithMaxStopsShouldReturnNoRoutesIfNoRoutesWithFewerThanMaxStops() {
        // given
        String input = "AD2";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityD, 4));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        List<Route> result = routeFactory.createAllRoutesWithMaxStops(input, routeMap);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void createAllRoutesWithMaxStopsShouldReturnAllRoutesWithFewerThanOrEqualToMaxStops() {
        // given
        String input = "AD4";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityD, 4));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        List<Route> result = routeFactory.createAllRoutesWithMaxStops(input, routeMap);

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    public void createAllRoutesWithExactStopsShouldReturnNoRoutesIfNoRoutesWithExactNumberOfStops() {
        // given
        String input = "AD2";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityD, 4));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        List<Route> result = routeFactory.createAllRoutesWithExactStops(input, routeMap);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void createAllRoutesWithExactStopsShouldReturnAllRoutesWithExactNumberOfStops() {
        // given
        String input = "AD3";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityD, 4));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        List<Route> result = routeFactory.createAllRoutesWithExactStops(input, routeMap);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    public void createAllRoutesWithMaxDistanceShouldReturnNoRoutesIfNoRoutesWithLessThanMaxDistance() {
        // given
        String input = "AD11";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityD, 4));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        List<Route> result = routeFactory.createAllRoutesWithMaxDistance(input, routeMap);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void createAllRoutesWithMaxDistanceShouldReturnAllRoutesWithLessThanMaxDistance() {
        // given
        String input = "AD12";
        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityD, 4));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        RouteMap routeMap = new RouteMap(new HashSet<>(Arrays.asList(cityA, cityB, cityC, cityD)));

        // when
        List<Route> result = routeFactory.createAllRoutesWithMaxDistance(input, routeMap);

        // then
        assertThat(result).hasSize(1);
    }
}
