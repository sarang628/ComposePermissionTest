package com.sryang.library.compose

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BastPracticePermission(permission: String,
                           permissionMessage : String) {
    val context = LocalContext.current
    val isGranted = checkPermission(context = context, permission = permission)
    var isExplain by remember { mutableStateOf(false) }

    // 3.Request permissions
    val request = rememberPermissionState(
        permission = permission,
        onPermissionResult = {

        })

    Column {
        // 1.Determine whether your app was already granted the permission
        if (isGranted == PackageManager.PERMISSION_GRANTED)
            Text(text = "ACCESS_FINE_LOCATION is PERMISSION_GRANTED")
        else if (isGranted == PackageManager.PERMISSION_DENIED)
            Text(text = "ACCESS_FINE_LOCATION is PERMISSION_DENIED")

        // 1.Determine whether your app was already granted the permission (compose)
        Text(text = "ACCESS_FINE_LOCATION is ${request.status}")

        // 2.Explain why your app needs the permission
        Button(onClick = {
            if (!isExplain) {
                isExplain = true

                AlertDialog
                    .Builder(context)
                    .setMessage(permissionMessage)
                    .setPositiveButton("확인") { _, _ -> }
                    .show()
            } else {
                // 3.1. Request permissions
                request.launchPermissionRequest()
            }
        }) {
            Text(text = "RequestPermission")
        }
    }

}

/**
 * @return
 * [android.content.pm.PackageManager.PERMISSION_DENIED],
 * [android.content.pm.PackageManager.PERMISSION_GRANTED]
 */
fun checkPermission(context: Context, permission: String): Int {

    return context.checkSelfPermission(permission)
}