# ConstraintLayout

> Android ConstraintLayout에 대하여

> 작성자 : 박태임
>
> Present Time : 2018–07-30-MON



## 목차

[TOC]

## 1. ConstraintLayout, 왜 사용해야 하죠?

### 1.1 뷰를 그리는 과정

3가지 단계를 거쳐 view가 화면에 나타난다.

1. 측정 (Measurement)
2. 레이아웃 (Layout)
3. 그리기 (Draw)

액티비티가 포커스를 갖게되면, 시스템은 액티비티의 root node를 요청한다. 

``Measure 단계`` 에서는 view의 크기를 결정한다. 측정은 root노드에서 시작해 반복적으로 호출되며, 각각의 호출은 부모로부터 전달된 인자들과 함께 발생한다. 이 인자들은 widthMeasureSpec, heightMeasureSpec이라는 이름으로 전달된다.

``Layout 단계`` 에서는 각각의 view 크기를 기준으로 view의 위치를 결정한다. root노드에서 시작해 leaf노드까지 반복적으로 호출된다.

``Draw 단계 ``는 최종적으로 뷰를 캔버스위에 실제 그리는 과정이다. 루트 노드가 자신을 화면에 그리라는 요청을 받으면, 자식들에게 자신들을 측정하라는 메시지가 전달되며 모든 view가 측정될 때까지 이러한 과정이 반복된다.

따라서, 중첩된 레이아웃이 많을 수록 측정하는데 걸리는 시간도 늘어나게 된다.



### 1.2 ConstraintLayout을 사용하면?

[안드로이드 성능개선](study/week5/android%20performance/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C%20%EC%84%B1%EB%8A%A5%EA%B0%9C%EC%84%A0.md) 포스팅의 **레이아웃 최적화** 부분과 위의 뷰를 그리는 과정을 참고하면

레이아웃을 좁고 깊게 만들기보다, 얇고 넓게 만들어 레이아웃을 중첩을 줄이면 성능을 향상시킬 수 있다는 것을 알 수 있다. 

![constraintlayout_1](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/constraintlayout_1.png)

 ###### [그림1] ConstraintLayout VS. 중첩된 여러 레이아웃

ConstraintLayout을 사용하면 위 그림과 같이 평평한(flattened) 계층 구조를 유지하면서도 복잡한 레이아웃을 작성할 수 있다. 



### 1.3 ConstraintLayout의 장점

- 여러 다른 레이아웃의 다양한 기능을 하나의 레이아웃에서 처리할 수 있는 유연성 제공
- 다른 종류의 레이아웃을 중첩할 필요가 없으므로 레이아웃의 복잡해지지 않으며, 런타임 시에 사용자 인터페이스를 화면에 보여주는 성능도 향상된다.
- 다양한 안드로이드 장치의 화면 크기를 자동으로 처리해 주는 뷰로 구현되었기 때문에, 어떤 장치에서 앱이 실행되더라도 응답성과 적응성이 좋은 사용자 인터페이스를 만들 수 있다. 





</br></br>

## 2. ConstraintLayout 구성요소와 알아야 할 것들

다른 레이아웃과 마찬가지로 ConstraintLayout도 자신이 포함하는 시각적인 컴포넌트(위젯)의 위치와 크기를 관리한다. 그리고 이때 각 자식 위젯에 설정되는 제약 연결(constraint connection)을 기반으로 처리한다. 

### 2.1 Constraints

제약조건은 레이아웃에서 두 위젯 간의 관계를 정의하고 해당 위젯이 레이아웃 내에 배치되는 방식을 제어한다.

또한, 장치 회전에 따른 화면 크기 변화가 생기거나 서로 다른 크기의 화면을 갖는 장치에서 레이아웃이 나타날 때 액티비티의 사용자 인터페이스 레이아웃이 응답하는 방법도 나타낸다. 

제약조건을 만드는 가장 쉬운 방법은 Android Studio의 Blueprint를 이용하는 것이다.

아래와 같은 방법으로 Blueprint를 사용할 수 있다.

![constraintlayout_2](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/constraintlayout_2.png)



### 2.2 제약 바이어스

기본적으로 ConstraintLayout에서는 동일한 상대 제약을 지정한다. 따라서 위젯이 수평과 수직 방향 모두 중앙에 위치하게 된다.

따라서 상대 제약을 사용하면서 위젯의 위치를 추가로 조정할 수 있게 하기 위하여 ConstraintLayout에서는 제약 바이어스 기능을 구현하였다. 제약 바이어스를 사용하면 하나의 제약에 지정된 비율을 기준으로 상대 제약을 갖도록 위젯을 위치시킬 수 있다. 

![bias](/Users/parktaeim/Desktop/bias.png)

