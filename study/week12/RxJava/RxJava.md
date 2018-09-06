# RxJava



## 1. 리액티브 프로그래밍과 RxJava

### 1.1 리액티브 프로그래밍이란?

- pull 방식이 아닌 push 방식

  - 예를 들어, 전국 매장의 매출액 정보를 실시간으로 집계한다고 할 때, 기존에는 각 매장의 변화 상황을 데이터베이스에서 가져(pull)와야한다. 하지만, 리액티브 프로그래밍서는 데이터의 변화가 발생했을 때 변경이 발생한 곳에서 새로운 데이터를 보내(push 방식)준다.

- 함수형 프로그래밍의 지원을 받는다.

- - 우리가 아는 콜백이나 옵서버 패턴을 넘어 RxJava기반의 리액티브 프로그래밍이 되려면 함수형 프로그래밍이 필요
  - 25p 콜백이나 옵서퍼 패턴은 옵서버가 1개이거나 단일 스레드 환경에서는 문제가 없지만 멀티 스레드 환경에서는 많은 주의가 필요하다. (대표적 예 : 데드락, 동기화 문제) 
  - 함수형 프로그램은 부수 효과가 없다. (부수효과: 같은 자원에 여러 스레드가 경쟁조건에 빠지게 되었을 때 예측할 수 없는 잘못된 결과가 나오는 현상.)
    - 한 두 개의 스레드가 있을 때는 정상 동작하다가 수십 수백 개의 스레드가 동시에 단일 자원에 접근하면 계산 결과가 꼬이고 디버깅하다가 매우 어렵다.
  - 함수형 프로그래밍은 부수 효과가 없는 순수 함수를 지향한다 ——> 왜쥬?  .  따라서, 멀티스레드 환경에서도 안전하다.

- 한마디로, “함수형 프로그래밍 도구를 활용한 비동기 프로그래밍”

  ​

### 1.2 RxJava 맛보기

```

```

- Observable 클래스 : 리액티브 프로그래밍이 Observable에서 시작

- just() 함수 : 가장 단순한 Observable 선언 방식.  Ex) Observable.just(“Hello!”, “RxJava2!!”).subscribe(System.out::println);

- subscribe() 함수 : Observable을 구독. Observable은 subscribe() 함수를 호출해야 비로소 변화한 데이터를 구독자에게 발행한다.

  - just()함수만 호출하면 데이터를 발행하지 않음.


-   반드시 데이터를 수신할 구독자가 subscribe()를 호출해야 Observable에서 데이터가 발행된다.

- System.out::println  

  - data -> System.out.println(data)

  ​



### 1.3 RxJava 소개, 특징

- RxJava는 함수형 프로그래밍

  - 함수형 프로그래밍은 부수효과가 없는 순수 함수를 지향하므로 스레드에 안전하다.


- 자바는 함수형 언어가 아니므로 RxJava 라이브러리는 순수 함수로 작성된 리액티브 연산자를 제공.
  - 이 리액티브 연산자 덕분이 RxJava는 리액티브 프로그래밍이 되는 것
  - 리액티브 연산자를 활용하면 목적을 달성할 수 있는 도구인 ‘함수형 프로그래밍’ 방식으로 ‘스레드에 안전한 비동기 프로그램’을 작성할 수 있음

- RxJava가 어려운 이유

  - 함수형 연산자를 어떻게 호출해야 하는지 모르기 때문


- “RxJava는 안드로이드 개발자 사이에서 가장 화제다. 유일한 단점은 처음에 배우기 어렵다는 것이다.”


