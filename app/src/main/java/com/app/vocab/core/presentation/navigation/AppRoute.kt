package com.app.vocab.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed class AppRoute {

    // other bottom nav screens

    val name: String
        get() = this::class.qualifiedName ?: (this::class.simpleName ?: this::class.toString())

    @Serializable
    data object HomeAppRoute : AppRoute()

}