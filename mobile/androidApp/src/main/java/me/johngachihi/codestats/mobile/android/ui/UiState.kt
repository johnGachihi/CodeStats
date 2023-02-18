package me.johngachihi.codestats.mobile.android.ui

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    class Success<T>(val data: T) : UiState<T>
    class Error(val error: Throwable) : UiState<Nothing>
}