package com.app.vocab.features.home.presentation.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.vocab.features.home.domain.state.SaveImageState
import com.app.vocab.features.home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _saveImageState = MutableStateFlow<SaveImageState>(SaveImageState.Idle)
    val saveImageState: StateFlow<SaveImageState> = _saveImageState
init {
    Log.e("TAG", "here!!!: ", )
}
    fun saveImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _saveImageState.value = SaveImageState.Loading
            try {
                val result = homeRepository.saveImage(bitmap)
                _saveImageState.value = result.fold(
                    onSuccess = { SaveImageState.Success(it) },
                    onFailure = { SaveImageState.Error("Failed to save image") }
                )
            } catch (e: Exception) {
                _saveImageState.value = SaveImageState.Error("Unknown error occurred")
            }
        }
    }

    fun resetSate() {
        _saveImageState.value = SaveImageState.Idle
    }
}