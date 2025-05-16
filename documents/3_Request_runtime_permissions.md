https://developer.android.com/training/permissions/requesting

요약
- 기본 원칙: 필요 시 요청, 권한 없이도 사용 가능, 권한 회수 시 우아한게 퇴보, 시스템 설정(ex 권한 그룹) 가정하지 않기.
- 권한 요청 절차: manifest 선언, UX 권한 필요 인지 시키기, 이미 권한 얻었는지 확인, rationale 표시 여부 확인, 런타임 권한 요청, 권한 허용 또는 거절 처리 
- 위치 권한 요청 : foreground(화면 에서 위치 처리), background(서비스에서 위치 처리)에 따라 권한 선언 방법 다름(안드로이드 10 이상)
- 권한 관련 데이터 처리 시 : 사용자가 인지할 수 있게 UI 처리 권장
- 원타임 퍼미션: 1회성 권한 허용. 
- 시스템 설정에서 권한 회수시 앱 강제 종료 됨. 
- 안드로이드 13 이상 부터 권한 회수 API 사용 가능
- 안드로이드 11 이상 시스템에서 권한 미사용시 자동 회수 기능 있음
- SMS나 Call Log와 같은 특정 사용자 정보는 default handler로 퍼미션 요청 전 사전에 처리하는 절차가 있음.

- 모든 앱은 '제한된 접근 샌드박스' 에서 실행. 샌드박스 밖의 자원을 필요로 한다면, 권한을 선언하고, 권한 요청을 설정할 수 있다.
- note: 특정(민감한) 권한은 요청 할 수 있는 방법이 다를 수 있음.
- dangerous permissions 은 안드로이드 6.0 이상에서 권한 요청 절차가 따로 있음
- dangerous permissions 아니거나, 안드로이드 5.1 버전 이하 에서는 앱 실행 시 권한 자동 승인.

