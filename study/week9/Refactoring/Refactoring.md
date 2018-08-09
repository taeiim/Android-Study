# Refactoring

> Android 리팩토링에 대하여

> 작성자 : 박태임
>
> Present Time : 2018–08-09-THU



## 리팩토링

### 리팩토링이란?

리팩토링은 겉으로 드러나는 코드의 기능은 바꾸지 않으면서 내부 구조를 개선하는 방식으로 소프트웨어 시스템을 수정하는 과정이다.  버그가 생길 가능성을 최소화하며 코드를 정리하는 정제된 방법이다. 

###### * 리팩토링이 아닌 것 :  -버그를 없애거나 새로운 기능을 추가하는 행위



### 리팩토링의 목적

> 컴퓨터가 인식 가능한 코드는 바보라도 작성할 수 있지만, 인간이 이해할 수 있는 코드는 실력 있는 프로그래머만 작성할 수 있다.



## 리소스 리팩토링

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

![refactoring_2](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/colors_1.png)

**Greyscales** 섹션에는 흰색, 검은색, 회색을 정의했는데, 네이밍 규칙을 잘 만드는 것이 중요하다. 

1. grey1, grey2, grey3 
2. grey darker, grey lighter   

2번과 같이 네이밍을 하면 다양한 회색 조 색상을 쓰는 경우가 생기는데, 'grey white', 'grey whiter', 'grey whitest' 로는 이해하기가 어렵기 때문에 1번과 같이 네이밍 규칙을 정했다. 

(하지만, 자신에게 더 코딩하기 쉬운 네이밍 규칙으로 정하면 될 것 같다. )



**Quipper colors** 섹션에서는 앱에서 사용하는 특정 생상을 정의하였다. 

"warning", "danger" 등 같은 이름도 사용하면 이해하기 쉽다. 

![refactoring_3](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/colors_2.png)



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

테마를 사용해서 각 컴포넌트의 윈도우 백그라운드, 컬러, 사이즈 등 공통 속성을 적용할 수 있다. 예를 들어 스플래시 액티비티는 투명한 테마를 사용해서 배경색을 투명하게 할 수 있다. 

themes.xml은 styles.xml과 비슷하다. 그렇다면 왜 둘이 분리되어 있는 것일까?

둘이 구조는 거의 같지만 ``styles.xml`` 이 좀 더 빈번하게 업데이트가 될 가능성이 크다. 

테마로 적용하면  적용한 경우에 따라 범위 (액티비티나 애플리케이션) 의 모든 내용이 변경된다. 스타일은 더 로컬이다. 



### 4. styles.xml

``style.xml``을 파일 몇개로 분리.

``styles_login.xml`` 이나 ``styles_messages.xml`` 등 과 같이 특정 페이지를 위한 다른 스타일을 만든다. 

``styles.xml`` 에는 아이콘, 텍스트, 버튼 처럼 기본 스타일을 정의한다.



#### styles.xml 이 뭐죠?

미리 속성들을 정의해놓고 사용하는 것. 레이아웃을 간단하게 만들 수 있다. 

![styles_1](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/styles_1.png)



#### (styles 꿀팁) 스타일도 상속이 가능하다?!

``.`` 으로 연결해서 스타일을 상속시킬 수 있다. 

다른 컬러의 버튼 스타일이 필요하면 ``Button.Primary`` 를 만들고 백그라운드와 텍스트컬러 속성만 정의하면 된다. 

###### 지금까지 색상이 다르거나 크기가 다르면 style을 각각 따로 만들어서 사용했는데 상속 개념을 이용하면 코드가 많이 줄어들것 같다.

![styles_2](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/styles_2.png)



### 5. drawables

일반적으로 앱에 너무 많은 drawable이 있으므로 관리하기 어려운 경우가 많다. 쉽게 drawable을 관리하는 방법은 **이름** 프리픽스를 결정하는 것이다. 

아이콘 이름은

- **"구성요소"가 아니라 "모양"을 포함해야 한다.**

  예를들어,  비디오 플레이 아이콘이 있다면 이 아이콘은 사운드 플레이로도 사용될 수 있으므로 `ic_video_play` 보다는 `ic_play`라고 이름 짓는 것이 좋다. 

- **컬러를 포함하지 않아야 한다.**

  이름에 컬러를 포함하지 않은 이유는 **틴트** 를 사용하기 때문입니다. 이미지 뷰는 틴트 속성이 있습니다. 

![drawable_namingrule](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/drawable_namingrule.png)

일반적으로 Drawable을 리팩토링하는 것은 어렵기보다는 귀찮은 일에 가깝다. 

먼저, 사용하지 않는 드로어블을 삭제한다. 애초에 사용하지 않는 드로어블을 없애면 리네이밍하는 데 시간을 쓰지 않아도 된다.

