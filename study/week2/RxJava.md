# 부상하고 있는 Rx Java

## 목차

1. Rx Java 등장 배경
   1.1. 동기/비동기 프로그래밍
   1.2. 명령형 프로그래밍
   1.3. 함수형 반응 프로그래밍
   1.4. 예제
2. Rx Java의 특징
   2.1. 장점
   2.2. 단점
   2.3. 예제 코드, 코드 비교
3. 사용되는 곳
4. 조금 더 자세히!!
   4.1. 이해를 돕기위해 알아 둘 것
           4.1.1. observer pattern
           4.1.2. Iterator pattern
           4.1.3. Functional programming 
           (1.3. 함수형 반응 프로그래밍)
           4.1.4. MVVM model
           4.1.5. Dataflow programming
   4.2. 기본적인 빌딩 블록
           4.2.1. Observable
           4.2.2. Subscriber
   4.3. 자주 사용하는 함수
5. 코드 읽어보기
   5.1 ~~구구단 코드~~
6. 레퍼런스
7. ~~더 나아가 보기~~

 

### 1. Rx Java의 등장배경

Rx Java는 비동기 프로그래밍 문제를 해결해주기 때문에 최근 2년간 급 부상 중입니다. 그렇다면 앞으로는 어떻게 Rx Java가 비동기 프로그래밍을 해결해 줄 수 있는지 알아보도록 하겠습니다. 

우선 안드로이드 스튜디오에서 기존에 사용했던 언어는 Java입니다. Java, C#, C++ 등의 개체 지향 프로그래밍 언어는 대부분 명령형 프로그래밍을 지원하기 위해 디자인 되었습니다. 하지만 지금 공부하려는 Rx Java는 함수적 반응 프로그래밍의 방법이며 리액티브 프로그래밍(Reactive Programming)으로 비동기 데이터 흐름에 기반을 둔 패러다임입니다. 

#### 1.1. 동기/비동기 프로그래밍

+ 동기 프로그래밍 : 작업을 하나 수행할 때, 그 작업이 끝나기 전까지 다른 작업을 수행하지 못하는 방식의 프로그래밍을 뜻합니다. 
+ 비동기 프로그래밍 : 작업을 요청 한 후에, 그 결과가 나오지 않았더라도 다른 작업을 수행할 수 있는 방식의 프로그래밍을 뜻합니다.

#### 1.2. 명령형 프로그래밍

명령형 프로그래밍은 사용자가 원하는 동작을 하나씩 하나씩 프로그래밍 하는 것입니다. 그렇기 때문에 매우 구체적이고 자세하고 동작 하나하나의 코드를 작성해야합니다. 이는 '어떤 방법'으로 무언가를 할지를 중심으로 하는 프로그래밍입니다. 	

명령형 프로그래밍이란 그렇다면 명령형 프로그래밍에서는 비동기 처리를 할 수 없다는 의문이 들 수도 있습니다. 비동기 처리도 가능하다고 하지만 명령형 프로그래밍의 경우에는 상태 변경의 중요도가 높고 작업을 실행하는 순서의 중요도 역시도 높기 때문에 쉽게 상태를 변경하기가 어렵습니다. 따라서, 명령형 프로그래밍은 **비동처리가 어렵다는** 결론을 낼 수가 있습니다.

#### 1.3. 함수형 반응 프로그래밍

명령형이 아닌 선언형으로 코드를 작성하는 프로그래밍 방법이 바로 함수형 반응 프로그램입니다.  

함수형 반응 프로그래밍은 상태 변경의 중요도와 실행 순서의 중요도가 낮습니다. 그렇기 때문에 절차적으로 작업이 수행되지 않고 비동기 처리에 수월할 수 있습니다. 

#### 1.4. 예제 

+ 명령형 프로그래밍 <양의 정수와 짝수와 소수 출력>

  ```c#
  Using System;
  using System.Collections.Generic;
  namespace _01_Even_and_Prime
  {
      class Program
      {
          static void Main(string[] args)
          {
              int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
              Print("All:", numbers);
              Print("Evens:", GetEvens(numbers));
              Print("Primes:", GetPrimes(numbers));
          }
          public static void Print(string label, int[] source)
          {
              Console.WriteLine(label);
              foreach (int item in source)
                  Console.Write(" {0}", item);
              Console.WriteLine("");
          }
          static int[] GetEvens(int[] numbers)
          {
              List result = new List();
              if (numbers != null)
              {
                  foreach (int number in numbers)
                  {
                      if ((number % 2) == 0)
                          result.Add(number);
                  }
              }
              return result.ToArray();
          }
      }
  }
  ```

  Console Result
  All : 1 2 3 4 5 6 7 8 9 10
  Evens : 2 4 6 8 10

  ---

  짝수는 GetEvens 함수에서 독립적으로 구현되어있고 재사용되는 코드가 없다. 어떻게 문제를 해결할지를 구체적으로 기술하고 있습니다.

