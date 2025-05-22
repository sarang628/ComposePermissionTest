package com.sryang.library.compose.workflow

/**
 * https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions
 * 1. 메니페스트에 선언해야하는 권한인지 확인
 * 2. UX에 권한을 필요로 하는 정보 인지 시키기
 * 3. 2번을 인지한 사용자의 응답 기다리기
 * 4. 이미 권한을 얻었는지 확인하기
 * 5. 시스템에 물어봐서 rationale을 표시해야 하는지 확인
 * 6. 런타임 권한 요청하기
 * 7. 사용자의 응답 체크하기
 * 8. 사용자가 권한을 허가했다면, 자원 접근 가능
 */
sealed interface PermissonWorkFlow {
    object InitialPermissionCheck : PermissonWorkFlow
    object RecognizeToUser : PermissonWorkFlow
    object CheckRationale : PermissonWorkFlow
    object ShowRationale : PermissonWorkFlow
    object RequestPermission : PermissonWorkFlow
    object GrantedPermission : PermissonWorkFlow
    object DeniedPermission : PermissonWorkFlow
    object SuggestSystemSetting : PermissonWorkFlow
}