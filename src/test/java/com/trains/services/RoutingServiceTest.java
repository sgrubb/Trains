package com.trains.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class RoutingServiceTest {

    private final RoutingService routingService;

    public RoutingServiceTest() {
        routingService = new RoutingService();
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
}