package com.mobile.calculadora

private val negativeSignRegex = """(?<=\d)-(?=\d)""".toRegex()
private val positiveSignCombinationRegex = """\+\+|--""".toRegex()
private val negativeSignCombinationRegex = """-\+""".toRegex()
private val notSimplifiedTermRegex = """-?\d+(\.\d+)?([/*][+-]?\d+(\.\d+)?)+""".toRegex()
private val operationRegex = """(?<=\d)[+-](?=-?\d)""".toRegex()
private val operandsRegex =
    """(?<operandA>((-?\d+(?:\.\d+)?)(?<operator>[/*]))+)(?<operandB>-?\d+(?:\.\d+)?)""".toRegex()


class Calculator {
    var expression: String = ""
        set(value) {
            field = value
                .reduceSignRules()
                .normalizeDecimalSeparator()
        }

    var result: Double = 0.0
        private set

    fun calculate() {
        result = expression
            .split(operationRegex)
            .filter { it.isNotBlank() }
            .sumOf { if (it.matches(notSimplifiedTermRegex)) simplifyTerm(it) else it.toDouble() }
    }

    private fun simplifyTerm(term: String): Double {
        val groups = operandsRegex.find(term)?.groups
            ?: throw IllegalArgumentException("Malformatted term: $term")

        val operandA = groups["operandA"]?.value?.replace("""[/*]$""".toRegex(), "")
            ?: throw IllegalArgumentException("Missing operand: $term")

        val operandB = groups["operandB"]?.value
            ?: throw IllegalArgumentException("Missing operand: $term")

        val operator = groups["operator"]?.value
            ?: throw IllegalArgumentException("Missing operator: $term")

        val operation: (Double, Double) -> Double =
            if (operator == "*") { a, b -> a * b }
            else { a, b -> a / b }

        val a = if (operandA.matches(operandsRegex)) simplifyTerm(operandA) else operandA.toDouble()
        val b = operandB.toDouble()
        return operation(a, b)
    }

    companion object {
        private fun String.reduceSignRules(): String {
            var normalized = this

            while (
                positiveSignCombinationRegex.containsMatchIn(normalized) ||
                negativeSignCombinationRegex.containsMatchIn(normalized)
            ) {
                normalized = normalized.replace(positiveSignCombinationRegex, "+")
                normalized = normalized.replace(negativeSignCombinationRegex, "-")
            }

            return normalized.replace(negativeSignRegex, "+-")
        }

        private fun String.normalizeDecimalSeparator(): String = this.replace(',', '.')
    }
}