- 권장하는 학습순서

 ​1. Observable 클래스 명확하게 이해 (특히 뜨거운(Hot) Observable과 차가운(Cold) Observable의 개념을 꼭 이해해야 함) 

 ​2. 간단한 예제로 map(), filter(), reduce, flatMap() 함수의 사용법을 익힙니다.

 ​3. 생성 연산자, 결합 연산자, 변환 연산자 등 카테고리별 주요 함수를 공부

 ​4. 스케줄러의 의미를 배우고 subscribeOn()과 observeOn() 함수의 차이를 알아둡니다.

 ​5. 그 밖의 디버깅, 흐름 제어 함수를 익힙니다.

 ​2장(Observable) => 3장(map-filter-reduce) => 4장(주요 연산자) => 5장(RxJava 때문에 달라지는 비동기 프로그래밍, 스케줄러) => 6장(RxAndroid) => 7장(디버깅, 예외처리)

- 자바 8을 사용하지 않아도 됨. 자바 8에서 제공하는 Consumer, Predicate, Function과 같은 함수형 인터페이스를 자체 구현했으므로 자바 6 이상이면 동작.

  - But, 8의 람다 표현식과 함수 레퍼런스를 활용하는 코드는 가독성을 좋게함. (람다식 & 함수 레퍼런스 공부)

  ​

### 1.4 마블 다이어그램

- 마블 다이어그램은 RxJava를 이해하는 핵심 도구
- 책 정독하면 이해됨
- RxJava는 마블 다이어그램으로 배운다고 해도 과언이 아니다.







## 2. Observable

- RxJava는 Observable에서 시작해 Observable로 끝난다고 해도 과언이 아닐 정도로 중요한 개념.

- RxJava 1.x 에서는 Observable, Single 클래스

  - 2.x 에서는 Observable, Maybe, Flowable 클래스 (상황에 맞게 세분화해 구분해 사용)

- ​

### 2.1 Observable 클래스

- Observable은 옵서버(observer) 패턴을 구현

- - 옵서버 패턴은 객체의 상태 변화를 관찰하는 관찰자(옵서버) 목록을 객체에 등록
  - 그리고 상태 변화가 있을 때마다 메서드를 호출하여 객체가 직접 목록의 각 옵서버에게 변화를 알려준다.
  - 라이브사이클은 존재하지 않으며, 보통 단일 함수를 통해 변화만 알린다.

- Observable은 무슨 뜻?

- - 직관적으로   => 관찰자(Observer)가 관찰하는 대상
  - 조금 부족해! => Observed라는 단어가 관찰을 통해서 얻은 결과를 의미한다면, Observable은 현재는 관찰되지 않았지만 이론을 통해서 앞으로 관찰할 가능성을 의미한다.

- 옵서버 패턴의 대표적인 예 ) 버튼을 누르면 버튼에 미리 등록해 둔 onClick() 메서드를 호출해 원하는 처리를 하는 것 

- RxJava의 Observable은 세가지의 알림을 구독자에게 전달

- - onNext  : Observable이 데이터의 발행을 알린다. 기존의 옵서버 패턴과 같음.
  - onComplete : 모든 데이터의 발행을 완료했음을 알린다. onComplete이벤트는 단 한번만 발생하며, 발생한 후에는 더 이상 onNext 이벤트가 발생해선 안 된다.
  - onError : 에러 발생을 알림. onError 이벤트가 발생하면 이후에 onNext 및 onComplete 이벤트가 발생하지 않는다. 즉, Observable의 실행을 종료.

- Observable 클래스에 많은 수의 함수 존재 (Observable을 생성하는 팩토리 함수, 중간 결과를 처리하는 함수, 디버그 및 예외 함수가 모두 포함)

- Observable을 생성할 때는 직접 인스턴스를 만들지 않고 정적 팩토리 함수를 호출.

- - 1.x 기본 팩토리 함수 : create(), just(), from()
  - 2.x 추가 팩토리 함수 : fromArray(), fromIterable(), fromCallable(), fromFuture(), fromPublisher()
  - 기타 팩토리 함수 : interval(), range(), timer(), defer()

- ​

### 2.2 정적 팩토리 함수

#### 2.2.1 just()

- 인자로 넣은 데이터를 차례로 발행하려고 Observable을 생성

