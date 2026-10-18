package com.example.helidon.application;

import com.example.helidon.controller.BlockingTaskHttpService;
import com.example.helidon.controller.CalculatorHttpService;
import com.example.helidon.controller.GreetingHttpService;
import com.example.helidon.controller.TimeHttpService;
import com.example.helidon.service.BlockingTaskService;
import com.example.helidon.service.CalculatorService;
import com.example.helidon.service.GreetingService;
import com.example.helidon.service.TimeService;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.service.registry.Services;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

import java.time.ZoneId;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        LogConfig.configureRuntime();
        Config config = Services.get(Config.class);

        WebServer server = WebServer.builder()
                .config(config.get("server"))
                .routing(routing -> routing(routing, config))
                .build()
                .start();

        System.out.printf("Server started: http://localhost:%d/api/greetings/Helidon%n", server.port());
    }

    public static void routing(HttpRouting.Builder routing) {
        routing(routing, Services.get(Config.class));
    }

    private static void routing(HttpRouting.Builder routing, Config config) {
        String greeting = config.get("app.greeting").asString().orElse("Merhaba");
        ZoneId timeZone = config.get("app.time-zone")
                .asString()
                .map(ZoneId::of)
                .orElse(ZoneId.of("UTC"));
        long defaultTaskDurationMs = config.get("app.task.default-duration-ms")
                .asLong()
                .orElse(1000L);
        long maximumTaskDurationMs = config.get("app.task.max-duration-ms")
                .asLong()
                .orElse(5000L);

        GreetingHttpService greetingHttpService = new GreetingHttpService(new GreetingService(greeting));
        TimeHttpService timeHttpService = new TimeHttpService(new TimeService(timeZone));
        CalculatorHttpService calculatorHttpService = new CalculatorHttpService(new CalculatorService());
        BlockingTaskHttpService blockingTaskHttpService = new BlockingTaskHttpService(
                new BlockingTaskService(maximumTaskDurationMs),
                defaultTaskDurationMs);

        routing.register("/api/greetings", greetingHttpService)
                .register("/api/time", timeHttpService)
                .register("/api/calculations", calculatorHttpService)
                .register("/api/tasks", blockingTaskHttpService);
    }
}
