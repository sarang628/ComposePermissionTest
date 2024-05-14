package com.sryang.library.compose

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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

@Preview
@Composable
fun test() {
    ComposeRequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION)
}