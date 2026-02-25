package com.mobile.calculadora

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CalculatorTests {
    @ParameterizedTest
    @MethodSource("testData")
    fun calculations_shouldBeCorrect(expression: String, expectedResult: Double) {
        val calculator = Calculator()
        calculator.expression = expression
        calculator.calculate()
        assert(calculator.result == expectedResult)
    }

    companion object {
        @JvmStatic
        fun testData(): Stream<Arguments> = Stream.of(
            arguments("10,5+4,5", 15),
            arguments("10/4", 2.5),
            arguments("2,5*3", 7.5),
            arguments("5--3", 8),
            arguments("10+-2", 8),
            arguments("-5+-5", -10),
            arguments("2+--2", 4),
            arguments("2---2", 0),
            arguments("10+5*2", 20),
            arguments("20-10/2", 15),
            arguments("2*3+4*5", 26),
            arguments("100/10/2", 5),
            arguments("2+-4*-5", 22),
            arguments("-10/-2+3*-4", -7),
            arguments("0,5*-2+10", 9),
            arguments("1--1--1", 3),
            arguments("5*-2/-4", 2.5),
            arguments("2+-4/3", 2-4/3.0),
            arguments("-1*-1*-1", -1),
            arguments("10/0,5", 20),
        )
    }
}