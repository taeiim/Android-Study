# 안드로이드 테스팅

> Android 단위테스트에 대하여

> 작성자 : 박태임
>
> Present Time : 2018–06-27-WED
>
> Last Updated : 

------

</br>

## 1. 테스트가 뭐쥬? 

### 1.1 테스트란?

  테스트의 사전적 의미 

- 제품의 성능이나 상태 따위를 일정한 기준에 따라 검사함.
- 일정한 기준에 따라 검사하다. 

  소프트웨어에 테스트

- 소프트웨어의 결함이 존재함을 보이는 과정이다.
- 소프트웨어 문제가 없다를 보이는 것이 아니라 문제가 있다를 밝히는 과정이다.
- 시스템이 정해진 요구를 만족하는지, 예상과 실제 결과가 어떤 차이를 보이는지 수동 또는 자동 방법을 동원하여 검사하고 평가하는 일련의 과정이다.

</br>

### 1.2 테스트의 목적

- 필요한 조건을 만족하는지 확인

- 오류를 발견

- 앱의 사용성을 확인

- 등





</br>


### 1.3 자동테스트 와 수동테스트

테스트는 수동 테스트와 자동테스트로 크게 둘로 나눌 수 있다.

수동 테스트는 코딩 작업이 끝난 상태에서 apk 를 만들고, 체크리스트로 작성한 테스트 케이스를 차례대로 실행하는 형태로 이루어지는 경우가 많다. 

반면 자동테스트는, 단위 테스트나 UI 테스트로 불리는 테스트 코드를 만들어 테스트 코드를 실행함으로써 앱이 바르게 동작하는지 확인 한다.

자동 테스트의 특성

- 반복 실행이 가능
- 테스트 실행 상황을 시각화 할 수 있다.
- 클래스와 메서드 등 사용자 조작으로 확인하기 어려운 단위의 동작을 확인할 수 있다.



수동 테스트의 특성

- 최종 사용자와 완전히 똑같이 동작을 확인할 수 있다.
- 사용성이나 쾌적함 등 정성적인 면에서 평가할 수 있다.


</br>

### 1.4 테스팅 왜 해야 하쥬?

- 테스트 하는 것은 개발자인 여러분이 다르게 생각할 수 있는 기회이기도하며 암묵적으로는 코드를 더 깔끔히 생산할 수 있게끔 한다.
- 테스트를 거치면 개발자는 코드에 더 자신감을 가질 수 있다.
- 빛나는 초록생 상태바와 리포트의 상세 내용은 테스트 보고자료가 된다.
- 자동화 테스트에서 버그를 먼저 잡아내기에 리그레션 테스트를 쉽게 할 수 있다. 이 중 리그레션 테스트가 가장 큰 장점을 갖는 부분으로, 여러분이 코드를 리팩토링하고 다시 테스트를 패스했을때 자신감 상승은 당연한 일이겠죠?! 테스트와 관련된 문제가 있다면 테스트를 한 즉시 그 효과를 확인할 수 있다기 보다 아무래도 몇 달(;)은 지나고 리팩토링이 필요해져야 알 수 있다는 점이다… 



### 1.5 테스팅 종류

공정과 요구하는 품질, 실행 방법, 기법 등 다양한 관점에 따라 테스트에는 수많은 종류가 있다.

예를 들어, 'V모델' 에서는 단위 테스트, 통합 테스트, 시스템 테스트, 인수 테스트 같은 공정별 테스트를 볼 수 있다. 또한 그 밖에도 화이트박스 테스트, 블랙박스 테스트, 부하 테스트, 성능 테스트 등등 많은 종류가 있다. 



*** 안드로이드 앱의 테스트 계층 사진** (테스팅의 피라미드)

