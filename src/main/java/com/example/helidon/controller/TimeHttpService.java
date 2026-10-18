package com.example.helidon.controller;

import com.example.helidon.model.TimeInfo;
import com.example.helidon.service.TimeService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.Json;

public final class TimeHttpService implements HttpService {

    private final TimeService timeService;

    public TimeHttpService(TimeService timeService) {
        this.timeService = timeService;
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/", this::currentTime);
    }

    private void currentTime(ServerRequest request, ServerResponse response) {
        TimeInfo timeInfo = timeService.currentTime();

        response.send(Json.createObjectBuilder()
                              .add("timestamp", timeInfo.timestamp())
                              .add("zone", timeInfo.zone())
                              .add("virtualThread", timeInfo.virtualThread())
                              .build());
    }
}
