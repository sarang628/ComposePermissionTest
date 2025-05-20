package com.sryang.library.compose.workflow

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DescribePermissionDialog(
    onYes: () -> Unit = {},
    onNo: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(onDismiss, confirmButton = {
        TextButton(onYes) {
            Text("Yes")
        }
    }, dismissButton = {
        TextButton(onNo) {
            Text("No")
        }
    }, text = { Text("권한이 필요합니다.") })
}