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
fun WorkFlowImpl(
    viewModel: BestPracticeViewModel = BestPracticeViewModel(),
    permission : String = Manifest.permission.ACCESS_FINE_LOCATION
) {
    var timeDiff : Long by remember { mutableStateOf(0L) } // 영구 권한 거부 상태 체크를 위한 시간
    val requestPermission = rememberPermissionState(permission, { viewModel.permissionResult(it, System.currentTimeMillis() - timeDiff); })
    val state = viewModel.state
    var stateTxt by remember { mutableStateOf("RequestPermission") }

    when (state) {
        InitialPermissionCheck  /* 최초 권한 체크 */ -> { viewModel.initialPermissionCheck(requestPermission.status.isGranted) }
        RecognizeToUser         /* UX에 권한을 필요로 하는 정보 인지 시키기 */-> { DescribePermissionDialog(onYes = { viewModel.yesInRecognizeUser() }, onNo = { viewModel.noInRecognizeUser() }) }
        CheckRational           /* rational 여부 확인 */-> { viewModel.checkRational(requestPermission.status.shouldShowRationale) }
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
```

## 1. 최초 권한 체크
```
Idle /* 1. 최초 */ -> { viewModel.checkGranted(requestPermission.status.isGranted) }
```
권한 유무를 체크하여 5.DeniedPermission 또는 6.GrantedPermission 으로 상태를 변경

## 권한 요청하기
```
viewModel.request(requestPermission.status.shouldShowRationale)
```
위와 같이 호출하면 7.RequestPermission 상태로 변경 된다.

## 2. UX에 권한을 필요로 하는 정보 인지 시키기

Alertdialog 정보를 보여주기 권장.

```
RecognizeToUser         /* UX에 권한을 필요로 하는 정보 인지 시키기 */-> { DescribePermissionDialog(onYes = { viewModel.yesInRecognizeUser() }, onNo = { viewModel.noInRecognizeUser() }) }
```
사용자의 허용 여부에 따라 viewModel.yesInRecognizeUser(), viewModel.noInRecognizeUser() 호출하기

## 3. Rational 여부 확인

아래 코드를 그대로 작성
viewmodel에서 showRationale 여부에 따라 프로세스 
```
CheckRational           /* rational 여부 확인 */-> { viewModel.checkRational(requestPermission.status.shouldShowRationale) }
```

## 4. 권한 요청하기

아래 코드 그대로 작성
timeDiff는 권한을 2번 거부하면, 요청 하더라도 바로 거부로 떨어져 시간을 체크해 영구 거부 여부를 확인용도.

```
RequestPermission       /* 런타임 권한 요청하기 */ -> { LaunchedEffect(state == RequestPermission) { requestPermission.launchPermissionRequest(); timeDiff = System.currentTimeMillis() } }
```

## 5. 시스템 권한 화면 이동

아래 코드 참고
화면 이동 여부 사용자 액션에 대해 이벤트를 설정 해줘야 함. viewModel.onMoveInSystemDialog(), viewModel.noInSystemDialog()

```
SuggestSystemSetting    /* 권한 거부 상태에서 요청 시 */ -> { MoveSystemSettingDialog(onMove = { viewModel.onMoveInSystemDialog() }, onDeny = {viewModel.noInSystemDialog()}) }
```