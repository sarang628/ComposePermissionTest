package com.sryang.library.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ComposeRequestPermission(permission: String) {
    val request = rememberPermissionState(permission = permission, onPermissionResult = {

    })
    Column {
        Button(onClick = {
            request.launchPermissionRequest()
        }) {

        }
    }
}