- 1~10개의 인자를 넣을 수 있음. (단, 타입은 모두 같아야함)

  ​

#### 2.2.2 subscribe() 함수 / Disposable 객체

##### 1. subscibe() 함수

- 내가 동작시키기 원하는 것을 사전에 정의해둔 다음 실제 그것이 실행되는 시점을 조절 => subscribe()

- just() 등의 팩토리 함수로 데이터 흐름을 정의한 후, subscribe()를 호출해야 실제로 데이터를 발행함.

- 주요 원형(4가지)

  - 특징 : 모두 Disposable 인터페이스의 객체를 리턴함.

  | 원형                                       | 특징                                       |
  | ---------------------------------------- | ---------------------------------------- |
  | Disposable subscribe()                   | 인자없음, onNext와 onComplete 이벤트를 무시하고 onError 이벤트가 발생했을 때만 exception throw함. ==> 테스트하거나 디버깅할 때 활용 |
  | Disposable subscribe(Consumer<? super T> onNext) | 인자가 1개 있는 오버로딩은 onNext 이벤트를 처리 /    이때도 onError 발생하면 exception을 던짐 |
  | Disposable subscribe (Consumer<? super T> onNext, Consumer<? super java.lang.Throwable> onError) | onNext와 onError 이벤트 처리                   |
  | Disposable subscibe(Consumer<? super T> onNext, Consumer<? super java.lang.Throwable> onError, Action OnComplete) | onNext, onError, onComplete 이벤트 모두 처리    |

- - ​

##### 2. Disposable 객체

- void dispose() / boolean isDisposed()  => 2개 함수만 존재

- dispose()

  - Observable에게 더 이상 데이터를 발행하지 않도록 구독을 해지하는 함수

  - Observable이 onComplete 알림을 보냈을 때, 자동으로 dispose()를 호출해

    Observable과 구독자의 관계를 끊는다. => 따로 dispose() 호출 X

- isDisposed()

  - Observable이 데이터를 발행하지 않는지 (구독을 해지했는지) 확인하는 함수
  - 구독을 해지했으면 -> true



#### 2.2.3 create() 함수

- onNext, onCreate, onError 같은 알림을 개발자가 직접 호출해야함.
  - 반면에) just() 함수는 데이터를 인자로 넣으면 자동으로 알림 이벤트가 발생
- (RxJava의 javadoc에 따르면)  create()는 RxJava에 익숙한 사용자만 활용하도록 권고
  - 다른 팩토리 함수를 활용하면 같은 효과를 낼 수 있음



#### 2.2.4 fromXXX() 계열 함수

- 단일 데이터가 아닐 때 fromXXX() 계열 함수 사용 
  - just(), create() 는 단일 데이터를 다뤘음.
- RxJava2에서 from() 함수 세분화 됨.
  - RxJava 1.x에서는 from(), fromCallable() 만 사용
  - from() 을 배열, 반복자, 비동기 계산 등에 모두 사용하다 보니 모호함이 있어 세분화함.



#### 2.2.4.1 fromArray() 함수

- 배열이 들어있는 데이터를 처리할 때 사용
  - Observable.fromArray(arr)
- RxJava에서 int 배열을 인식시키려면 Integer[] 로 변환해야 함.



#### 2.2.4.2 fromIterable() 함수

- Observable을 만드는 다른 방법은 Iterable 인터페이스를 구현한 클래스에서 Observable 객체를 생성하는 것.
- Iterable< E > 인터페이스 구현하는 대표적인 클래스
  - ArrayList(List), ArrayBlockingQueue(BlockingQueue), HashSet(Set), LinkedList, Stack, TreeSet, Vector 등..

```java
Observable.fromIterable(arrayList);
```



#### 2.2.4.2 fromCallable() 함수

#### 

#### 2.2.4.2 fromFuture() 함수



#### 2.2.4.2 fromPublisher() 함수





### 2.2 Single 클래스

- Observable의 특수한 형태 (1.x 버전부터 존재)

