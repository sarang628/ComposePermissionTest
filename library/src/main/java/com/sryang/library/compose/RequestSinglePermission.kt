package com.sryang.library.compose

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestSinglePermission(permission: String, onPermissionResult: (Boolean) -> Unit = {}) {
    val request = rememberPermissionState(permission = permission, onPermissionResult = onPermissionResult)

    LaunchedEffect(permission) {
        request.launchPermissionRequest()
    }
}

@Preview
@Composable
fun test() {
    var result = ""
    RequestSinglePermission(permission = Manifest.permission.ACCESS_FINE_LOCATION){

    }
}