+ 함수형 반응 프로그래밍 <짝수 구하기>

  ``` F#
  [1 .. 15]
  |> List.sortBy(fun x -> -x)
  |> List.filter(fun x -> x % 2 = 0)
  |> List.iter(printfn "%d")
  ```

  ‘|>’는 파이프라인 연산자이며 List의 sortBy, filter, iter 함수는 함수를 인자로 받을 수 있는 고차 함수이다. 인자로 전달할 함수는 람다 표현식을 나타내는 fun을 이용해 ‘x -> -x’와 ‘x -> x % 2’를 전달하고 있습니다.

  ~~높은 추상화 제공~~

### 2. Rx Java의 특징

#### 2.1. 장점

우선 명령형 프로그래밍을 더 이상 개선하기 힘든 여러가지 현상을 개선할 수 있다는 점에서 Rx Java를 눈여겨 보아야 한다고 합니다. 예를 들어보자면, 모바일 사용자들이 원하는 즉각적인 반응을 원합니다. 하지만 안드로이드 내부에서는 여러 쓰레드를 옮겨 다니면서 실행해야 하기때문에 즉각적인 반응이 어려운 경우가 많이 있습니다. 하지만 Rx Java의 경우에는 쓰레드 간의 전환이 쉽기 때문에 이에 좋은 대체 법이 될 수 있습니다. 

또한, Operator들의 조합으로 상황을 해결하기 때문에 연쇄적인 API 호출에서 Rx Java의 유연함을 보여 줄 수 있습니다. 편하게 흐름을 파악 할 수 있으며, 구현 역시도 Java에서 Async Task와 비교한다면 훨씬 간결한 것을 볼 수 있습니다.

#### 2.2. 단점

Rx Java는 우선 진입장벽이 높은 편입니다. Rx Java 뿐만이 아니라, Java나 C++등의 명령형 프로그래밍에 익숙해져있는 우리에게 함수형 반응 프로그래밍 자체가 익숙하지 않을 수 밖에 없습니다.	

배우는 것이 어렵기 때문에 학습비용이 많이 들어 큰 익숙하지 않은 사람들이 진행하는 프로젝트에 Rx Java는 적합하지 않을 수 있습니다.

~~현업에서 일하시는 개발자 님들도 개념을 완벽히 이해하시는데 6개월이 걸리셨다는 소리가...~~

#### 2.3. 예제 (코드 비교)

가정된 상황 : 가상의 서버에 User API를 호출한 뒤에, 그 사용자의 ID값을 다시 서번에 보내서 유효한 사용자인지 아닌지 알아야하는 상황입니다.

\<Async Task를 사용한 연쇄된 네트워크 통신\>

```java
public class GetUserAsyncTask extends AsyncTask {
      doInBackground() {
          //… network logic
          return user;
      }
 
      onPostExecute(User user) {
           new ValidUserAsyncTask(user).execute();
      }
}
 
public class ValidUserAsyncTask extends AsyncTask {
      public ValidUserAsyncTask(User user) { }
 
      doInBackground() {
           //… more network logic
           return isUserValid; 
      }
 
      onPostExecute(boolean isUserValid) {
        isUserValid ? doSomething : doSomethingElse
      }
}
```

\<Rx Java를 사용한 연쇄된 네트워크 통신\>

```
UserAPI.getUser()
    .subscribeOn(Schedulers.io())
    .flatMap(user ->  VerifyAPI.checkValid(user))
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(isUserValid -> isUserValid ? doSomething : doSomethingElse);
```



### 3. 사용되는 곳

+ **연쇄 API 호출**
+ 평행 API 호출
+ 테스팅
+ **UI 로직 핸들링**
+ **비동기 통신을 순차적으로 보내고 싶을때 (A작업이 끝나고 B가 시작됐으면 좋겠다.)**
+ **콜백을 받았는데 받는 화면이 사라져서 null에러를 뿜으면서 죽을 때**
+ **핸들러랑 콜백 지옥에 빠져서 디버깅도 힘들고 햇깔릴 때**
+ **두개의 비동기 처리가 완료된 후에 결과 값을 합쳐서 하나로 만들고 싶을 때**
+ **버튼을 연타로 눌러서 이벤트가 중복실행 될 때**
+ **쓰레드 관리가 힘들 때**

