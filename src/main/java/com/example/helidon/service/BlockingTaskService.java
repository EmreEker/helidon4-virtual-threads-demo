package com.example.helidon.service;

import com.example.helidon.model.BlockingTaskResult;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class BlockingTaskService {

    private final long maximumDurationMs;

    public BlockingTaskService(long maximumDurationMs) {
        if (maximumDurationMs < 1) {
            throw new IllegalArgumentException("maximumDurationMs must be greater than zero");
        }
        this.maximumDurationMs = maximumDurationMs;
    }

    public BlockingTaskResult waitFor(long durationMs) {
        validateDuration(durationMs);

        Thread currentThread = Thread.currentThread();
        long startTime = System.nanoTime();

        try {
            Thread.sleep(Duration.ofMillis(durationMs));
        } catch (InterruptedException exception) {
            currentThread.interrupt();
            throw new IllegalStateException("Blocking task was interrupted", exception);
        }

        long elapsedDurationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        return new BlockingTaskResult(
                durationMs,
                elapsedDurationMs,
                currentThread.getName(),
                currentThread.isVirtual());
    }

    private void validateDuration(long durationMs) {
        if (durationMs < 1 || durationMs > maximumDurationMs) {
            throw new IllegalArgumentException(
                    "duration must be between 1 and %d milliseconds".formatted(maximumDurationMs));
        }
    }
}
