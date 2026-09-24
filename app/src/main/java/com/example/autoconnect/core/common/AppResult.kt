package com.example.autoconnect.core.common

/**
 * Generic result wrapper for Clean Architecture boundary operations.
 */
sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Error(val message: String, val throwable: Throwable? = null) : AppResult<Nothing>
    data object Loading : AppResult<Nothing>
}
