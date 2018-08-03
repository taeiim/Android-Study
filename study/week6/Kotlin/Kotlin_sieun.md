------

> 발표자 : 송시은
>
> 발표주제 : 코틀린
>
> 발표날짜 : 2018.07.13

------



[TOC]

# 0.참고문서

Kotlin IN ACTION [드미트리 제메로프, 스베트라나 이사코바 지음 / 오현석 옮김]

깡샘의 코틀린 프로그래밍 [강성윤 지음]

차세대 안드로이드 개발자를 위한 커니의 코틀린 [김태호 지음]

[inlining] https://stackoverflow.com/questions/3924995/what-is-method-inlining

[앤터프라이즈 애플리케이션] https://scroogy.atlassian.net/wiki/spaces/SPRING/pages/8847424

[트레이드오프] https://zetawiki.com/wiki/트레이드오프



# 1. Kotlin을 알아보자

### 1.0 소개

> 코틀린은 자바 플랫폼에서 돌아가는 새로운 프로그래밍 언어
>
> 코틀린은 간결하고 실용적이며, 자바 코드와의 상호운용성을 중시
>
> 대표적으로 서버 개발, 안드로이드 앱 개발 등의 분야에서 코틀린을 씀



### 1.1 코틀린의 주요 특성

#### 1.1.1 대상 플랫폼

코틀린이 가장 일반적으로 사용되는 영역은 서버상의 코드(특히 웹 애플리케이션의 백엔드), 안드로이드 디바이스에서 실행되는 모바일 애플리케이션이다. 하지만 코틀린은 다른 환경에서도 잘 작동한다.

- 인텔의 멀티OS 엔진을 사용하면 코틀린을 iOS디바이스에서 실행할 수 있다.

- 데스크탑 애플리케이션을 작성하고 싶다면 코틀린과 토네이도FX, 자바FX 등을 함께 사용할 수 있다.

- 자바뿐 아니라 자바스크립트도 코틀린을 컴파일할 수 있다 => 코틀린 코드를 브라우저나 노드에서 실행가능

- 2017년 3월 1일에 발표된 코틀린 1.1부터는 자바스크립트를 공식적으로 지원한다.

  

#### 1.1.2 정적 타입 지정 언어

자바와 마찬가지로 코틀린도 **정적 타입 지정* 언어다. 

동적 타입 지정 언어에서는 타입과 관계없이 모든 값을 변수에 넣을 수 있고, 메소드나 필드 접근에 대한 검증이 생행 시점에 일어나며, 그에 따라 코드가 더 짧아지고 데이터 구조를 더 유연하게 생성하고 사용할 수 있다. 하지만 반대로 이름을 잘못 입력하는 등의 실수도 컴파일 시 걸러내지 못하고 실행 시점에 오류가 발생한다.

한편 자바와 달리 코틀린에서는 모든 변수의 타입을 프로그래머가 직접 명시할 필요가 없다. 대부분의 경우 코틀린 컴파일러가 문맥으로부터 변수 타입을 자동으로 유추할 수 있기 때문에 프로그래머는 타입 선언을 생략해도 된다.

> var x = 1

여기서는 변수를 정의하면서 정수 값으로 초기화한다. 코틀린은 이 변수의 타입이 Int임을 자동으로 알아낸다. 이렇게 컴파일러가 문맥을 고려해 변수 타입을 결정하는 이런 기능을 타입 추론이라고 부른다.



##### 정적 타입 지정의 장점

- **성능** 

  실행 시점에 어떤 메소드를 호출할지 알아내는 과정이 필요 없으므로 메소드 호출이 더 빠르다.

- **신뢰성** 

  컴파일러가 프로그램의 정확성을 검증하기 때문에 실행 시 프로그램이 오류로 중단될 가능성이 더 적어진다.

- **유지 보수성** 

  코드에서 다루는 객체가 어떤 타입에 속하는지 알 수 있기 때문에 처음 보는 코드를 다룰 때도 더 쉽다.

- **도구 지원** 

  정적 타입 지정을 활용하면 더 안전하게 리팩토링 할 수 있고, 도구는 더 정확한 코드 완성 기능을 제공할 수 있으며, IDE의 다른 지원 기능도 더 잘 만들 수 있다.

  

코틀린의 타입 시스템을 살펴보면 클래스, 인터페이스, 제네릭스는 모두 자바와 비슷하게 작동한다. 따라서 자바에 대해 아는 내용을 코틀린에서도 쉽게 적용할 수 있다. 하지만 몇 가지 새로운 점이 있다.

- 코틀린이 널이 될 수 있는 타입을 지원한다. 널이 될 수 있는 타입을 지원함에 따라 컴파일 시점에 널 포인터 예외가 발생할 수 있는지 여부를 검사할 수 있어서 좀 더 프로그램의 신뢰성을 높일 수 있다. 

- 코틀린의 타입 시스템에 있는 다른 새로운 내용으로는 함수 타입에 대한 지원을 들 수 있다.

------

***정적 타입 지정** 모든 프로그램 구성 요소의 타입을 컴파일 시점에 알 수 있고 프로그램 안에서 객체의 필드나 메소드를 사용할 때 마다 컴파일러가 타입을 검증해준다.



#### 1.1.3 함수형 프로그래밍과 객체지향 프로그래밍

##### 함수형 프로그래밍의 핵심 개념

- ***일급 시민인 함수**

