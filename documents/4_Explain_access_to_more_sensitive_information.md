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
사생활 데이터 접근에 대한 이유를 작성해야함. 사생활 대시보드나, 시스템 설정 앱 권한 페이지에서 정보를 표시해줄 수 있음. 

1. manifest의 activity 요소 안에 android:permission에 어떤 '데이터 접근 동작'을 사용하는지 명시.
안드로이드 12 이상에서는 exported 요소 필수
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
