package com.app.vocab.features.home.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.vocab.animation.AnimateScreen
import com.app.vocab.core.presentation.utils.plus
import com.app.vocab.features.home.presentation.navigation.HomeRoute.HomeScreen
import com.app.vocab.features.home.presentation.navigation.HomeRoute.SavedImageScreen
import com.app.vocab.features.home.presentation.screen.HomeScreen
import com.app.vocab.features.home.presentation.screen.SavedImageScreen

@Composable
fun HomeNavGraph(
    innerPadding: PaddingValues,
    onBackOrFinish: () -> Unit
) {
    val homeNavController = rememberNavController()

    Scaffold(
        content = { authGraphPadding ->
            NavHost(
                navController = homeNavController,
                startDestination = HomeScreen
            ) {
                composable<HomeScreen>(
                    popEnterTransition = AnimateScreen.rightPopEnterTransition(),
                    enterTransition = AnimateScreen.leftEnterTransition(),
                    popExitTransition = AnimateScreen.rightPopExitTransition(),
                    exitTransition = AnimateScreen.leftExitTransition()
                ) {
                    HomeScreen(
                        innerPadding = authGraphPadding.plus(innerPadding),
                        moveToImageScreen = { uri -> homeNavController.navigate(SavedImageScreen(uri)) }
                    )
                }

                composable<SavedImageScreen>(
                    popEnterTransition = AnimateScreen.rightPopEnterTransition(),
                    enterTransition = AnimateScreen.leftEnterTransition(),
                    popExitTransition = AnimateScreen.rightPopExitTransition(),
                    exitTransition = AnimateScreen.leftExitTransition()
                ) {
                    val args = it.toRoute<SavedImageScreen>()
                    SavedImageScreen(
                        innerPadding = authGraphPadding.plus(innerPadding),
                        uri = args.uri.toUri(),
                        onBackOrFinish = { handleBackClick(homeNavController, onBackOrFinish) },
                        onSaveClick = { handleBackClick(homeNavController, onBackOrFinish) }
                    )
                }
            }
        }
    )
}

private fun handleBackClick(homeNavController: NavHostController, onBackOrFinish: () -> Unit) {
    if (homeNavController.previousBackStackEntry == null) onBackOrFinish()
    else homeNavController.navigateUp()
}