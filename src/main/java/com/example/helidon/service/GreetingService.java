package com.example.helidon.service;

import com.example.helidon.model.Greeting;

public final class GreetingService {

    private final String greeting;

    public GreetingService(String greeting) {
        this.greeting = greeting;
    }

    public Greeting greet(String name) {
        String normalizedName = name == null || name.isBlank() ? "World" : name.trim();
        Thread currentThread = Thread.currentThread();

        return new Greeting(
                "%s, %s!".formatted(greeting, normalizedName),
                currentThread.getName(),
                currentThread.isVirtual());
    }
}
