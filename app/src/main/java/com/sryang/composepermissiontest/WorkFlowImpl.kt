package com.sryang.composepermissiontest

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.sryang.library.compose.workflow.BestPracticeViewModel
import com.sryang.library.compose.workflow.DescribePermissionDialog
import com.sryang.library.compose.workflow.MoveSystemSettingDialog
import com.sryang.library.compose.workflow.PermissonWorkFlow.CheckAlreadyGranted
import com.sryang.library.compose.workflow.PermissonWorkFlow.DeniedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.GrantedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.Idle
import com.sryang.library.compose.workflow.PermissonWorkFlow.RecognizeToUser
import com.sryang.library.compose.workflow.PermissonWorkFlow.RequestPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.ShowRationale
import com.sryang.library.compose.workflow.PermissonWorkFlow.SuggestSystemSetting
import com.sryang.library.compose.workflow.PermissonWorkFlow.UserDeinedFromRecognize
import com.sryang.library.compose.workflow.RationaleDialog

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkFlowImpl(
    viewModel: BestPracticeViewModel = BestPracticeViewModel(),
    permission : String = Manifest.permission.ACCESS_FINE_LOCATION
) {
    val requestPermission = rememberPermissionState(permission, { viewModel.permissionResult(it) })
    val state = viewModel.state
    var stateTxt by remember { mutableStateOf("RequestPermission") }

    when (state) {
        Idle                    /* 최초 */ -> { viewModel.checkGranted(requestPermission.status.isGranted) }
        RecognizeToUser         /* UX에 권한을 필요로 하는 정보 인지 시키기 */-> { DescribePermissionDialog(onYes = { viewModel.yes() }, onNo = { viewModel.no() }) }
        UserDeinedFromRecognize /* 다이얼로그에서 사용자 거절 */ -> { stateTxt = "권한을 요청을 원하지 않음." }
        CheckAlreadyGranted     /* 권한 요청 전 권한 이미 있는지 확인 */-> { viewModel.alreadyGranted(requestPermission.status) }
        DeniedPermission        /* 권한 거부 */-> { stateTxt = "권한을 거부함." }
        GrantedPermission       /* 사용자가 권한을 허가했다면, 자원 접근 가능 */-> { stateTxt = "권한을 허용함." }
        RequestPermission       /* 런타임 권한 요청하기 */ -> { LaunchedEffect(state == RequestPermission) { requestPermission.launchPermissionRequest() } }
        SuggestSystemSetting    /* 권한 거부 상태에서 요청 시 */ -> { MoveSystemSettingDialog { viewModel.denied() } }
        ShowRationale           /* rationale을 표시 */ -> { RationaleDialog({ viewModel.yesRational() }, {viewModel.no()}) }
    }

    Box {
        MyLocation(hasPermission = requestPermission.status.isGranted, 0, onRequestPermission = {
            viewModel.request(requestPermission.status.shouldShowRationale)
        })
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkFlowImplEmpty(
    viewModel: BestPracticeViewModel = BestPracticeViewModel(),
    permission : String = Manifest.permission.ACCESS_FINE_LOCATION
) {
    val requestPermission = rememberPermissionState(permission, { viewModel.permissionResult(it) })
    val state = viewModel.state
    var stateTxt by remember { mutableStateOf("RequestPermission") }

    when (state) {
        Idle                    /* 최초 */ -> {  }
        RecognizeToUser         /* UX에 권한을 필요로 하는 정보 인지 시키기 */-> {  }
        UserDeinedFromRecognize /* 다이얼로그에서 사용자 거절 */ -> {  }
        CheckAlreadyGranted     /* 권한 요청 전 권한 이미 있는지 확인 */-> {  }
        DeniedPermission        /* 권한 거부 */-> {  }
        GrantedPermission       /* 사용자가 권한을 허가했다면, 자원 접근 가능 */-> {  }
        RequestPermission       /* 런타임 권한 요청하기 */ -> {  }
        SuggestSystemSetting    /* 권한 거부 상태에서 요청 시 */ -> {  }
        ShowRationale           /* rationale을 표시 */ -> {  }
    }
}

@Preview
@Composable
fun MyLocation(hasPermission : Boolean = true, distance : Int = 0, onRequestPermission:()->Unit = {}) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
        Text("한국 음식점")
        Text("∙", modifier = Modifier.padding(horizontal = 4.dp))
        if(!hasPermission) {
            TextButton(onRequestPermission) { Text("내 위치(위치 권한 필요)") }
        }
        else{
            Text("$distance m")
        }
        Text("∙", modifier = Modifier.padding(horizontal = 4.dp))
        Text("★ 4.5")
    }
}