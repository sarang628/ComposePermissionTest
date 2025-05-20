package com.sryang.library.compose.workflow

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.sryang.library.compose.workflow.PermissonWorkFlow.CheckAlreadyGranted
import com.sryang.library.compose.workflow.PermissonWorkFlow.DeniedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.GrantedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.Idle
import com.sryang.library.compose.workflow.PermissonWorkFlow.RecognizeToUser
import com.sryang.library.compose.workflow.PermissonWorkFlow.RequestPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.ShowRationale
import com.sryang.library.compose.workflow.PermissonWorkFlow.SuggestSystemSetting
import com.sryang.library.compose.workflow.PermissonWorkFlow.UserDeinedFromRecognize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BestPracticeViewModel : ViewModel() {
    var state: PermissonWorkFlow by mutableStateOf(Idle)
        private set

    fun yes() {
        state = CheckAlreadyGranted
    }

    fun yesRational() {
        state = RequestPermission
    }

    fun no() {
        state = UserDeinedFromRecognize
    }

    fun denied() {
        state = DeniedPermission
    }

    fun permissionResult(bool: Boolean, timeDiff: Long = 1000L) {
        state = if (bool) GrantedPermission else {
            if (timeDiff > 1000) DeniedPermission else SuggestSystemSetting
        }
    }

    fun checkGranted(isGranted: Boolean) {
        viewModelScope.launch {
            delay(1)
            state = if (isGranted) GrantedPermission else DeniedPermission
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun checkGranted(permissionState: MultiplePermissionsState) {
        if (permissionState.allPermissionsGranted) {
            viewModelScope.launch {
                delay(1)
                state = GrantedPermission
            }
        }
    }

    /**
     * 권한 요청 전 권한이 이미 있는지 확인
     */
    fun alreadyGranted(isGranted: Boolean) {
        if (!isGranted)
            state = ShowRationale // 권한이 없다면 Rationale 표시 여부 확인
        else {
            state = GrantedPermission
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun alreadyGranted(permissionStatus: PermissionStatus) {
        if (!permissionStatus.isGranted)
            if (permissionStatus.shouldShowRationale)
                state = ShowRationale // 권한이 없다면 Rationale 표시 여부 확인
            else
                state = RequestPermission
        else {
            state = GrantedPermission
        }
    }


    fun request(showRationale: Boolean) {
        if (state != GrantedPermission) {
            state = RecognizeToUser
        }

    }

    fun onMoveInSystemDialog() {
        state = Idle
    }

    fun onNoInSystemDialog() {
        state = Idle
    }


}