# Refactoring

> Android 리팩토링에 대하여

> 작성자 : 박태임
>
> Present Time : 2018–08-01-WED



## 리팩토링

### 리팩토링이란?

소프트웨어 공학에서 '결과의 변경 없이 코드의 구조를 재조정함'을 뜻한다.

- 주로 가독성을 높이고 유지보수를 편하게 한다.
- 사용자가 보는 외부 화면은 그대로 두면서 내부 논리나 구조를 바꾸고 개선하는 유지보수 행위.



리팩토링이 아닌 것

- 버그를 없애거나 새로운 기능을 추가하는 행위



## 리소스 리팩토링

``color.xml`` 과 ``dimens.xml`` 부터 수정하는 것이 좋다. 

왜냐하면, 	``style.xml`` 과 ``themes.xml`` 도 color와 dimens에 의존하기 때문이다.

![refactoring_1](/Users/parktaeim/Documents/GitHub/Android-Study/study/week9/Refactoring/images/refactoring_1.png)



### 1. color.xml

### 2. dimens.xml

### 3. themes.xml

### 4. styles.xml

### 5. drawables

일반적으로 앱에 너무 많은 drawable이 있으므로 관리하기 어려운 경우가 많다. 쉽게 drawable을 관리하는 방법은 이름 프리픽스를 결정하는 것이다. 





## 코드 리팩토링



## 마무리

### 소감



### 출처

#### 도서



#### 웹사이트

- https://academy.realm.io/kr/posts/android-resources-refactoring/
- https://ko.wikipedia.org/wiki/%EB%A6%AC%ED%8C%A9%ED%84%B0%EB%A7%81