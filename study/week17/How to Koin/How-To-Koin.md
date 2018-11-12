# How to Koin

> Koin을 어떻게 쓰는지 알아봅시당~~
>
> 작성자: 이종현
>
> Present Time: 2018-11-9 FRI

## 0. 시작하기 전에, 제어의 역전이란?

koin이 무엇인지 알아보기 전에 **제어의 역전**이 무엇인지 알아봅시다.

프로젝트가 커지면 커질수록 프로그램에서 제어해야할 부분은 많아지며 코드 관리가 점점 힘들어집니다.  그래서 **제어의 역전**이라는 방법을 이용하여 코드를 관리합니다.

**제어의 역전(Inversion of Control)**은 프레임워크에 객체 사용 및 생성 등 제어의 권한을 넘기며 클라이언트가 신경써야할 것을 줄이는 방법입니다.

### 0-1. 어떻게 구현해요?

**제어의 역전** 구조를 구현할 때에는 대표적인 방법으로 **의존성 주입(Dependency Injection)**과 **서비스 로케이터(Service Locator)**가 있습니다. 

#### 0-1-1. 의존성 주입이란?

```kotlin
class Car{
    val tire = Tire()
    val engine = Engine()
}
```

위와 같은 코드는 객체를 `Car` 클래스 내부에서 의존 객체를 생성하게 됩니다. 하지만 `tire`와 `engine`을 내부에서 생성합니다. 하지만 이런 방법은 모듈간의 결합도가 높고, Mock Object를 사용하기 힘들기 때문에 테스팅을 할 때 용이하지 않을 수 있습니다. 

#### 0-1-2. 생성자, setter를 이용한 의존성 주입

```kotlin
val car = Car()
car.tire = Tire()
car.engine = Engine()

val car2 = Car(Tire(), Engine())
```

위처럼 외부로부터 객체를 받아오게 되면 결합도가 떨어지고, 테스팅을 할 경우에는 Mock Object를 주입하면 되기 때문에 테스트가 더 용이해집니다.

DI 프레임워크는 위처럼 객체를 생성하고 넘겨주며 객체의 제어를 프레임워크에서 하게 되는데, 이때 IoC 개념을 구현합니다.

보통 **Spring**같은 프레임워크를 이용하여 DI를 사용합니다.

안드로이드는  **Dagger2**라는 프레임워크를 이용하여 DI를 구현합니다.

#### 0-1-2. Service Locator란?

**Service Locator**란,  의존성이 필요 할 때, 외부에서 클래스를 받아와서 사용하는 방식입니다.

```kotlin
class Car{
    val car = Locator.resolve<Car>()
}

class Locator{
    // 이하생략
}
```

위처럼 `Locator` 클래스에서 객체를 받아와 사용하여도 결합도를 떨어뜨리고 테스트하기 용이한 코드를 작성할 수 있습니다.

물론 실제 DI, Service Locator 프레임워크는 위 코드보다 훨씬 좋은 방법으로 구성되어 있겠지만, 이번에는 이해를 돕기 위해 이런 예제를 사용하였습니당~~~ 

### 0-2. 사용 시 얻는 이점

  프레임워크에서 객체를 관리하게 되며 여러가지 이점을 얻게 됩니다.

1. 다른 시스템이 무엇을 할지 추측을 하지 않아도 됩니다.
2. 시스템을 바꿔도 다른 시스템에 지장을 주지 않습니다.

### 0-2. 그런데

마틴 파울러가 쓴 [Inversion of Control Containers and the Dependency Injection pattern]("https://martinfowler.com/articles/injection.html")이라는 글을 읽어보면,

> As a result I think we need a more specific name for this pattern. Inversion of Control is too generic a term, and thus people find it confusing. As a result with a lot of discussion with various IoC advocates we settled on the name *Dependency Injection*.

라는 구절이 있습니다.

IoC라는 단어가 너무 모호하여 그냥 앞으로는 **DI(Dependency injection)**이라는 단어를 사용하자고 하였으니까 이번 정리에서는 그냥 모두 통틀어 DI라고 말할게요



## 1. koin이란?

Koin은 **DSL**로 이루어진 **의존성 주입 프레임워크**입니다. 

모두 다 코틀린으로 작성되었고, 코틀린으로만 사용할 수 있습니다. 

### 1-1. Dagger와의 차이점

**Dagger**는 어노테이션, 클래스 기반으로 약간 가독성이 떨어지지만, 어노테이션 프로세서를 이용하여 컴파일 단위에서 오류를 잡아낼 수 있습니다.

**koin**은 DSL 기반으로 이루어져 있어 가독성이 좋고, 객체를 주입할 때 by를 이용하여 지연 방식을 사용할 수 있습니다. 하지만 오류를 **컴파일에서 잡아낼 수 없고** 런타임에서 오류를 잡아낼 수 있습니다. 또한, 서비스 로케이터 패턴을 사용합니다.

### 1-2. Module

