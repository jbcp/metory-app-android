package com.devmon.crcp.ui.screening

import com.devmon.crcp.Constants

sealed class UiState {
    object Loading: UiState()
    object Success: UiState()
    data class Error(private val message: String?): UiState() {
        fun message() = message ?: Constants.SERVER_ERROR
    }

    companion object {
        fun loading() = Loading
        fun success() = Success
        fun error(message: String?) = Error(message)
    }
}