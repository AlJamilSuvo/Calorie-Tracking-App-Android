package com.simple.calorie.app.processor

class FoodEntryInputProcessor : CalorieAmountInputProcessor() {

    fun isFoodNameValid(foodName: String): Boolean {
        return foodName.isNotBlank()
    }

    fun isFoodEntryTimeValid(timestamp: Long): Boolean {
        if (timestamp < 0) return false
        if (timestamp > System.currentTimeMillis()) return false
        return true
    }

}