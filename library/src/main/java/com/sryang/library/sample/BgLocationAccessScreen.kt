package com.sryang.library.sample

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable

@Composable
fun BgLocationAccessScreen() {
    // Request for foreground permissions first
    PermissionBox(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        requiredPermissions = listOf(Manifest.permission.ACCESS_COARSE_LOCATION),
        onGranted = {
            // From Android 10 onwards request for background permission only after fine or coarse is granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PermissionBox(permissions = listOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {

                }
            } else {

            }
        },
    )
}