- Observable 클래스는 데이터를 무한하게 발행할 수 있지만, Single 클래스는 오직 1개의 데이터만 발행하도록 한정

- - 보통 결과과 유일한 서버 API를 호출할 때 유용하게 사용

- 데이터 하나가 발행과 동시에 종료

- - 라이프사이클 관점에서 onNext() / onComplete() 함수가 onSuccess() 로 통합
  - onSuccess(T value) / onError() 함수로 구성



#### 2.2.1 Single 객체 생성 가장 쉬운 방법 - just()

Single 객체를 생성할 때, 가장 간단한 방법은 just() 함수를 사용하는 것이다.

```
Single.just(“Hello Single”);
```



#### 2.2.2 Observable에서 Single 클래스 사용 

- Single은 Observable의 특수한 형태이므로 Observable에서 변환할 수 있다.
- Observable 클래스에서 Single 클래스 사용



#### 2.2.3 Single 클래스의 올바른 사용방법

just() 에 여러개의 값을 넣으면 어떻게 될까~?

```
Observable.just("one","error").single("default item");
```

- 에러 발생!!
- 두번째 값을 발행하면서 onNext 이벤트가 발생할 때 에러가 발생됨
  - (IllegalArgumentException: Sequence contains more than one element!)



### 2.3 Maybe 클래스

- RxJava2에 처음 도입된 Observable의 또 다른 특수형태
  - RxJava1.X 에는 없었음.
- Single 클래스와 마찬가지로 최대 데이터 하나를 가질 수 있지만 데이터 발행 없이 바로 데이터 발생을 완료할 수 있다.
  - Single : 1개 완료 / Maybe : 0 or 1 완료
- 생성방법
  - Maybe 클래스를 통해 생성할 수 있지만,
  - 보통 Observable의 특정 연산자를 통해 생성할 때가 많음
    - elementAt(), firstElement(), flatMapMaybe(), lastElement(), reduce(), singleElement()  함수 등



### 2.4 뜨거운 VS. 차가운 Observable

- Observable 2가지로 나뉨 ==> 뜨거운 Observable / 차가운 Observable

- 차가운 -> 뜨거운 변환 가능 (Subject 객체를 만들거나 ConnectableObservable 클래스 활용)

  ​

#### 2.4.1 차가운 Observable

- Observable을 선언하고 just() 함수를 호출해도 옵서버가 subscribe() 함수를 호출하여 구독하지 않으면 데이터를 발행하지 않는다.  ==> 게으른 접근법
  - 예시) 웹 요청, 데이터베이스 쿼리와 읽기 등 / 내가 원하는 URL이나 데이터를 지정하면 그때부터 서버나 데이터베이스에 요청을 보내고 결과를 받아옴
- 구독자가 구독하면 준비된 데이터를 처음부터 발행.
- 지금까지 우리가 다룬거, 앞으로도 별도의 언급이 없으면 차가운 Observable 



#### 2.4.2 뜨거운 Observable

- 구독자가 존재 여부와 관계없이 데이터를 발행함. => 여러 구독자를 고려할 수 있다.
- 단, 구독자로서는 Observable에서 발행하는 데이터를 모두 수신할 것으로 보장할 수 없다.
- 구독한 시점부터 Observable에서 발행한 값을 받는다.
  - 예시) 마우스 이벤트, 키보드 이벤트, 시스템 이벤트, 센서 데이터, 주식 가격 등 
- 주의할점 ) 배압을 고려해야 함.
  - 배압은 Observable에서 데이터를 발행하는 속도와 구독자가 처리하는 속도의 차이가 클 때 발생
  - Flowable이라는 특화 클래스에서 배압을 처리



### 2.5 Subject 클래스

Subject 클래스는 **차가운 Observable을 뜨거운 Observable로 변환**해준다.

#### 2.5.1 Subject 클래스의 특성

