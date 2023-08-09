package com.sryang.composepermissiontest

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RequestPermissionInCompose() {
    val launch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {

        })

    Button(onClick = {
        launch.launch(
            Manifest.permission.CAMERA
        )
    }) {

    }
}

@Composable
fun testSouldShowRequestPermissionRationale() {
    when (
        LocalContext.current.checkSelfPermission(
            Manifest.permission.CAMERA
        )
    ){

    }
}

