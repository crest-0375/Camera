package com.app.vocab.core.presentation.components

import androidx.annotation.DrawableRes
import com.app.vocab.R
import com.app.vocab.core.presentation.navigation.AppRoute
import com.app.vocab.core.presentation.navigation.AppRoute.*

data class BottomNavigationItem(
    val appRoute: AppRoute,
    val title: String,
    @DrawableRes val icon: Int
)

val items = listOf(
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Home",
        icon = R.drawable.ic_home,
    ),
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Popular",
        icon = R.drawable.ic_home,
    ),
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Search",
        icon = R.drawable.ic_home,
    ),
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Profile",
        icon = R.drawable.ic_home,
    )
)