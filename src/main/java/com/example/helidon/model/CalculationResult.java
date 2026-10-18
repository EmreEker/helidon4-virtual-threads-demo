package com.example.helidon.model;

import java.math.BigDecimal;

public record CalculationResult(String operation,
                                BigDecimal left,
                                BigDecimal right,
                                BigDecimal result,
                                boolean virtualThread) {
}
