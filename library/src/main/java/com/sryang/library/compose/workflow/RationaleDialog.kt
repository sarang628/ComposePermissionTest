package com.sryang.library.compose.workflow

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RationaleDialog(onYes: () -> Unit, onNo: () -> Unit) {
    AlertDialog(
        onDismissRequest = onNo,
        text = { Text("RationaleDialog") },
        confirmButton = { TextButton(onYes) { Text("Yes") } },
        dismissButton = { TextButton(onNo) { Text("No") } }
    )
}