package com.app.vocab.features.auth.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.vocab.features.auth.domain.model.OnBoardingDetail
import com.app.vocab.features.auth.domain.model.User
import com.app.vocab.features.auth.domain.repository.AuthRepository
import com.app.vocab.features.auth.domain.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    val signUpForm = MutableStateFlow(listOf(false, false, false))
    val signInForm = MutableStateFlow(listOf(false, false))
    val forgetPassForm = MutableStateFlow(false)

    fun signUp(user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val resultList = mutableListOf(false, false, false)

                if (user.name.isEmpty()) resultList[0] = true

                if (!Patterns.EMAIL_ADDRESS
                        .matcher(user.email)
                        .matches()
                ) resultList[1] = true

                if (user.password.length < 6) resultList[2] = true

                if (resultList.all { !it }) {
                    val result = authRepository.signUp(user)
                    _authState.value = result.fold(
                        onSuccess = { AuthState.Success(it) },
                        onFailure = { AuthState.Error(it.message ?: "Sign up failed") }
                    )
                } else {
                    _authState.value = AuthState.Idle
                    signUpForm.value = resultList
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun signIn(user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val resultList = mutableListOf(false, false)

                if (!Patterns.EMAIL_ADDRESS
                        .matcher(user.email)
                        .matches()
                )
                    resultList[0] = true

                if (user.password.length < 6) resultList[1] = true

                if (resultList.all { !it }) {
                    val result = authRepository.signIn(user)
                    _authState.value = result.fold(
                        onSuccess = { AuthState.Success(it) },
                        onFailure = { AuthState.Error(it.message ?: "Sign in failed") }
                    )
                } else {
                    _authState.value = AuthState.Idle
                    signInForm.value = resultList
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun forgetPassword(user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                if (Patterns.EMAIL_ADDRESS
                        .matcher(user.email)
                        .matches()
                ) {
                    val result = authRepository.resetPassword(user)
                    _authState.value = result.fold(
                        onSuccess = { AuthState.Success(it) },
                        onFailure = { AuthState.Error(it.message ?: "Failed to send reset link") }
                    )
                } else {
                    _authState.value = AuthState.Idle
                    forgetPassForm.value = true
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.signOut()
                _authState.value = result.fold(
                    onSuccess = { AuthState.Success(it) },
                    onFailure = { AuthState.Error(it.message ?: "Unknown error occurred") }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
        
    fun getOnBoardingPages(): List<OnBoardingDetail> {
        return authRepository.getOnBoardingPages()
    }
}
