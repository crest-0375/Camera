package com.app.vocab.features.home.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RationaleDialog(onDismiss: () -> Unit, onRequestPermission: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission Required") },
        text = { Text("Camera permission is required to use this feature. Please grant permission.") },
        confirmButton = {
            TextButton(onClick = onRequestPermission) {
                Text("Try Again")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