위젯을 선택하면 속성 창에 두 개의 슬라이더가 나타난다. 이것을 마우스로 끌면 수평과 수직 방향의 제약 바이어스가 조정된다. 



### 2.3 Handles

![constraintlayout_3](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/constraintlayout_3.png)

- #### Resize Handle

  - 위젯의 각 꼭지점의 사각형모양
  - 위젯의 크기를 조정한다.

- #### anchor points

  - 각 모서리의 중간에 있는 원은 앵커포인트이며 제약조건을 만드는 데 사용된다.
  - 앵커 포인트를 한번 더 클릭하면 제약조건을 삭제할 수 있다.




### 2.4 Chains

두 개 이상의 위젯을 하나의 그룹처럼 동작할 수 있게 하는 개념이 체인이다. 체인은 수직 또는 수평 축으로 구성될 수 있으며, 체인으로 연결된 위젯들의 여백과 크기를 일괄 조절할 수 있다. 

체인의 첫 번째 요소를 체인 헤드(chain head)라고 한다. 그리고 체인은 체인 헤드 위젯에 설정된 체인 스타일(chain style) 속성들에 의해 제어된다. 

#### 2.4.1 체인 스타일

- ##### Spread 체인

  체인에 포함된 위젯들이 체인 내부의 사용 가능한 공간에 고르게 분산 배치되며 기본 스타일 값이다. 

  ![chain_spread](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/chain_spread.png)

- ##### Spread Inside 체인

  체인에 포함된 위젯들이 체인 헤드와 체인의 마지막 위젯 사이에 고르게 배치된다. 단, 헤드와 마지막 위젯은 체인 내부의 공간 분배에 포함되지 않는다.

  ![chain_spreadinside](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/chain_spreadinside.png)

- ##### Weighted 체인

  체인의 각 위젯들이 차지하는 공간을 각 위젯의 가중치 속성으로 제어할 수 있다.

  ![chain_weighted](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/chain_weighted.png)

  Weighted 체인은 spread 체인 스타일을 사용할 때문 구현할 수 있으며, 체인에 포함된 모든 위젯들의 크기가 match constraint로 지정되어야 한다. 

  위젯의 크기를 match constraint로 지정할 때는 속성창의 크기 속성 (horizontal : layout_width / vertical : layout_height 속성) 값을 0dp로 변경하면 된다.

  위젯의 크기가 0dp 라는 것은 크기가 없다는 것이 아니고, 위젯에 설정된 제약에 맞추어 해당 위젯의 크기가 자동 결정된다는 의미이다. 

- ##### Packed 체인

  체인을 구성하는 위젯들이 간격 없이 붙어서 배치되며, 체인 외부의 좌우 또는 상하의 남는 공간은 기본적으로 동일하게 배정된다.

  ![chain_packed](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/chain_packed.png)




### 2.5 Guidelines

제약조건을 쉽게 설정하도록 도와주는 헬퍼클래스로, 뷰를 배치하기 위해 뷰그룹 내에 설정할 수 있는 일종의 기준선이다.

Guidelines 객체는 화면에 나타나지 않는다.(View.GONE 상태)



###2.6 Margin

특정 위젯과 다른 요소 사이의 간격(마진)을 띄우기 위해 제약을 사용할 수 있다.

바이어스가 조정되더라도 항상 지정된 마진을 유지한다. 

바이어스가 100%로 설정되어 오른쪽에 붙어있더라도 오른쪽 마진이 50dp 설정되어 있다면 항상 레이아웃의 오른쪽에서 50dp 만큼 떨어진다. 

![margin](/Users/parktaeim/Desktop/margin.png)

마진 값은 속성 창에서 변경할 수 있다. 드롭다운을 사용해도 되고, 값을 직접 입력해도 된다.



### 2.7 상대 제약과 바이어스의 중요성

ConstraintLayout을 사용할 때는 상대 제약과 마진 및 바이어스가 레이아웃 디자인의 핵심이다.

만일 상대 제약 없이 위젯의 제약이 연결되면 기본 적으로 마진 제약이 설정된다. (레이아웃 편집기에서는 마진 값을 갖는 직전으로 보여준다.)

마진 제약은 항상 지정된 여백을 띄운 후 위젯을 위치시킨다. 따라서 장치를 가로로 회전되어 화면의 높이가 작아지면 하단에 위치해 있는 위젯은 볼 수 없게 된다.  

![constraintlayout_4](/Users/parktaeim/Documents/GitHub/Android-Study/study/week7/ConstraintLayout/images/constraintlayout_4.png)



이와 달리, 상대 제약으로 수평과 수직 방향의 바이어스를 설정하면 장치 화면의 크기가 달라지더라도 그것의 화면 크기에 비례하여 일정한 위치에 위젯이 나타날 수 있다.

