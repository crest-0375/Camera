package com.app.vocab.features.auth.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.vocab.animation.AnimateScreen
import com.app.vocab.core.presentation.utils.plus
import com.app.vocab.features.auth.presentation.screen.ForgotPassScreen
import com.app.vocab.features.auth.presentation.screen.OnBoardingScreen
import com.app.vocab.features.auth.presentation.screen.SignInScreen
import com.app.vocab.features.auth.presentation.screen.SignUpScreen
import com.app.vocab.features.auth.presentation.navigation.AuthRoute.*

@Composable
fun AuthNavGraph(
    innerPadding: PaddingValues,
    onBackOrFinish: () -> Unit,
    onOnboardingSuccess: () -> Unit
) {
    val authNavController = rememberNavController()

    Scaffold(
        content = { authGraphPadding ->
            NavHost(
                navController = authNavController,
                startDestination = OnBoardingScreen
            ) {
                composable<OnBoardingScreen>(
                    popEnterTransition = AnimateScreen.rightPopEnterTransition(),
                    enterTransition = AnimateScreen.leftEnterTransition(),
                    popExitTransition = AnimateScreen.rightPopExitTransition(),
                    exitTransition = AnimateScreen.leftExitTransition()
                ) {
                    OnBoardingScreen(
                        innerPadding = authGraphPadding.plus(innerPadding),
                        onFinish = {
                            authNavController.navigate(SignInScreen) {
                                popUpTo(OnBoardingScreen) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable<SignInScreen>(
                    popEnterTransition = AnimateScreen.rightPopEnterTransition(),
                    enterTransition = AnimateScreen.leftEnterTransition(),
                    popExitTransition = AnimateScreen.rightPopExitTransition(),
                    exitTransition = AnimateScreen.leftExitTransition()
                ) {
                    val email = it.savedStateHandle.get<String>("email") ?: ""
                    SignInScreen(
                        innerPadding = authGraphPadding.plus(innerPadding),
                        onBackClick = {
                            handleBackClick(
                                authNavController,
                                onBackOrFinish
                            )
                        },
                        email = email,
                        onSignInSuccess = onOnboardingSuccess,
                        moveToSignUp = { authNavController.navigate(SignUpScreen) },
                        moveToForgotPass = { forgottenAcc ->
                            authNavController.navigate(ForgotPassScreen(forgottenAcc))
                        }
                    )
                }

                composable<SignUpScreen>(
                    popEnterTransition = AnimateScreen.rightPopEnterTransition(),
                    enterTransition = AnimateScreen.leftEnterTransition(),
                    popExitTransition = AnimateScreen.rightPopExitTransition(),
                    exitTransition = AnimateScreen.leftExitTransition()
                ) {
                    SignUpScreen(
                        innerPadding = authGraphPadding.plus(innerPadding),
                        onBackClick = {
                            handleBackClick(
                                authNavController,
                                onBackOrFinish
                            )
                        },
                        onSignUpSuccess = onOnboardingSuccess
                    )
                }

                composable<ForgotPassScreen>(
                    popEnterTransition = AnimateScreen.rightPopEnterTransition(),
                    enterTransition = AnimateScreen.leftEnterTransition(),
                    popExitTransition = AnimateScreen.rightPopExitTransition(),
                    exitTransition = AnimateScreen.leftExitTransition()
                ) {
                    val args = it.toRoute<ForgotPassScreen>()
                    ForgotPassScreen(
                        innerPadding = innerPadding,
                        onBackClick = {
                            handleBackClick(
                                authNavController,
                                onBackOrFinish
                            )
                        },
                        email = args.email,
                        moveToSignIn = { email->
                            authNavController.previousBackStackEntry?.savedStateHandle?.set("email", email)
                            authNavController.navigateUp()
                        }
                    )
                }
            }
        }
    )
}

private fun handleBackClick(authNavController: NavHostController, onBackOrFinish: () -> Unit) {
    if (authNavController.previousBackStackEntry == null) onBackOrFinish()
    else authNavController.navigateUp()
}