package com.app.vocab.features.home.domain.state

sealed class SaveImageState {
    data object Idle : SaveImageState()
    data object Loading : SaveImageState()
    data class Success(val uri: String) : SaveImageState()
    data class Error(val error: String) : SaveImageState()
}
