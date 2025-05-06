package com.sryang.library.compose

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.sryang.library.CheckPermission

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SimplePermissionDialog(
    permission: String,
    permissionMessage: String,
    onPermissionRequest: (Int) -> Unit,
    onCancle: () -> Unit,
) {
    val permissionState = rememberPermissionState(permission)
    CheckPermission(permissionState = permissionState, onRequest = {
        /*AlertDialog(
            title = { Text(text = "permission") },
            text = { Text(text = permissionMessage) },
            onDismissRequest = { }, confirmButton = {
                TextButton(onClick = { // 3.1. Request permissions
                    //request.launchPermissionRequest()
                }) {
                    Text(text = "확인")
                }
            }, dismissButton = {
                TextButton(onClick = {
                    onCancle.invoke()
                }) {
                    Text(text = "취소")
                }
            })*/
    }, onGranted = {
        // 1.Determine whether your app was already granted the permission
        Log.d("__BastPracticePermission", "isGranted")
    }, onRationale = {
        // 2.Explain why your app needs the permission
        onPermissionRequest.invoke(1)
    })


}