함수를 일반 값처럼 다룰 수 있다. 함수를 변수에 저장할 수 있고, 함수를 인자로 다른 함수에 전달할 수 있으며, 함수에서 새로운 함수를 만들어서 반환할 수 있다.

- **불변성**

함수형 프로그래밍에서는 일단 만들어지고 나면 내부 상태가 절대로 바뀌지 않는 불변 객체를 사용해 프로그램을 작성한다.

- **부수 효과 없음**

함수형 프로그래밍에서는 입력이 같으면 항상 같은 출력을 내놓고 다른 객체의 상태를 변경하지 않으며, 함수 외부나 다른 바깥 환경과 상호작용하지 않는 **순수 함수*를 사용한다.



##### 함수형 스타일로 프로그램을 작성할 때의 유익한 점

1. 간결성

   함수형 코드는 그에 상응하는 명령형 코드에 비해 더 간결하며 우아하다. 함수를 값처럼 활용할 수 있으면 더 강력한 추상화를 할 수 있고 강력한 추상화를 사용해 코드 중복을 막을 수 있다.

2. 다중 스레드를 사용해도 안전

   다중 스레드 프로그램에서는 적절한 동기화 없이 같은 데이터를 여러 스레드가 변경하는 경우 가장 많은 문제가 생긴다. 불변 데이터 구조를 사용하고 순수 함수를 그 데이터 구조에 적용한다면 다중 스레드 환경에서 같은 데이터를 여러 스레드가 변경할 수 없다. 따라서 복잡한 동기화를 적용하지 않아도 된다.

3. 테스트하기 쉽다.

   부수 효과가 있는 함수는 그 함수를 실행할 때 필요한 전체 환경을 구성하는 준비 코드가 따로 필요하지만, 순수 함수는 그런 준비 코드 없이 독립적으로 테스트할 수 있다.

   

코틀린은 처음부터 함수형 프로그래밍을 풍부하게 지원해 왔다. 그런 지원은 다음과 같다

- 함수 타입을 지원함에 따라 어떤 함수가 다른 함수를 파라미터로 받거나 함수가 새로운 함수를 반환할 수 있다.
- 람다 식을 지원함에 따라 번거로운 준비 코드를 작성하지 않아도 코드 블록을 쉽게 정의하고 여기저기 전달할 수 있다.
- 데이터 클래스는 불변적인 값 객체를 간편하게 만들 수 있는 구문을 제공한다.
- 코틀린 표준 라이브러리는 객체와 컬렉션을 함수형 스타일로 다룰 수 있는 API를 제공한다.



코틀린은 함수형 스타일로 프로그램을 짤 수 있게 지원하지만 함수형 프로그래밍 스타일을 강제하지는 않는다. 명령형 방식이 더 적합한 경우라면 함수형 프로그래밍으로 번거롭게 코드를 작성할 필요 없이 직접 변경 가능한 데이터와 부수 효과를 활용하는 함수를 사용해도 된다.

------

***일급 시민인 함수(일급 객체)** 함수가 프로그램의 최상위 구성요소라는 의미. 객체지향 프로그래밍에서는 클래스가 일급 객체입니다. 

***순수 함수** 부수효과가 발생하지 않는 함수. 부수효과가 발생하지 않는다는 것은 같은 인수를 전달해서 함수를 호출하면 항상 같은 결괏값을 반환한다는 의미.



#### 1.1.4 무료 오픈소스

코틀린 언어와 컴파일러, 라이브러리 및 코틀린과 관련된 모든 도구는 오픈소스며, 어떤 목적에든 무료로 사용할 수 있다. 코틀린은 아파치2 라이선스하에 제공된다. 

코틀린 애플리케이션을 개발하고 싶은 경우 인텔리J 아이디어 커뮤니티 에디션, 안드로이드 스튜디오, 이클립스 같은 오픈소스 IDE를 활용할 수 있다.



### 1.2 코틀린 응용

#### 1.2.2 코틀린 안드로이드 프로그래밍

전형적인 모바일 애플리케이션은 전형적인 **엔터프라이즈 애플리케이션*과 아주 많이 다르다. 모바일 애플리케이션은 엔터프라이즈 애플리케이션보다 더 작고 기존 코드 기반과 새 코드를 통합할 필요도 더 적다. 또 모바일 애플리케이션은 보통 더 다양한 디바이스에 대해 서비스의 신뢰성을 보장하면서 더 빠르게 배포할 필요가 있다.

코틀린 언어의 특성과 안드로이드 프레임워크의 특별한 컴파일러 플러그인 지원을 조합하면 안드로이드 애플리케이션 개발의 생산성을 더 높이고 개발의 즐거움을 더할 수 있다.

컨트롤에 리스너를 추가하거나 레이아웃 요소를 필드와 바인딩하는 등의 흔한 안드로이드 개발 작업을 훨씬 더 적은 코드로 달성할 수 있고, 때로는 전혀 코드를 작성하지 않고 그렇게 할 수도 있다(컴파일러가 자동으로 필요한코드를 생성).

코틀린 팀이 만든 안코 라이브러리를 사용하면 수많은 안드로이드 API에 대한 코틀린 어댑터를 제공받을 수 있어서 안드로이드 프로그래밍 경험을 더 개선할 수 있다.



##### 코틀린을 사용하면 얻을 수 있는 이익

