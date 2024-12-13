package com.app.vocab.features.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed class AuthRoute {

    // auth related screens

    @Serializable
    data object OnBoardingScreen : AuthRoute()

    @Serializable
    data object SignInScreen : AuthRoute()

    @Serializable
    data object SignUpScreen : AuthRoute()

    @Serializable
    data class ForgotPassScreen(val email: String) : AuthRoute()

}