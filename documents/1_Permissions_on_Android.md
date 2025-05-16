https://developer.android.com/guide/topics/permissions/overview
Permissions on Android

## 요약
- 권한은 정보, 동작 접근 제한. 사용자의 privacy를 보호
- 권한 없이 정보를 얻는 대안이 있으므로 사용 고려
- 인스톨 타임 퍼미션: 앱 설치시 자동 획득. 위험성 낮음. 노멀 퍼미션과 시그니처 퍼미션 하위 카테고리로 나뉨
- 노멀 퍼미션: 사용자 개인정보에 아주 적은 리스크를 갖음
- 시그니처 퍼미션: 같은 인증서를 사용해야 사용 가능한 퍼미션
- 런타임 퍼미션: 위험성 높음. 사용자에게 명시적으로 요청
- 스페셜 퍼미션: 플래폼, OEM 전용 퍼미션. 제조사 에서만 만들 수 있음.
- 퍼미션 그룹: 비슷한 권한이 묶여진 그룹. 한 번에 권한을 부여할 수 있음.
- 모범 사례: 최소권한 요청, 특정 동작에서만 요청, 라이브러리 필요 권한 확인, 투명성 유지, 시스템 접근 명시
- 커스텀 권한: 권한은 시스템에서만 제공하는것이 아닌 앱에서도 자체적으로 제작 하여 제공 가능. 

앱 권한은 사용자의 privacy를 지켜주는데 도움을 준다.
- 시스템 상태나 연락처와 같은 정보 접근 제한
- 다른 디바이스나 연결하거나 녹음 하는 등의 동작을 제한

이 장에서 제공 정보, 권한 획득, 처리 절차, 권한의 다른 종류 설명, best practice.

다른 장은 권한 요청, 선언 등의 '최소화' 방법, 다른 앱들과 상호 작용을 '제한' 하는 방법 제공.

## [권한 획득 작업 절차](https://developer.android.com/guide/topics/permissions/overview#workflow)
제한된 동작 또는 데이터에 접근 시, 권한 필요여부 점검. 권한없이 사진, 미디어 등의 정보를 얻는 방법도 있어 이 기능을 사용 할지 고려.
그렇지 않다면 권한을 얻어 데이터에 접근. 일부 권한들은 앱 설치 시 자동 부여되는 '인스톨 타임 퍼미션' 이 있고, 앱 실행중 권한을 얻는 '런타임 퍼미션'이 있다.

<img src="workflow-overview.svg" >

## [퍼미션의 종류](https://developer.android.com/guide/topics/permissions/overview#types)
인스톨 타임, 런타임, 특별권한 퍼미션으로 나뉨. 

### [인스톨 타임 퍼미션](https://developer.android.com/guide/topics/permissions/overview#install-time)
시스템과 다른앱에 영향을 별로 미치지 않는 퍼미션들
앱스토어에서 앱 설치 전 사용하는 권한들을 확인
'인스톨 타임 퍼미션'은 '노멀 퍼미션'과 '시그니처 퍼미션'을 하위 종류로 둠.

### [Normal permissions](https://developer.android.com/guide/topics/permissions/overview#normal)
사용자 개인정보에 아주 적은 리스크를 갖음

### [Signature permissions](https://developer.android.com/guide/topics/permissions/overview#signature)
권한을 정의한 앱또는 OS와 똑같은 인증서를 사용해야 권한을 허용 함.
autofill이나 vpn 서비스에 해당할 수 있는데, 서비스 바인딩 요청 시 인증서를 확인하여 바인딩 시킬지 결정함.

### [Runtime permissions](https://developer.android.com/guide/topics/permissions/overview#runtime)
'위험한 퍼미션'으로도 알려진 '런타임 퍼미션'은 시스템이나 다른앱에 상당히 영향을 줄 수 있는 권한들.
그렇기에 권한 필요시 사용자에게 접근 요청을 한다. 권한이 주어졌다고 가정하지 말 것.(항상 기능이 필요한 시점에 다시 체크)
시스템은 위험한 권한으로 판단면 런타임에 할당한다.

### [Special permissions](https://developer.android.com/guide/topics/permissions/overview#special)
플랫폼과 OEM에서만 만들수 있는 권한. 제조사에서 특별한 기능을 제공을 위한 권한을 정의하고 싶을때 사용. 

### [Permission groups](https://developer.android.com/guide/topics/permissions/overview#groups)
퍼미션은 그룹에 속할 수 있다. 비슷한 권한의 경우 다이얼로그 표시 등 절차를 최소화하고 사용성을 높이기 위해 그룹으로 권한을 요청 할 수 있다.
퍼미션 그룹은 별도 공지 없이 바뀔 수 있으므로, 앱에서는 필수 권한들을 기능 사용 전 항상 체크해야 한다.

## [Best practices](https://developer.android.com/guide/topics/permissions/overview#best-practices)
안드로이드 권한은 사용자 프라이버시를 지원해야 함.
- control: 사용자가 앱에 제공하는 데이터를 제어
- transparency: 앱이 '어떤' 데이터를 '왜' 사용하는지 이유
- data minimization 앱은 데이터가 필요한 시점에만 요청 하고 사용

### [최소 권한만 요청](https://developer.android.com/guide/topics/permissions/overview#minimal-number)
사용자의 동작이 권한을 필요로 할 때에만 요청, 권한 요청없이 데이터를 가져오는 방법이 있으므로 이 방법 고려

### [특정 동작에서 요청](https://developer.android.com/guide/topics/permissions/overview#associate-with-actions)
가능한 권한 요청을 최대한 늦춰야 함. 예) 채팅장에서 오디오 메세지를 보내는 버튼을 클릭 시 마이크 권한 요청

### [라이브러리 필요 권한 확인](https://developer.android.com/guide/topics/permissions/overview#minimal-number)
라이브러리에서 권한을 필요하는 권한 인지 하기

### [투명성](https://developer.android.com/guide/topics/permissions/overview#minimal-number)
'어떤' 데이터를 '왜' 필요로 하는지 설명하기

### [시스템 접근 명시](https://developer.android.com/guide/topics/permissions/overview#system-accesses)
시스템의 데이터를 사용하는 동안 인디케이터와 같은 표시를 데이터 사용이 끝날 때 까지 해주기.

### [커스텀 권한](https://developer.android.com/guide/topics/permissions/overview#system-components)
권한은 오직 시스템에서만 제공하는 것이 아님. 다른 앱에서 상호작용을 원할 경우 내 앱만의 권한을 정의