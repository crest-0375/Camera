package com.app.vocab.features.auth.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.vocab.R
import com.app.vocab.features.auth.domain.state.AuthState
import com.app.vocab.features.auth.domain.model.User
import com.app.vocab.features.auth.presentation.components.AuthButton
import com.app.vocab.features.auth.presentation.viewmodel.AuthViewModel
import com.app.vocab.features.auth.presentation.components.ForgotPassDialog

@Composable
fun ForgotPassScreen(
    innerPadding: PaddingValues,
    onBackClick: () -> Unit,
    email: String,
    moveToSignIn: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var emailText by remember { mutableStateOf(TextFieldValue(email)) }
    var isDialogShow by remember { mutableIntStateOf(0) }
    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val authState by viewModel.authState.collectAsState()
    val formError by viewModel.forgetPassForm.collectAsState()

    LaunchedEffect(Unit) {
        passwordFocusRequester.requestFocus()
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            isDialogShow = 1
        } else if (authState is AuthState.Error) {
            isDialogShow = 2
        }
    }

    when (isDialogShow) {
        1 -> {
            ForgotPassDialog(
                message = "Reset password link sent to ${emailText.text}",
                titleMessage = "Check your mail",
                onConfirmClick = {
                    isDialogShow = 0
                    moveToSignIn(emailText.text)
                },
                onDismissRequest = { isDialogShow = 0 })
        }

        2 -> {
            ForgotPassDialog(
                message = "Something went wrong!",
                titleMessage = "We are sorry",
                onConfirmClick = {
                    isDialogShow = 0
                    moveToSignIn(emailText.text)
                },
                onDismissRequest = { isDialogShow = 0 })
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
                text = "Forgot password",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 34.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Enter your email account to reset\nyour password",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )

            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                isError = formError,
                label = { Text(text = stringResource(id = R.string.email)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .focusRequester(passwordFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        val user = User(email = emailText.text)
                        viewModel.forgetPassword(user)
                    }
                )
            )
            AnimatedVisibility(
                visible = formError,
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

            AuthButton(authState = authState, btnText = "Reset Password") {
                keyboardController?.hide()
                val user = User(email = emailText.text)
                viewModel.forgetPassword(user)
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
fun ForgotPassScreenPreview() {
    ForgotPassScreen(innerPadding = PaddingValues(0.dp), {}, "", { })
}