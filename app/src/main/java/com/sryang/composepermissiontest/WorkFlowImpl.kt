package com.sryang.composepermissiontest

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.sryang.library.compose.workflow.BestPracticeViewModel
import com.sryang.library.compose.workflow.DescribePermissionDialog
import com.sryang.library.compose.workflow.MoveSystemSettingDialog
import com.sryang.library.compose.workflow.PermissonWorkFlow.CheckRationale
import com.sryang.library.compose.workflow.PermissonWorkFlow.DeniedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.GrantedPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.InitialPermissionCheck
import com.sryang.library.compose.workflow.PermissonWorkFlow.RecognizeToUser
import com.sryang.library.compose.workflow.PermissonWorkFlow.RequestPermission
import com.sryang.library.compose.workflow.PermissonWorkFlow.ShowRationale
import com.sryang.library.compose.workflow.PermissonWorkFlow.SuggestSystemSetting
import com.sryang.library.compose.workflow.RationaleDialog

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkFlowImpl(
    viewModel: BestPracticeViewModel = BestPracticeViewModel(),
    permission : String = Manifest.permission.ACCESS_FINE_LOCATION
) {
    var timeDiff : Long by remember { mutableStateOf(0L) } // 영구 권한 거부 상태 체크를 위한 시간
    val lifecycleOwner = LocalLifecycleOwner.current
    val requestPermission = rememberPermissionState(permission, { viewModel.permissionResult(it, System.currentTimeMillis() - timeDiff); })
    val state = viewModel.state
    var stateTxt by remember { mutableStateOf("RequestPermission") }

    // 앱이 포그라운드로 복귀했을 때 실행
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.onStart()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    when (state) {
        InitialPermissionCheck  /* 최초 권한 체크 */ -> { viewModel.initialPermissionCheck(requestPermission.status.isGranted) }
        RecognizeToUser         /* UX에 권한을 필요로 하는 정보 인지 시키기 */-> { DescribePermissionDialog(onYes = { viewModel.yesInRecognizeUser() }, onNo = { viewModel.noInRecognizeUser() }) }
        CheckRationale           /* rational 여부 확인 */-> { viewModel.checkRational(requestPermission.status.shouldShowRationale) }
        DeniedPermission        /* 권한 거부 */-> { stateTxt = "권한을 거부함." }
        GrantedPermission       /* 사용자가 권한을 허가했다면, 자원 접근 가능 */-> { stateTxt = "권한을 허용함." }
        RequestPermission       /* 런타임 권한 요청하기 */ -> { LaunchedEffect(state == RequestPermission) { requestPermission.launchPermissionRequest(); timeDiff = System.currentTimeMillis() } }
        SuggestSystemSetting    /* 권한 거부 상태에서 요청 시 */ -> { MoveSystemSettingDialog(onMove = { viewModel.onMoveInSystemDialog() }, onDeny = {viewModel.noInSystemDialog()}) }
        ShowRationale           /* rationale을 표시 */ -> { RationaleDialog({ viewModel.yesRationale() }, {viewModel.noRationale()}) }
    }

    Column {
        Text(state.toString().split("$")[1].split("@")[0])
        MyLocation(hasPermission = requestPermission.status.isGranted, 0, onRequestPermission = {
            viewModel.request()
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
        InitialPermissionCheck  /* 1. 최초 */ -> { viewModel.initialPermissionCheck(requestPermission.status.isGranted) }
        RecognizeToUser         /* 2. UX에 권한을 필요로 하는 정보 인지 시키기 */-> { /* 다이얼로그 버튼 클릭 이벤트에 viewModel.yes()  viewModel.no() 넣기*/ }
        CheckRationale           /* 4. 권한 요청 전 권한 이미 있는지 확인 */-> { viewModel.checkRational(requestPermission.status) }
        DeniedPermission        /* 5. 권한 거부 */-> {  }
        GrantedPermission       /* 6. 사용자가 권한을 허가했다면, 자원 접근 가능 */-> {  }
        RequestPermission       /* 7. 런타임 권한 요청하기 */ -> { LaunchedEffect(state == RequestPermission) { requestPermission.launchPermissionRequest() } }
        SuggestSystemSetting    /* 8. 권한 거부 상태에서 요청 시 */ -> {  }
        ShowRationale           /* 9. rationale을 표시 */ -> {  }
    }

    // 권한 요청시 호출
    // viewModel.request(requestPermission.status.shouldShowRationale)
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