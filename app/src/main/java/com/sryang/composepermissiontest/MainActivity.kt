package com.sryang.composepermissiontest

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sryang.composepermissiontest.ui.theme.ComposePermissionTestTheme
import com.sryang.library.compose.SimplePermissionDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //PermissionsUtil.requestPermission(Manifest.permission.CAMERA, this)

        setContent {
            var text by remember { mutableStateOf("") }
            var showDialog by remember { mutableStateOf(false) }
            ComposePermissionTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Button(onClick = { showDialog = true }) {
                            Text(text = text)
                        }
                        /*BastPracticePermission(
                            permission = Manifest.permission.CALL_PHONE,
                            permissionMessage = "전화 권힌 필요.",
                            onPermissionRequest = {
                                text = if (it == 0) "granted"
                                else if (it == 1) "shouldShowRationale"
                                else "ungranted"
                            },
                            contents = { onClick ->
                                Text(text = text, Modifier.clickable {
                                    onClick.invoke()
                                })
                            }
                        )*/
                    }

                    if (showDialog) {
                        SimplePermissionDialog(
                            permission = Manifest.permission.CALL_PHONE,
                            permissionMessage = "전화 권힌 필요.",
                            onPermissionRequest = {
                                text = if (it == 0) "granted"
                                else if (it == 1) "shouldShowRationale"
                                else "ungranted"
                            },
                            onCancle = {
                                showDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionTest() {

}