1. 애플리케이션의 신뢰성이 더 높아진다

   안드로이드 앱을 개발해본 사람이라면 '프로세스가 중단됨(Process Has Stopped)' 대화상자를 본 일이 많을 것이다. 그 대화상자는 애플리케이션에서 처리되지 않은 예외(주로 NullPointerException)가 발생한 경우에 표시된다. 코틀린 타입 시스템은 null 값을 정확히 추적하며 널 포인터로 인해 생기는 문제를 줄여준다. 자바에서 NullPointerException을 일으키는 유형의 코드는 대부분 코틀린에서는 컴파일도 되지 않는다. 따라서 개발 중인 애플리케이션이 사용자에게 전달되기 전에 널 포인터 관련 오류를 수정할 수 있다.

2. 코틀린은 자바 6과 완전히 호환된다

   따라서 호환성과 관련한 새로운 문제를 야기하지 않는다. 코틀린이 제공하는 새롭고 멋진 언어 기능을 사용해 프로그램을 작성해도 사용자의 디바이스에서 그 프로그램을 실행할 수 있다. 사용자의 디바이스에 최신 안드로이드가 탑재되지 않은 경우에도 말이다.

3. 코틀린을 사용하더라도 성능 측면에서 아무 손해가 없다

   코틀린 컴파일러가 생성한 바이트코드는 일반적인 자바 코드와 똑같이 효율적으로 실행된다. 코틀린의 런타임 시스템은 상당히 작기 때문에 컴파일 후 패키징한 애플리케이션 크기도 자바 애플리케이션에 비해 그리 많이 늘어나지 않는다. 또한 대부분의 코틀린 표준 라이브러리 함수는 인자로 받은 람다 함수를 **인라이닝* 한다. 따라서 람다를 사용해도 새로운 객체가 만들어지지 않으므로 객체 증가로 인해 가비지 컬렉션이 늘어나서 프로그램이 자주 멈추는 일도 없다.

------

***엔터프라이즈 애플리케이션** 기업과 조직의 비즈니스를 처리해주는 시스템을 의미

***인라이닝** Java Just-In-Time 컴파일러가 수행하는 최적화



### 1.3 코틀린의 철학

#### 1.3.1 실용성

1. 코틀린은 실제 문제를 해결하기 위해 만들어진 실용적인 언어다. 

   코틀린 설계는대규모 시스템을 개발해본 다년간의 IT업계 경험을 바탕으로 이뤄졌으며, 수많은 소프트웨어 개발자들의 사용에 잘 들어맞을 수 있게 주의 깊게 언어 특성을 선택했다. 더 나아가 젯브레인스나 코틀린 커뮤니티 내부의 개발자들이 다년간 코틀린 초기 버전을 사용하면서 전달한 피드백이 현재 발표된 최종 코틀린 버전에 반영돼 있다.

2. 코틀린은 연구를 위한 언어가 아니다.

   코틀린은 다른 프로그래밍 언어가 채택한 이미 성공적으로 검증된 해법과 기능에 의존한다.

3. 코틀린은 어느 특정 프로그래밍 스타일이나 패러다임을 사용할 것을 강제로 요구하지 않는다.

4. 실용성에 있어 코틀린의 다른 측면은 도구를 강조한다는 점이다.

   코틀린의 경우 인텔리J 아이디어의 개발과 컴파일러의 개발이 맞물려 이뤄져 왔다. 그리고 코틀린 언어의 특성은 항상 도구의 활용을 염두에 두고 설계돼 왔다.

5. 코틀린의 여러 특성을 배울 때도 IDE의 코틀린 언어 지원이 중요한 역할을 한다.

   흔히 쓰이지만 더 간결한 구조로 바꿀 수 있는 대부분의 코드 패턴을 도구가 자동으로 감지해서 수정하라고 제안한다. 이런 자동 수정 안내를 살펴보면서 코틀린 언어의 특성을 잘 이해하면 자신의 코드에 그런 특성을 적용하는 방법을 배울 수 있다.



#### 1.3.2 간결성

코틀린은 게터, 세터, 생성자 파라미터를 필드에 대입하기 위한 로직 등 자바에 존재하는 여러 가지 번거로운 준비 코드를 코틀린은 묵시적으로 제공하기 때문에 코틀린 소스코드는 그런 준비 코드로 인해 지저분해지는 일이 없다.

다른 최신 언어와 마찬가지로 코틀린은 기능이 다양한 표준 라이브러리를 제공하기 때문에 반복되거나 길어질 수 있는 코드를 라이브러리 함수 호출로 대치할 수 있다. 코틀린은 람다를 지원하기 때문에 작은 코드 블록을 라이브러리 함수에 쉽게 전달할 수 있다. 따라서 일반적인 기능을 라이브러리 안에 캡슐화하고 작업에 따라 달라져야 하는 개별적인 내용을 사용자가 작성한 코드 안에 남겨둘 수 있다.

- java 코드

```java
package com.example.kotlinlab.ch1;
import java.util.ArrayList;
class User {
    private int no;
    private String name;
    private String email;
    public int getNo() {
        return no;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

public class JavaClass {
    public static void main(String[] args) {
        ArrayList<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setNo(1);
        user1.setname("kkang");
        user1.setEmail("a@a.com");
        list.add(user1);
        
        User user2 = new User();
        user2.setNo(1);
        user2.setName("kkang");
        user2.setEmail("a@a.com");
        list.add(user2);
        
        //ArrayList 항목 개수 출력
        System.out.println("List Size : " + list.size());
        //user1 객체와 user2 객체의 데이터가 동일한 데이터인지 판단
        if(user1.getNo() == user2.getNo() && 
           user1.getName().equals(user2.getName()) && 
           user1.getEmail().equals(user2.getEmail())) {
            System.out.println("user1 == user2");
        }else {
            System.out.println("user1 != user2");
        }
    }
}
```



