package com.simple.calorie.app.repositories

sealed class ResponseResource<T>(val response: T? = null, val message: String? = null) {
    class Succeed<T>(response: T?) : ResponseResource<T>(response)
    class Pending<T> : ResponseResource<T>()
    class Failed<T>(message: String?) : ResponseResource<T>(null, message)
}