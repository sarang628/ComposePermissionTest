package com.sryang.library.compose

import android.Manifest
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
@Preview
@Composable
fun BastPracticePermission() {
    val context = LocalContext.current
    val isGranted = checkLocationPermission(context = context)
    var isExplain by remember { mutableStateOf(false) }

    // 3.Request permissions
    val request = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
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
                    .setMessage("어떤 이유로 민감한 정보에 접근합니다.")
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
fun checkLocationPermission(context: Context): Int {

    return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
}