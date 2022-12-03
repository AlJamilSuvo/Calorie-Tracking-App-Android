package com.simple.calorie.app.processor

import com.google.common.truth.Truth
import org.junit.Test

class CalorieAmountInputProcessorTest {

    private val calorieAmountInputProcessor = CalorieAmountInputProcessor()


    @Test
    fun testCheckInputValidityOk1() {
        val result = calorieAmountInputProcessor.checkInputValidity("1.0")
        Truth.assertThat(result).isEqualTo(CalorieAmountInputProcessor.InputStatus.VALID)
    }

    @Test
    fun testCheckInputValidityOk2() {
        val result = calorieAmountInputProcessor.checkInputValidity("0.2")
        Truth.assertThat(result).isEqualTo(CalorieAmountInputProcessor.InputStatus.VALID)
    }

    @Test
    fun testCheckInputValidityOk3() {
        val result = calorieAmountInputProcessor.checkInputValidity("2.1")
        Truth.assertThat(result).isEqualTo(CalorieAmountInputProcessor.InputStatus.VALID)
    }

    @Test
    fun testCheckInputValidityInvalidInput1() {
        val result = calorieAmountInputProcessor.checkInputValidity("abc")
        Truth.assertThat(result)
            .isEqualTo(CalorieAmountInputProcessor.InputStatus.NON_NUMERICAL_INPUT)
    }

    @Test
    fun testCheckInputValidityInvalidInput2() {
        val result = calorieAmountInputProcessor.checkInputValidity("12s")
        Truth.assertThat(result)
            .isEqualTo(CalorieAmountInputProcessor.InputStatus.NON_NUMERICAL_INPUT)
    }

    @Test
    fun testCheckInputValidityInvalidInput3() {
        val result = calorieAmountInputProcessor.checkInputValidity("0.5p")
        Truth.assertThat(result)
            .isEqualTo(CalorieAmountInputProcessor.InputStatus.NON_NUMERICAL_INPUT)
    }

    @Test
    fun testCheckInputValidityNegative() {
        val result = calorieAmountInputProcessor.checkInputValidity("-2.4")
        Truth.assertThat(result)
            .isEqualTo(CalorieAmountInputProcessor.InputStatus.LESS_OR_EQUAL_ZERO)
    }

    @Test
    fun testCheckInputValidityZero() {
        val result = calorieAmountInputProcessor.checkInputValidity("0")
        Truth.assertThat(result)
            .isEqualTo(CalorieAmountInputProcessor.InputStatus.LESS_OR_EQUAL_ZERO)
    }


    @Test
    fun testGetCalorieAmountOk1() {
        val result = calorieAmountInputProcessor.getCalorieAmount("1.0")
        Truth.assertThat(result).isEqualTo(1.0f)
    }

    @Test
    fun testGetCalorieAmountOk2() {
        val result = calorieAmountInputProcessor.getCalorieAmount("0.2")
        Truth.assertThat(result).isEqualTo(0.2f)
    }

    @Test
    fun testGetCalorieAmountOk3() {
        val result = calorieAmountInputProcessor.getCalorieAmount("2.1")
        Truth.assertThat(result).isEqualTo(2.1f)
    }

    @Test
    fun testGetCalorieAmountInvalidInput1() {
        val result = calorieAmountInputProcessor.getCalorieAmount("abc")
        Truth.assertThat(result)
            .isEqualTo(null)
    }

    @Test
    fun testGetCalorieAmountInvalidInput2() {
        val result = calorieAmountInputProcessor.getCalorieAmount("12s")
        Truth.assertThat(result)
            .isEqualTo(null)
    }

    @Test
    fun testGetCalorieAmountInvalidInput3() {
        val result = calorieAmountInputProcessor.getCalorieAmount("0.5p")
        Truth.assertThat(result)
            .isEqualTo(null)
    }

    @Test
    fun testGetCalorieAmountNegative() {
        val result = calorieAmountInputProcessor.getCalorieAmount("-2.4")
        Truth.assertThat(result)
            .isEqualTo(null)
    }

    @Test
    fun testGetCalorieAmountZero() {
        val result = calorieAmountInputProcessor.getCalorieAmount("0")
        Truth.assertThat(result)
            .isEqualTo(null)
    }
}