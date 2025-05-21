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
import com.sryang.library.compose.workflow.PermissonWorkFlow.CheckRational
import com.sryang.library.compose.workflow.PermissonWorkFlow.DeniedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.GrantedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.InitialPermissionCheck
import com.sryang.library.compose.workflow.PermissonWorkFlow.RecognizeToUser
import com.sryang.library.compose.workflow.PermissonWorkFlow.RequestPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.ShowRationale
import com.sryang.library.compose.workflow.PermissonWorkFlow.SuggestSystemSetting
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 위치 권한 처리 순서를 구현 ViewModel
 */
class BestPracticeViewModel : ViewModel() {
    var state: PermissonWorkFlow by mutableStateOf(InitialPermissionCheck)
        private set

    /**
     * 최초 권한 체크
     * @param isGranted 권한 허용 여부
     * @return GrantedPermission(허용 시) or DeniedPermission(그 외)
     */
    fun initialPermissionCheck(isGranted: Boolean) {
        viewModelScope.launch {
            delay(1)
            state = if (isGranted) GrantedPermission else DeniedPermission
        }
    }

    /**
     * 최초 다중 권한 체크
     */
    @OptIn(ExperimentalPermissionsApi::class)
    fun initialPermissionCheck(permissionState: MultiplePermissionsState) {
        if (permissionState.allPermissionsGranted) {
            viewModelScope.launch {
                delay(1)
                state = GrantedPermission
            }
        }
        else{
            state = DeniedPermission
        }
    }

    /** 사용자 알림 화면에서 Yes */
    fun yesInRecognizeUser() { state = CheckRational }

    /** 사용자 알림 화면에서 No */
    fun noInRecognizeUser() { state = InitialPermissionCheck }

    fun yesRationale() { state = RequestPermission }
    fun noRationale() { state = InitialPermissionCheck }
    fun noInSystemDialog() { state = InitialPermissionCheck }

    /**
     * 권한 체크 결과
     * @param isGranted 권한 허용 여부
     * @param timeDiff 권한 허용하는데 걸린 시간
     *
     * 권한을 거부했고 거부하는데 1초가 걸리지 않았다면, 영구 권한 거부로 판단 시스템 이동 창 띄우기
     */
    fun permissionResult(isGranted: Boolean, timeDiff: Long = 2000L) {
        state = if (isGranted) GrantedPermission else {
            if (timeDiff > 1000) DeniedPermission else SuggestSystemSetting
        }
    }


    /**
     * 권한 요청 전 권한이 이미 있는지 확인
     * @param isShowRationale showRatinale을 보여줘야 하는지 유무
     *
     * ration 체크는 권한 요청 시점에 들어오는 이벤트로
     * showRationale 가 flase 라면 바로 권한 요청
     *
     */
    fun checkRational(isShowRationale: Boolean) {
        if (isShowRationale) {
            state = ShowRationale
        }
        else {
            state = RequestPermission
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun checkRational(permissionStatus: PermissionStatus) {
        checkRational(permissionStatus.shouldShowRationale)
    }

    /**
     * 권한 요청시 권한 허용 상태가 아니라면 사용자 알림 상태로 변경
     */
    fun request() {
        if (state != GrantedPermission) {
            state = RecognizeToUser
        }

    }

    fun onMoveInSystemDialog() {
        state = InitialPermissionCheck
    }

    fun onNoInSystemDialog() {
        state = InitialPermissionCheck
    }


}