- kotlin 코드

```kotlin
package com.example.kotlinlab.ch1
data class User(val no: Int, val name: String, val email: String)

fun main(args: Array<String>) {
    val list = ArrayList<User>()
    val user1 = User(1, "kkang", "a@a.com")
    list.add(user1)
    
    val user2 = User(1, "kkang", "a@a.com")
    list.add(user2)
    println("List Size ${list.size}")
    
    if(user1.equals(user2)) {
        println("user1 == user2")
    }else {
        println("user1 != user2")
    }
}
```



#### 1.3.3 안전성

일반적으로 프로그래밍 언어가 안전하다는 말은 프로그램에서 발생할 수 있는 오류 중에서 일부 유형의 오류를 프로그램 설계가 원천적으로 방지해준다는 뜻이다. 더 큰 안전성을 얻기 위해서는 프로그램에 더 많은 정보를 덧붙여야 하므로 생산성이 하락하는 것을 감수해야 하며 안전성과 생산성 사이에는 **트레이드오프* 관계가 성립한다.

코틀린을 JVM에서 실행한다는 사실은 이미 상당한 안전성을 보장할 수 있다는 뜻이다. 예를 들어 JVM을 사용하면 메모리 안전성을 보장하고, 버퍼 오버플로를 방지하며, 동적으로 할당한 메모리를 잘못 사용함으로 인해 발생할 수 있는 다양한 문제를 예방할 수 있다.

코틀린은 거기서 한걸음 더 나아가 실행 시점에 오류를 발생시키는 대신 컴파일 시점 섬사를 통해 오류를 더 많이 방지해준다. 가장 중요한 내용으로 코틀린은 프로그램의 NullPointerException이 발생할 수 있는 연산을 사용하는 코드를 금지한다. 어떤 타입이 널이 될 수 있는지 여부를 표시하기 위해서는 ? 한 글자만 추가하면 된다

```kotlin
val s1: String? = null	//Null이 될 수 있음
val s2: String = ""		//Null이 될 수 없음
```



코틀린이 방지해주는 다른 예외로는 ClassCastException이 있다. 어떤 객체를 다른 타입으로 캐스트하기 전에 타입을 미리 검사하지 않으면 ClassCastException이 발생할 수도 있다. 코틀린에서는 타입 검사와 캐스트가 한 연산자에 의뤄진다. 어떤 객체의 타입을 검사했고 그 객체가 그 타입에 속한다면 해당 타입의 메소드나 필드 등의 멤버를 별도의 캐스트 없이 사용할 수 있다.

```kotlin
if (value is String)				//타입을 검사한다
	println(value.toUpperCase())	//해당 타입의 메소드를 사용한다
```

------

***트레이드오프** 이율 배반의 관계, 한쪽을 추구하면 다른 쪽은 희생되어야 하는 관계



#### 1.3.4 상호운용성

1. 코틀린은 기존 라이브러리를 그대로 사용할 수 있다. 

   라이브러리가 어떤 API를 제공하던 간에 코틀린에서 그 API를 활용할 수 있다. 자바 메소드를 호출하거나 자바 클래스를 상속하거나 인터페이스를 구현하거나 자바 애노테이션을 코틀린 코드에 적용하는 등의 일이 모두 가능하다.

2. 코틀린의 클래스나 메소드를 일반적인 자바 클래스나 메소드와 똑같이 사용할 수 있다.

   기존 자바 프로젝트에 코틀린을 도입하는 경우 자바를 코틀린으로 변환하는 도구를 코드베이스 안에 있는 자바 클래스에 대해 실행해서 그 클래스를 코틀린 클래스로 변환할 수 있다.

3. 기존 자바 라이브러리를 가능하면 최대한 활용한다.

   예를 들어 코틀린은 자체 컬렉션 라이브러리를 제공하지 않는다. 코틀린은 자바 표준 라이브러리 클래스에 의해 의존한다. 이는 코틀린에서 자바 API를 호출할 때 객체를 감싸거나 변환할 필요가 없고, 자바에서 코틀린 API를 호출할 때도 마찬가지로 아무런 변환이 필요없다는 뜻이다.

4. 코틀린이 제공하는 도구도 다중 언어 프로젝트를 완전히 지원한다.

   코틀린은 자바와 코틀린 소스 파일이 임의로 섞여 있어도 제대로 프로그램을 컴파일할 수 있다. IDE 기능도 언어와 관계없이 제대로 작동한다. 다음과 같은 동작이 가능하다.

   - 자바와 코틀린 소스 파일을 자유롭게 내비게이션 할 수 있다.
   - 여러 언어로 이뤄진 프로젝트를 디버깅하고 서로 다른 언어로 작성된 코드를 언어와 관계없이 한 단계씩 실행할 수 있다.
   - 자바 메소드를 리팩토링해도 그 메소드와 관련 있는 코틀린 코드까지 제대로 변경된다. 역으로 코틀린  메소드를 리팩토링해도 자바 코드까지 모두 자동으로 변경된다.

   

### 1.4 요약

