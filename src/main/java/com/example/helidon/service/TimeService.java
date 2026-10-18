package com.example.helidon.service;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.example.helidon.model.TimeInfo;

public final class TimeService {

    private final Clock clock;

    public TimeService(ZoneId zoneId) {
        this(Clock.system(zoneId));
    }

    TimeService(Clock clock) {
        this.clock = clock;
    }

    public TimeInfo currentTime() {
        return new TimeInfo(
                ZonedDateTime.now(clock).toOffsetDateTime().toString(),
                clock.getZone().getId(),
                Thread.currentThread().isVirtual());
    }
}
