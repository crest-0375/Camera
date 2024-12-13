package com.app.vocab.core.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.vocab.animation.AnimateScreen
import com.app.vocab.core.presentation.components.AppBottomBar
import com.app.vocab.core.presentation.navigation.AppRoute.HomeAppRoute
import com.app.vocab.core.presentation.utils.plus
import com.app.vocab.features.home.presentation.navigation.HomeNavGraph

@Composable
fun AppNavGraph(
    innerPadding: PaddingValues,
    onBackOrFinish: () -> Unit,
    onSignOut: () -> Unit
) {
    val appNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomBar(appNavController = appNavController)
        },
        content = { appGraphPadding ->
            NavHost(
                navController = appNavController,
                startDestination = HomeAppRoute
            ) {
                composable<HomeAppRoute>(
                    popEnterTransition = AnimateScreen.rightPopEnterTransition(),
                    enterTransition = AnimateScreen.leftEnterTransition(),
                    popExitTransition = AnimateScreen.rightPopExitTransition(),
                    exitTransition = AnimateScreen.leftExitTransition()
                ) {
                    HomeNavGraph(
                        innerPadding = appGraphPadding.plus(innerPadding),
                        onBackOrFinish = { handleBackClick(appNavController, onBackOrFinish) }
                    )
                }
            }
        }
    )
}

private fun handleBackClick(appNavController: NavHostController, onBackOrFinish: () -> Unit) {
    if (appNavController.previousBackStackEntry == null) onBackOrFinish()
    else appNavController.navigateUp()
}