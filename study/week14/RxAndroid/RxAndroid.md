# RxAndroid

> 안드로이드의 RxJava 활용

> 작성자 : 박태임
>
> Present Time : 2018-09-20 (THU)



## 1. RxAndroid 소개

기존 안드로이드 개발에서 가장 어려움을 겪는 문제 중 하나는 **복잡한 스레드 사용**이다. 복잡한 스레드 사용으로 발생하는 문제는 다음과 같다.

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

### 2.3 RxLifecycle 라이브러리

RxAndroid에는 `RxLifecycle` 라이브러리를 제공한다. 

안드로이드의 `Activity` 와 `Fragment` 의 라이프 사이클을 RxJava에서 사용할 수 있게 한다.

안드로이드와 UI 라이프 사이클을 대체한다기보다 **구독할 때 발생할 수 있는 메모리 누수를 방지하기 위해 사용**한다. **완료하지 못한 구독을 자동으로 해제(dispose)** 한다. 

</br>

#### 2.3.1 RxLifecycle, 왜 사용하죠?

Observable은 안드로이드의 Context를 복사하여 유지한다. onComplete(), onError() 함수가 호출되면 내부에서 자동으로 unsubscribe() 함수를 호출한다. 

그런데 액티비티가 비정상적으로 종료되면 뷰가 참조하는 액티비티는 종료해도 가비지 컬렉션의 대상이 되지 못한다. 따라서 **메모리 누수**가 발생하게 된다.

이때 이런 문제를 해결하기 위한 방법 중 하나가 **RxLifecycle**을 이용하는 것이다.

</br>

#### 2.3.2 RxLifecycle 라이프 사이클 컴포넌트

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

#### 2.3.3 RxLifecycle 사용해보기 - 예제

- build.gradle 파일의 depencies에 추가

```groovy
dependencies{
	// RxLifecycle
	implementation 'com.trello.rxlifecycle2:rxlifecycle-android:2.2.2'
	implementation 'com.trello.rxlifecycle2:rxlifecycle:2.2.2'
	implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.2.2'
}
```


Observable.interval()을 이용해 1초에 한번 로그를 찍다가 

화면이 뒤로가 액티비티가 종료되어도 unsubscribe(구독 해제)를 하지 않는다면, 스트림은 계속 유지되어 로그가 계속 찍히는 것을 확인할 수 있다.

```java
Observable.interval(0,1,TimeUnit.SECONDS)
  .map(String::valueOf)
  .subscribe(s -> Lob.i("###",s));
```



compose() 함수로 라이프 사이클을 관리하도록 추가했다. Observable은 해당 클래스가 종료되면 자동으로 해제(dispose)가 된다. 

```java
Observable.interval(0,1,TimeUnit.SECONDS)
  .map(String::valueOf)
  .compose(bindToLifecycle())
  .subscribe(s -> Lob.i("Lifecycle 적용한 Observable",s));
```



</br>

#### 2.3.4 RxLifecycle 마무리

RxJava2 에서는 RxLifecycle의 컴포넌트 이외에도 메모리 관리를 위한 방법을 제공한다. 

예를 들어 RxJava에 익숙한 개발자들은 안드로이드의 전통적인 라이프사이클 관리 기법보다는 직접 관리하기 편한 dispose() 함수를 사용한 것 등이 있다.

어떤 것이 좋다고 이야기하기는 어렵고 각각 장,단점이 있다. 상황에 맞게 개발자가 잘 선택하여 사용하면 된다.



</br></br>



## 3. RxAndroid 활용

### 3.1 REST API를 활용한 네트워크 프로그래밍

#### Retrofit2 + OkHttp 활용하기

**OkHttp**는 안드로이드에서 사용할 수 있는 대표 클라이언트 중 하나이며 페이스북에서 사용하는 것으로 유명하다. SPDY/GZIP 지원 등 네트워킹 스택을 효율적으로 관리할 수 있고, 빠른 응답속도를 보일 수 있다는 점이 장점이다.

**Retrofit**은 서버 연동과 응답 전체를 관리하는 라이브러리이다. OkHttp가 서버와의 연동 관련 기능만 제공한다면 응답까지 관리해준다는 면에서 편리하다. Retofit 1.x 버전에서는 OkHttp, HttpClient 등 사용자가 원하는 클라이언트를 선택해서 사용할 수 있었지만, 2.x 버전에서는 HttpClient는 더 이상 사용할 수 없고 **OkHttp에 의존**하도록 변경되었다.



#### RxAndroid에서 Retrofit2 + OkHttp 라이브러리 사용하기 

