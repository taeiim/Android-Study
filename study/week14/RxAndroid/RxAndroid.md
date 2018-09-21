# RxAndroid

> 안드로이드의 RxJava 활용

> 작성자 : 박태임
>
> Present Time : 2018-09-20 (THU)



## 1. RxAndroid 소개

기존 안드로이드 개발에서 가장 어려움을 겪는 문제 중 하나는 복잡한 스레드 사용이다. 복잡한 스레드 사용으로 발생하는 문제는 다음과 같다.

- 안드로이드의 비동기 처리 및 에러 핸들링
- 수많은 핸들러와 콜백 때문에 발생하는 디버깅 문제
- 2개의 비동기 처리 후 결과를 하나로 합성하는 작업
- 이벤트 중복 실행

숙련도 높은 개발자도 멀티 스레드 환경에서 발생하는 이러한 문제를 디버깅하는 데 많은 시간을 투자한다.

RxAndroid 는 습득하기 어려운 부분도 있지만 기존 안드로이드 개발과 비교했을 때 장점이 많다. 다음과 같은 특징을 통해 앞 문제를 해결하는 데 도움을 준다.

- 간단한 코드로 복잡한 병행(concurrency) 프로그래밍을 할 수 있다.
- 비동기 구조에서 에러를 다루기 쉽다.
- 함수형 프로그래밍 기법도 부분적으로 적용할 수 있다.


</br>


### 1.1 리액티브 라이브러리와 API

RxAndroid는 기본적으로 RxJava의 리액티브 라이브러리를 이용한다. 안드로이드에서 이용하는 리액티브 API와 라이브러리는 상당히 많다. 

###### * 안드로드에서 사용할 수 있는 리액티브 API와 라이브러리 )  RxLifecycle, RxBinding, RxLocation, RxFit, RxWear, RxImagePicker, ReactiveNetwork, RxDataBinding 등

</br>

### 1.2 Android Studio Setting

gradle의 dependencies 부분에 RxAndroid 라이브러리를 추가한다. 

###### 참고 ) RxAndroid는 RxJava에 대한 의존성이 있어 RxJava를 추가하지 않아도 되지만, 최신 버전의 RxJava를 사용하려면 명시해 주는 것이 좋다. 

```groovy
dependencies{
	// RxJava & RxAndroid
	implementation 'io.reactivex.rxjava2:rxjava:2.2.2'
	implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
	
	// RxLifecycle
	implementation 'com.trello.rxlifecycle2:rxlifecycle-android:2.2.2'
	implementation 'com.trello.rxlifecycle2:rxlifecycle:2.2.2'
	implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.2.2'
}
```



</br></br>



## 2. RxAndroid 기본 

RxAndroid의 기본 개념은 RxJava와 동일하다. RxJava의 구조에 안드로이드의 각 컴포넌트를 사용할 수 있도록 변경해 놓은 것이다. 따라서 RxAndroid의 구성 요소는 다음처럼 RxJava의 구성 요소와 같다.

- `Observable` : 비즈니스 로직을 이용해 데이터를 발행한다.
- `구독자` : Observable에서 발행한 데이터를 구독한다.
- `스케줄러` : 스케줄러를 통해서 Observable, 구독자가 어느 스레드에서 실행될지 결정할 수 있다. 



이러한 과정을 간단한 코드로 나타내면 다음과 같다.

Observable과 구독자가 연결되면 스케줄러에서 각 요소가 사용할 스레드를 결정하는 기본적인 구조이다. **Observable이 실행되는 스레드는 subscribeOn() 함수**에서 설정하고 **처리된 결과를 onserveOn() 함수**에 설정된 스레드로 보내 최종 처리한다.

```java
// 1. Observable 생성
Observable.create()
  .subscribe();  // 2. 구독자 이용

  // 3. 스케줄러 이용
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
```

</br>

### 2.1 RxAndroid에서 제공하는 스케줄러

| 스케줄러 이름                        | 설명                        |
| ------------------------------ | ------------------------- |
| AndroidSchedulers.mainThread() | 안드로이드의 UI 스레드에서 동작하는 스케줄러 |
| HandlerScheduler.from(handler) | 특정 핸들러에 의존하여 동작하는 스케줄러    |

안드로이드의 UI스레드는 메인스레드를 뜻한다. 

스레드와 Handler에 관련한 자세한 내용은 다음 글을 참고 [[스터디 1회차 Thread , Handler , Looper 참고]](study/week01/Thread%20%2C%20Handler%20%2C%20Looper.md) 

</br>

### 2.2 TextView에 텍스트 출력해보기 - 예제

#### 2.2.1 create() 사용

```java
Observer<String> observer = new DisposableObserver<String>() {
  @Override
  public void onNext(String s){
    textView.setText(s);
  }
  @Override
  public void onError(Throwable e) { }
  @Override
  public void onComplete() { }  
}

Observable.create(new ObservableOnSubscribe<String>(){
  @Override
  public void subscribe(ObservableEmitter<String> e) throws Exception{
    e.onNext("I'm student.");
    e.onComplete();
  }
}).subscribe(observer);
```

`Observable.create()` 로 `Observable` 을 생성해 'I'm student.'(출력할 텍스트) 를 입력 받고 `subscribe()` 함수 안 `onNext()` 함수에 전달한다. 

onNext() 함수는 전달된 문자를 텍스트뷰에 업데이트 하도록 정의되어 있으므로, 

실제 구독자를 `subscribe(observer)` 함수를 통해 등록하고 호출하면 'I'm student.' 를 텍스트뷰에 표시한다.



