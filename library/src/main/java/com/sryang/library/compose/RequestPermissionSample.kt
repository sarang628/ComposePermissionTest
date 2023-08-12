package com.sryang.library.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class RequestPermissionSample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //Sample()
        }
    }
}