### 4. 조금 더 자세히!!

#### 4.1. 이해를 돕기 위해 알아둘 것

##### 4.1.1. observer pattern

옵저버 패턴은 한 객체의 상태가 바뀌면 그 객체에 의존하는 다른 객체들에게 연락이 가고 자동으로 정보가 갱신되는 1:N 의 관계를 정의합니다.

4.1.2. ~~Iterator pattern~~
4.1.3. Functional programming 
(1.3. 함수형 반응 프로그래밍)
4.1.4. MVVM model
4.1.5. ~~Dataflow programming~~

####4.2. 기본적인 빌딩 블록 

반응적인 코드의 기본적인 빌딩 블록은 **Observable**들과 **Subscriber**들입니다. 사실 가장 작은 빌딩 블록은 **Observer**이지만 실제로 Subscriber를 더 자주 사용합니다. 

여기서 Observable은 아이템을 발행하는 곳에 사용되고 Subscriber은 이 아이템들을 소비하는데 사용됩니다.

##### 4.2.1. Observable

데이터 처리를 실행(시작?)하는 부분입니다.

##### 4.2.2. Subscriber

Observable에 속해있는 Subcrber마다 Subcirber.onNext( ), Subscriber.onComplete( ), 또는 Subscriber.onError( )가 뒤따릅니다. 

Observable은 아까 말했듯이이는 성공적인 완료(Subscriber. onComplete( )) 또는 에러(Subscriber.onEror( ))에 인해서 종료가 되는데 그 각각의 상태가 위의 메서드에 속해있습니다. 

스스로 이해해본 바로는 이 부분이 메서드 처럼 데이터를 가공하고 처리하는 부분입니다. 이게 가능한 이유는 연쇄적으로 메서드를 부를 수 있기 때문이다. Observable이랑 Subscriber이랑 헷갈릴 필요가 없는게 Observable 안에 Subscriber이 존재하기 때문입니다. 그리고 기 결과들을 수신받는 부분이 Observer이다.

Observable을 데이터베이스 쿼리라고 생각하고 Subscriber은 그 결과를 가지고 화면에 보여준다고 생각 할 수 있다. 또 다른 예로는 Observable은 화면의 클릭이고 Subscriber은 그에 대한 반응이라고 볼 수 있다. 

#### 4.3. 자주 사용하는 함수

+ **map( )**
+ **flatMap( )**
+ **just( )**
+ **merge( )**
+ **filter( )**
+ **action( )**

http://pluu.github.io/blog/rx/2015/04/29/rxjava/

http://rxmarbles.com/#map

### 5. 코드 읽어보기

#### 가장 기본적인 코드

```
Observable<String> myObservable = Observable.create(
  new Observable.OnSubscribe<String>() {
    @Override
    public void call(Subscriber<? super String> sub) {
      sub.onNext(“Hello, world!”);
      sub.onCompleted();
    }
  }
);
```

Observable

```
Subscriber<String> mySubscriber = new Subscriber<String>() {
  @Override
    public void onNext(String s) { System.out.println(s); }
  @Override
    public void onCompleted() { }
  @Override
    public void onError(Throwable e) { }
};
```

Subscriber

```
myObservable.subscribe(mySubscriber);
// Outputs “Hello, world!”
```

합췌!!!♥♪♥♩♥♪

### 6. 레퍼런스

https://academy.realm.io/kr/posts/kau-felipe-lima-adopting-rxjava-airbnb-android/

http://kimjihyok.info/2017/06/02/asynctask%EC%99%80-%EB%B9%84%EA%B5%90%ED%95%B4%EC%84%9C-%EB%8B%B9%EC%8B%A0%EC%9D%B4-rxjava%EB%A5%BC-%EB%8B%B9%EC%9E%A5-%EC%8D%A8%EC%95%BC%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0/

http://alltogetherwe.tistory.com/16

https://medium.com/@LIP/rxjava-29cfb3ceb4ca

https://poqw.github.io/RxJava2_1/

http://flowarc.tistory.com/entry/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%98%B5%EC%A0%80%EB%B2%84-%ED%8C%A8%ED%84%B4Observer-Pattern