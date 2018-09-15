# RxAndroid

> 안드로이드의 RxJava 활용

> 작성자 : 박태임
>
> Present Time : 2018-09-14 (Fri)



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



### 1.1 리액티브 라이브러리와 API

RxAndroid는 기본적으로 RxJava의 리액티브 라이브러리를 이용한다. 안드로이드에서 이용하는 리액티브 API와 라이브러리는 상당히 많다. 

###### * 안드로드에서 사용할 수 있는 리액티브 API와 라이브러리 )  RxLifecycle, RxBinding, RxLocation, RxFit, RxWear, RxImagePicker, ReactiveNetwork, RxDataBinding 등



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





## 2. RxAndroid 기본 





