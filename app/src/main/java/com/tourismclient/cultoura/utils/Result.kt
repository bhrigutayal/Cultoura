package com.tourismclient.cultoura.utils

/**
 * A generic class that holds a value or an error status
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()

    /**
     * Check if this is a success result with data
     */
    fun isSuccess(): Boolean {
        return this is Success
    }

    /**
     * Get data if this is a success result, or null otherwise
     */
    fun getOrNull(): T? {
        return (this as? Success)?.data
    }

    /**
     * Get error message if this is an error result, or null otherwise
     */
    fun errorMessage(): String? {
        return (this as? Error)?.message
    }
}