- 코틀린은 타입 추론을 지원하는 정적 타입 지정 언어다. 따라서 소스코드의 정확성과 성능을 보장하면서도 소스코드를 간결하게 유지할 수 있다.
- 코틀린은 객체지향과 함수형 프로그래밍 스타일을 모두 지원한다. 코틀린에서는 일급 시민 함수를 사용해 수준 높은 추상화가 가능하고, 불변 값 지원을 통해 다중 스레드 애플리케이션의 개발과 테스트를 더 쉽게 할 수 있다.
- 코틀린을 서버 애플리케이션 개발에 잘 활용할 수 있다. 코틀린은 기존 자바 프레임워크를 완벽하게 지원하는 한편, HTML 생성기나 영속화 등의 일반적인 작업을 위한 새로운 도구를 제공한다.
- 코틀린을 안드로이드에도 활용할 수 있다. 코틀린의 런타임 라이브러리는 크기가 작고, 코틀린 컴파일러는 안드로이드 API를 특별히 지원한다. 그리고 코틀린의 다양한 라이브러리는 안드로이드에서 흔히 하는 작업에 사용할 수 있으면서 코틀린과 잘 통합될 수 있는 함수를 제공한다.
- 코틀린은 무료며 오픈소스다. 또한 주요 IDE와 빌드 시스템을 완전히 지원한다.
- 코틀린은 실용적이며 안전하고, 간결하며 상호운용성이 좋다. 이는 코틀린을 설계하면서 일반적인 작업에 대해 이미 잘 알려진 해법을 채택하고, NullPointerException과 같이 흔히 발생하는 오류를 방지하며, 읽기 쉽고 간결한 코드를 지원하면서 자바와 아무런 제약 없이 통합될 수 있는 언어를 만드는데 초점을 맞췄다는 뜻이다.



# 2. Kotlin을 배워보자

### 2.1 코틀린 파일정의

#### 2.1.1 일반 파일과 클래스 파일

- 코틀린 프로그램은 확장자가 kt인 파일을 작성하여 개발한다.

- 코틀린 파일은 클래스 파일일 수도 있고 일반 파일일 수도 있다.

  코틀린 자체의 규칙이라기보다는 IDE에서의 편의를 위한 구분이다. 클래스 파일에는 클래스가 꼭 작성되어야 하고, 일반 파일에는 클래스를 작성하지 못한다는 규칙은 없으며, 클래스 파일로 만든 후 클래스가 아닌 함수와 변수 등으로만 파일을 구성해도 상관없고, 일반 파일로 만든 후 그곳에 클래스를 선언해도 상관없다.

  

#### 2.1.2 파일의 구성요소

![https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20파일의%20구성요소.jpg](https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20파일의%20구성요소.jpg)

- 하나의 파일에 패키지, 임포트, 클래스를 선언한 단순 구조이며, 다른 객체지향 언어와 큰 차이가 없다.

- 코틀린 파일에도 패키지를 선언할 수 있는데, 반드시 파일의 첫 줄에 선언해야 한다.

  ![https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20파일의%20구성요소1.jpg](https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20파일의%20구성요소1.jpg)

- 코틀린 파일은 클래스를 사용하지 않고 변수와 함수로만 구성할 수도 있다.

- 코틀린에서는 모든 구성요소를 꼭 클래스로 묶지 않아도 되며, 변수나 함수를 클래스 외부에 선언할 수 있다.

  ![https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20파일의%20구성요소2.jpg](https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20파일의%20구성요소2.jpg)

- 코틀린은 파일의 구성요소에 대한 규칙이 없으며 개발자의 편의에 따라서 변수, 함수, 클래스 등을 자유롭게 정의할 수 있다.

------

Q. 코틀린이 JVM에서 수행된다고 했는데 클래스 선언 없이 변수와 함수를 정의하면 JVM 실행 시 문제가 발생?

A. 코틀린으로 작성한 코드가 그대로 JVM에서 실행되는 게 아니라, JVM에서 실행되도록 적절하게 변형된다. 					파일명에 "Kt"가 추가되는 클래스가 자동으로 만들어진다.



#### 2.1.3 변수 / 함수 임포트

- 클래스 없이 선언된 변수나 함수를 외부에서 이용하려면 클래스처럼 그대로 임포트해서 사용하면 된다.

  ```kotlin
  package com.example.ch3.two.sub
  
  val threeVal = 10
  
  fun threeFun() {
      
  }
  ```

  이 파일의 변수와 함수를 다른 패키지의 파일에서 이용하려면

  ```kotlin
  package com.example.ch3.two
  
  import com.example.ch3.two.sub.threeFun
  import com.example.ch3.two.sub.threeVal
  
  fun main(args: Array<String>) {
      println(threeVal)
      threeFun()
  }
  ```

  이처럼 변수나 함수를 직접 임포트하여 이용할 수 있는 것은 클래스로 묶지 않은 변수와 함수를 최상위 레벨로 관리하기 때문이다. 즉, 패키지 내에 선언된 전역변수나 전역함수처럼 취급한다.



#### 2.1.4 기본 패키지

코틀린에서 다음의 패키지는 별도의 import 구문 없이 사용할 수 있다.

- kotlin.*
- kotlin.annotation.*
- kotlin.collections.* 
- kotlin.comparisons.* (since 1.1)
- kotlin.io.*
- kotlin.ranges.*
- kotlin.sequences.*
- kotlin.text.*
- kotlin.lang.*
- kotlin.jvm.*



#### 2.1.5 이름 변경해서 임포트하기

클래스를 임포트할 때 이름을 바꾸어 다른 이름으로 사용할 수도 있다.

```kotlin
import java.text.SimpleDateFormat as MySimpleDateFormat
```

