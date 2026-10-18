package com.example.helidon.controller;

import com.example.helidon.model.BlockingTaskResult;
import com.example.helidon.service.BlockingTaskService;
import io.helidon.http.Status;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public final class BlockingTaskHttpService implements HttpService {

    private final BlockingTaskService blockingTaskService;
    private final long defaultDurationMs;

    public BlockingTaskHttpService(BlockingTaskService blockingTaskService, long defaultDurationMs) {
        this.blockingTaskService = blockingTaskService;
        this.defaultDurationMs = defaultDurationMs;
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/wait", this::waitForTask);
    }

    private void waitForTask(ServerRequest request, ServerResponse response) {
        try {
            String durationParameter = request.query()
                    .first("duration")
                    .orElse(Long.toString(defaultDurationMs));
            long durationMs = Long.parseLong(durationParameter);
            BlockingTaskResult result = blockingTaskService.waitFor(durationMs);

            JsonObject body = Json.createObjectBuilder()
                    .add("requestedDurationMs", result.requestedDurationMs())
                    .add("elapsedDurationMs", result.elapsedDurationMs())
                    .add("threadName", result.threadName())
                    .add("virtualThread", result.virtualThread())
                    .build();
            response.send(body);
        } catch (IllegalArgumentException exception) {
            sendBadRequest(response, exception.getMessage());
        }
    }

    private void sendBadRequest(ServerResponse response, String message) {
        JsonObject body = Json.createObjectBuilder()
                .add("error", message)
                .build();
        response.status(Status.BAD_REQUEST_400)
                .send(body);
    }
}
