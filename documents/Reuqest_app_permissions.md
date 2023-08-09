https://developer.android.com/training/permissions/requesting

[Basic principles](#Basic principles)
[Explain why your app needs the permission](#Explain why your app needs the permission)

안드로이드는 제한된 샌드박스 안에서 실행
밖에 있는 자원을 필요로한다면 런타임 권한 수행 할 수 있음
이장은 작업 흐름에 대한 설명

민감한 정보는 특별권한을 통해 권한 요청 다른 방법 학습 필요

위험한 권한을 사용 시 API 23 이상에서는 이 장의 가이드를 따름
위험한 권한을 사용 시 API 22 이하에서는 권한이 자동으로 부여 됨


<a id="Basic principles"></a>
## [Basic principles](https://developer.android.com/training/permissions/requesting#principles)

앱 사용중 사용자가 그 권한이 필요 할 때 요청하기
권한이 없다고 사용자에 앱사용을 막지 않기
사용자가 권한 회수 시, 우아하게 퇴보시키기
같은 시스템 다이얼로그가 나왔다고 권한을 다 가졌다고 가정하지 않기

## [Workflow for requesting permissions](https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions)

권한요청 전 필요한 권한인지 확인하기

1. 메니페스트 파일에 선언해야하는 권한인지 확인
2. UX에 해당 작업이 권한이 필요하다는 것을 인지 시키기
3. 사용자가 해당 자원이 필요할 때 권한 요청하기
4. 이미 권한을 얻었는지 확인하기
5. 해당 권한이 필요한 근거 명시하기
6. 런타임 권한 요청하기
7. 사용자의 응답 체크하기
8. 사용자가 권한을 허가했다면, 자원 접근 가능

## Determine whether your app was already granted the permission

To check whether the user already granted your app a particular permission, pass that permission
into the ContextCompat.checkSelfPermission() method. This method returns either PERMISSION_GRANTED
or PERMISSION_DENIED, depending on whether your app has the permission.

<a id="Explain why your app needs the permission"></a>
## [Explain why your app needs the permission](https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions)

requestPermissions() 호출 시 시스템 다이얼로그 발생
사용자 편의를 위해 requestPermissions() 전 사용이유 설명하기

```
Note: Starting in Android 12 (API level 31) 이상에서는 카메라나 마이크 사용시 노티알림 표시 됨.
```

ContextCompat.checkSelfPermission() == PERMISSION_DENIED 일 경우 다음 함수 호출
shouldShowRequestPermissionRationale() == true 일 경우 권한이 필요한 이유 설명하기.

[권한 요청하기](https://developer.android.com/training/permissions/requesting#request-permission)

shouldShowRequestPermissionRationale() 권한에 필요성 전달하기

RequestPermission contract 사용(androidX 라이브러리 사용 필요.)
편리하고 콜백 로직도 포함시킬 수 있음.

아래 라이브러리를 포함시켜야 사용 할 수 있음.

```
androidx.activity, version 1.2.0 or later
androidx.fragment, version 1.3.0 or later
```

하나의 권한의 경우 RequestPermission 사용하기
여러개 권한의 경우 RequestMultiplePermissions 사용하기

액티비티 프레그먼트 안에서 사용하기
registerForActivityResult() 사용하기 사용자 콜백 응답받기
ActivityResultLauncher 사용하기. launch() 호출 시 이전 스텝 저장
launch() 호출 후 권한 다이얼로그 발생.
다이얼로그를 커스텀 할 수 없음.

[예제코드](../app/src/main/java/com/sryang/composepermissiontest/RequestPermissions.kt)

```
val requestPermissionLauncher =
    registerForActivityResult(RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }
    }
```

[Manage the permission request code yourself](https://developer.android.com/training/permissions/requesting#manage-request-code-yourself)
원하는 권한만 요청하기

```
when {
    ContextCompat.checkSelfPermission(
            CONTEXT,
            Manifest.permission.REQUESTED_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
        // You can use the API that requires the permission.
        performAction(...)
    }
    shouldShowRequestPermissionRationale(...) -> {
        // In an educational UI, explain to the user why your app requires this
        // permission for a specific feature to behave as expected, and what
        // features are disabled if it's declined. In this UI, include a
        // "cancel" or "no thanks" button that lets the user continue
        // using your app without granting the permission.
        showInContextUI(...)
    }
    else -> {
        // You can directly ask for the permission.
        requestPermissions(CONTEXT,
                arrayOf(Manifest.permission.REQUESTED_PERMISSION),
                REQUEST_CODE)
    }
}
```

onRequestPermissionsResult() 결과값 반환

```
override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
        PERMISSION_REQUEST_CODE -> {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission is granted. Continue the action or workflow
                // in your app.
            } else {
                // Explain to the user that the feature is unavailable because
                // the feature requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }
            return
        }

        // Add other 'when' lines to check for other
        // permissions this app might request.
        else -> {
            // Ignore all other requests.
        }
    }
}
```