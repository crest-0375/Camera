package com.app.vocab.features.home.presentation.navigation

import kotlinx.serialization.Serializable

sealed class HomeRoute {

    // home section related screens

    @Serializable
    data object HomeScreen : HomeRoute()
}