임포트하고자 하는 클래스는 java.text.SimpleDateFormat이다. as라는 예약어를 이용하여 이름을 MySimpleDateFormat이라는 이름으로 변경했다. 따라서 임포트한 파일 내에서는 이제 MySimpleDateFormat이라는 이름으로 SimpleDateFormat클래스를 이용할 수 있다.



### 2.2 코틀린의 주요 문법

#### 2.2.1 값 및 변수 선언

- 타입을 먼저 적고 그 다음 이름을 적는 자바와 달리, 코틀린에서는 이름을 먼저 적고 타입을 적는다. 타입은 상황에 따라 생략 가능하다.

  ```kotlin
  // String 타입을 갖는 값 a 선언
  val a: String = "foo"
  
  // 할당하는 자료의 타입에 따라 자동으로 타입을 추론하므로 타입을 생략할 수 있다.
  val b = "bar"
  
  // 선언 시 자료를 할당하지 않으면 타입을 꼭 붙여야 한다.
  val c: String
  
  // 자료 할당
  c = "baz"
  
  // 변수 d 선언
  var d: Int = 0
  
  // 변수 값 변경
  d += 1
  ```

- val(value)은 **Assign-once 변수*로 선언하고, var(variable)는 * *Mutable 변수*로 선언한다.

------

***Assign-once 변수** 한 번 초기화하면 더는 변경할 수 없다.

***Mutable 변수** 언제든지 변경 가능 



#### 2.2.2 함수 선언

- 함수는 자바의 메서드에 대응하며, 값 및 변수 선언과 마찬가지로 반환 타입을 뒤에 적는다.

- 코틀린에서는 함수를 선언할 때는 fun이라는 예약어를 사용한다.

  ```kotlin
  fun greet(name: String) : Unit {
      println("Hello, $name!")
  }
  
  fun sum(a: Int, b: Int) : Int {
      return a + b
  }
  
  fun some(a: Int, b: Int): Int = a + b
  // 반환 타입 생략
  fun some(a: Int, b: Int) = a + b
  ```

- 함수의 반환값에 사용하는 Unit은 자바의 void와 유사하게 사용하며 Unit을 반환하는 함수는 생략할 수 있다. 함수의 반환 타입이 선언되지 않았다면 기본으로 Unit이 적용된다.

- 단일 표현 함수는 반환 타입과 중괄호를 생략할 수 있다. 반환 타입을 생략하면 컴파일러가 유추해서 정한다.



#### 2.2.3 클래스 및 인터페이스 선언

클래스와 인터페이스를 선언하는 방법은 자바와 크게 다르지 않다. 단, 클래스의 생성자를 정의하는 방법에는 다소 차이가 있다.

##### 2.2.3.1 생성자

코틀린에서는 생성자를 주 생성자와 보조 생성자로 구분한다.

**주 생성자**

- 클래스 선언 부분에 작성
- 하나의 클래스에 하나의 주 생성자만 정의 가능
- 꼭 작성해야 하는 건 아니며 보조 생성자가 있다면 작성하지 않을 수 있음

**보조 생성자**

- 클래스 몸체에 constructor 예약어로 선언
- 보조 생성자를 선언했으면 주 생성자는 선언하지 않아도 됨

```kotlin
// 주 생성자로 선언
class User1(name: String, age: Int){
    
}
// 보조 생성자로 선언
class User2{
    constructor(name: String, age: Int){}
}

fun main(args: Array<String>){
    val user1 = User1("kkang", 30)
    val user2 = User2("kkang", 30)
}
```



##### 2.2.3.2 자바 VS 코틀린

- 코틀린에서는 파일 내에 파일명과 같은 public 클래스를 강제하지 않는다.
- 코틀린에는 new 연산자가 없다.
- 코틀린의 생성자는 자바의 작성 방법 및 동작 원리가 많이 다르다.
  - constructor 예약어를 이용해 생성자 선언
  - 주 생성자와 보조 생성자로 구분
  - 생성자의 초기화 로직을 위해 init{} 구문을 사용
  - 주 생성자의 매개변수에 val, var를 추가하면 클래스의 **프로퍼티*가 되어 함수에서도 사용 가능
- 코틀린에서는 함수 내의 지역변수를 final로 선언하지 않아도 함수 내에 선언한 Nested 클래스에서 이용
- 코틀린에서는 static이라는 예약어를 제공하지 않음

------

***프로퍼티** 코틀린에서는 클래스의 변수를 프로퍼티라 부른다. var, val로 선언되는 변수들이 프로퍼티이다.



#### 2.2.4 조건문

- if - else문은 자바와 사용 방법이 동일하다.
- when문은 자바의 switch와 동일한 역할을 한다.



#### 2.2.5 반복문

인덱스 기반 for문과 for - each문을 모두 지원하는 자바와 달리 코틀린은 for - each문만 지원한다.



#### 2.2.6 람다 표현식

- 람다식, 또는 람다 함수 프로그래밍 언어에서 사용되는 개념으로 익명 함수를 지칭하는 용어이다.

- 많은 프로그래밍 언어에서 지원하며 함수형 프로그래밍을 목적으로 하지 않더라도 코드의 간결함을 주목적으로 자주 이용한다.

- 람다 함수는 fun과 함수이름을 명시하지 않고 축약형으로 선언한다.

  > 코틀린에서 함수 정의
  >
  > fun 함수이름(매개변수) { 함수내용 }
  >
  > 
  >
  > 람다 함수
  >
  > { 매개변수 -> 함수내용 }

