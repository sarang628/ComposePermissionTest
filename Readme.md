# 권한 요청 연습

권한을 편하게 요청할 수 있는 라이브러리를 만들고 싶었는데, 권한 요청에 대한 절차가 복잡하여 라이브러리를 쉽게 만들수가 없다.
절차 별로 구현하는 가이드를 제공하는 연습부터 해야할 것 같다.

## 권한 학습하기
1. [Permissions on Android](./documents/1_Permissions_on_Android)
2. [Declare app permissions](./documents/2_Declare_app_permissions)
3. [Request runtime permissions](./documents/3_Request_runtime_permissions)


[ButtonPermission](./documents/1_Permissions_on_Android)


권한을 여러개를 한꺼번에 요청하는 방법이 있는데 공식 페이지에서는 예제를 찾기가 어려웠다. 권한을 요청할 때 각 권한마다 (동일한 기능을 사용하기 위할지라도)
왜 필요한지를 설명하고 권한을 요청해야 한다고 이해.

## 현재 권한이 있는지 여부 확인 방법
[CheckPermission.kt](./library/src/main/java/com/sryang/library/CheckPermission.kt)



# 권한 요청, 위치 요청 Util 모듈

rememberMultiplePermissionsState 권한을 요청하고, 허용여부를 알 수 있는 기능 제공


## 1. 권한이 있는지 파악하기

## 2. 최초 요청 전 설명하기