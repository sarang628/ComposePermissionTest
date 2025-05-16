package com.sryang.library.compose.workflow

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveSystemSettingDialog(onDeny: () -> Unit) {
    val context = LocalContext.current
    BasicAlertDialog({}) {
        Row {
            Button({
                val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.setData(uri)
                context.startActivity(intent)
            }) {
                Text("시스템 설정 이동?")
            }
            Button(onDeny) {
                Text("No")
            }
        }
    }
}