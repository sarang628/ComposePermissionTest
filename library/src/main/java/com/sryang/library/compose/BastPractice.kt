package com.sryang.library.compose

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

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

    Column {
        // 1.Determine whether your app was already granted the permission

        // 2.Explain why your app needs the permission
        contents?.invoke {
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
                        AlertDialog
                            .Builder(context)
                            .setMessage(permissionMessage)
                            .setNegativeButton("취소") { _, _ -> }
                            .setPositiveButton("확인") { _, _ ->
                                // 3.1. Request permissions
                                request.launchPermissionRequest()
                            }
                            .show()
                    }
                }
            }
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