- 람다 함수는  {} 안에 매개변수와 함수 내용을 선언하는 함수로 다음 규칙에 따라 정의한다.

  - 람다 함수는 항상 {}으로 감싸서 표현해야 한다.
  - {} 안에 -> 표시가 있으며 -> 왼쪽은 매개변수, 오른쪽은 함수 내용이다.
  - 매개변수 타입을 선언해야 하며 추론할 수 있을 때는 생략할 수 있다.
  - 함수의 반환값은 함수 내용의 마지막 표현식이다.

  ```kotlin
  // 일반 함수 정의법
  fun sum(x1: Int, x2: Int): Int {
      return x1 + x2
  }
  
  // 람다 함수로 정의
  val sum1 = {x1: Int, x2: Int -> x1 + x2}
  
  fun main(args: Array<String>) {
      val result1 = sum1(10, 20)
  }
  ```

  ```kotlin
  // 람다 함수를 사용하여 리스너 선언
  Button button = ... // 버튼 인스턴스
  
  // 람다 표현식을 사용하여 리스너를 선언합니다.
  button.setOnClickListener((View v) -> doSomething())
  
  // 인자의 타입을 생략할 수 있다.
  button.setOnClickListener(v -> doSomething())
  ```

- 람다 표현식을 사용하면, 함수를 인자로 넘길 수 있는 고차함수에 들어갈 함수형 인자를 쉽게 표현할 수 있다.



#### 2.2.7 인라인 함수

람다 표현식을 사용하여 작성한 함수는 컴파일 과정에서 익명 클래스로 변환된다. 따라서 익명 클래스를 사용하는 코드를 호출할 때마다 매번 새로운 객체가 생성되므로 이러한 코드가 여러번 호출되는 경우 실행 시점의 성능에 영향을 미치게 된다.

인라인 함수를 사용하면, 함수의 매개변수로 받는 함수형 인자의 본체를 해당 인자가 사용되는 부분에 그대로 대입하므로 성능 하락을 방지할 수 있다. 인라인 함수로 선언하려면 함수 선언 앞에 inline 키워드를 추가하면 된다.



# 3. Kotlin을 사용하자

### 3.1 코틀린 안드로이드 익스텐션

#### 3.1.1 **findViewById() 메서드**

- 액티비티나 프래그먼트 등 레이아웃 파일에 선언된 여러개의 뷰로 구성된 와면에서 특정 뷰의 인스턴스를 얻기위해 사용된다.
- 이 메서드에서 반환한 뷰 객체를 잘못된 타입의 뷰로 캐스팅하거나 다른 레이아웃에 선언된 ID를 잘못 사용하면 널 값을 반환한다.
- 화면을 구성하는 뷰의 인스턴스를 얻기 위해 각 컴포넌트의 초기화 시점에 인스턴스가 필요한 뷰의 개수만큼 findViewById() 메서드를 사용해야 한다.
- 복잡한 구조로 구성된 화면을 다루는 경우 뷰 인스턴스를 받는 코드만 몇십 줄을 차지하여 코드의 가독성이 떨어진다.

위와 같은 findViewById의 불편함은 코틀린 안드로이드 익스텐션을 사용하면 말끔히 해결된다.



#### 3.1.2 코틀린 안드로이드 익스텐션 설정

코틀린 안드로이드 익스텐션을 사용하려면 이를 사용할 모듈의 빌드스크립트에 kotlin-android-extensions 플러그인을 적용해야 한다. 이 플러그인을 적용하기 위해 kotlin-android 플러그인도 함께 적용한다.

```java
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'

// 코틀린 안드로이드 익스텐션을 이 모듈이 적용한다.
apply plugin: 'kotlin-android-extensions'

android {
    ...
}

dependencies {
    ...
    compile "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}"
}
```

코틀린 안드로이드 익스텐션은 현재 액티비티, 프래그먼트, 리사이클러뷰에서 사용할 수 있다. 이들 컴포넌트에서 사용하는 레이아웃에 포함된 뷰를 프로퍼티처럼 사용할 수 있도록 지원하며, 각 뷰의 ID를 프로퍼티 이름으로 사용한다. 레이아웃 내 선언된 뷰를 프로퍼티처럼 사용하기 위해, 코틀린 안드로이드 익스텐션을 사용하는 컴포넌트에서는 특별한 import 문을 추가해야 한다.

```kotlin
import kotlinx.android.synthetic.{sourceSet}.{layout}.*

// src/main/res/layout/activity_main.xml 레이아웃을 사용하는 경우
import kotlinx.android.synthetic.main.activity_main.*
```



#### 3.1.3 액티비티에서 사용하기

```kotlin
// activity_main 레이아웃에 있는 뷰를 사용하기 위한 import 문
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 뷰 ID를 사용하여 인스턴스에 바로 접근합니다.
        btn_submit.setOnClickListener {
            tv_message.text = "Hello, " + et_name.text.toString()
        }
    }
}
```



### 3.2 Anko

Anko는 코틀린 언어의 제작사인 젯브레인에서 직접 제작하여 배포하는 코틀린 라이브러리로, 안드로이드 애플리케이션 개발에 유용한 유틸리티 함수를 제공한다. Anko에서는 유틸리티 함수의 성격에 따라 다음과 같이 네 종류의 라이브러리를 제공한다.

- Anko Commons
- Anko Layouts
- Anko SQLite
- Anko Coroutines



#### 3.2.1 Anko Commons

Anko Commons은 안드로이드 애플리케이션을 작성할 때 일반적으로 자주 구현하는 기능을 간편하게 추가할 수 있는 유틸리티 함수를 제공한다. Anko Commons을 사용하려면 이를 사용할 모듈의 빌드 스크립트에 다음과 같이 의존성을 추가하면 된다.

