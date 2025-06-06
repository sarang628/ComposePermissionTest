# 권한(Permission) 처리 모듈

권한을 필요로 하는 화면에 추가하기

```
dependencies {
    implementation 'com.github.sarang628:ComposePermissionTest:be2b9647fc'
}
```

권한 처리 절차는 따로 모아서 보고싶어 ViewModel에서 구현 

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

<img src="./screenshots/1.png" height="500px" >

```
InitialPermissionCheck  /* 최초 권한 체크 */ -> { 
    viewModel.initialPermissionCheck(requestPermission.status.isGranted) 
}
```
권한 유무를 체크하여 5.DeniedPermission 또는 6.GrantedPermission 으로 상태를 변경

## 2. UX에 권한을 필요로 하는 정보 인지 시키기

<img src="./screenshots/2.png" height="500px" >

Alertdialog 정보를 보여주기 권장.

```
RecognizeToUser /* UX에 권한을 필요로 하는 정보 인지 시키기 */-> { 
DescribePermissionDialog(onYes = { viewModel.yesInRecognizeUser() }, onNo = { viewModel.noInRecognizeUser() }) 
}
```
사용자의 허용 여부에 따라 viewModel.yesInRecognizeUser(), viewModel.noInRecognizeUser() 호출하기

확인을 누르면 권한을 요청한다.

<img src="./screenshots/3.png" height="500px" >

## 3. Rational 여부 확인

1회 거부를 하면 showRationale을 표시하라고 라이브러리에서 알려준다.<br>
아래와 같이 다이얼로그를 띄운다.

<img src="./screenshots/4.png" height="500px" >

아래 코드를 그대로 작성
viewmodel에서 showRationale 여부에 따라 프로세스 
```
CheckRational           /* rational 여부 확인 */-> { viewModel.checkRational(requestPermission.status.shouldShowRationale) }
ShowRationale           /* rationale을 표시 */ -> { RationaleDialog({ viewModel.yesRationale() }, {viewModel.noRationale()}) }
```

## 4. 권한 요청하기

아래 코드 그대로 작성
timeDiff는 권한을 2번 거부하면, 요청 하더라도 바로 거부로 떨어져 시간을 체크해 영구 거부 여부를 확인 용도.

<img src="./screenshots/3.png" height="500px" >

```
RequestPermission       /* 런타임 권한 요청하기 */ -> { LaunchedEffect(state == RequestPermission) { requestPermission.launchPermissionRequest(); timeDiff = System.currentTimeMillis() } }
```

## 5. 시스템 권한 화면 이동

2회 이상 권한을 거부하면, 다음 권한 요청시 SuggestSystemSetting 상태로 변경 됨.<br>
이 상태에서 아래와 같은 다이얼로그를 띄워준다.

<img src="./screenshots/5.png" height="500px" >

아래 코드 참고
화면 이동 여부 사용자 액션에 대해 이벤트를 설정 해줘야 함. viewModel.onMoveInSystemDialog(), viewModel.noInSystemDialog()

```
SuggestSystemSetting    /* 권한 거부 상태에서 요청 시 */ -> { MoveSystemSettingDialog(onMove = { viewModel.onMoveInSystemDialog() }, onDeny = {viewModel.noInSystemDialog()}) }
```

## 6. (추가) onStart 시 권한 다시 체크하기

시스템에서 권한을 주고 앱으로 돌아왔을 때 권한 체크를 다시해줘야 올바른 상태를 체크 할 수 있었다.<br>
onResume에 체크를 하면 다이얼로그를 닫을때도 체크를 하게되 onStart에서 체크하도록 설정.

```
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
```