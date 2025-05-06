package com.sryang.library.compose

import android.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.sryang.library.CheckPermission

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BastPracticePermission(
    permission: String,
    permissionMessage: String,
    onPermissionRequest: (Int) -> Unit,
    contents: @Composable ((
        onClick: () -> Unit,
    ) -> Unit)? = null,
) {
    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(permission = permission)
    // 1.Determine whether your app was already granted the permission
    CheckPermission(cameraPermissionState, onGranted = {
    }, onRationale = {

    }, onRequest = {
        if (cameraPermissionState.status.shouldShowRationale) {
            onPermissionRequest.invoke(1)
        } else {
            // 2.Explain why your app needs the permission
            AlertDialog
                .Builder(context)
                .setMessage(permissionMessage)
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { _, _ ->
                    // 3.1. Request permissions
                    cameraPermissionState.launchPermissionRequest()
                }
                .show()
        }
    })
}