package com.app.vocab.core.presentation.main_screen

import kotlinx.serialization.Serializable

sealed class RootRoute {

    // screens that has separate module

    @Serializable
    data object AuthNavGraph : RootRoute()

    @Serializable
    data object AppNavGraph : RootRoute()
}