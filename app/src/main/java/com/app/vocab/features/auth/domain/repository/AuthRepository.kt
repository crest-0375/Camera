package com.app.vocab.features.auth.domain.repository

import com.app.vocab.features.auth.domain.model.OnBoardingDetail
import com.app.vocab.features.auth.domain.model.User

interface AuthRepository {
    suspend fun signIn(user: User): Result<String>
    suspend fun signUp(user: User): Result<String>
    suspend fun resetPassword(user: User): Result<String>
    suspend fun signOut(): Result<String>
    fun getOnBoardingPages(): List<OnBoardingDetail>
}