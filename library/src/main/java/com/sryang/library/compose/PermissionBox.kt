/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sryang.library.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sryang.library.checkPermissions
import com.sryang.library.grantedPermissions
import com.sryang.library.rejectedPermissionsString

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SinglePermissionBox(modifier: Modifier = Modifier, permission: String, description: String? = null, contentAlignment: Alignment = Alignment.TopStart, onGranted: @Composable BoxScope.() -> Unit, contents : (@Composable (MultiplePermissionsState)->Unit)? = null) {
    PermissionBox(modifier, listOf(permission), listOf(permission), description, contentAlignment, {onGranted()},{}, contents)
}

/**
 * @param modifier
 * @param permissions 요청할 권한
 * @param requiredPermissions 필수 권한
 * @param description (optional) 권한 필요 이유
 * @param contentAlignment 박스에서 컨텐츠 위치
 * @param onGranted 허용한 권한
 * @param onFirstOrDenied 박스 내용
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionBox(modifier: Modifier = Modifier, permissions: List<String>, requiredPermissions: List<String> = permissions, description: String? = null, contentAlignment: Alignment = Alignment.TopStart, onGranted: @Composable BoxScope.(List<String>) -> Unit, onRationalContents : @Composable (MultiplePermissionsState) -> Unit = {}, onFirstOrDenied : (@Composable (MultiplePermissionsState)->Unit)? = null) {
    var errorText /* 에러 메시지 */ by remember { mutableStateOf("") }
    val permissionState /* 권한 상태 */ = rememberMultiplePermissionsState(permissions = permissions, onPermissionsResult = {
        errorText = rejectedPermissionsString(requiredPermissions, it)
    })

    Box(modifier = Modifier.then(modifier), contentAlignment = contentAlignment) {
        if (checkPermissions(permissionState, requiredPermissions)) { // 필수 권한을 허용 했다면
            onGranted(grantedPermissions(permissionState))
        } else if(permissionState.shouldShowRationale){ // 권한을 1회 거부하여 설명이 필요
            onRationalContents(permissionState)
        } else { // 권한을 하나라도 허용하지 않음 or 최초
            if (onFirstOrDenied != null) onFirstOrDenied(permissionState) // 컨텐츠를 등록했다면 표시
        }
    }
}