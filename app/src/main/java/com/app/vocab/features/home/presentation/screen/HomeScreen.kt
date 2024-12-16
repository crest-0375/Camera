package com.app.vocab.features.home.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.vocab.R
import com.app.vocab.features.home.domain.state.SaveImageState
import com.app.vocab.features.home.presentation.components.CameraPreview
import com.app.vocab.features.home.presentation.components.RationaleDialog
import com.app.vocab.features.home.presentation.components.SettingsDialog
import com.app.vocab.features.home.presentation.utils.Utils.openAppSettings
import com.app.vocab.features.home.presentation.utils.handlePermissionRequest
import com.app.vocab.features.home.presentation.viewmodel.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    moveToImageScreen: (String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val saveImageState by homeViewModel.saveImageState.collectAsState()

    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showRationaleDialog by remember { mutableStateOf(false) }
    var hasRequestedPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            handlePermissionRequest(
                cameraPermissionState = cameraPermissionState,
                hasRequested = hasRequestedPermission,
                onHasRequested = {
                    hasRequestedPermission = it
                },
                onResult = { rationale, settings ->
                    showRationaleDialog = rationale
                    showSettingsDialog = settings
                }
            )
        }
    }

    LaunchedEffect(saveImageState) {
        if (saveImageState is SaveImageState.Success) {
            moveToImageScreen((saveImageState as SaveImageState.Success).uri)
            homeViewModel.resetSate()
        }
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_forget_pass_alert),
                contentDescription = ""
            )
            Text(text = "Vocab", fontSize = 40.sp)
        }
        when {
            cameraPermissionState.status.isGranted -> {
                CameraPreview(captureImage = { homeViewModel.saveImage(it) })
            }

            showRationaleDialog -> {
                RationaleDialog(
                    onDismiss = { showRationaleDialog = false },
                    onRequestPermission = {
                        showRationaleDialog = false
                        cameraPermissionState.launchPermissionRequest()
                    }
                )
            }

            showSettingsDialog -> {
                SettingsDialog(
                    onDismiss = { showSettingsDialog = false },
                    onOpenSettings = {
                        context.openAppSettings()
                    }
                )
            }

            else -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Camera permission is required to use this feature.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (!cameraPermissionState.status.isGranted) {
                                handlePermissionRequest(
                                    cameraPermissionState = cameraPermissionState,
                                    hasRequested = hasRequestedPermission,
                                    onHasRequested = {
                                        hasRequestedPermission = it
                                    },
                                    onResult = { rationale, settings ->
                                        showRationaleDialog = rationale
                                        showSettingsDialog = settings
                                    }
                                )
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }
}