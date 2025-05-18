권한을 필요로 하는 화면에 추가하기

```
dependencies {
    implementation 'com.github.sarang628:ComposePermissionTest:be2b9647fc'
}
```

'1. 최초' 권한을 체크.<br>
권한이 허용된 상태라면 '6. GrantedPermission 상태'로 변경 됨. 권한 데이터 접근 가능.<br>
그렇지 않으면 '5. DeniedPermission 상태'. 최초 진입 시 , 1회 거부 시, 권한 회수 시에도 이 상태 이므로 UI 구성을 3가지 상태를 하나로 나타내면 됨.

```
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
        Idle                    /* 1. 최초 */ -> { viewModel.checkGranted(requestPermission.status.isGranted) }
        RecognizeToUser         /* 2. UX에 권한을 필요로 하는 정보 인지 시키기 */-> { /* 다이얼로그 버튼 클릭 이벤트에 viewModel.yes()  viewModel.no() 넣기*/ }
        UserDeinedFromRecognize /* 3. 다이얼로그에서 사용자 거절 */ -> {  }
        CheckAlreadyGranted     /* 4. 권한 요청 전 권한 이미 있는지 확인 */-> { viewModel.alreadyGranted(requestPermission.status) }
        DeniedPermission        /* 5. 권한 거부 */-> {  }
        GrantedPermission       /* 6. 사용자가 권한을 허가했다면, 자원 접근 가능 */-> {  }
        RequestPermission       /* 7. 런타임 권한 요청하기 */ -> { LaunchedEffect(state == RequestPermission) { requestPermission.launchPermissionRequest() } }
        SuggestSystemSetting    /* 8. 권한 거부 상태에서 요청 시 */ -> {  }
        ShowRationale           /* 9. rationale을 표시 */ -> {  }
    }

    // 권한 요청시 호출
    // viewModel.request(requestPermission.status.shouldShowRationale)
}
```