다음 단계는, 모든 아이콘 이름을 리네이밍 하는 것이다. 

아이콘을 변경한 다음으로는 단계적인 리팩토링을 위해 이미지 이름을 변경한다. 

마지막으로 다른 드로어블을 조금씩 리네이밍 한다. 



## 코드 리팩토링

#### 메서드 정리 > 메서드 추출 (Extract Method)

리팩토링의 주된 작업은 코드를 포장하는 메서드를 적절히 정리하는 것이다. 거의 모든 문제점은 장황한 메서드로 인해 생긴다. 

> 어떤 코드를 그룹으로 묶어도 되겠다고 판단될 때는, 그 코드를 빼내어 목적을 잘 나타내는 직관적 이름의 메서드로 만들자.

**메서드 추출** 기법은 제일 많이 사용된다. 메서드가 너무 길거나 코드에 주석을 달아야만 의도를 이해할 수 있을 때, 그 코드를 빼내어 별도의 메서드로 만든다.

직관적인 이름의 간결한 메서드가 좋다. 왜냐하면

1. 메서드가 적절히 잘게 쪼개져 있으면 다른 메서드에서 쉽게 사용할 수 있다.
2. 상위 계층의 메서드에서 주석 같은 더 많은 정보를 읽어들일 수 있다. 
3. 재정의하기도 훨씬 수월하다.



긴 메서드에 익숙해진 사람은 잘 쪼개진 간결한 메서드에 익숙해지기까지 시간이 좀 걸린다. 메서드 내용이 간결한 것도 중요하지만, 효과를 보려면 메서드의 이름도 잘 지어야 한다. 



### 기타 꿀팁

#### Button1, Button2, Button3 … 로 이름지었을 때

일일이 1,2,3,4 모두 view를 inflate할 필요 없이 아래와 같이 + 기호를 사용해 한번에 해주면 된다.

![refactoring_2](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/refactoring_2.png)



#### 삼항 연산자

> 조건식 ? 피연산자1 : 피연산자2

조건식의 연산결과가 true 이면 결과는 피연산자1이고 , 조건식의 연산결과가 false 이면 결과는 피연산자2 이다.

```java
// 3항 연산자를 사용했을 때
int a = (5>4) ? 50 : 40;
```

```java
// 3항 연산자 대신 if-else문을 사용했을 대
int a = 0;
if(5 > 4){
    a = 50;
}else{
    a = 40;
}
```

##### 안드로이드에서 사용한 예

```Java
position = getArguments() != null ? getArguments().getInt("position") : 0;
```



#### for-each문

for each라는 명령어가 따로 있는 것이 아니고 기존과 동일한 for를 사용한다. 하지만, 보통 다른 언어에서 for each라고 많이 하므로 자바에서도 보통 for each문이라고 말한다. 

단, for each문은 따로 반복횟수를 명시적으로 주는 것이 불가능하고, 1스텝씩 순차적으로 반복될 때만 사용가능하다.

```java
// 기존 for 문
String[] numbers = {"one", "two", "three"};
for(int i=0; i<numbers.length; i++) {
    System.out.println(numbers[i]);
}
```

```java
// for each 문 구조로 변경
String[] numbers = {"one", "two", "three"};
for(String number: numbers) {
    System.out.println(number);
}
```

##### 내 코드에 있는 for문 for each로 바꿔보기

```java
// 변경전
for (int i = 0; i < 5; i++) {
    if (reviewCnts[i] > max) max = reviewCnts[i];
}
     
// 변경후
for(int reviewCnt : reviewCnts){
    if (reviewCnt > max) max = reviewCnt;
}
```

for문보다 for each문이 타이핑의 양도 작고 가독성이 더 좋은 것 같다.



## 마무리

### 소감

 리팩토링은 거창한 것이 아니다. 변수나 메서드의 이름을 이해하기 쉽게 바꾸는 것도 작은 리팩토링의 예가 된다. 

 개발자로서 코드의 품질을 생각하고, 협업할 때 같이 일하는 개발자들을 생각하게 된다면 리팩토링은 더욱 중요해질 것 같다. 



### 출처

#### 도서

- 리팩토링 : 코드 품질을 개선하는 객체지향 사고법 (마틴 파울러 지음)

- 소프트웨어 악취를 제거하는 리팩토링 : 구조적 설계 문제를 풀어내는 최선의 실천법 (기리쉬 서야나라야나 외 2명 지음)

  ​

#### 웹사이트

- https://academy.realm.io/kr/posts/android-resources-refactoring/ (한글문서)
- https://academy.realm.io/posts/android-resources-refactoring/ (영어문서)
- https://wikidocs.net/264

