package com.bnc.ticketmanager.common

@Suppress("UNUSED")
sealed class UiState<T> {
    class Loading<T>(
        val loading: Boolean = true,
        val data: T? = null
    ) : UiState<T>()

    class Success<T>(
        val data: T,
        val message: String? = null
    ) : UiState<T>()

    class Failure<T>(
        val error: String,
        val data: T? = null
    ) : UiState<T>()
}