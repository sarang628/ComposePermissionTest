package com.sryang.library


import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * @param activity Rationale 여부를 알기위해 필요
 * @param permission 요청 권한
 * @param onGranted 권한 허용
 * @param onRationale rationale 알림 필요
 * @param onRequest 권한 없음 요청 필요
 */
fun checkPermission(
    activity: Activity,
    permission: String,
    onGranted: () -> Unit = {},
    onRationale: () -> Unit = {},
    onRequest: () -> Unit = {}
) {
    when {
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED -> {
            // You can use the API that requires the permission.
            onGranted()
        }

        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
            // 권한을 최초 거부했을 경우 다음 요청 시
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            onRationale()
        }

        else -> {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            onRequest()
        }
    }
}

/**
 * @param permission 요청 권한
 * @param onGranted 권한 허용
 * @param onRationale rationale 알림 필요
 * @param onRequest 권한 없음 요청 필요
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermission(
    permissionState: PermissionState,
    onGranted: () -> Unit = {},
    onRationale: () -> Unit = {},
    onRequest: () -> Unit = {}
) {
    LaunchedEffect(key1 = permissionState) {
        if (permissionState.status.isGranted) onGranted()
        else if (permissionState.status.shouldShowRationale) onRationale()
        else onRequest.invoke()

    }
}

/**
 * @param permissions
 * @param onGranted
 * @param onRationale
 * @param onRequest 권한이 없는 요청 리스트 반환
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckMultiplePermission(permissions: List<String> = listOf(), onGranted: @Composable () -> Unit = {}, onRationale: () -> Unit = {}, onRequest: @Composable (List<String>) -> Unit = {}) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)
    if (multiplePermissionsState.allPermissionsGranted) {
        onGranted()
    } else if (multiplePermissionsState.shouldShowRationale) {
        onRationale()
    } else {
        onRequest(
        multiplePermissionsState.revokedPermissions.filter { it.status != PermissionStatus.Granted }.map { it.permission }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun checkPermissions(permissionState : MultiplePermissionsState, requiredPermissions : List<String>) : Boolean{
    val allRequiredPermissionsGranted /* 필수 권한들을 모두 허용 했는지 여부 */ = permissionState.revokedPermissions.none { it.permission in requiredPermissions }
    return allRequiredPermissionsGranted
}