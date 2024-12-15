package com.app.vocab.features.auth.domain.state

import android.net.Uri

sealed class SaveImageState {
    data object Idle : SaveImageState()
    data object Loading : SaveImageState()
    data class Success(val uri: Uri) : SaveImageState()
    data class Error(val error: String) : SaveImageState()
}
