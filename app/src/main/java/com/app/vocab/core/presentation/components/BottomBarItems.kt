package com.app.vocab.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.app.vocab.core.presentation.navigation.AppRoute
import com.app.vocab.core.presentation.navigation.AppRoute.*

data class BottomNavigationItem(
    val appRoute: AppRoute,
    val title: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

val items = listOf(
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Home",
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Default.Home,
    ),
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Popular",
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Default.Home,
    ),
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Search",
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Default.Home,
    ),
    BottomNavigationItem(
        appRoute = HomeAppRoute,
        title = "Profile",
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Default.Home,
    )
)