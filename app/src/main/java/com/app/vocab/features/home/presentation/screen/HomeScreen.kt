package com.app.vocab.features.home.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.vocab.features.home.presentation.components.CameraPreview
import com.app.vocab.features.home.presentation.components.RationaleDialog
import com.app.vocab.features.home.presentation.components.SettingsDialog
import com.app.vocab.features.home.presentation.utils.Utils.openAppSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    onBackOrFinish: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
        var showSettingsDialog by remember { mutableStateOf(false) }
        var showRationaleDialog by remember { mutableStateOf(false) }

        LaunchedEffect(cameraPermissionState.status) {
            when {
                !cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale -> {
                    showSettingsDialog = true
                }

                !cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale -> {
                    showRationaleDialog = true
                }
            }
        }

        when {
            cameraPermissionState.status.isGranted -> {
                CameraPreview(onImageCaptured = { imageUri ->
                    Toast.makeText(context, "Image saved at $imageUri", Toast.LENGTH_SHORT).show()
                })
            }

            showSettingsDialog -> {
                SettingsDialog(
                    onDismiss = { showSettingsDialog = false },
                    onOpenSettings = {
                        context.openAppSettings()
                    }
                )
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

            else -> {
                PermissionRequiredText(
                    onRequestPermission = {
                        if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
                            showSettingsDialog = true
                        } else
                            cameraPermissionState.launchPermissionRequest()
                    }
                )
            }
        }
    }
}

@Composable
fun PermissionRequiredText(onRequestPermission: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Camera permission is required to use this feature.")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}