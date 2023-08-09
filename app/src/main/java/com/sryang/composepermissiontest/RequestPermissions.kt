package com.sryang.composepermissiontest

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

fun requestPermissions(activity: ComponentActivity) {
    // 사용자 응답에 따른 콜백 동작 작성하기.
    // 시스템 다이얼로그 리턴값 저장하기.
    //  val 또는 lateinit var로 저장하기
    val requestPermissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // 권한허가 시. 앱에서 작업 흐름 이어나가기
            } else {
                // 사용자에게 권한 거부로 못 사용하는 기능 설명하기  사용자의 결정 존중하기.
                // 링크를 사용해 시스템 설정으로 이동시키지 않기.
            }
        }


    when {
        ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED -> {
            // 권한 요청하기
        }

        activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
            // 교육적 UI, 사용자에게 권한이 왜 필요한지 설명하기. 거부시 못 사용하는 기능 설명하기
            // cancel, no thanks 버튼 포함하여 권한없이 작업 흐름 포함하기
            //showInContextUI(...)
            AlertDialog.Builder(activity)
                .setMessage("test")
                .show()

        }

        else -> {
            // 바로 권한을 요청 할 수 있음
            // ActivityResultCallback 등록하여 결과값 처리하기
            requestPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

}