https://developer.android.com/training/permissions/requesting

안드로이드는 제한된 샌드박스 안에서 실행
밖에 있는 자원을 필요로한다면 런타임 권한 수행 할 수 있음
이장은 작업 흐름에 대한 설명

민감한 정보는 특별권한을 통해 권한 요청 다른 방법 학습 필요

위험한 권한을 사용 시 API 23 이상에서는 이 장의 가이드를 따름
위험한 권한을 사용 시 API 22 이하에서는 권한이 자동으로 부여 됨

기본 이론
앱 사용중 사용자가 그 권한이 필요 할 때 요청하기
권한이 없다고 사용자에 앱사용을 막지 않기
사용자가 권한 회수 시, 우아하게 퇴보시키기
같은 시스템 다이얼로그가 나왔다고 권한을 다 가졌다고 가정하지 않기

권한요청을 위한 작업 흐름
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
To check whether the user already granted your app a particular permission, pass that permission into the ContextCompat.checkSelfPermission() method. This method returns either PERMISSION_GRANTED or PERMISSION_DENIED, depending on whether your app has the permission.

## Explain why your app needs the permission
The permissions dialog shown by the system when you call requestPermissions() says what permission your app wants, but doesn't say why. In some cases, the user might find that puzzling. It's a good idea to explain to the user why your app wants the permissions before you call requestPermissions().

Research shows that users are much more comfortable with permissions requests if they know why the app needs them, such as whether the permission is needed to support a core feature of the app or for advertising. As a result, if you're only using a fraction of the API calls that fall under a permission group, it helps to explicitly list which of those permissions you're using and why. For example, if you're only using coarse location, let the user know this in your app description or in help articles about your app.

Under certain conditions, it's also helpful to let users know about sensitive data access in real time. For example, if you’re accessing the camera or microphone, it’s a good idea to let the user know by using a notification icon somewhere in your app, or in the notification tray (if the application is running in the background), so it doesn't seem like you're collecting data surreptitiously.
```
Note: Starting in Android 12 (API level 31), privacy indicators notify the user whenever applications access the microphone or camera.
```

Ultimately, if you need to request a permission to make something in your app work, but the reason isn't clear to the user, find a way to let the user know why you need the most sensitive permissions.

If the ContextCompat.checkSelfPermission() method returns PERMISSION_DENIED, call shouldShowRequestPermissionRationale(). If this method returns true, show an educational UI to the user. In this UI, describe why the feature that the user wants to enable needs a particular permission.

Additionally, if your app requests a permission related to location, microphone, or camera, consider explaining why your app needs access to this information.



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