![constraintlayout_5](/Users/parktaeim/Desktop/constraintlayout_5.png)

결론적으로, 유연한 사용자 인터페이스 레이아웃을 생성하려면 상대 제약과 제약 바이어스를 같이 고려하는 것이 중요하다. 





</br></br>

## 3. 사용해보기

### 3.1 XML 직접 작성 VS. 레이아웃 편집기의 디자인 모드 사용

ConstraintLayout으로 레이아웃을 생성할 때, 레이아웃 편집기의 텍스트 모드에서 XML을 직접 작성할 것인지, 디자인 모드를 사용할 것인지는 각자의 취향에 달렸다. 그러나 디자인 모드를 사용하는 것이 장점이 많다.

- XML을 자동 생성해주므로 빠르고 편리

- 안드로이드 SDK의 각종 뷰 클래스들이 갖는 속성과 값을 자세히 알 필요가 없다.

  (굳이 안드로이드 문서를 일일이 찾지 않아도 속성 창에서 바로 볼 수 있기 때문.)

그러나 레이아웃 편집기의 디자인 모드와 텍스트 모드는 상호 배타적이지 않다. 따라서 디자인 모드에서 레이아웃을 그리듯이 작성하면 XML이 자동 생성되며, 텍스트 모드에서는 그 XML을 사용해서 우리가 직접 편집할 수 있다. 반대의 경우도 마찬가지다. 그러므로 두 가지 모드를 번갈아 사용하면서 레이아웃을 개발하는 것이 좋은 방법이다. 



### 3.2 레이아웃 편집기

레이아웃 편집기에서는 제약 연결을 생성 및 변경하는 세 가지 방법을 제공한다. 그것은 **자동 연결**과 **제약 추론** 및 **수동 연결**이다.



#### 3.2.1 자동연결

자동연결(Autoconnect)은 제약을 자동으로 추가해 주는 기능이며, 자석처럼 생긴 Autoconnect 툴바 버튼을 사용해서 활성화/비활성화를 상호 전환할 수 있다.

자동연결이 활성화된 상태에서 레이아웃에 위젯을 끌어 놓으면 자동으로 제약 연결이 추가된다. 

![autoconnect](/Users/parktaeim/Desktop/autoconnect.png)

자동 연결에서는 레이아웃에 추가되는 위젯과 인접한 부모 레이아웃 및 다른 요소의 위치를 기준으로 그 위젯의 제약 연결을 생성한다. 이때 내부적으로 알고리즘을 사용한다.

그러나 레이아웃 편집기에서 자동으로 제약 연결을 생성할 수 없는 경우가 생길 수 있다. 이때는 수동으로 제약을 추가해주어야 한다.



#### 3.2.2 제약 추론

제약 추론(Inference constraints)에서는 이미 레이아웃에 추가된 위젯들을 대상으로 제약 연결을 추가 및 변경해준다. 이때 알고리즘과 확률이 수반된 경험적 방법이 사용된다. 자동연결에 비활성화된 상태에서 레이아웃을 디자인(ex.위젯추가)을 한 후 제약 추론 기능을 사용하면 편리하다. 

그러나 자동연결처럼 제약 추론 기능을 사용할 때도 레이아웃 편집기에서 부적합한 제약 연결을 추가할 수 있다. 이때는 수동으로 변경할 수 있다.

![inferconstraint](/Users/parktaeim/Desktop/inferconstraint.png)



#### 3.2.3 수동 연결

수동으로 제약 연결을 추가 및 변경하려면 핸들을 이용하면 된다. 



</br></br>



## 4. 마무리

### 4.1 소감

ConstraintLayout은 공부하면 할 수록 매력있고 왜 안쓰나 하는 생각을 들게했다.

제일 매력있었던 것은 바이어스를 이용해 상대제약을 만들어 화면을 회전해도 깨지지 않는 뷰를 쉽게 만들 수 있다는 것이었다. 

다른 레이아웃을 계속 사용하다보니 ConstraintLayout을 처음 접했을 때 이해하기가 조금 힘들었지만 이렇게 공부하고 정리하다 보니 어느새 이해가 되어있어서 뿌듯하다. 다음부터 레이아웃을 만들때는 꼭 ConstraintLayout을 사용할 것이다.



### 4.2 출처

#### 도서

- 핵심만 골라 배우는 안드로이드 스튜디오3 & 프로그래밍 (닐 스미스 지음/심재철 옮김)

  ​

#### 웹사이트

- https://academy.realm.io/kr/posts/constraintlayout-it-can-do-what-now/
- https://academy.realm.io/kr/posts/cool-constraintlayout-droidcon-boston-2017/
- https://academy.realm.io/kr/posts/exploring-new-android-layouts/ (이미지1 출처)