> [Basic principles](#workflow_for_requesting_permissions)
>
> [Explain why your app needs the permission](#workflow_for_requesting_permissions)
>
> [Request permissions](#request-permission)

## [기본 원칙](https://developer.android.com/training/permissions/requesting#principles)

- 앱 사용중 사용자에게 해당 권한을 필요 할 때 요청하기
- 권한이 없다고 사용자에 앱 사용을 막지 않기
- 사용자가 권한 회수 시, 우아하게 퇴보시키기
- 시스템의 행동을 가정하지 않기. 예를들어 권한들이 같은 권한 그룹에 속해 있다고 가정하지 않기.

<a id="workflow_for_requesting_permissions"></a>
## [권한 요청 절차](https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions)

권한 없이 다양한 정보들을 얻는 방법이 있으므로, 요청 해서 얻어야 하는 권한인지 고려하기

1. 메니페스트에 선언해야하는 권한인지 확인
2. UX에 권한을 필요로 하는 정보 인지 시키기
3. 2번을 인지한 사용자의 응답 기다리기
4. 이미 권한을 얻었는지 확인하기
5. 시스템에 물어봐서 rationale을 표시해야 하는지 확인
6. 런타임 권한 요청하기
7. 사용자의 응답 체크하기
8. 사용자가 권한을 허가했다면, 자원 접근 가능

## [이미 권한이 부여된 상태인지 확인하기](https://developer.android.com/training/permissions/requesting#already-granted)

ContextCompat.checkSelfPermission()에서 권한 상태(PERMISSION_GRANTED or PERMISSION_DENIED) 확인 가능.

<a id="workflow_for_requesting_permissions"></a>
## [권한을 필요로 하는지 이유 설명하기](https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions)

시스템 다이얼로그는 어떤(what) 권한이 필요한 지만 나옴. 왜(why) 필요한 지는 앱에서 설명.

```
Note: Starting in Android 12 (API level 31) 이상에서는 카메라나 마이크 사용시 노티알림 표시 됨.
```

ContextCompat.checkSelfPermission() == PERMISSION_DENIED 일 경우 다음 함수 호출
shouldShowRequestPermissionRationale() == true 일 경우 권한이 필요한 이유 설명하기.

<a id="request-permission"></a>
## [권한 요청](https://developer.android.com/training/permissions/requesting#request-permission)


사용자가 권한 필요 설명 다이얼로그 또는 shouldShowRequestPermissionRationale() 로 만든 다이얼로그를 본 후 권한을 요청한다.
RequestPermission contract 사용(androidX 라이브러리 사용 필요.) 편리하고 콜백 로직도 포함시킬 수 있음.

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

[권한 요청 코드 관리](https://developer.android.com/training/permissions/requesting#manage-request-code-yourself)
시스템에서 제공하는 코드 대신에 권한 요청 코드를 직접 정의하여 사용 가능.
사용자가 권한 디어얼로그 확인 후 onRequestPermissionsResult() 결과값 반환

[위치 권한 요청](https://developer.android.com/training/permissions/requesting#location)
위치 권한 요청은 여러 다른 위치와 관련된 권한이 함께 포함됨. 앱이 위치 관련 기능을 사용 상황에 따라 요청 방법이 다름.

[포 그라운드 위치](https://developer.android.com/training/permissions/requesting#foreground)
1회 또는 일정 시간 동안 필요한 기능이라면 포 그라운드 위치 액세스를 요청 하기. 예:
- 네비게이션 앱에서, 각 회전에 대해 알리고자 하는 기능
- 매시제 앱에서, 현재 내 위치를 다른 사용자에게 제공 하는 기능

시스템은 다음 상황으로 앱이 현재 위치를 접근할 때, 'forground location'을 사용하는지 고려한다.
- 액티비티가 화면에 보일 때
- 서비스가 동작중일 때. 시스템은 노티피케이션 아이콘 활성화. 
- 안드로이드 10(api 29) 이상은 foreground service type을 반드시 명시 해야함. 이전 버전에서는 선택사항
```
<!-- Recommended for Android 9 (API level 28) and lower. -->
<!-- Required for Android 10 (API level 29) and higher. -->
<service
    android:name="MyNavigationService"
    android:foregroundServiceType="location" ... >
    <!-- Any inner elements go here. -->
</service>
```

[백그라운드 위치](https://developer.android.com/training/permissions/requesting#background)
지속적인 위치 공유 또는 지오펜스 API를 사용하는 경우.
- 가족 위치 공유 앱
- IOT 앱. 사용자가 귀가 시 자동을 불 켜짐

시스템은 위에 언급한 포그라운드 상황 외에는 모두 'background location' 사용으로 간주한다.
안드로이드 10(api 29) 이상은 manifest에 반드시 아래 권한 명시
```
<manifest ... >
  <!-- Required only when requesting background location access on
       Android 10 (API level 29) and higher. -->
  <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
</manifest>
```

[권한 거부 처리](https://developer.android.com/training/permissions/requesting#handle-denial)
사용자 가이드 하기: 데이터가 표시되는 곳에 메세지 처리. 다른 색상의 버튼, 에러 아이콘 등.
자세히 가이드 하기: 일반적인 메세지 표시가 아닌 자세한 이유 표시
UI 막지 않기: 전체화면을 가리는 등. 사용자가 계속 앱을 사요할 수 있게 만들기.
안드로이드 11(api 30) 이상 부터 동일 권한을 2번 거부 시, 더 이상 요청 불가. 이전 버전에서는 don't ask again. 선택 전까지 계속 표시 가능

[거부 상태 테스트 디버그](https://developer.android.com/training/permissions/requesting#inspect_denial_status_when_testing_and_debugging)
아래 명령어 실행
```
adb shell dumpsys package PACKAGE_NAME
```

결과
```
...
runtime permissions:
  android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
  android.permission.ACCESS_FINE_LOCATION: granted=false, flags=[ USER_SET|USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
  android.permission.BLUETOOTH_CONNECT: granted=false, flags=[ USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
...
```
USER_SET : 1회 거부 시
USER_FIXED : 2회 거부 시(permanent deny)

아래 명령어로 리셋
```
adb shell pm clear-permission-flags PACKAGE_NAME PERMISSION_NAME user-set user-fixed
```


[원타임 퍼미션](https://developer.android.com/training/permissions/requesting#one-time)
안드로이드 11(api 30) 이상에서 1회만 권한을 허용하는 옵션이 있음. 'Only this time' 허용 시
- 화면이 보이는 동안 
- 백그라운드로 이동 시 짧은 시간동안 유지
- 포그라운드 서비스를 실행 후 백그라운드 이동 시 포그라운드 서비스 종료시까지 유지

[권한 회수 시 프로세스 종료](https://developer.android.com/training/permissions/requesting#app_process_terminates_when_permission_revoked)
시스템 세팅에서 권한 제거 시 바로 권한 데이터 접근이 불가. 앱이 종료됨.(강제 종료)

[미사용 권한 리셋](https://developer.android.com/training/permissions/requesting#reset-unused-permissions)
API 호출로 제거 하거나, 시스템에서 자동으로 회수 2가지 있음.

[앱에서 제거](https://developer.android.com/training/permissions/requesting#remove-access)
안드로이드 13(api 33) 이상, 앱에서 권한 제거 가  revokeSelfPermissionOnKill(), revokeSelfPermissionsOnKill()
퍼미션 그룹으로 제거 가능하지만 안보일 경우 권한을 개별로 다 제거해 줘야함.
권한 제거시 앱은 반드시 종료됨. 작업중인 백그라운드는 시스템이 기다려줌.
권한 회수 후 사용자가 앱에 재접속 시 더이상 권한 요청을 하지 않는다는 다이얼로그를 표시 권장.

[자동 권한 리셋](https://developer.android.com/training/permissions/requesting#auto-reset-permissions-unused-apps)
안드로이드 11 (api 30) 이상, 몇 개월간 권한을 사용하지 않으면 회수.

[default handler로 요청](https://developer.android.com/guide/topics/permissions/default-handlers#request-default-handler)
SMS나 Call log와 같은 특정 사용자 정보는 권한 요청 전 default handler로 사전에 작성하는 절차가 있음.

[런타임 퍼미션 테스트 부여](https://developer.android.com/guide/topics/permissions/default-handlers#test-grant-all)
```
adb shell install -g PATH_TO_APK_FILE
```