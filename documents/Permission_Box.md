# 안드로이드 권한 샘플 코드 PermissionBox 분석

안드로이드에서 권한 [샘플 코드](https://github.com/android/platform-samples/blob/main/shared/src/main/java/com/example/platform/shared/PermissionBox.kt)를 제공한다.

## 하나의 권한 요청

단순하게 하나의 권한만 요청하는 기능과 여러 권한을 요청하는 기능을 제공한다.

여러 권한을 요청할 때는 요청 권한 리스트와, 필수 권한 리스트를 따로 받아 처리한다. 👍

파라미터를 여러가 받지만 중요한 건 permission 파라미터. 이것만 요청해도 동작한다.
```
/**
 * The PermissionBox uses a [Box] to show a simple permission request UI when the provided [permission]
 * is revoked or the provided [onGranted] content if the permission is granted.
 *
 * This composable follows the permission request flow but for a complete example check the samples
 * under privacy/permissions
 *
 * 하나의 권한만 요청
 * @param permission 요청 권한
 */
@Composable
fun PermissionBox(modifier: Modifier = Modifier, permission: String, description: String? = null, contentAlignment: Alignment = Alignment.TopStart, onGranted: @Composable BoxScope.() -> Unit, ) {
    PermissionBox(modifier, permissions = listOf(permission), requiredPermissions = listOf(permission), description, contentAlignment,) { onGranted() }
}
```

실제 권한 요청에 대한 처리는 '여러 권한'을 받는 함수에서 처리. 위에 '하나의 권한' 요청은 요청 권한과 필수 권한을 동일하게 해서 '여러 권한' 처리 하는 함수를 다시 호출한다.

## 여러 권한과 필수 권한 요청

rememberMultiplePermissionsState 이 객체가 핵심 정보를 모두 담고있다.

구글에서 accompanist.permissions 라는 compose용 권한을 편리하게 처리 할 수 있는 라이브러리를 제공한다.

https://google.github.io/accompanist/permissions/

compose 개발을 돕는 라이브러리로, androidX에 포함 될 수 없는 아직 실험 상태인 라이브러리 이다.

다른 코드로 작성한 권한 [샘플 코드](https://github.com/android/platform-samples/blob/main/samples/privacy/permissions/src/main/java/com/example/platform/privacy/permissions/ComposePermissions.kt)도 있어 나중에 분석해 봐야겠다.

아래와 같이 권한을 체크하는데 필요한 유용한 정보를 모아서 제공해준다.
```
@ExperimentalPermissionsApi
@Stable
interface MultiplePermissionsState {
    val permissions: List<PermissionState>
    val revokedPermissions: List<PermissionState>
    val allPermissionsGranted: Boolean
    val shouldShowRationale: Boolean
    fun launchMultiplePermissionRequest(): Unit
}
```

### 거부한 권한 정보

권한 허용/거부에 대한 결과를 아래 함수에서 콜백 받도록 제공해준다.<br>
아래 한 줄로 거부 권한에 대한 정보를 필터 한다.<br>
Map에서 값에 대한 타입이 Boolean 일 경우 filterValues를 사용해서 true/false를 필터 할 수 있다. 👍<br>
샘플 코드에서 아래 부분은 권한을 거부 했을 때 에러 메세지를 표시하는 용도로 사용한다.
```
onPermissionsResult = { permissionResultMap ->
            val rejectedPermissions = permissionResultMap.filterValues { !it }.keys
        }
```

### 필수 권한 허용 여부
회수된 권한(권한 허용을 안한 권한들?) 중에 필수 권한이 있는지 체크하는 코드이다. 매우 간결하다<br>
none라는 함수를 사용한다. 두개의 리스트를 비교해 같은 값이 없어야 하는 경우 유용하게 사용할 수 있는 것 같다.
```
val allRequiredPermissionsGranted = permissionState.revokedPermissions.none { it.permission in requiredPermissions }
```

## 로직

필수 권한을 허용했다면 -> 종료

필수 권한을 설정하지 않았다면 -> '권한 허용이 안된 경우' 로직 이동


## 권한 허용이 안된 경우

필수 권한에 대한 정보를 알리고 요청 버튼이 나온다.

버튼 클릭 시 해야할 작업이 있다.

shouldShowRationale 여부 확인

권한을 1회 거부 시 shouldShowRationale 상태가 true가 된다.
한번 더 거부 시 영구 거부상태가 되므로, 권한을 다시 요청할 때 이 값이 true라면 다이얼로그로 사용자에게 권한이 필요한 이유에 대해
좀 더 상세하게 설명해줘야 한다.

최초 요청 시 showRationale = false 상태 -> 바로 시스템 권한 팝업<br>
1회 거부 시 showRationale = true 상태 -> 다이얼로그 띄움 -> 사용자 확인 -> 시스템 권한 팝업<br>
2회 거부 시 showRationale = false -> 바로 시스템 권한 팝업 but 팝업이 뜨지도 않고 거부 처리 됨.<br>

```
if (state.shouldShowRationale) { showRationale = true }
```

## 요약

- 필수 권한을 모두 허용 했다면, 화면 진행.
- 필수 권한 미 허용 시, 필수 권한 표시와 요청 버튼 표시
- 권한 요청 시 showRationale = true 라면 다이얼로그 보여주기