package com.trains;

import com.trains.services.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private RoutingService routingService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.run(args);
    }

    public void run(String... args) {
        String result = routingService.executeRoutingCommand(Arrays.asList(args));
        System.out.println("#######################################################\n");
        System.out.println("Output: " + result);
        System.out.println("\n#######################################################");
    }
}
