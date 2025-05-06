package com.sryang.composepermissiontest

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.sryang.composepermissiontest.ui.theme.ComposePermissionTestTheme
import com.sryang.library.PermissionsUtil
import com.sryang.library.compose.BastPracticePermission
import com.sryang.library.compose.CurrentLocationScreen
import com.sryang.library.compose.PermissionBox
import com.sryang.library.compose.SimplePermissionDialog
import com.sryang.library.compose.SinglePermissionButton

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposePermissionTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //PermissionBoxTest()
                    SinglePermissionButtonTest()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SinglePermissionButtonTest(){
    Row { Text("Korean");
        Spacer(Modifier.width(4.dp));
        SinglePermissionButton(permission = Manifest.permission.ACCESS_FINE_LOCATION, onGranted = {}, onRationalContents = { Text(modifier = Modifier.clickable{ it.launchMultiplePermissionRequest() }, text = "0m(권한 필요)") }, onFirstOrDenied = { Text(modifier = Modifier.clickable{ it.launchMultiplePermissionRequest() }, text = "0m(권한 필요)")})
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionBoxTest() {
    PermissionBox(
        modifier = Modifier, listOf(Manifest.permission.CAMERA), listOf(Manifest.permission.CAMERA), "", Alignment.TopStart,
        onGranted = { Text("granted") },
        onRationalContents = { Button({ it.launchMultiplePermissionRequest() }) { Text("RationalState") } },
        onFirstOrDenied = { Button({ it.launchMultiplePermissionRequest() }) { Text("첫 번째 또는 모든 권한을 거부 했을 경우.") } }
    )
}

@Composable
fun test() {
    var text by remember { mutableStateOf("") }
    BastPracticePermission(
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
    )
}

@Composable
fun testDialog() {
    var showDialog by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
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

@Composable
fun CurrentLocationScreenTest() {
    var location by remember { mutableStateOf("") }
    Column {
        CurrentLocationScreen {
            location = it.toString()
        }
        Text(location)
    }
}

fun PermissionUtilTest(activity: ComponentActivity) {
    PermissionsUtil.requestPermission(Manifest.permission.CAMERA, activity)
}