koin에서는 Module에 필요한 의존성을 정의하며 설계합니다.

모듈을 정의하는법은 다음과 같습니다.

```kotlin
val module = module {
    
}
```

`module`도 마찬가지로 DSL 형식으로 사용할 수 있기 때문에, 가독성이 높습니다.

모듈을 정의하고, 모듈 안에 들어갈 의존성을 정의합니다. 

### 1-3. 의존성 정의

모듈 안에서 의존성을 정의하고, 차후에 클라이언트에서 `inject()`, `viewModel()`등의 키워드를 이용하여 모듈로부터 의존성을 주입받습니다. 

사용할 수 있는 키워드는 다음과 같습니다.

1. `factory`:  `inject()` 가 호출될 때 마다 새로운 객체를 만들어줍니다.
2. `single`:  `inject()`가 호출될 때, 싱글톤 객체를 주입해줍니다.
3. `viewModel`:  **AAC**의 ViewModel을 사용할 때 쓰는 키워드로, `ViewModelProviders`를 통하여 생명주기에 안전한 ViewModel을 주입해줍니다.
4. `get():` 모듈에서 각 컴포넌트끼리의 의존성 주입을 위해 쓰이는 키워드입니다.

이외에도 여러 키워드를 통해서 의존성을 정의하고, 클라이언트에서 의존성을 주입합니다.

### 1-4. 의존성 주입하기

모듈에서 정의한 의존성을 필요한 곳에 주입합니다. Dagger2와 달리 위임 프로퍼티를 사용할 수도 있고, 그냥 받아올 수도 있습니다.

사용하는 키워드는 다음과 같습니다.

1. `by inject()`: 위임 프로퍼티를 이용하여 호출될 시점에 의존성을 주입받습니다.
2. `by viewModel()` : 위임 프로퍼티를 이용하여 호출될 시점에 뷰모델을 주입받습니다. 모듈에서 `viewModel`을 정의했을 때 사용합니다.
3. `by sharedViewModel()`: 위와 같은 동작을 수행하지만, 프래그먼트에서만 사용할 수 있고 액티비티 아래에서 공유할 수 있는 뷰모델을 주입받습니다.
4. `getViewModel(), getSharedViewModel()`: 위임 프로퍼티를 사용하지 않고, 위의 친구들을 그냥 바로 받아옵니다.

## 2. koin 시작해보기

지~난번에 예시로 들었던 MVP 코드에 koin을 적용해봅시다~~

### 2-1. 의존성 추가하기

```groovy
def koin_version = '1.0.1'

// Koin for Android
implementation "org.koin:koin-android:$koin_version"
// Koin Android Scope feature
implementation "org.koin:koin-android-scope:$koin_version"
// Koin Android ViewModel feature
implementation "org.koin:koin-android-viewmodel:$koin_version"
```

1. 모듈 단위 `build.gradle`의 `dependencies`에  위와 같은 의존성을 추가합니다.
2. 싱크하면 완성~~

### 2-2. 모듈 정의하기

```kotlin
val mainModule = module {
    factory { MainPresenter(get()) }
    single { MainRepository() }
}

class MainPresenter(val repository: MainRepository) : MainContract.Presenter {
    
}
```

1. 코틀린에서는 꼭 클래스를 만들 필요가 없기 때문에, 클래스를 정의하지 않고 파일만 만들어 그곳에 모듈들을 정의하는 방법을 사용합니다.

2. `MainActivity`에서 `MainPresenter` 의존성을 받아와야 하기 때문에 mainModule에 `MainPresnter`를 정의합니다.

3. 프레젠터는 `factory` 키워드를 이용하여, `inject()` 가 호출될 때 마다 새로운 객체를 받아오게끔 했습니다.
4. 레포지토리 클래스는 `single` 키워드를 이용하여 싱글톤으로 정의합니다.
5. `MainPresenter`는 생성자로 `MainRepository`를 받습니다. 의존성에  `get()` 키워드를 생성자 안에 작성해두면 koin이 알아서 의존성을 찾아서 넣어줍니다.

### 2-3. Application 정의하기

`Application`을 상속받는 클래스를 만들고, `onCreate()`에 `startKoin()` 을 작성합니다.

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(mainModule))
    }
}
```

`startKoin()`의 인자에는 context와 정의해둔 모듈 리스트가 들어갑니다.

this와 `listof` 키워드를 이용하거나, 미리 정의해둔 모듈 리스트를 인자로 넣어줍니다.

```xml
<application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:name=".App"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
```

그 다음에 메니페스트의 어플리케이션 태그에 `android:name` 속성에 방금 만든 어플리케이션을 정의해줍니다.

### 2-4. 의존성 주입받기

```kotlin
class MainActivity : AppCompatActivity(), MainContract.View {

    val presenter: MainPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.view = this
    }
}
```

정의해둔 모듈을 `by inject()`를 이용하여 주입받습니다.

와! 끝입니당~~

