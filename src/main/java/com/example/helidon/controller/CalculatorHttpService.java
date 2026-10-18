package com.example.helidon.controller;

import java.math.BigDecimal;

import com.example.helidon.model.CalculationResult;
import com.example.helidon.service.CalculatorService;
import io.helidon.http.Status;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.Json;

public final class CalculatorHttpService implements HttpService {

    private final CalculatorService calculatorService;

    public CalculatorHttpService(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/add/{left}/{right}", (request, response) -> calculate(request, response, true))
                .get("/multiply/{left}/{right}", (request, response) -> calculate(request, response, false));
    }

    private void calculate(ServerRequest request, ServerResponse response, boolean addition) {
        try {
            BigDecimal left = numberParameter(request, "left");
            BigDecimal right = numberParameter(request, "right");
            CalculationResult result = addition
                    ? calculatorService.add(left, right)
                    : calculatorService.multiply(left, right);

            response.send(Json.createObjectBuilder()
                                  .add("operation", result.operation())
                                  .add("left", result.left())
                                  .add("right", result.right())
                                  .add("result", result.result())
                                  .add("virtualThread", result.virtualThread())
                                  .build());
        } catch (NumberFormatException exception) {
            response.status(Status.BAD_REQUEST_400)
                    .send(Json.createObjectBuilder()
                                  .add("error", "left and right must be valid numbers")
                                  .build());
        }
    }

    private BigDecimal numberParameter(ServerRequest request, String name) {
        return new BigDecimal(request.path().pathParameters().get(name));
    }
}
