package com.trains;

import com.trains.factories.RouteFactory;
import com.trains.factories.RouteMapFactory;
import com.trains.services.RoutingService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class AppTest {

    private static final String[] GRAPH_ARGS = {"AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"};

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void TheDistanceOfRouteABCShouldBe9() {
        // given
        String[] commandArgs = {"dist", "ABC"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("9");
    }

    @Test
    public void TheDistanceOfRouteADShouldBe5() {
        // given
        String[] commandArgs = {"dist", "AD"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("5");
    }

    @Test
    public void TheDistanceOfRouteADCShouldBe5() {
        // given
        String[] commandArgs = {"dist", "ADC"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("13");
    }

    @Test
    public void TheDistanceOfRouteAEBCDhouldBe2() {
        // given
        String[] commandArgs = {"dist", "AEBCD"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("22");
    }

    @Test
    public void TheDistanceOfRouteAEDShouldNotExist() {
        // given
        String[] commandArgs = {"dist", "AED"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("NO SUCH ROUTE");
    }

    @Test
    public void TheNumberOfRoutesFromCToCWithAMaximumOf3StopsShouldBe2() {
        // given
        String[] commandArgs = {"maxstops", "CC3"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("2");
    }

    @Test
    public void TheNumberOfRoutesFromAToCWithExactly4StopsShouldBe3() {
        // given
        String[] commandArgs = {"stops", "AC4"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("3");
    }

    @Test
    public void TheLengthOfTheShortestRouteFromAToCShouldBe9() {
        // given
        String[] commandArgs = {"short", "AC"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("9");
    }

    @Test
    public void TheLengthOfTheShortestRouteFromBToBShouldBe9() {
        // given
        String[] commandArgs = {"short", "BB"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("9");
    }

    @Test
    public void TheNumberOfDifferentRoutesFromCToCWithADistanceLessThan30ShouldBe7() {
        // given
        String[] commandArgs = {"maxdist", "CC30"};
        String[] args = generateArgs(commandArgs);

        // when
        App.main(args);

        // then
        assertThatOutputEquals("7");
    }

    private String[] generateArgs(String[] commandArgs) {
        return Stream.of(commandArgs, GRAPH_ARGS).flatMap(Stream::of).toArray(String[]::new);
    }

    private void assertThatOutputEquals(String expected) {
        assertThat(outContent.toString()).contains(String.format("\nOutput: %s\n", expected));
    }
}
