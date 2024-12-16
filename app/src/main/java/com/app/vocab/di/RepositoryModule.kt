package com.app.vocab.di

import android.content.Context
import com.app.vocab.features.auth.data.AuthRepositoryImpl
import com.app.vocab.features.auth.domain.repository.AuthRepository
import com.app.vocab.features.home.data.HomeRepositoryImpl
import com.app.vocab.features.home.domain.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideHomeRepository(
        @ApplicationContext context: Context,
        storageReference: StorageReference
    ): HomeRepository = HomeRepositoryImpl(context, storageReference)
}