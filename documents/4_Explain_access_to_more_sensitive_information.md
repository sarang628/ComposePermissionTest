# 민감 정보 접근에 대한 설명하기
위치, 마이크, 카메라와 같은 권한에 대한 권한 필요 시, 어떤 앱이 권한을 사용하는지 사용자의 이해를 돕는 방법
권한 구현을 모범 사례를 잘 따른다면, 이 개인정보 보호 시스템은 앱에 영향을 주지 않음.
- 카메라 권한 얻기 전까지 카메라 접근 대기하기
- 마이크 권한 얻기 전까지 마이크 접근 대기하기
- 위치 권한을 얻기 전까지 사용자와의 상호작용을 대기하기
- ACCESS_BACKGROUND_LOCATION 권한 요청 전 ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION 우선 얻기

# [사생활 대시보드](https://developer.android.com/training/permissions/explaining-access#privacy-dashboard)

안드로이드 12 이상부터 사생활 대시보드을 시스템 설정에서 볼 수 있음. 타임라인드로 카메라, 마이크, 위치 정보를 요청한 시간을 알 수 있음.

[데이터 접근에 대한 이유 표시](https://developer.android.com/training/permissions/explaining-access#privacy-dashboard-show-rationale)
시스템에서 사생활 데이터 접근에 대한 이유를 사용자에게 보여주는 기능 제공. 사생활 대시보드 또는 앱 권한 페이지에서 정보 제공이 가능.

(권한에 접근을 설명하는 액티비티를 만들라는 얘기?)

1. manifest의 activity 요소 안에 android:permission에 START_VIEW_PERMISSION_USAGE 설정
* 안드로이드 12 이상에서는 exported 요소 필수
```
android:permission="android.permission.START_VIEW_PERMISSION_USAGE"
```
2. intent filter 추가
```
<intent-filter>
     <!-- VIEW_PERMISSION_USAGE 시스템 설정안 앱 권한 페이지에서 선택 가능한 아이콘 표시
          VIEW_PERMISSION_USAGE_FOR_PERIOD 사생활 대시보드에서 선택 가능한 아이콘 표시 -->
       <action android:name="android.intent.action.VIEW_PERMISSION_USAGE" />
       <action android:name="android.intent.action.VIEW_PERMISSION_USAGE_FOR_PERIOD" />
       <category android:name="android.intent.category.DEFAULT" />
       ...
    </intent-filter>
```

3. rational 액티비티 호출 시
데이터 접근 이유 설명을 위해, 핼프 센터 또는 웹사이트와 같은 페이지를 보여 줄 수 있다.
ACTION_VIEW_PERMISSION_USAGE 로 호출 될 경우, EXTRA_PERMISSION_GROUP_NAME가 extra로 포함됨.
ACTION_VIEW_PERMISSION_USAGE_FOR_PERIOD 로 호출 될 경우, EXTRA_PERMISSION_GROUP_NAME, EXTRA_ATTRIBUTION_TAGS, EXTRA_START_TIME, EXTRA_END_TIME 정보가 extra로 포함 됨.

등록한 인텐트 필터에 따라, 사용자는 사생활 대시보드(VIEW_PERMISSION_USAGE_FOR_PERIOD)나 권한 페이지(VIEW_PERMISSION_USAGE)에서 정보를 볼 수 있다.

## [인디케이터](https://developer.android.com/training/permissions/explaining-access#indicators)
참고: 사생활 모범 사례를 보고 구현했다면 따로 코드를 변경할 필요 없음.
안드로이드 12 이상에서는, 카메라, 마이크 접근 시 상태바에 아이콘 표시 됨.
시스템 바를 숨기는 immersive 모드가 실행되더라도 우측 상단에 아이콘이 표시됨.
이는 사용자가 쉽게 권한에 대해 원하는 대처를 할 수 있음.

### [Identify the screen location of indicators](https://developer.android.com/training/permissions/explaining-access#identify-screen-location-indicators)
전체 화면 모드에서 인디케이터가 앱을 가리는 순간에 인디케이터 위치를 파악해서 대처 하고 싶을 때,
아래 코드로 인디케이터 위치를 파악 할 수 있다.

```
view.setOnApplyWindowInsetsListener { view, windowInsets ->
    val indicatorBounds = windowInsets.getPrivacyIndicatorBounds()
    // change your UI to avoid overlapping
    windowInsets
}
```

## [토글](https://developer.android.com/training/permissions/explaining-access#toggles)
참고: 사생활 모범 사례를 보고 구현했다면 따로 코드를 변경할 필요 없음.

안드로이드 12 이상, 사용자가 카메라나 마이크를 액세스를 (퀵세팅) 토글 버튼으로 제어 가능.

토글을 off 하면 카메라는 검정 화면 출력, 마이크는 빈 소리만 출력한다.(접근을 막거나 하는 건 아님.)

긴급 상황에는 접근이 활성화 될 수 있음. (ex 911)

토글이 꺼진 상태로 권한을 필요로 하는 앱 실행 시, 시스템은 사용자에게 토글 off 상태를 remind 시킨다.

### [토글 상태 체크](https://developer.android.com/training/permissions/explaining-access#toggles-check-device-support)
앱 에서는 아래 코드로 토글 상태를 알 수 있다.
```
val sensorPrivacyManager = applicationContext
        .getSystemService(SensorPrivacyManager::class.java)
        as SensorPrivacyManager
val supportsMicrophoneToggle = sensorPrivacyManager
        .supportsSensorToggle(Sensors.MICROPHONE)
val supportsCameraToggle = sensorPrivacyManager
        .supportsSensorToggle(Sensors.CAMERA)
```