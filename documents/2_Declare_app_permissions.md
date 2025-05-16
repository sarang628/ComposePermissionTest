https://developer.android.com/training/permissions/declaring


## 요약
- manifest에 권한 선언 필요
- 인스톨 퍼미션: 설치 시 자동부여, 런타임, 스페셜 퍼미션 : 앱에서 시스템에 요청 필요
- 하드웨어 필수 여부 옵션 설정 가능, 하드웨어 사용 가능 여부 확인 체크 기능 있음
- 신 구형 단말(api 버전에 따라) 권한 요청 방법을 다르게 하는 옵션 있음(사용 가능 상황 제한적)


앱 권한을 사용하려면 manifest 파일에 선언해야 함.
manifest에 선언함으로써 플레이스토어나 사용자의 이해를 도울 수 있음

인스톨 퍼미션 : 노멀, 시그니처 퍼미션이 있고 앱 설치시 자동 부여
런타임, 스페셜 퍼미션  : 안드로이드 6.0 이상 버전에서는 반드시 요청 해야함. 


## [메니페스트에 추가](https://developer.android.com/training/permissions/declaring#add-to-manifest)

'<uses-permission>' 요소를 manifest안에 선언 하기.

```
<manifest ...>
    <uses-permission android:name="android.permission.CAMERA"/>
    <application ...>
        ...
    </application>
</manifest>
```

## [하드웨어 필수 여부](https://developer.android.com/training/permissions/declaring#hardware-optional)
하드웨어 필수 여부를 옵션으로 선택 할 수 있다. 필수 여부는 가급적 false로 설정하는 것이 좋으며, 반드시 필요하다면 true로 설정.
(플레이 스토어에서 이 옵션을 체크해서 하드웨어가 없는 기기는 앱이 설치 안되게 할 수 있음.)

## [하드웨어 사용가능 여부](https://developer.android.com/training/permissions/declaring#determine-hardware-availability)
hasSystemFeature()를 사용하여 하드웨어 사용 가능 여부를 확인 할 수 있다.
```
// Check whether your app is running on a device that has a front-facing camera.
if (applicationContext.packageManager.hasSystemFeature(
        PackageManager.FEATURE_CAMERA_FRONT)) {
    // Continue with the part of your app's workflow that requires a
    // front-facing camera.
} else {
    // Gracefully degrade your app experience.
}
```

[api레벨 퍼미션 선언](https://developer.android.com/training/permissions/declaring#declare-by-api-level)
런타임 퍼미션은 안드로이드 6.0 api 23(Marshmallow) 이상 부터 사용이 가능 하며 '<uses-permission-sdk-23>' 을 요소로 사용한다.
특정 버전 이상부터 더 이상 권한 요청을 안해도 되는 경우, <uses-permission> 요소에 maxSdkVersion 속성을 추가하면, 구형 버전에서는 권한을 묻고
신규 버전에서는 묻지 않는다. 사용자 편리성 증가.
```
<uses-permission android:name="android.permission.SOME_PERMISSION"
                 android:maxSdkVersion="28" />
```