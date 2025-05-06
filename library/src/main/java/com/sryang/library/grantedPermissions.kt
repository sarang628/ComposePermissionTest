package com.sryang.library

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
fun grantedPermissions(permissionState: MultiplePermissionsState): List<String> {
    return permissionState.permissions.filter { it.status.isGranted }.map { it.permission }
}