Retrofit의 장점 중 하나는 **어노테이션**을 지원하는 것이다. 스프링처럼 어노테이션으로 API를 설계할 수 있다. 예를 들어 **@Header**를 이용하여 헤더를 설정하고, **@GET**이나 **@POST** 등으로 사용할 HTTP 메소드를 선언할 수 있다.



Retrofit은 RxJava를 정식으로 지원하므로 **Observable을 API 리턴값**으로 사용할 수 있다. 그 외에 Call과 Future 인터페이스도 지원한다. 



Java를 이용한 기존 코드

```java
public interface RestAPI {
    @GET(APIUrl.QUIZZES_URL)
    Call<JsonObject> quizzes();

    @FormUrlEncoded
    @POST(APIUrl.LOGIN_URL)
    Call<JsonObject> login(@Field("kakaoID") long kakaoId);

    @FormUrlEncoded
    @POST(APIUrl.UPDATE_EXP)
    Call<JsonObject> updateExp(@Field("exp") int exp, @Field("id") long id);
}

```



RxJava를 적용한 코드

반환값이 **Observable**인 것이 차이점이다.

```java
public interface RestAPI {
    @GET(APIUrl.QUIZZES_URL)
    Observable<JsonObject> quizzes();

    @FormUrlEncoded
    @POST(APIUrl.LOGIN_URL)
    Observable<JsonObject> login(@Field("kakaoID") long kakaoId);

    @FormUrlEncoded
    @POST(APIUrl.UPDATE_EXP)
    Observable<JsonObject> updateExp(@Field("exp") int exp, @Field("id") long id);
}

```



Rxjava와 Retrofit2을 연결하기 위한 어뎁터로 사용하기 위해 추가했다. 정확히는 `RxJava2CallAdapterFactory`를 사용하기 위함이다. 아래와 같이 retrofit instance를 빌드 할 때 `RxJava2CallAdapterFactory`를 추가해 주면 아래처럼 `Observable`이나 `Single`같은 타입을 뱉을 수 있게 된다.

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://google.com/")
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  	.addConverterFactory(GsonConverterFactory.create())
    .build();
```



```java
// RxJava + Retofit + OkHttp
private void startRx(){
  GithubServiceApi service = RestfulAdapter.getInstance().getServiceApi();
  Observable<List<Contributor>> observable = 
    service.getObContributors(name,repo);
  
  mCompositeDisposable.add(
    observable.subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    //Observable을 구독
    .subscribeWith(new DisposableObserver<List<Contributor>>(){
      @Override
      public void onNext(List<Contributor> contributors){
        for(Contributor c : contributors){
          log(c.toString());
        }
      }
    })
    
  )
}
```

startRx() 메소드는 RestfulAdapter 클래스의 getServiceApi() 메서드 안 retrofit 변수를 이용해 생성된 API 프락시를 가져온다. owner와 repo의 값을 전달하면 observable 변수에 저장된 Observable을 리턴한다. 생성된 Observable에 구독자를 설정하면 getServiceApi() 메소드를 호출하여 github.com에서 정보를 얻어온다. 

결과는 구독자가 수신하게 되고 GSON에서 Contributor 클래스의 구조에 맞게 디코딩한 다음 UI스레드를 이용해 화면에 업데이트한다. 



생성된 Disposable 객체를 CompositeDisposable에서 관리하도록 CompositeDisposable.add() 함수를 사용하여 추가한다. `CompositeDisposable` 을 사용하면 여러 `Disposable` 객체를 한번에 관리할 수 있다.

```java
protected void onDestroy() {
    super.onDestroy();

    mCompositeDisposable.clear();
}
```





[기존 Java 코드]

```java
// Retofit + OkHttp
Call<JsonObject> call = restAPI.updateExp(30,pref.getLong("userID", 0));
call.enqueue(new Callback<JsonObject>() {
  @Override
  public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
    if (response.code() == 200) {
      Log.d("response----", response.body().toString());
      
    }
  }

  @Override
  public void onFailure(Call<JsonObject> call, Throwable t) {
    Log.d("ERROR! onFailure!", t.toString());
  }
});
```

위와 동일하다. 하지만 getXXX 메소드의 실행을 위해서는 retrofit에서 제공하는 Call 인터페이스를 사용한다. Call 인터페이스의 enqueue() 메소드에 콜백을 등록하면 GSON에서 디코딩한 결과를 화면에 업데이트할 수 있다. 



</br> </br>



## 마치며

### Reference

[[도서] RxJava 프로그래밍 (유동한, 박정준 지음)](https://book.naver.com/bookdb/book_detail.nhn?bid=12495967)

