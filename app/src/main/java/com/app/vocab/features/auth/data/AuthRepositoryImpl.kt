package com.app.vocab.features.auth.data

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.app.vocab.R
import com.app.vocab.features.auth.domain.model.OnBoardingDetail
import com.app.vocab.features.auth.domain.model.User
import com.app.vocab.features.auth.domain.repository.AuthRepository
import com.app.vocab.theme.secondary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signIn(user: User): Result<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(user.email, user.password).await()
            if (result.user != null) Result.success("Sign in was successful")
            else Result.failure(Exception("Sign in failed"))
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(e.message ?: "Sign in failed"))
        } catch (e: Exception) {
            Result.failure(Exception("Unknown error occurred during sign in"))
        }
    }

    override suspend fun signUp(user: User): Result<String> {
        return try {
            val result =
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            if (result.user != null) Result.success("Sign up was successful")
            else Result.failure(Exception("Sign up failed"))
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(e.message ?: "Sign up failed"))
        } catch (e: Exception) {
            Result.failure(Exception("Unknown error occurred during sign up"))
        }
    }

    override suspend fun resetPassword(user: User): Result<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(user.email).await()
            Result.success("Password reset email sent successfully")
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(e.message ?: "Password reset failed"))
        } catch (e: Exception) {
            Result.failure(Exception("Unknown error occurred during password reset"))
        }
    }

    override suspend fun signOut(): Result<String> {
        return try {
            firebaseAuth.signOut()
            Result.success("Sign Out successfully!")
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(e.message ?: "Sign Out failed"))
        } catch (e: Exception) {
            Result.failure(Exception("Unknown error occurred during sign Out"))
        }
    }

    override fun getOnBoardingPages(): List<OnBoardingDetail> {
        return listOf(
            OnBoardingDetail(
                image = R.drawable.onboarding_1,
                titleText = getAnnotatedString(
                    otherText = "Life is short and the world is ",
                    mainText = "wide"
                ),
                detailText = "At Friends tours and travel, we customize trustworthy tours to destinations all over the world",
                buttonText = "Next",
                descriptionOfImage = "Page 1"
            ), OnBoardingDetail(
                image = R.drawable.onboarding_2,
                titleText = getAnnotatedString(
                    otherText = "It’s a big world out there go ",
                    mainText = "explore"
                ),
                detailText = "To get the best of your adventure you just need to go where you like. we are waiting for you",
                buttonText = "Next",
                descriptionOfImage = "Page 2"
            ), OnBoardingDetail(
                image = R.drawable.onboarding_3,
                titleText = getAnnotatedString(
                    otherText = "It's always, Journey over ",
                    mainText = "destination"
                ),
                detailText = "Whether it's your first step or your hundredth, we're here to make your journey unforgettable.",
                buttonText = "Get Started",
                descriptionOfImage = "Page 3"
            )
        )
    }

    private fun getAnnotatedString(otherText: String, mainText: String): AnnotatedString {
        return buildAnnotatedString {
            append(otherText)
            withStyle(SpanStyle(color = secondary)) {
                append(mainText)
            }
        }
    }
}
