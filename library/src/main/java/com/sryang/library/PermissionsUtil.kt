package com.sryang.library

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

class PermissionsUtil {

    companion object {

        /**
         * permission -
         */
        fun requestPermission(permission: String, activity: ComponentActivity) {
            // 사용자 응답에 따른 콜백 동작 작성하기.
            // 시스템 다이얼로그 리턴값 저장하기.
            //  val 또는 lateinit var로 저장하기
            val requestPermissionLauncher =
                activity.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    testResponse(isGranted, activity)
                }


            when {
                ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 권한 요청하기
                    requestPermissionLauncher.launch(
                        permission
                    )
                }

                activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    // 교육적 UI, 사용자에게 권한이 왜 필요한지 설명하기. 거부시 못 사용하는 기능 설명하기
                    // cancel, no thanks 버튼 포함하여 권한없이 작업 흐름 포함하기
                    //showInContextUI(...)
                    AlertDialog.Builder(activity)
                        .setMessage("shouldShowRequestPermissionRationale")
                        .show()

                }

                else -> {
                    // 바로 권한을 요청 할 수 있음
                    // ActivityResultCallback 등록하여 결과값 처리하기
                    requestPermissionLauncher.launch(
                        permission
                    )
                }
            }

        }

        @Composable
        fun RequestPermissionInCompose(permission: String) {
            val context = LocalContext.current
            val launch = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted: Boolean ->
                    testResponse(isGranted, context)
                })

            Button(onClick = {
                launch.launch(permission)
            }) {

            }
        }

        private fun testResponse(isGranted: Boolean, context: Context) {
            if (isGranted) {
                // 권한허가 시. 앱에서 작업 흐름 이어나가기
                AlertDialog.Builder(context)
                    .setMessage("권한을 허용했습니다.")
                    .show()
            } else {
                // 사용자에게 권한 거부로 못 사용하는 기능 설명하기  사용자의 결정 존중하기.
                // 링크를 사용해 시스템 설정으로 이동시키지 않기.
                AlertDialog.Builder(context)
                    .setMessage("권한을 거부했습니다.")
                    .show()
            }
        }
    }
}