- Observable의 속성과 구독자의 속성이 모두 있다.
  - **Observable 처럼**) 데이터 발행 가능
  - **구독자 처럼** ) 발행된 데이터를 바로 처리 가능



#### 2.5.2 주요 Subject 클래스

주요 Subject 클래스에는 AsyncSubject / BehaviorSubject / PublishSubject / ReplaySubject 등이 있다.

#### 2.5.2.1 AsyncSubject 클래스

#### 2.5.2.2 BehaviorSubject 클래스

#### 2.5.2.3 PublishSubject 클래스

#### 2.5.2.4 ReplaySubject 클래스





### 2.6 ConnectableObservable 클래스







## 3. 리액티브 연산자 - 기초

### 3.1 리액티브 연산자 특징

- 다양한 연산자 함수가 존재
  - 하지만, 모두 알아야 리액티브 프로그래밍을 할 수 있는건 아니다
    - 필수 연산자의 개념을 알면 나머지는 필수 연산자에서 파생된 연사자이므로 이해할 수 있기 때문
- 언어 특성과 크게 연관이 없다.

  - ReactiveX는 자바 뿐만 아니라 자바스크립트, 닷넷, 스칼라, 클로저, 스위프트의 리액티브 연산자 목록 함께 제공
  - RxJava에 익숙해지면 다른 프로그래밍 언어에서도 리액티브 프로그래밍을 쉽게 익힐 수 있음.



### 3.2 리액티브 연산자의 종류

| 연산자 종류                   | 무엇                                       | Ex                                       |
| ------------------------ | ---------------------------------------- | ---------------------------------------- |
| 생성(Creating) 연산자         | Observable, Single 클래스 등으로 데이터의 흐름을 만들어 내는 함수 | create(), just(), fromArray(), interval(), range(), timer() 등 |
| 변환(Transforming) 연산자     | 입력을 받아서 원하는 출력을 내는 전통적인 의미의 함수           | map(), flatmap() 등                       |
| 필터(Filter) 연산자           | 입력 데이터 중 원하는 데이터만 걸러냄                    | filter(), first(), take() 등              |
| 합성(Combining) 연산자        | 여러 Observable 조합                         |                                          |
| 오류처리(Error Handling) 연산자 |                                          |                                          |
| 유틸리티(Utility) 연산자        |                                          |                                          |
| 조건(Conditional) 연산자      |                                          |                                          |
| 수학과 집합형 연산자              |                                          |                                          |
| 배압(Back pressure) 연산자    |                                          |                                          |

- 2017년 5월 등록된 연산자 개수: 400개 이상



### 3.3 map() 함수

- 입력값을 어떤 함수에 넣어서 원하는 값으로 변환하는 함수. 

- - String을 String으로 변환할 수도 있고, String을 Integer나 다른 객체로도 변환할 수 있음

- .map(ball -> ball + “*”);

- - map 원형 : public final <R> Observable<R> map(Function<? super T, ? extends R> maper)

  - - 즉 map 함수 인자 ‘ Function<? super T, ?extends R> mapper’ 에  ‘ball->ball+”*” ‘ 들어간거

    - Function 인터페이스를 이용해 분리

    - - Function<String, String> getStar = ball -> ball + “*” ;
      - .map(getStar)    // 원래는 .map(ball -> ball + “*”); 이거 였음
      - 나중에 코드가 길어질수록 Function으로 빼서 사용하면 좋을듯 (내생각)

- map() 핵심

- - 내가 원하는 값을 ‘어떤 함수’ 에 넣는 것

  - - 어떤 함수 : Function 인터페이스 객체 / 람다 표현식

  - 원하는 함수를 정의할 수 있느냐가 관건



### 3.4 flatMap() 함수

- map()을 좀 더 발전시킨 함수

- 결과가 Observable로 나옴

- flatMap() => 일대다 or 일대일 Observable 함수

- - map() => 일대일 함수
  - RxJava에서 여러 개의 데이터를 발행하는 방법은 Observable 밖에 없음 (배압(back pressure)을 고려하면 Observable 대신에 Flowable)