#### 2.2.2 create() 사용 - 람다표현식으로 변형

```java
Observable.<String> create(s->{
  s.onNext("I'm smart!");
  s.onComplete();
}).subscribe(o -> textView.setText(o));
```

위 2.2.1과 비교하면 콜백 함수를 람다 표현식으로 바꾸며 데이터의 흐름이 명확해져 가독성이 좋아졌다.

전달자는 명확한 단어로 변경할 수도 있지만 기본적으로 이니셜을 많이 사용한다.



#### 2.2.3 just() 사용

```java
Observable.just("I'm hungry.")
  .subscribe(textView::setText);
```

`메서드 레퍼런스` 를 이용하여 Observable의 생성 코드를 단순하게 했다. 



###### 참고)  `Observable` 의 생성 방법은 워낙 다양하고 개발자의 성향에 따라 선택의 기준이 달라질 수 있다. 



</br></br>

### 2.3 제어 흐름

쉽고 간단해 보이는 문법도 정확하게 구조를 알고 깊이 이해한 후 사용해야 한다.

조건문, 순환문은 코드에서 가장 많은 비중을 차지한다. 리액티브 프로그래밍의 세계에서도 예외는 아니지만 구현 방법이 다르다.



</br>

### 2.4 RxLifecycle 라이브러리

RxAndroid에는 `RxLifecycle` 라이브러리를 제공한다. 

안드로이드의 `Activity` 와 `Fragment` 의 라이프 사이클을 RxJava에서 사용할 수 있게 한다.

안드로이드와 UI 라이프 사이클을 대체한다기보다 **구독할 때 발생할 수 있는 메모리 누수를 방지하기 위해 사용**한다. **완료하지 못한 구독을 자동으로 해제(dispose)** 한다. 

</br>

#### 2.4.1 RxLifecycle 라이프 사이클 컴포넌트

`RxLifecycle`  라이브러리는 안드로이드의 라이프 사이클에 맞게 `Observable` 을 관리할 수 있는 컴포넌트를 제공한다.

| 컴포넌트                      | 설명                                       |
| ------------------------- | ---------------------------------------- |
| RxActivity                | **액티비티**에 대응                             |
| RxDialogFragment          | Native/Support 라이브러리인 **DialogFragment** 에 대응 |
| RxFragment                | Native/Support 라이브러리인 **Fragment** 에 대응  |
| RxPreferenceFragment      | **PreferenceFragment**에 대응               |
| RxAppCompatActivity       | Support 라이브러리인 **DialogFragment**에 대응    |
| RxAppCompatDialogFragment | Support 라이브러리인 **AppCompatDialogFragment**에 대응 |
| RxFragmentActivity        | Support 라이브러리인 **FragmentActivity**에 대응  |

</br>

#### 2.4.2 RxLifecycle 사용해보기 - 예제

- build.gradle 파일의 depencies에 추가

  ```groovy
  dependencies{
  	// RxLifecycle
  	implementation 'com.trello.rxlifecycle2:rxlifecycle-android:2.2.2'
  	implementation 'com.trello.rxlifecycle2:rxlifecycle:2.2.2'
  	implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.2.2'
  }
  ```



```

```





</br>

### 2.5 UI 이벤트 처리

#### 2.5.1 이벤트 리스너



</br></br>



## 3. RxAndroid 활용

뷰의 구성, 스케줄러, REST API 등 안드로이드의 기본 구현을 리액티브 프로그램으로 변경 (RxAndroid의 실제 활용 방법 알아보기)

### 3.1 리액티브 RecyclerView

#### 3.1.1 RecyclerView 클래스

RecyclerView 클래스의 구조와 동작 방식

`RecyclerView` 클래스에는 서브 클래스인 `LayoutManaget` 가 있다. 이를 이용하여 뷰를 정의하고, Adapter 클래스를 이용하여 데이터 셋에 맞는 ViewHolder 클래스를 구현할 수 있다. 이외에도 RecyclerView 클래스는 뷰를 제어하는 ItemDecoration, ItemAnimation 라는 서브 클래스를 둔다. Adapter 클래스와 상호 연결되며, ViewHolder 클래스에서 데이터와 뷰를 받아 이를 재사용할 수 있게 한다. 



##### RecyclerView 클래스와 함께 사용하는 주요 클래스

| 클래스 이름         | 설명                           |
| -------------- | ---------------------------- |
| Adapter        | 데이터 세트의 아이템을 나타내는 뷰를 생성      |
| ViewHolder     | 재활용 뷰에 대한 모든 서브 뷰를 저장        |
| LayoutManager  | 뷰에 있는 아이템을 배치하고 관리           |
| ItemDecoration | 아이템을 꾸미는 서브 뷰를 제어            |
| ItemAnimation  | 아이템을 추가, 정렬, 제거할 때의 애니메이션 효과 |

3.1.2 와 3.1.3 에서 주요 클래스인 **Adapter**와 **LayoutManger**를 하나씩 더 자세히 살펴본다. 

#### 3.1.2 Adapter 클래스

#### 3.1.3 LayoutManager 클래스

#### 3.1.4 설치된 앱 리스트 나열하기 - 예제

</br>

### 3.2 안드로이드 스레드를 대체하는 RxAndroid

</br>

### 3.3 REST API를 활용한 네트워크 프로그래밍

</br>

### 3.4 메모리 누수 (Memory Leak)

</br>



## 마치며

### 소감



### Reference

[[도서] RxJava 프로그래밍 (유동한, 박정준 지음)](https://book.naver.com/bookdb/book_detail.nhn?bid=12495967)

