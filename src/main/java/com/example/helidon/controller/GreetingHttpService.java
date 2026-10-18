package com.example.helidon.controller;

import com.example.helidon.model.Greeting;
import com.example.helidon.service.GreetingService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public final class GreetingHttpService implements HttpService {

    private final GreetingService greetingService;

    public GreetingHttpService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/", this::hello)
                .get("/{name}", this::helloByName);
    }

    private void hello(ServerRequest request, ServerResponse response) {
        sendGreeting(response, greetingService.greet("World"));
    }

    private void helloByName(ServerRequest request, ServerResponse response) {
        String name = request.path().pathParameters().get("name");
        sendGreeting(response, greetingService.greet(name));
    }

    private void sendGreeting(ServerResponse response, Greeting greeting) {
        JsonObject body = Json.createObjectBuilder()
                .add("message", greeting.message())
                .add("threadName", greeting.threadName())
                .add("virtualThread", greeting.virtualThread())
                .build();

        response.send(body);
    }
}
