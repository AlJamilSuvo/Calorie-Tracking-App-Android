package com.simple.calorie.app.processor

import com.google.common.truth.Truth
import org.junit.Test

class FoodEntryResponseInputProcessorTest {

    private val foodEntryInputProcessor = FoodEntryInputProcessor()

    @Test
    fun testFoodNameValid() {
        val result = foodEntryInputProcessor.isFoodNameValid("Mango")
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun testFoodNameEmpty() {
        val result = foodEntryInputProcessor.isFoodNameValid("")
        Truth.assertThat(result).isEqualTo(false)
    }

    @Test
    fun testFoodNameBlank() {
        val result = foodEntryInputProcessor.isFoodNameValid("    ")
        Truth.assertThat(result).isEqualTo(false)
    }


    @Test
    fun testTimestampLastDay() {
        val result =
            foodEntryInputProcessor.isFoodEntryTimeValid(System.currentTimeMillis() - 24 * 1000 * 3600)
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun testTimestampCurrent() {
        val result = foodEntryInputProcessor.isFoodEntryTimeValid(System.currentTimeMillis())
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun testTimestampFuture() {
        val result = foodEntryInputProcessor.isFoodEntryTimeValid(System.currentTimeMillis() + 24 * 1000 * 3600)
        Truth.assertThat(result).isEqualTo(false)
    }

    @Test
    fun testTimestampNegative() {
        val result = foodEntryInputProcessor.isFoodEntryTimeValid(- 24 * 1000 * 3600)
        Truth.assertThat(result).isEqualTo(false)
    }
}