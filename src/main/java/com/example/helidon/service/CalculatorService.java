package com.example.helidon.service;

import java.math.BigDecimal;

import com.example.helidon.model.CalculationResult;

public final class CalculatorService {

    public CalculationResult add(BigDecimal left, BigDecimal right) {
        return result("addition", left, right, left.add(right));
    }

    public CalculationResult multiply(BigDecimal left, BigDecimal right) {
        return result("multiplication", left, right, left.multiply(right));
    }

    private CalculationResult result(String operation,
                                     BigDecimal left,
                                     BigDecimal right,
                                     BigDecimal result) {
        return new CalculationResult(
                operation,
                left,
                right,
                result,
                Thread.currentThread().isVirtual());
    }
}
