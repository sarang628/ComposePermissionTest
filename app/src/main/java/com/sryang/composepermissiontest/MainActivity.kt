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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //testRequestPermissions(this)

        when {
            checkSelfPermission(
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("PERMISSION_GRANTED")
                    .show()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("shouldShowRequestPermissionRationale")
                    .show()
            }
            else -> {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("PERMISSION_Denied")
                    .show()
                //testRequestPermissions(this)
            }
        }

        setContent {
            ComposePermissionTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        PermissionTest()
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionTest() {
    RequestPermissionInCompose()
}