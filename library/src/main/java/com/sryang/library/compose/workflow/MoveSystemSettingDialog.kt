package com.sryang.library.compose.workflow

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun MoveSystemSettingDialog(onDeny: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(confirmButton = {
        TextButton({
            val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.setData(uri)
            context.startActivity(intent)
        }) {
            Text("Move")
        }
    }, onDismissRequest = {}, dismissButton = {
        TextButton(onDeny) {
            Text("No")
        }
    }, text = {
        Text("시스템 설정 이동?")
    })
}