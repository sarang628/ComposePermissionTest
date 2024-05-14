package com.sryang.library.compose

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SimplePermissionDialog(
    permission: String,
    permissionMessage: String,
    onPermissionRequest: (Int) -> Unit,
    onCancle: () -> Unit,
) {
    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(permission = permission)

    LaunchedEffect(key1 = permission) {
        val isGranted = checkPermission(context = context, permission = permission)
        onPermissionRequest.invoke(
            if (isGranted == PackageManager.PERMISSION_GRANTED) 0
            else if (cameraPermissionState.status.shouldShowRationale) 1
            else 2
        )
    }

    // 3.Request permissions
    val request = rememberPermissionState(
        permission = permission,
        onPermissionResult = {
            onPermissionRequest(
                if (it) 0
                else if (cameraPermissionState.status.shouldShowRationale) 1
                else 2
            )
        }
    )

    // 1.Determine whether your app was already granted the permission

    // 2.Explain why your app needs the permission

    when (cameraPermissionState.status) {
        is PermissionStatus.Granted -> {
            Log.d("__BastPracticePermission", "isGranted")
        }

        is PermissionStatus.Denied -> {
            Log.d("__BastPracticePermission", "isDenied")
            Log.d(
                "__BastPracticePermission",
                "shouldShowRationale : ${cameraPermissionState.status.shouldShowRationale}"
            )
            if (cameraPermissionState.status.shouldShowRationale) {
                onPermissionRequest.invoke(1)
            } else {
                AlertDialog(
                    title = { Text(text = "permission") },
                    text = { Text(text = permissionMessage) },
                    onDismissRequest = { }, confirmButton = {
                        TextButton(onClick = { // 3.1. Request permissions
                            request.launchPermissionRequest()
                        }) {
                            Text(text = "확인")
                        }
                    }, dismissButton = {
                        TextButton(onClick = {
                            onCancle.invoke()
                        }) {
                            Text(text = "취소")
                        }
                    })
            }
        }
    }
}
