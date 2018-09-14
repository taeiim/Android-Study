# MVP, 실제로 적용해보자!

> MVP를 실제 프로젝트에도 적용해봅시당~~

> 작성자 : 이종현
>
> Present Time : 2018–09-14-FRI

</br>

## 0. 개요

### 0-1. 기존의 구조, MVC
우리는 실질적으로 디자인 패턴을 적용하기 전에는, MVC 패턴에 가까운 구조를 사용하고 있었습니다.  
각 요소는 아래와 같은 부분을 담당합니다.
- Model: 데이터, 상태, 비즈니스 로직을 담당
- View: UI layout, Model로 부터 오는 데이터를 보여줌
- Controller: 사용자로부터의 입력 수신, View의 이벤트에 알림을 받는 역할  
![MVC 구조 사진](https://cdn-images-1.medium.com/max/880/1*1SWYJzbQSdKQbeVj4FvLwA.png)
그림으로 보면 다음과 같은 구조를 가지고 있습니다.  
안드로이드에서는 `View`와 `Controller`가 모두 액티비티, 프래그먼트같은 뷰에 종속되어있다는 특성을 가지고 있습니다.
```
```
뷰에서 컨트롤러의 역할까지 모두 담당하기에, **View가 너무 거대해지고**, 안드로이드 API에 종속되어 있기 때문에 **테스트가 힘들다**는 단점을 가지고 있습니다.

### 0-2. MVP란?
위와 같은 단점을 보완하기 위해서 새로운 디자인 패턴인 MVP 패턴이 등장했습니다.  
실제로 MVP 패턴을 적용하기 전에, MVP 패턴의 개념을 알아봅시다.
  
MVP는 **Model**, **View**, **Presenter**라는 세개의 요소로 이루어져 있습니다.    
안드로이드에서 각 요소는 아래와 같은 부분을 담당합니다.
- Model: 데이터, 상태, 비즈니스 로직을 담당
- View: xml, View(Activity, Fragment)가 View에 해당하며, Presenter에 이벤트를 전달합니다.
- Presenter: View로부터 받아온 이벤트를 처리하고, Model을 업데이트 합니다.
![MVP 예시 사진](https://cdn-images-1.medium.com/max/800/1*p2JvbgEir0BusDiiVHMvIA.png)
MVP 패턴은 다음과 같이 진행됩니다.
1. View에서 이벤트가 발생합니다.
2. View는 Presenter에 이벤트를 전달합니다.
3.  Presnter에서 로직을 처리합니다.
   - 이 때, Model을 업데이트하거나, Model에서 데이터를 가져올 수 있습니다.
4. Presenter에서 View를 업데이트합니다.

### 0-3. MVP, 어떻게 구현해야할까?
[구글에서 제공하는 문서](https://github.com/googlesamples/android-architecture/tree/todo-mvp-kotlin)
에 따라서 MVP 패턴을 어떻게 구현을 해야할지 알아봅시다!

#### Contract
구글에서 제공한 예제에서는 `Contract` 인터페이스를 만들어, MVP 패턴을 구현합니다.
```kotlin
interface TaskDetailContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
    }
}
```
`Contract`에서는 `View` 인터페이스와, `Presenter` 인터페이스를 정의합니다.  
`BaseView`, `BasePresenter`에는 모든 뷰와 프레젠터에 공통적으로 들어갈 부분을 정의합니다.
- `View` 인터페이스는 Activity 혹은 Fragment, 즉 뷰에 상속이 됩니다.  
- `View` 인터페이스는 `Presenter`에서 `View`를 컨트롤할 때 사용됩니다.  
- `Contract`를 정의함으로써, 우리는 Contract만 보고 대략적인 코드를 이해할 수 있습니다.  
  
이제, 전반적인 구조를 알게 되었으니 실제로 구현해봅시당~~

## 구현해보기
두 개의 EditText에서 텍스트를 받아와 버튼을 클릭하면 결과를 출력해주는 간단한 계산기를 만들어보며, MVP 패턴이 무엇인지 이해해봅시다!
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        plus.setOnClickListener {
            result.text = calc(firstNum.toInt(), secondNum.toInt(), '+').toString()
        }

        minus.setOnClickListener {
            result.text = calc(firstNum.toInt(), secondNum.toInt(), '-').toString()
        }

        divide.setOnClickListener {
            result.text = calc(firstNum.toInt(), secondNum.toInt(), '/').toString()
        }

        multiple.setOnClickListener {
            result.text = calc(firstNum.toInt(), secondNum.toInt(), '*').toString()
        }

    }

    fun calc(x: Int, y: Int, type: Char) = when (type) {
        '+' -> x + y
        '-' -> x - y
        '*' -> x * y
        '/' -> x / y
        else -> 0
    }
```
기존 코드는 다음과 같습니다.  
1. `plus`, `minus`, `divide`, `multiple`을 클릭합니다.
2. 두 개의 EditText의 텍스트를 받아와 `calc` 함수를 호출합니다.
3. 결과를 result 라는 TextView에 출력합니다.

사실 바꾸든 안바꾸든 별로 다를게 없을거같은데 아무튼 MVP로 바꿔봅시다~~
### 1. Contract 정의하기
```kotlin
interface MainContract {
    interface View {
        fun setCalcResult(res: Int)
        fun getFirstNum(): Int
        fun getSecondNum(): Int
    }

    interface Presenter {
        fun calc(x: Int, y: Int, type: Char)
    }
}
```

### 2. View 인터페이스 구현하기
View(Activity, Fragment)에 View 인터페이스를 구현합니다.
```kotlin
class MainActivity : AppCompatActivity(), MainContract.View {
    val presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setCalcResult(res: Int) { result.text = res.toString() }

    override fun getFirstNum(): Int = firstNum.toInt()

    override fun getSecondNum(): Int = secondNum.toInt()
}
```


### 3. Presenter 인터페이스 구현하기
`MainPresenter` 클래스를 만든 후, `MainContract.Presenter`를 구현합니다.
```kotlin
class MainPresenter(val view: MainContract.View) : MainContract.Presenter {
    override fun calc(x: Int, y: Int, type: Char) {
        val res = when (type) {
            '+' -> x + y
            '-' -> x - y
            '*' -> x * y
            '/' -> x / y
            else -> 0
        }
        view.setCalcResult(res)
    }
}
```
프레젠터를 만들 때, `View`를 생성자로 받음으로써, 프레젠터에서 간접적으로 View를 조종할 수 있습니다. 

### 4. 액티비티 구현하기
이제, 남은 액티비티를 구현합시다.
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        plus.setOnClickListener {
            presenter.calc(getFirstNum(), getSecondNum(), '+')
        }

        minus.setOnClickListener {
            presenter.calc(getFirstNum(), getSecondNum(), '-')
        }

        divide.setOnClickListener {
            presenter.calc(getFirstNum(), getSecondNum(), '/')
        }

        multiple.setOnClickListener {
            presenter.calc(getFirstNum(), getSecondNum(), '*')
        }
    }
```
MVC 구조였다면 원래 Activity 안에 계산을 수행하는 로직이 있었지만, Presenter로 이벤트를 보내 Presenter에서 로직을 수행하고, View를 업데이트하게 했습니다.

## 테스트 해보기

### 0. Mockito란?
Mockito는 JUnit 위에서 동작하는 Mocking, Verification을 도와주는 라이브러리입니다.
```groovy
testImplementation 'org.mockito:mockito-core:2.22.0'
```
위의 코드를 모듈 단위 `build.gradle`에 추가하여 사용합니다.  

#### Mock이란?
Mock이란 가짜 객체로, 테스트 할 때 Mock 객체를 통해 행위를 검증 할 수 있습니다.

이번에는 View 인터페이스를 mocking해 View의 행위를 검증할 것 입니다.  

### 1. 테스트 케이스 작성하기
```kotlin
class MainPresenterTest {

    lateinit var presenter: MainPresenter
    lateinit var view: MainContract.View

    @Before
    fun setUp() {
        view = Mockito.mock(MainContract.View::class.java)
        presenter = MainPresenter(view)
    }

    @Test
    fun plusTest(){
        presenter.calc(3,5,'*')
        Mockito.verify(view).setCalcResult(15)
    }
}
```

#### @Before
`Before` 어노테이션이 붙은 메소드는 테스트 케이스가 실행되기 전에, 무조건 실행되고 넘어가는 메소드입니다.  
`setUp()` 메소드에서 view를 mocking 시키고, presenter를 생성해줍니다.  

#### @Test
- 테스트 케이스에서는 Presenter의 메소드를 수행합니다.  
- `calc` 메소드는 계산을 하고, 결과를 `setCalcResult` 메소드를 이용해서 결과를 뷰에 출력시켜주는 메소드입니다.  
- `Mockito`의 `verify()` 메소드를 이용해서, `setCalcResult(15)`가 동작되었는지 검증합니다.  


## 참고 문서
https://thdev.tech/androiddev/2016/10/23/Android-MVC-Architecture/  
https://medium.com/nspoons/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-architecture-%ED%8C%A8%ED%84%B4-part-1-%EB%AA%A8%EB%8D%B8-%EB%B7%B0-%EC%BB%A8%ED%8A%B8%EB%A1%A4%EB%9F%AC-model-view-controller-881c6fda24d9  
https://upday.github.io/blog/model-view-presenter/  
https://academy.realm.io/kr/posts/eric-maxwell-mvc-mvp-and-mvvm-on-android/