- ​

### 3.5 filter() 함수

- Observable에서 원하는 데이터만 걸러내는 역할

- - 필요없는 데이터는 제거하고 오직 관심 있는 데이터만 filter() 함수를 통과하게 됨.

- Predicate를 인자로 넣음 (Predicate - 진위 판별이라는 뜻, boolean 값을 리턴하는 특수한 함수형 인터페이스)

- - Map()은 Function 객체를 인자로 넣음
  - 람다를 사용하면 Function인지 Predicate인지 신경쓰지 않고 동일하게 코딩 할 수 있는 장점 (구분은 컴파일러가~)

- Observable<Integer> source = Observable.fromArray(data).filter(number -> number % 2 ==0 );

- filter()와 비슷한 함수들

- - first(default) : Observable의 첫 번째 값을 필터. 값이 없이 완료되면 기본값 리턴.

  - last(default) : 마지막 값

  - take(N) : 최초 N 개 값만 가져옴.

  - takeLast(N) : 마지막 N 개 값만 필터함.

  - skip(N) : 최초 N 값을 건너뜀.

  - skipLast(N) : 마지막 N개 값을 건너뜀.

  - 가장 유용한 함수는 take()

    ​

### 3.6 reduce() 함수

- 상황에 따라 발행된 데이터를 취합하여 어떤 결과를 만들어낼 때

- - 발행한 데이터를 모두 사용하여 어떤 최종 결과 데이터를 합성할 때

- Maybe<String> source = Observable.fromArray(balls).reduce((ball1,ball2) -> ball2 + ”(“ + ball1 + “)” );

- - Observable<String> 이 아니라 Maybe<String>

  - - Why? : reduce()함수를 호출하면 인자로 넘긴 람다 표현식에 의해 결과 없이 완료될 수도 있음.
    - 따라서 Observable이 아니라 Maybe 객체로 리턴

- reduce(BiFunction<T,T,T> reducer)  => 원형

- - BiFunction 인터페이스를 인자로 활용 (Function X)

  - - BiFunction이란?   입력 인자로 2개의 값을 받는 함수형 인터페이스
    - 람다에서 인자의 개수가 2개 이상일 때는, 괄호로 인자의 목록을 명시적으로 표현해줘야 함.  Ex)   (ball1,ball2) -> ball2 + ”(“ + ball1 + “)” 

  - BiFunction<String, String, String> : 인자1, 인자2, 리턴 타입 모두 String

- ​

## 4. 리액티브 연산자 - 활용

### 4.1 연산자 분류

연산자 종류가 많아 카테고리 별로 나눔 (ReactiveX 홈페이지 기준)

|           |                                          |
| --------- | ---------------------------------------- |
| 생성 연산자    | just(), fromXXX(), create(), interval(), range(), timer(), intervalRange(), defer(), repeat() |
| 변환 연산자    | map(), flatMap(), concatMap(), switchMap), groupBy(), scan(), buffer(), window() |
| 필터 연산자    | filter(), take(), skip(), distinct()     |
| 결합 연산자    | zip(), combineLatest(), Merge(), concat() |
| 조건 연산자    | amb(), takeUntil(), skipUntil(), all()   |
| 에러 처리 연산자 | onErrorReturn(), onErrorResumeNext(), retry(), retryUntil() |
| 기타 연산자    | subscribe(), subscribeOn(), observeOn(), reduce(), count() |



### 4.2 생성 연산자

생성 연산자의 역할은 **데이터의 흐름을 만드는 것**이다.

간단하게 **Observable** (Observable, Single, Maybe 객체 등)을 만드는 것

#### 4.2.1 interval()



#### 4.2.2 timer()



#### 4.2.3 range()



#### 4.2.4 intervalRange()



#### 4.2.5 defer()



#### 4.2.6 repeat()





### 4.3 변환 연산자