```
// build.gradle
android {
    ...
}

dependencies {
    // Anko Commons 라이브러리를 추가
    compile "org.jetbrains.anko:anko-commons:0.10.2"
    
    // appcompat-v7용 Anko Commons 라이브러리를 추가
    compile "org.jetbrains.anko:anko-appcompat-v7-commons:0.10.2"
    
    // support-v4용 Anko Commons 라이브러리를 추가
    compile "org.jetbrains.anko:anko-commons:0.10.2"
    
    ...
}
```



##### 3.2.1.1 토스트 표시하기

```kotlin
// 다음 코드와 동일한 역할을 한다.
// Toast.makeText(Context, "Hello, Kotlin!", Toast.LENGTH_SHORT).show();
toast("Hello, Kotlin!")

// Toast.makeText(Context, R.string.hello, Toast.LENGTH_SHORT).show();
toast(R.string.hello)
      
// Toast.makeText(Context, "Hello, Kotlin!", Toast.LENGTH_LONG).show();
longtoast("Hello, Kotlin!")
```



##### 3.2.1.2 다이얼로그 생성 및 표시하기

arert() 함수를 사용하면 AlertDialog를 생성할 수 있으며, 토스트와 마찬가지로 Context 클래스 혹은 이를 상속하는 클래스(액티비티, 프래그먼트) 내부에서만 사용할 수 있다.

```kotlin
// 다이얼로그의 제목과 본문을 지정한다.
alert(title = "Message", message = "Let's learn Kotlin!") {
    // AlertDialog.Builder.setPositiveButton()
    positiveButton("Yes") {
        toast("Yay!")
    }
    ...    
}.show()
```

이렇게 Anko Commons 라이브러리는 인텐트, 로그 메시지, 단위 변환등 다양한 곳에 적용할 수 있다.



#### 3.2.2 Anko Layouts

Anko Layouts는 소스 코드로 화면을 구성할 때 유용하게 사용할 수 있는 여러 함수들을 제공하며, 이를 사용하면 XML 레이아웃을 작성하는 것처럼 편리하게 소스코드로도 화면을 구성할 수 있다. Anko Layout을 사용하려면 이를 사용할 모듈의 빌드스크립트에 의존성을 추가하면 된다.

```
android {
    defaultConfig {
        // minSdkVersion이 15로 설정되어 있다.
        minSdkVersion 15
        targetSdkVersion 27
        ...
    }
    ...
}

dependencies {
    // minSdkVersion에 맞추어 Anko Layouts 라이브러리를 추가한다.
    compile "org.jetbrains.anko:anko-sdk15:0.10.2"
    ...
}
```



#####  3.2.2.1 액티비티에서 DSL로 화면 구성하기

```kotlin
class MainActivity : AppcompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // AnkoComponent에 정의된 뷰를 액티비티 화면으로 설정한다.
        MainActivityUI().setContentView(this)
    }
}

// MainActivity에 표시할 화면을 구성한다.
class MainActivityUI : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = ui.apply {
        verticalLayout {
            padding = dip(12)
            
            textView("Enter Login Credentials")
            
            editText {
                hint = "E-mail"
            }
            
            editText {
                hint = "Password"
            }
            
            button("Submit")
        }
    }.view
}
```

추가로, 액티비티에서는 AnkoComponent 없이 직접 액티비티 내에서 DSL을 사용하여 화면을 구성할 수 있다.



#### 3.2.3 Anko SQLite

Anko SQLite는 SQLite 데이터베이스를 다룰 때 유용한 함수를 제공한다.

자세한 정보 >> http://github.com/Kotlin/anko/wiki/Anko-SQLite 



#### 3.2.4 Anko Coroutines

Anko Coroutines은 코틀린 코루틴 라이브러리(kotlinx.coroutines)와 함께 사용할 수 있는 함수를 제공한다.

자세한 정보 >> http://github.com/Kotlin/anko/wiki/Anko-Coroutines



# 4. kotlin을 응용하자 - Todo 앱 만들기

### 4.1 개발환경 설정

include kotlin support 체크박스가 체크된 상태로 새 프로젝트를 만들면 자동으로 코틀린 파일이 만들어 진다.

![https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20개발환경%20설정.PNG](https://github.com/taeiim/Android-Study/blob/master/study/week6/Kotlin/코틀린%20개발환경%20설정.PNG)

체크하지 않고 만들더라도 아래와 같이 설정해주면 된다.

```
// build.gradle(Project ...)
...
dependencies {
        classpath 'com.android.tools.build:gradle:3.1.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
}
...
```

```
// build.gradle(Module: app)
...
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
...

dependencies {
	...
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
	...
}
```

------

코틀린 언어 온라인 커뮤니티

------

- 공식 코틀린 포럼  https://discuss.kotlinlang.org
- Slack 채팅  http://kotlinlang.slack.com (http://kotlinslackin.herokuapp.com 에서 초대)
- Stack Overflow의 코틀린 태그  http://stackoverflow.com/questions/tagged/kotlin
- 코틀린 Reddit  http://www.reddit.com/r/Kotlin
- 페이스북 한국 코틀린 사용자 그룹  www.facebook.com/groups/kotlinkr/
- 한국 코틀린 사용자 슬랙 채팅  http://kotlinkr.slack.com/ (https://kotlinkr.herokuapp.com/ 에서 초대)