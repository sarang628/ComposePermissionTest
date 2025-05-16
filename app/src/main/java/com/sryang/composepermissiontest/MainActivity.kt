package com.sryang.composepermissiontest

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.sryang.composepermissiontest.ui.theme.ComposePermissionTestTheme
import com.sryang.library.PermissionsUtil
import com.sryang.library.compose.workflow.BestPracticeViewModel

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
                    WorkFlowImplTest()
                }
            }
        }
    }
}

@Composable
fun WorkFlowImplTest(){
    WorkFlowImpl(BestPracticeViewModel())
}


fun PermissionUtilTest(activity: ComponentActivity) {
    PermissionsUtil.requestPermission(Manifest.permission.CAMERA, activity)
}