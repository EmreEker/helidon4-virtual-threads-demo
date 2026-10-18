package com.example.helidon;

import com.example.helidon.application.Main;
import io.helidon.http.Status;
import io.helidon.webclient.http1.Http1Client;
import io.helidon.webclient.http1.Http1ClientResponse;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.testing.junit5.ServerTest;
import io.helidon.webserver.testing.junit5.SetUpRoute;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ServerTest
class ApiIntegrationTest {

    private final Http1Client client;

    ApiIntegrationTest(Http1Client client) {
        this.client = client;
    }

    @SetUpRoute
    static void routing(HttpRouting.Builder routing) {
        Main.routing(routing);
    }

    @Test
    void greetsByNameOnAVirtualThread() {
        try (Http1ClientResponse response = client.get("/api/greetings/Emre").request()) {
            assertEquals(Status.OK_200, response.status());

            JsonObject body = response.as(JsonObject.class);
            assertEquals("Merhaba, Emre!", body.getString("message"));
            assertTrue(body.getBoolean("virtualThread"));
        }
    }

    @Test
    void returnsCurrentTimeInConfiguredTimeZone() {
        try (Http1ClientResponse response = client.get("/api/time").request()) {
            assertEquals(Status.OK_200, response.status());

            JsonObject body = response.as(JsonObject.class);
            assertEquals("Europe/Istanbul", body.getString("zone"));
            assertTrue(body.getString("timestamp").endsWith("+03:00"));
            assertTrue(body.getBoolean("virtualThread"));
        }
    }

    @Test
    void addsTwoNumbers() {
        try (Http1ClientResponse response = client.get("/api/calculations/add/12.5/7.5").request()) {
            assertEquals(Status.OK_200, response.status());

            JsonObject body = response.as(JsonObject.class);
            assertEquals("addition", body.getString("operation"));
            assertEquals(20, body.getJsonNumber("result").bigDecimalValue().intValueExact());
            assertTrue(body.getBoolean("virtualThread"));
        }
    }

    @Test
    void multipliesTwoNumbers() {
        try (Http1ClientResponse response = client.get("/api/calculations/multiply/6/7").request()) {
            assertEquals(Status.OK_200, response.status());
            assertEquals(42, response.as(JsonObject.class).getInt("result"));
        }
    }

    @Test
    void rejectsInvalidNumbers() {
        try (Http1ClientResponse response = client.get("/api/calculations/add/not-a-number/2").request()) {
            assertEquals(Status.BAD_REQUEST_400, response.status());
            assertEquals("left and right must be valid numbers",
                         response.as(JsonObject.class).getString("error"));
        }
    }

    @Test
    void runsBlockingTaskOnAVirtualThread() {
        try (Http1ClientResponse response = client.get("/api/tasks/wait")
                .queryParam("duration", "10")
                .request()) {
            assertEquals(Status.OK_200, response.status());

            JsonObject body = response.as(JsonObject.class);
            assertEquals(10, body.getInt("requestedDurationMs"));
            assertTrue(body.getInt("elapsedDurationMs") >= 10);
            assertTrue(body.getBoolean("virtualThread"));
        }
    }

    @Test
    void rejectsTaskDurationAboveConfiguredMaximum() {
        try (Http1ClientResponse response = client.get("/api/tasks/wait")
                .queryParam("duration", "5001")
                .request()) {
            assertEquals(Status.BAD_REQUEST_400, response.status());
            assertEquals("duration must be between 1 and 5000 milliseconds",
                         response.as(JsonObject.class).getString("error"));
        }
    }

    @Test
    void rejectsNonNumericTaskDuration() {
        try (Http1ClientResponse response = client.get("/api/tasks/wait")
                .queryParam("duration", "invalid")
                .request()) {
            assertEquals(Status.BAD_REQUEST_400, response.status());
        }
    }
}
