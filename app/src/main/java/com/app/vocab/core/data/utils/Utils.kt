package com.app.vocab.core.data.utils

import com.app.vocab.core.data.Preferences
import com.app.vocab.core.presentation.main_screen.RootRoute.*

fun getStartDestination(): Any {
    return if (Preferences.getIsOnboarded()) AppNavGraph
    else AuthNavGraph
}