변환 연산자는 **만들어진 데이터 흐름**을 **원하는 대로 변형**할 수 있다.

기본이 되는 함수(map(), flatMap())와 비교하여 **어떻게 다른지 그 차이점**을 기억하는게 좋음. 실무에 도움이됨



#### 4.3.1 concatMap()



#### 4.3.2 switchMap()



#### 4.3.3 groupBy()



#### 4.3.4 scan()

#### 





### 4.4 결합 연산자

결합 연산자는 **여러개의 Observable을 조합**하여 활용하는 연산자이다.

다수의 Observable을 하나로 합하는 방법을 제공한다.

#### 4.4.1 zip()



#### 4.4.2 combineLatest()



#### 4.4.3 merge()



#### 4.4.4 concat()





### 4.5 조건 연산자

조건 연산자는 **Observable의 흐름을 제어하는 역할**을 한다.

필터 연산자는 발행된 값을 채택하느냐 기각하느냐 여부에 초점을 맞춘다면,  조건 연산자는 지금까지의 흐름을 어떻게 제어할 것인지에 초점을 맞춘다.

#### 4.5.1 amb()



#### 4.5.2 takeUntil()



#### 4.5.3 skipUntil()



#### 4.5.4 all()





### 4.6 수학 및 기타 연산자

#### 4.6.1 수학 함수



#### 4.6.2 delay()



#### 4.6.3 timeInterval()







## 5. 스케줄러

### 5.1 스케줄러 개념

스케줄러는 RxJava의 핵심 요소로 Observable 만큼 중요하다.

- 스케줄러는 **스레드를 지정**할 수 있게 해줌.

- 지금까지 자바로 비동기 프로그래밍을 할 때 자바 스레드를 만들면서 경쟁 조건이나 synchronized 키워드를 생각했다면 스케줄러의 코드를 보고 놀랄 것!! (간결한 코드로 다시 탄생!!)

- subscribeOn() 함수와 onserveOn() 함수를 모두 지정하면,  [**Observable에서 데이터 흐름이 발행**하는 스레드]와  [**처리된 결과를 구독자에게 발행**하는 스레드]를 분리할 수 있다.

- **subscribeOn()** 함수만 호출하면 Observable의 모든 흐름이 **동일한 스레드**에서 실행됨

- 스케줄러를 별도로 지정하지 않으면 현재(main) 스레드에서 동작을 실행

  ​

### 5.2 스케줄러의 종류

#### 5.2.1 뉴 스레드 스케줄러

#### 5.2.2 계산 스케줄러

#### 5.2.3 IO 스케줄러

#### 5.2.4 트램펄린 스케줄러

#### 5.2.5 싱글 스레드 스케줄러

#### 5.2.6 Executor 변환 스케줄러



### 5.3 스케줄러 활용 - 콜백 지옥 벗어나기

### 5.4 onserveOn() 함수의 활용

- RxJava 스케줄러의 핵심은 결국 제공되는 **스케줄러의 종류를 선택**한 후 **subscribeOn() 과 observeOn() 함수를 호출**하는 것.

- subscribeOn()  =>  Observable에서 구독자가 subscribe() 함수를 호출했을 때, 데이터 흐름을 발행하는 스레드를 지정

- - 처음 지정한 스레드를 고정시키므로 다시 subscribeOn()  함수를 호출해도 무시. (하지만, observeOn() 함수는 다름)

- observeOn()     =>  처리된 결과를 구독자에게 전달하는 스레드 지정.

- - 여러번 호출할 수 있으며, 호출되면 그 다음부터 동작하는 스레드를 바꿀 수 있다.

- 전통적인 스레드 프로그래밍에서는 일일이 스레드를 만들어야 하고 스레드가 늘어날 때마다 동기화하는 것이 매우 부담스럽기 때문에 이러한 로직을 구현하는 것이 매우 힘듬.

- - 하지만, onserveOn() 함수는 스레드 변경이 쉬우므로 활용할 수 있는 범위가 넓음

