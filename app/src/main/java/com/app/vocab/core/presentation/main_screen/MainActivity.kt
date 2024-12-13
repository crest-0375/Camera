package com.app.vocab.core.presentation.main_screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.vocab.core.data.utils.getStartDestination
import com.app.vocab.theme.TravenorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            val rootNavController = rememberNavController()
            TravenorTheme {
                RootNavGraph(
                    rootNavController = rootNavController,
                    startDestination = getStartDestination(),
                    onBackOrFinish = { handleBackClick(rootNavController) }
                )
            }
        }
    }

    private fun handleBackClick(rootNavController: NavHostController) {
        if (rootNavController.previousBackStackEntry == null) finish()
        else rootNavController.navigateUp()
    }
}