![앱 테스트 계층](https://github.com/taeiim/Android-Study/blob/master/study/week03/Img_AndroidTesting/%EC%95%B1%20%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EA%B3%84%EC%B8%B5.png)

기본적으로 테스트 케이스의 수나 실행 횟수 (실행 타이밍)는 '단위테스트'가 가장 많고, '그 밖의 테스트'가 가장 적다. 아래로 갈 수록 많아지므로 다음과 같은 사진으로 정리할 수 있다.

최하층의 단위테스트는 클래스나 메서드처럼 아주 작은 단위를 검증하기 위해 실행되는 테스트이다. 단위 테스트는 가장 작은 단위로 검증이나 실행이 이루어 지므로 실행 시간도 아주 짧다. 

다음 계층에 있는 UI 테스트는 실제 동작 환경을 중요시 하므로 일반적으로는 실제 기기나 에뮬레이터 상에서 실행된다. UI테스트라는 이름 그대로 단말의 표시를 바탕으로 확인해 간다.

나머지 2계층인 인수 계층과 그 밖의 테스트에서는 사양을 만족하는지, 사용성에 문제는 없는지와 같은 정성적인 사항을 포함한 확인이 이루어진다. 기타 테스트의 일부로서 성능 테스트나 보안 테스트를 들 수 있다. 



</br>

</br>

## 2. 단위 테스트 (Unit Test)

### 2.1 단위 테스트란?

프로그램을 작성한 후에 코드를 테스트하기 위한 방법 중에 메소드 단위로 기능을 테스트

단위 테스트는 특정 모듈이 의도한 대로 잘 동작하는가를 테스트하는 것이다. 즉, 모든 함수와 메서드에 대한 테스트  케이스를 작성하는 절차를 말한다. 

관련 툴: [JUnit](http://junit.org/junit4/), [Mockito](http://mockito.org/), [PowerMock](https://github.com/jayway/powermock) 

번외 ) UI 테스트 관련 툴: [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/), [UIAutomator](https://developer.android.com/training/testing/ui-testing/uiautomator-testing.html), [Robotium](http://robotium.com/), [Appium](http://appium.io/), [Calabash](http://calaba.sh/), [Robolectric](http://robolectric.org/) 



### 2.2 단위 테스트, 왜 해야하쥬?

- 내 코드가 제대로 동작하는지 확인하는 것
- 애플리케이션의 유닛(함수/메서드) 를 더 작게 만든다. -> 코드를 이해하고 테스트하기 쉽게 만들며, 변화시키는 것 또한 쉽도록 한다. 코드의 단위는 작을 수록 좋다. 
- 문제를 빨리 발견하고 변화를 쉽게하며 통합을 간단하게 하고 설계를 개선할 수 있다.



### 2.3 Mockito

이상적으로, 각 테스트 케이스는 서로 분리되어야 한다. 이를 위해 가짜 객체 (Mock object) 를 생성하는 것도 좋은 방법이다. 



</br>

</br>



## 3. JUnit

###### 이 문서는 JUnit4를 기준으로 작성되었습니다. 

### 3.1 JUnit 이란?  

안드로이드 단위 테스트에는 JUnit 이라는 프레임워크가 사용된다.  JUnit은 자바용 단위테스트 작성을 위한 산업 표준 프레임워크 이다. 

해당 함수에 대한 실행 시간을 표시해 준다. 

</br>



### 3.2 기본 단정문

- ##### 단정문이 뭔가요?

  JUnit에서 테스트의 성공 실패를 판단하도록 하는 문장을 **단정문**이라 한다.

- ##### 단정문의 종류

  |            Method             |                설명                |                 비고 (예시)                  |
  | :---------------------------: | :------------------------------: | :--------------------------------------: |
  | assertEqaul(expected, actual) |        두 값이 같은지 비교하는 단정문         |   assertEqual(10, Calculator.add(2,8))   |
  |          assertSame           | 두 객체가 정말 동일한 객체인지 주소값으로 비교하는 단정문 |                                          |
  |          assertTrue           |       계산 결과가 참인지 판별하는 단정문        | boolean값을 리턴하는 메서드를 테스트하기 적합 (assertTrue,assertFalse 둘다) |
  |          assertFalse          |       계산 결과가 거짓인지 판별하는 단정문       | assertFalse(Calculator.isInteger("abc")) |
  |          assertNull           |      대상 값이 null이면 참이 되는 단정문      |           assertNull(textFile)           |
  |         assertNotNull         |     대상값이 null이 아니면 참이 되는 단정문     |         assertNotNull(textFile)          |
  |        fail([message])        |   호출 즉시 테스트 케이스를 실패로 판정하는 단정문    | [fail 단정문 활용사례](https://m.blog.naver.com/PostView.nhn?blogId=netrance&logNo=110182837782&proxyReferer=https%3A%2F%2Fwww.google.co.kr%2F) |


</br>



### 3.3 JUnit 어노테이션 (Annotation)

- ##### 어노테이션

  Ex) @Override, SuppressWarnings

  어노테이션은 다양한 목적으로 사용되지만 메타 데이터로서의 용도가 가장 크다 할 수 있다. 

  메타데이터 : 데이터를 위한 데이터를 의미, 한 데이터에 대한 설명을 의미하는 데이터 (자신의 정보를 담고있는 데이터)

  ​

  JUnit4에서 부터 테스트 코드에 어노테이션이 이용된다. 

  </br>

- ##### Junit4에서의 주요 어노테이션 

  |  Annotation  |                  설명                  |                    비고                    |
  | :----------: | :----------------------------------: | :--------------------------------------: |
  |    @Test     |           테스트할 메서드를 정의한다.            | @Test(timeout=1000)//1초 초과시 에러@Test(expected = RuntimeException.class) //예외 예상 |
  |   @Ignore    |          무시. 단위테스트에서 제외된다.           |                                          |
  | @BeforeClass |  테스트 클래스 실행 전에 한번 수행되는 메서드 임을 선언한다.  |                                          |
  | @AfterClass  |  테스트 클래스 실행 후에 한번 수행되는 메서드임을 선언한다.   |                                          |
  |   @Before    | 다음에 나오는 @Test 실행 전에 실행되는 메서드임을 선언한다. |                                          |
  |    @After    | 이전에 나온 @Test 실행 후에 실행되는 메서드임을 선언한다.  |                                          |


</br>


- BeforeClass/AfterClass 와  Before/After의 호출 순서 이해 

  ![Annotation](https://github.com/taeiim/Android-Study/blob/master/study/week03/Img_AndroidTesting/img_annotation.png)

</br>

### 3.4 JUnit을 이용해 직접 테스트해보자! - Calculator

1. ##### Caluculator 클래스를 만들어준다.

   ```java
   public class Calculator {
       public static int add(int a, int b) {
           return a + b;
       }

       public static int subtract(int a, int b) {
           return a - b;
       }

       public static int multiply(int a, int b) {
           return a * b;
       }

       public static double division(int a, int b) {
           return a / b;
       }
   }
   ```


</br>

2. ##### 테스트 클래스를 만들자

   Caculator 클래스에 커서를 놓고 마우스 오른쪽 클릭 -> [GoTo -> Test] -> [Create New Test] 

   Create New Test를 클릭하면 저런 창이 뜹니다. 테스트 코드를 작성하고자 하는 메서드를 선택

   OK를 누르면 디렉토리를 선택하는 창이 나오는데, com(test) 를 선택한다. (그 이유는 하단에 [참고] 를 참고하세요.)

   ![Create Test](https://github.com/taeiim/Android-Study/blob/master/study/week03/Img_AndroidTesting/img_createTest.png)


</br>

[참고] com(androidTest) 라고 표시된 디렉토리는 UI테스트를 작성하는 곳이고, 

​          com(test) 라고 표시된 디렉토리는 단위 테스트 코드를 작성하는 곳이다. 	

- androidTest : Android Instrumentation 테스트로 안드로이드 에뮬레이터나 실물 디바이스에서 테스트를 수행할 수 있다.

- test : 유닛 테스트가 위치하는 곳으로 여러분의 로컬 머신의 JVM에서 수행되며 에뮬레이터나 실물 디바이스에서 돌릴 수 없다. 즉, 이 테스트로는 `Context`와 같은 안드로이드 클래스에 접근할 수 없다.

  ​

- ![test directory](https://github.com/taeiim/Android-Study/blob/master/study/week03/Img_AndroidTesting/img_app_com(test).png)


</br>


3. ##### 생성된 CaculatorTest 클래스에 테스트 코드 작성

   ```java
   	public class CalculatorTest {
          // 초기화 시 한번 호출되는 메서드
          @Before
          public void setUp() throws Exception {
          }

          // 테스트 완료 시에 호출되는 메서드
          @After
          public void tearDown() throws Exception {
          }

          @Test
          public void add() throws Exception {
              assertEquals(10, Calculator.add(2,8));
          }

          @Test
          public void subtract() throws Exception {
              assertEquals(4, Calculator.subtract(10,6));
          }

          @Test
          public void multiply() throws Exception {
              assertEquals(0, Calculator.multiply(0,10));
              assertEquals(10, Calculator.multiply(1,10));

          }

          @Test
          public void division() throws Exception {
              assertEquals(2.5, Calculator.division(5,2),0);   //세번째 인자: 오차 허용치
              assertEquals(2, Calculator.division(4,2),0);
          }

      }
   ```

*    문법적으로 함수명 한글도 가능.

     </br>

4. 각 함수나 클래스 앞에 있는 실행버튼을 클릭해 테스트를 수행한다. 

   하단에 테스트 결과가 뜬다. 




</br>

</br>



## 4. 마무리

#### 소감





#### 출처

##### 책

- 안드로이드 개발 레벨업 교과서 (쯔쯔이 슌스케 외 5명 지음) 
- 될 때까지 안드로이드 (오준석 지음) - 30장.1. 단위테스트 부분 참고 

##### 웹사이트

- http://junit.sourceforge.net/javadoc/org/junit/Assert.html


- http://cocomo.tistory.com/346
- https://www.slideshare.net/youngeunchoi12/springcamp2014-35347399
- ​

