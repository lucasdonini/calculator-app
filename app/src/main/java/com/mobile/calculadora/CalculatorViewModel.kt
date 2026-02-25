package com.mobile.calculadora

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    private var _lastResult: String? = null
    private var _isSolved: Boolean = false
    private val _calculator: Calculator = Calculator()
    private val _result = mutableStateOf("")
    private val _buffer = mutableStateOf("")
    private val _operationPreview = mutableStateOf("")

    val result: State<String> = _result
    val operationPreview: State<String> = _operationPreview

    fun beautifyResult(result: Double): String = "$result".replace(".0", "").replace('.', ',')

    fun solve() {
        _calculator.expression = _buffer.value
        _calculator.calculate()
        _result.value = beautifyResult(_calculator.result)
        _lastResult = _result.value
        _isSolved = true
        _operationPreview.value = _buffer.value + '=' + _result.value
    }

    fun type(value: Char) {
        var buffer = when {
            _isSolved && !Character.isDigit(value) -> _lastResult!!
            !_isSolved -> _buffer.value
            else -> ""
        }

        buffer += value
        _buffer.value = buffer
        _operationPreview.value = buffer
        _isSolved = false
    }

    fun backspace() {
        if (_buffer.value.isEmpty()) return

        val buffer = StringBuilder(_buffer.value)
        buffer.setLength(buffer.length - 1)

        _buffer.value = buffer.toString()
        _operationPreview.value = buffer.toString()
    }

    fun clear() {
        _buffer.value = ""
        _result.value = ""
        _operationPreview.value = ""
        _lastResult = null
        _isSolved = false
    }
}
