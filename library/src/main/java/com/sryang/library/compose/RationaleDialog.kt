package com.sryang.library.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RationaleDialog(onConfirm : ()->Unit, onDismiss : ()->Unit, permissions : String){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Permissions required by the sample") },
        text = { Text(text = "The sample requires the following permissions to work:\n $permissions") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Continue") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Dismiss") } },
    )
}