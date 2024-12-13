package com.app.vocab.features.home.presentation.utils

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
fun handlePermissionRequest(
    cameraPermissionState: PermissionState,
    hasRequested: Boolean,
    onHasRequested: (Boolean) -> Unit,
    onResult: (showRationale: Boolean, showSettings: Boolean) -> Unit
) {
    if (!hasRequested) {
        cameraPermissionState.launchPermissionRequest()
        onHasRequested(true)
        return
    }

    val status = cameraPermissionState.status
    when {
        status.shouldShowRationale -> onResult(true, false)
        !status.isGranted -> onResult(false, true)
    }
}