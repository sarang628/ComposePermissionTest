package com.sryang.composepermissiontest

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sryang.composepermissiontest.ui.theme.ComposePermissionTestTheme
import com.sryang.library.PermissionsUtil
import com.sryang.library.PermissionsUtil.Companion.RequestPermissionInCompose
import com.sryang.library.compose.Sample

//import com.sryang.library.compose.Sample

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionsUtil.requestPermission(Manifest.permission.CAMERA, this)

        setContent {
            ComposePermissionTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        PermissionsUtil.RequestPermissionInCompose(Manifest.permission.CAMERA)
                    }

                    Sample()
                }
            }
        }
    }
}

@Composable
fun PermissionTest() {

}