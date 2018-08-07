# Refactoring

> Android 리팩토링에 대하여

> 작성자 : 박태임
>
> Present Time : 2018–08-01-WED



## 리팩토링

### 리팩토링이란?

리팩토링은 겉으로 드러나는 코드의 기능은 바꾸지 않으면서 내부 구조를 개선하는 방식으로 소프트웨어 시스템을 수정하는 과정이다.  버그가 생길 가능성을 최소화하며 코드를 정리하는 정제된 방법이다. 

###### * 리팩토링이 아닌 것 :  -버그를 없애거나 새로운 기능을 추가하는 행위



### 리팩토링의 목적

> 컴퓨터가 인식 가능한 코드는 바보라도 작성할 수 있지만, 인간이 이해할 수 있는 코드는 실력 있는 프로그래머만 작성할 수 있다.



## 리소스 리팩토링

### 복잡한 리소스 문제 



### 리팩토링 단계

``colors.xml`` 과 ``dimens.xml`` 부터 수정하는 것이 좋다. 

왜냐하면, 	``style.xml`` 과 ``themes.xml`` 이 color와 dimens에 의존하기 때문이다.

아래 사진을 보면 style.xml에서 color와 dimens에 의존성을 갖고 있는 것을 알 수 있다.

따라서, 리소스를 리팩토링할 때는 먼저 컬러와 디멘션(크기)을 수정하는 것이 좋다. 

![refactoring_1](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/refactoring_1.png)



### 1. colors.xml

``colors.xml`` 은 앱에 사용하는 모든 색을 정의한다.



#### 섹션 나누어 컬러 관리하기

나는 원래 모든 색상을 순서없이 정리해 두었는데 섹션을 나누어 컬러를 관리하는 것이 좋은 방법이라고 한다.

**Greyscales** 과  **Quipper colors** 두가지 섹션으로 나눈다. (너무 많은 섹션을 만드는 것보다는 두가지로 간단하게 나누는 것으로도 충분하다.)

![refactoring_2](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/refactoring_2.png)

**Greyscales** 섹션에는 흰색, 검은색, 회색을 정의했는데, 네이밍 규칙을 잘 만드는 것이 중요하다. 

1. grey1, grey2, grey3 
2. grey darker, grey lighter   

2번과 같이 네이밍을 하면 다양한 회색 조 색상을 쓰는 경우가 생기는데, 'grey white', 'grey whiter', 'grey whitest' 로는 이해하기가 어렵기 때문에 1번과 같이 네이밍 규칙을 정했다. 

(하지만, 자신에게 더 코딩하기 쉬운 네이밍 규칙으로 정하면 될 것 같다. )



**Quipper colors** 섹션에서는 앱에서 사용하는 특정 생상을 정의하였다. 

"warning", "danger" 등 같은 이름도 사용하면 이해하기 쉽다. 

![refactoring_3](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/refactoring_3.png)



### 2. dimens.xml

#### dimens_base.xml 과 dimens.xml

``dimens_base.xml`` 과  ``dimens.xml`` 두 파일을 정의하는 것이 좋다.

``dimens_base.xml`` : 아이콘, 텍스트, 버튼 등 기본 구성요소의 기본 크기를 정의

space, text, button, radius, elevation 등으로 섹션을 만든다. 

![dimens_base](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/dimens_base.png)

``dimens.xml`` : 특정 페이지의 크기를 정의

![dimens](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/dimens.png)



컬러와 디멘션을 리팩토링 했으니 이제 테마와 스타일을 리팩토링하러 고고고~~‼️



### 3. themes.xml



### 4. styles.xml

### 5. drawables

일반적으로 앱에 너무 많은 drawable이 있으므로 관리하기 어려운 경우가 많다. 쉽게 drawable을 관리하는 방법은 이름 프리픽스를 결정하는 것이다. 





## 코드 리팩토링





## 마무리

### 소감



### 출처

#### 도서

- 리팩토링 : 코드 품질을 개선하는 객체지향 사고법 (마틴 파울러 지음)

- 소프트웨어 악취를 제거하는 리팩토링 : 구조적 설계 문제를 풀어내는 최선의 실천법 (기리쉬 서야나라야나 외 2명 지음)

- 유지보수 가능한 코딩의 기술 : 클린코드의 비결 - 자바편 (주스트뷔서 등 5인 지음)

  ​

#### 웹사이트

- https://academy.realm.io/kr/posts/android-resources-refactoring/
- ​