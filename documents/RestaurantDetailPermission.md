# 권한 요쳥 예제1

권한을 요청하는 방법
- 사용자가 음식점 상세화면으로 진입
- 사용자와 음식점과의 거리를 측정하기위해 위치 권한이 필요
- 권한이 없는경우 사용자와의 위치를 보여주는 곳에 권한 요청 버튼을 표시
- 버튼 클릭 시 '사용자와 음식점 거리를 위해 권한이 필요' 하다는 메세지와 권한 요청을 할 건지에 대한 alert 표시
- 권한 요청 시 시스템에 권한 요청
- 허용 시 사용자의 위치 측정
- 거부 시 권한 거부 상태를 위치 측정하는 곳에 표시

고려사항
- 앱 사용중 사용자에게 해당 권한을 필요 할 때 요청하기
- 권한이 없다고 사용자에 앱 사용을 막지 않기
- 사용자가 권한 회수 시, 우아하게 퇴보시키기

1. rememberMultiplePermissionsState 이용해 권한  

ButtonPermission 컴포넌트 제공

