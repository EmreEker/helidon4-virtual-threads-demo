package com.example.helidon.model;

public record BlockingTaskResult(long requestedDurationMs,
                                 long elapsedDurationMs,
                                 String threadName,
                                 boolean virtualThread) {
}
