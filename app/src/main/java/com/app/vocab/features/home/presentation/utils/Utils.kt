package com.app.vocab.features.home.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object Utils {
    fun Context.openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}