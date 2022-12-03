package com.simple.calorie.app.processor

open class CalorieAmountInputProcessor {

    enum class InputStatus {
        VALID,
        NON_NUMERICAL_INPUT,
        LESS_OR_EQUAL_ZERO
    }

    private fun processInput(calorie: String): Pair<InputStatus, Float?> {
        kotlin.runCatching {
            val amount = calorie.toFloat()
            if (amount <= 0) return Pair(
                InputStatus.LESS_OR_EQUAL_ZERO,
                null
            )
            return Pair(
                InputStatus.VALID, amount
            )
        }.getOrElse {
            return Pair(
                InputStatus.NON_NUMERICAL_INPUT, null
            )
        }
    }

    fun checkInputValidity(input: String): InputStatus {
        return processInput(input).first
    }

    fun getCalorieAmount(input: String): Float? {
        return processInput(input).second
    }
}