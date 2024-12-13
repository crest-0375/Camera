package com.app.vocab.features.auth.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.vocab.R
import com.app.vocab.features.auth.domain.state.AuthState
import com.app.vocab.features.auth.domain.model.User
import com.app.vocab.features.auth.presentation.components.AuthButton
import com.app.vocab.features.auth.presentation.viewmodel.AuthViewModel

@Composable
fun SignInScreen(
    innerPadding: PaddingValues,
    onBackClick: () -> Unit,
    moveToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit,
    moveToForgotPass: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    email: String
) {
    var emailText by remember { mutableStateOf(TextFieldValue(email)) }
    var passwordText by remember { mutableStateOf(TextFieldValue("")) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val authState by viewModel.authState.collectAsState()
    val formError by viewModel.signInForm.collectAsState()

    LaunchedEffect(Unit) {
        if (emailText.text.isEmpty())
            emailFocusRequester.requestFocus()
        else
            passwordFocusRequester.requestFocus()
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onSignInSuccess()
        }
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onTertiary),
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back Icon"
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 36.dp)
                .align(Alignment.CenterHorizontally),
            text = "Sign in now",
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 34.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = "Please sign in to continue our app",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.tertiary
        )

        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            isError = formError[0],
            label = { Text(text = stringResource(id = R.string.email)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .focusRequester(emailFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            )
        )
        AnimatedVisibility(
            visible = formError[0],
            enter = expandVertically(),
            exit = shrinkVertically()

        ) {
            Text(
                text = "Please enter valid email address",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Normal
            )
        }

        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text(text = stringResource(id = R.string.password)) },
            isError = formError[1],
            singleLine = true,
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(id = R.string.password_visibility_toggle),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .focusRequester(passwordFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    val user = User(
                        email = emailText.text,
                        password = passwordText.text
                    )
                    viewModel.signIn(user)
                }
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        AnimatedVisibility(
            visible = formError[1],
            enter = expandVertically(),
            exit = shrinkVertically()

        ) {
            Text(
                text = "Password must contain 6 or more characters",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Normal
            )
        }

        Text(
            text = "Forget Password?",
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .align(Alignment.End)
                .clip(RoundedCornerShape(100))
                .clickable {
                    moveToForgotPass(emailText.text)
                }
                .padding(vertical = 6.dp)
                .padding(horizontal = 10.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp
        )

        AuthButton(authState = authState, btnText = "Sign In") {
            keyboardController?.hide()
            val user = User(
                email = emailText.text,
                password = passwordText.text
            )
            viewModel.signIn(user)
        }

        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        ) {
            Text(
                text = "Don't have an account?",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp
            )
            Text(
                text = "Sign up",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        moveToSignUp()
                    },
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        AnimatedVisibility(
            visible = authState is AuthState.Error,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier.padding(12.dp)

        ) {
            when (authState) {
                is AuthState.Error -> {
                    Text(
                        text = (authState as AuthState.Error).error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
@Preview
fun SignInScreenPreview() {
    SignInScreen(innerPadding = PaddingValues(0.dp), {}, {}, {}, {}, email = "abc")
}