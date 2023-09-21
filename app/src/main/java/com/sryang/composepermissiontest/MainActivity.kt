package com.sryang.composepermissiontest

import android.Manifest
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
import com.sryang.library.compose.BastPracticePermission
import com.sryang.library.compose.ComposeRequestPermission
import com.sryang.library.compose.RequestLocationPermissionsSample

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //PermissionsUtil.requestPermission(Manifest.permission.CAMERA, this)

        setContent {
            ComposePermissionTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        //ComposeRequestPermission(Manifest.permission.CAMERA)
                        /*RequestLocationPermissionsSample(
                            listOf(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                            )
                        )*/
                        BastPracticePermission()
                    }

                }
            }
        }
    }
}

@Composable
fun PermissionTest() {

}