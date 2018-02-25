package com.trains.factories;

import com.trains.exceptions.DuplicateRouteException;
import com.trains.exceptions.InvalidRouteException;
import com.trains.exceptions.SelfRouteException;
import com.trains.models.City;
import com.trains.models.RouteMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class RouteMapFactoryTest {

    private final RouteMapFactory routeMapFactory;

    public RouteMapFactoryTest() {
        this.routeMapFactory = new RouteMapFactory();
    }

    @Test(expected = InvalidRouteException.class)
    public void createRouteMapShouldThrowInvalidRouteExceptionIfInvalidRoute() {
        // given
        List<String> routes = Arrays.asList("AB5", "BCC3");

        // when
        routeMapFactory.createRouteMap(routes);
    }

    @Test(expected = DuplicateRouteException.class)
    public void createRouteMapShouldThrowDuplicateRouteExceptionIfDuplicateRoute() {
        // given
        List<String> routes = Arrays.asList("AB5", "BC3", "AB6");

        // when
        routeMapFactory.createRouteMap(routes);
    }

    @Test(expected = SelfRouteException.class)
    public void createRouteMapShouldThrowSelfRouteExceptionIfSelfRoute() {
        // given
        List<String> routes = Arrays.asList("AB5", "BC3", "CC6");

        // when
        routeMapFactory.createRouteMap(routes);
    }

    @Test
    public void createRouteMapShouldCreateRouteMap() {
        // given
        List<String> routes = Arrays.asList("AB5", "CA4", "BC3", "CD6");

        // when
        RouteMap result = routeMapFactory.createRouteMap(routes);

        // then
        assertThat(result.getCities().size()).isEqualTo(4);
        City cityA = getCityFromRouteMapByName(result, 'A');
        City cityB = getCityFromRouteMapByName(result, 'B');
        City cityC = getCityFromRouteMapByName(result, 'C');
        City cityD = getCityFromRouteMapByName(result, 'D');
        assertThat(cityA.getDistanceTo(cityB)).isEqualTo(5);
        assertThat(cityC.getDistanceTo(cityA)).isEqualTo(4);
        assertThat(cityB.getDistanceTo(cityC)).isEqualTo(3);
        assertThat(cityC.getDistanceTo(cityD)).isEqualTo(6);
    }

    private City getCityFromRouteMapByName(RouteMap routeMap, char name) {
        return routeMap.getCities().stream()
                .filter(c -> c.getName() == name)
                .findAny()
                .orElseThrow(AssertionError::new);
    }
}
