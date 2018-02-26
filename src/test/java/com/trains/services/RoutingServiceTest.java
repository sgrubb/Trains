package com.trains.services;

import com.trains.exceptions.*;
import com.trains.factories.RouteFactory;
import com.trains.factories.RouteMapFactory;
import com.trains.models.City;
import com.trains.models.Route;
import com.trains.models.RouteMap;
import com.trains.models.UnitRoute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RoutingServiceTest {

    private final RouteMapFactory routeMapFactory;
    private final RouteFactory routeFactory;
    private final RoutingService routingService;

    public RoutingServiceTest() {
        routeMapFactory = mock(RouteMapFactory.class);
        routeFactory = mock(RouteFactory.class);
        routingService = new RoutingService(routeMapFactory, routeFactory);
    }

    @Test
    public void executeRoutingCommandShouldReturnUsageMessageIfTooFewArgs() {
        // given
        List<String> args = Arrays.asList("dist", "ABC");

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).contains("enter at least 3 arguments");
        assertThat(result).contains("<command> <command input> <route 1>");
    }

    @Test
    public void executeRoutingCommandShouldReturnInvalidCommandMessageIfInvalidCommandArg() {
        // given
        List<String> args = Arrays.asList("invalid", "ABC", "AB5", "BC3");

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).contains("enter a valid command");
    }

    @Test
    public void executeRoutingCommandShouldReturnInvalidRouteMessageIfCreateRouteMapThrowsInvalidRouteException() {
        // given
        List<String> args = Arrays.asList("dist", "ABC", "AB5", "BCC3");
        when(routeMapFactory.createRouteMap(anyListOf(String.class)))
                .thenThrow(new InvalidRouteException());

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).contains("enter a valid route");
    }

    @Test
    public void executeRoutingCommandShouldReturnInvalidDistanceInputMessageIfCreateRouteThrowsInvalidDistanceInputException() {
        // given
        List<String> args = Arrays.asList("dist", "AB2", "AB5", "BC3");
        when(routeFactory.createRoute(anyString(), any(RouteMap.class)))
                .thenThrow(new InvalidDistanceInputException());

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).contains("enter a valid distance command input");
    }

    @Test
    public void executeRoutingCommandShouldReturnDuplicateRouteMessageIfCreateRouteMapThrowsDuplicateRouteException() {
        // given
        List<String> args = Arrays.asList("dist", "ABC", "AB5", "BC3", "AB6");
        when(routeMapFactory.createRouteMap(anyListOf(String.class)))
                .thenThrow(new DuplicateRouteException());

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).contains("do not enter duplicate routes");
    }

    @Test
    public void executeRoutingCommandShouldReturnSelfRouteMessageIfCreateRouteMapThrowsSelfRouteException() {
        // given
        List<String> args = Arrays.asList("dist", "ABC", "AB5", "BC3", "CC6");
        when(routeMapFactory.createRouteMap(anyListOf(String.class)))
                .thenThrow(new SelfRouteException());

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).contains("do not enter a route with the same origin and destination");
    }

    @Test
    public void executeRoutingCommandShouldReturnMissingCityMessageIfCreateRouteThrowsNoSuchCityException() {
        // given
        List<String> args = Arrays.asList("dist", "ABCD", "AB5", "BC3");
        when(routeFactory.createRoute(anyString(), any(RouteMap.class)))
                .thenThrow(new NoSuchCityException('D'));

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).contains("No such city D");
        assertThat(result).contains("do not enter a distance command input that includes a city not present");
    }

    @Test
    public void executeRoutingCommandShouldReturnNoSuchRouteMessageIfCreateRouteReturnsRouteWithNoPossibleRoute() {
        // given
        List<String> args = Arrays.asList("dist", "ABCD", "AB5", "BC3", "CA4", "AD6");

        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityA, 4));
        cityA.addUnitRoute(new UnitRoute(cityD, 6));
        Route route = new Route(Arrays.asList(cityA, cityB, cityC, cityD));

        when(routeFactory.createRoute(anyString(), any(RouteMap.class)))
                .thenReturn(route);

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).isEqualTo("NO SUCH ROUTE");
    }

    @Test
    public void executeRoutingCommandShouldReturnDistanceIfCreateRouteReturnsRoute() {
        // given
        List<String> args = Arrays.asList("dist", "ABCD", "AB5", "CA4", "BC3", "CD6");

        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityC.addUnitRoute(new UnitRoute(cityA, 4));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityC.addUnitRoute(new UnitRoute(cityD, 6));
        Route route = new Route(Arrays.asList(cityA, cityB, cityC, cityD));

        when(routeFactory.createRoute(anyString(), any(RouteMap.class)))
                .thenReturn(route);

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).isEqualTo("14");
    }

    @Test
    public void executeRoutingCommandShouldReturnNoSuchRouteMessageIfCreateShortestRouteThrowsNoPossibleRouteException() {
        // given
        List<String> args = Arrays.asList("short", "AD", "AB5", "BC3", "CA4", "DA6");
        when(routeFactory.createShortestRoute(anyString(), any(RouteMap.class)))
                .thenThrow(new NoSuchRouteException());

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).isEqualTo("NO SUCH ROUTE");
    }

    @Test
    public void executeRoutingCommandShouldReturnShortestDistanceIfCreateShortestRouteReturnsRoute() {
        // given
        List<String> args = Arrays.asList("short", "AD", "AB5", "CD2", "BC3", "BD6");

        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityC.addUnitRoute(new UnitRoute(cityD, 2));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));
        Route route = new Route(Arrays.asList(cityA, cityB, cityC, cityD));

        when(routeFactory.createShortestRoute(anyString(), any(RouteMap.class)))
                .thenReturn(route);

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).isEqualTo("10");
    }

    @Test
    public void executeRoutingCommandShouldReturnNumberOfRoutesIfCreateAllRoutesWithExactStopsReturnsRoute() {
        // given
        List<String> args = Arrays.asList("stops", "AD3", "AB5", "CD2", "BC3", "BD6");

        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityC.addUnitRoute(new UnitRoute(cityD, 2));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        Route route = new Route(Arrays.asList(cityA, cityB, cityC, cityD));
        List<Route> routes = Collections.singletonList(route);

        when(routeFactory.createAllRoutesWithExactStops(anyString(), any(RouteMap.class)))
                .thenReturn(routes);

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).isEqualTo("1");
    }

    @Test
    public void executeRoutingCommandShouldReturnNumberOfRoutesIfCreateAllRoutesWithMaxStopsReturnsRoute() {
        // given
        List<String> args = Arrays.asList("maxstops", "AD3", "AB5", "CD2", "BC3", "BD6");

        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityC.addUnitRoute(new UnitRoute(cityD, 2));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        Route route1 = new Route(Arrays.asList(cityA, cityB, cityC, cityD));
        Route route2 = new Route(Arrays.asList(cityA, cityB, cityD));
        List<Route> routes = Arrays.asList(route1, route2);

        when(routeFactory.createAllRoutesWithMaxStops(anyString(), any(RouteMap.class)))
                .thenReturn(routes);

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).isEqualTo("2");
    }

    @Test
    public void executeRoutingCommandShouldReturnNumberOfRoutesIfCreateAllRoutesWithMaxDistanceReturnsRoute() {
        // given
        List<String> args = Arrays.asList("maxdist", "AD11", "AB5", "CD2", "BC3", "BD6");

        City cityA = new City('A', new HashSet<>());
        City cityB = new City('B', new HashSet<>());
        City cityC = new City('C', new HashSet<>());
        City cityD = new City('D', new HashSet<>());
        cityA.addUnitRoute(new UnitRoute(cityB, 5));
        cityC.addUnitRoute(new UnitRoute(cityD, 2));
        cityB.addUnitRoute(new UnitRoute(cityC, 3));
        cityB.addUnitRoute(new UnitRoute(cityD, 6));

        Route route = new Route(Arrays.asList(cityA, cityB, cityC, cityD));
        List<Route> routes = Collections.singletonList(route);

        when(routeFactory.createAllRoutesWithMaxDistance(anyString(), any(RouteMap.class)))
                .thenReturn(routes);

        // when
        String result = routingService.executeRoutingCommand(args);

        // then
        assertThat(result).isEqualTo("1");
    }
}