

# MVVM, 실제로 적용해보자!

> MVVM을 실제 프로젝트에도 적용해봅시당~~

> 작성자 : 이종현
>
> Present Time : 2018–10-03-WED
> Supplement Present Time: 2018-10-24 WED



## 0. 개요

### 0-1. MVVM이란? 
MVVM이란 Model-View-ViewModel으로, 구글 Android Architecture Component(이하 AAC)의 지원으로 인해 안드로이드에서 점점 부상하고있는 디자인 패턴입니다.  
각 요소는 아래와 같은 부분을 담당합니다.

#### Model
- 데이터 모델이고 네트워크, 내부 DB 등으로부터 데이터 소스를 담당합니다.
#### View
- Activity, Fragment 등 전반적인 모든 View를 뜻합니다.
- ViewModel을 관측하며, 변화된 ViewModel의 데이터를 View에 반영합니다.
#### ViewModel
- View에 대한 Model입니다.
- Model에서 제공받은 데이터를 가지고 있고, 상태가 변경되었을때 Event를 호출합니다.
- MVP 패턴과는 다르게 1:N 구조를 가질 수 있습니다.
- ViewModel과 View는 DataBinding으로 긴밀하게 결합되어 있습니다.

Databinding에 관한 내용은 [이 글을 참고해주세요.](https://medium.com/@PaperEd/android-how-to-databinding-169c78e7dc28)

## 1. 구현해보기
지난번과 같이, 두 개의 EditText에서 텍스트를 받아와 버튼을 클릭하면 결과를 출력해주는 간단한 계산기를 만들어봅시다!

### 1-1. ViewModel, Layout 작성하기
레이아웃에는 2개의 EditText, 4개의 계산 버튼, 결과를 띄워줄 텍스트뷰가 존재합니다. 이에 상응하는 뷰모델을 만들어봅시다.
```kotlin
class MainViewModel {
    val firstNum = ObservableField<String>()
    val secondNum = ObservableField<String>()
    val result = ObservableField<String>()

    fun calc(op: Char) {
        result.set(when (op) {
            '+' -> (firstNum.get()!!.toInt() + secondNum.get()!!.toInt()).toString()
            '-' -> (firstNum.get()!!.toInt() - secondNum.get()!!.toInt()).toString()
            '*' -> (firstNum.get()!!.toInt() * secondNum.get()!!.toInt()).toString()
            '/' -> (firstNum.get()!!.toInt() / secondNum.get()!!.toInt()).toString()
            else -> ""
        })
    }
}
```
- `ObservableField`를 이용하여 xml에 데이터를 바인딩 할 수 있는 클래스를 만듭니다.

- `calc` 메소드로 `firstNum`과 `secondNum`의 데이터를 받아와서 연산을 한 후, `result`에 넣습니다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="viewModel"
            type="com.example.dwg76.databinding_example.MainViewModel" />
    </data>

    <android.support.constraint.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <EditText
            android:id="@+id/firstNum"                            
            android:text="@={viewModel.firstNum}" />

        <EditText
            android:id="@+id/secondNum"
            android:text="@={viewModel.secondNum}" />

        <Button
            android:id="@+id/plus"
            android:onClick="@{() -> viewModel.calc('+')}"
            android:text="더하기" />

        <Button
            android:id="@+id/minus"
            android:onClick="@{() -> viewModel.calc('-')}"
            android:text="빼기" />

        <Button
            android:id="@+id/divide"
            android:onClick="@{() -> viewModel.calc('/')}"
            android:text="나누기"/>

        <Button
            android:id="@+id/multiple"
            android:onClick="@{() -> viewModel.calc('*')}"
            android:text="곱하기"/>

        <TextView
            android:id="@+id/result"
            android:text="@{viewModel.result}"/>
    </android.support.constraint.ConstraintLayout>
</layout>
```
* Two-way Databinding을 이용하여 ViewModel의 `ObservableField`들을 EditText에 바인딩해줍니다.
* 각 Button에 onClick이 실행될 시 ViewModel의 `calc` 메소드를 호출하도록 해 두었습니다.
  1. 그러면 ViewModel에 이벤트가 들어오면 ViewModel은 이벤트를 처리하고, ViewModel의 데이터가 변경될 것입니다.
  2. View는 ViewModel의 데이터를 관측하고 데이터가 바뀐다면 View에 반영합니다.

### Activity 작성하기

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = MainViewModel()
        binding.viewModel = viewModel
    }
}
```

- 기존 `setContentView `를 지우고,  `DatabindingUtil`을 이용하여 다시 `setContentView`를 해줍니다.
- `ActivityMainBinding`은 레이아웃의 루트 태그를 `layout`으로 바꾸면 이름에 맞춰 자동으로 생성됩니다.
- `binding`객체에 `viewModel`을 할당해주면 xml에서 `viewModel`에 접근 할 수 있게 됩니다.

## 2-0. Android Architecture Component
근데 이게 화면을 돌리면 데이터들이 사라져요

그래서 구글은 생명주기에 최적화된 **ViweModel**과 **LiveData**를 내놓습니다.   

기존 코드를 조금 리팩토링만 하면  프로그래머는 생명주기를 힘들게 관리할 필요가 사라집니다. 물론 기존 구조가 MVVM 한정일 때 이지만요.

이 글이 AAC를 다루는 글은 아니기 때문에, 비중은 적지만 ViewModel과 LiveData가 무엇인지 간략하게 설명하도록 하겠습니다.

### ViewModel
AAC의 ViewModel은 생명주기에 최적화되어 있습니다.  
아까 만들었던 ViewModel은 View가 멈추면 ViewModel이 사라지는 반면, AAC의 ViewModel은 Activity가 끝날 때 까지 사라지지 않고, View의 생명주기와 별개로 흘러갑니다.

![Illustrates the lifecycle of a ViewModel as an activity changes state.](https://developer.android.com/images/topic/libraries/architecture/viewmodel-lifecycle.png)

#### 어떻게 사용하나요?

```kotlin
class MainViewModel : ViewModel() {

}
```
1. AAC의 뷰모델을 사용하려면 `ViewModel` 클래스를 상속받습니다.
2. View에서 ViewModel을 불러올 때에는 `ViewModelProviders`를 이용하여 아래와 같은 방법으로 받아옵니다.
```kotlin
val viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
```
위와 같은 방법을 이용하여 AAC의 ViewModel을 사용하면 생명주기에 영향을 받지 않고 안전한 ViewModel을 만들 수 있습니다.

#### tl;dr
`ViewModel`은 `ViewModelProviders.of()`의 인자로 `FragmentActivit `혹은 `Fragment`를 받습니다. 

이 두 클래스는 `LiceCycleOwner`를 가지고 있는데, `ViewModelProviders`는 `LifeCycleOwner`에 따라 `ViewModel `인스턴스를 반환해줍니다. 

이를 이용하여 같은 액티비티 아래에 있는 프래그먼트들끼리 데이터 공유가 가능합니다. 

`Fragment`에서 `getActivity()`로 `LifeCycleOwner`를 가져오면 같은 액티비티 아래에 있는 두개의 프래그먼트가 같은 `ViewModel`을 공유할 수 있습니다.

### LiveData
LiveData는 생명주기와 데이터의 변경을 인지할 수 있는 관찰이 가능한 클래스입니다.   

`observe` 메소드를 이용해서 `LiveData`를 관측할 수 있고, 값이 변화되면 `observe`가 호출됩니다.

**Start** 와 **Resume** 상태일 때 `observe`가 활성화되고, **Destroyed** 상태로 들어갈 때 관찰을 취소합니다.

이로써, 메모리 릭을 방지할 수 있습니다.

`observe` 이외에도 

#### LiveData와 Databinding을 혼용하려면

보통의 Databinding을 사용할 때에는 데이터바인딩에서 지원하는 `Observable` 과 같은 클래스들을 사용해야했지만, Databinding 객체에서 `setLifecycleOwner()`를 호출하면 `Observable` 대신 LiveData를 사용할 수 있습니다. 할당시키는 법은 다음과 같습니다.

```kotlin
val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
binding.setLifecycleOwner(this)
```
## 2-1. AAC로 바꿔보자
방금 짰던 코드에 AAC를 적용해봅시다! 

생각보다 간단하게, `ObservableField`들을 모두 `LiveData`로 바꾸고, 데이터 바인딩 객체의 `setLifeCycleOwner`함수 를 호출하면 됩니다.

```kotlin
class MainViewModel : ViewModel() {
    val firstNum = MutableLiveData<String>()
    val secondNum = MutableLiveData<String>()
    val result = MutableLiveData<String>()

    fun calc(op: Char) {
        Log.d("MainViewModel", "${firstNum.value}, ${secondNum.value}")
        result.value = when (op) {
            '+' -> (firstNum.value!!.toInt() + secondNum.value!!.toInt()).toString()
            '-' -> (firstNum.value!!.toInt() - secondNum.value!!.toInt()).toString()
            '*' -> (firstNum.value!!.toInt() * secondNum.value!!.toInt()).toString()
            '/' -> (firstNum.value!!.toInt() / secondNum.value!!.toInt()).toString()
            else -> ""
        }
        Log.d("MainViewModel", "${result.value}")
    }
}
```
- ` MutableLiveData`는 변할 수 있는 `LiveData`로, 값이 변경되는 데이터일 때 사용합니다.
- 보통 ViewModel 안에 `private`로 `MutableLiveData`를 생성하고, `LiveData`로 View에 데이터를 노출합니다.

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
    }
}
```
xml은 따로 건드릴 필요 없이 똑같이 두어도 상관이 없습니다. 간단하게 AAC를 적용시킬 수 있습니다. 이제 생명주기에도 안전하네요. 와!

## 3-1. 실제로 적용하면 어떨까요? with JustGo
JustGo는 필자가 진행했던 프로젝트인데, AAC와 MVVM 패턴을 이용하여 개발하였습니다. JustGo와 함께 MVVM 패턴을 어떻게 적용해야할지 알아봅시다.
전체 프로젝트는 [이 곳](https://github.com/Team-JustGo/App_For-Competition)에서 확인할 수 있습니다.

### 사전 상황 설명
- JustGo는 무작위 여행지 서비스로, 관광지까지 간단한 **네비게이션**을 제공해줍니다. 
- 화면에는 가야 할 목적지(ex. 법원사거리까지 도보로 이동)를 알려줍니다.
- 화면에는 지도를 띄워주며, 지도는 서버에서 가져온 마커, 폴리라인 정보를 알려줍니다.
- 사용자의 위치 정보를 계속 받아오며, 목적지에 도착하면 다음 목적지를 띄워줍니다.
- 최종 관광지에 도착하면, 그 여행지 정보를 알려주는 새로운 액티비티를 띄워줍니다. 


위와 같은 간단한 네비게이션 코드를 리뷰하며 MVVM 패턴이 어떤 식으로 진행되는지 알아봅시다.

### ViewModel
```kotlin
class NavigationViewModel : ViewModel() {
    private val _direction = MutableLiveData<ArrayList<DirectionModel.Point>>()
    private val _polyLine = MutableLiveData<String>()
    private val _transit = MutableLiveData<String>()
    private val _type = MutableLiveData<String>()   

    val direction get() = _direction as LiveData<ArrayList<DirectionModel.Point>>
    val polyLine get() = _polyLine as LiveData<String>   
    val transit get() = _transit as LiveData<String>
    val type get() = _type as LiveData<String>    
    val travelFinishEvent = SingleLiveEvent<Any>()
    
    var index = 0


    fun getNavigation(transport: String, lat: Double, lng: Double, desLat: Double, desLng: Double) {
        getDirection(transport, lat, lng, desLat, desLng) {
            onSuccess = {
                _direction.value = body()!!.points
                _polyLine.value = body()!!.polyline.replace("\\", """\""")
                _transit.value = _direction.value!![index].instruction
                _type.value = _direction.value!![index + 1].let { " With ${it.mode}" } 
            }
        }

    }

    fun compareLocation(lat: Double, lng: Double) {
        direction.value?.let { directionLiveData ->
            if (index < directionLiveData.size - 1) {                
                val direction = directionLiveData[index + 1]
                direction.lat = String.format("%.5f", direction.lat).toDouble()
                direction.lng = String.format("%.5f", direction.lng).toDouble()

                if (direction.lat - 0.001 < lat.toFive() && lat.toFive() < direction.lat + 0.001) {
                    if (direction.lng - 0.001 < lng.toFive() && lng.toFive() < direction.lng + 0.001) {                        
                        index++
                        _transit.value = directionLiveData[index].instruction
                        _type.value = directionLiveData[index + 1].let { " With ${it.mode}" }
                    }
                }
            } else if (directionLiveData.size != 0) {
                travelFinishEvent.call()
            }
        }
    }

    fun Double.toFive() = String.format("%.5f", this).toDouble()
}
```
위는 **사전 상황 설명에** 의 조건들에 따라서 코드를 작성한 코드입니다.

#### ??? 왜 MutableLiveData는 _가 붙어있고 LiveData는 없어요?

**뷰**에서 뷰모델의 데이터를 참조할 때에는 `MutableLiveData`를 데이터를 변경할 수 없는 `LiveData`로 치환시켜, View에서 ViewModel의 데이터 상태를 변경할 수 없게 만듭니다.

- 처음 시작할 때, `getNavigation()` 을 통하여 데이터를 받아오며, 성공 시 `LiveData`에 데이터를 집어넣습니다.

- `compareLocation()` 함수는 **View**로부터 호출 될 예정입니다. 위도와 경도를 인자로 받으며, 받은 위치가 목적지와 인접하면 다음 목적지를 제공합니다.

- `transit`과 `type`을 `DataBinding`을 이용하여 **xml**에 바인딩 해 **가야 할 목적지** 와, **가야 할 방법**을 띄워줍니다. 

### xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="viewModel"
            type="com.justgo.ui.navigation.NavigationViewModel" />
    </data>

    <android.support.constraint.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/colorPrimary"
        tools:context="com.justgo.ui.navigation.NavigationActivity">

        <android.support.constraint.ConstraintLayout
            android:id="@+id/navigation_trans_container"/>

            <ImageView                              
                android:src="@drawable/ic_bus_24px" />

            <TextView
                android:id="@+id/navigation_trans_tv"                
                android:text="To"
                android:textColor="#AFFF" />

            <TextView
                android:id="@+id/navigation_trans_location_tv"
                android:textColor="#FFF"
                android:text="@{viewModel.transit}" />

        </android.support.constraint.ConstraintLayout>

        <android.support.constraint.ConstraintLayout
            android:id="@+id/navigation_tag_container">

            <ImageView
                android:src="@drawable/ic_bookmark_24px" />

            <TextView
                android:id="@+id/navigation_tag_location_tv"
                android:textColor="#FFF"
                android:text="@{viewModel.type}"/>

        </android.support.constraint.ConstraintLayout>       

    </android.support.constraint.ConstraintLayout>
</layout>
```

위 코드는 네비게이션의 일부입니다.  아래 레이아웃에서 파란색 선으로 감싸진 부분을 담당합니다.

![Navigation](\Navigation.png)

`android:text="@{viewModel.transit}"`, `android:text="@{viewModel.type}"` 과 같이 데이터 바인딩을 이용하여 액티비티의 별 간섭 없이 바로 xml에 바인딩 할 수 있습니다.    

### Activity
```kotlin
class NavigationActivity : DataBindingActivity<ActivityNavigationBinding>(), OnMapReadyCallback {
    override fun getLayoutId(): Int = R.layout.activity_navigation

    val viewModel by lazy { ViewModelProviders.of(this)[NavigationViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        
        viewModel.getNavigation(transType.toString(), lat, lng, desLat, desLng)
        
        viewModel.travelFinishEvent.observe(this, Observer {
            toast("Travel Is Finish!")
            val arriveIntent = Intent(this, ArriveActivity::class.java)
            arriveIntent.putExtra("placeid", placeId)
            startActivity(arriveIntent)
            finish()
        })
    }

    override fun onMapReady(map: GoogleMap) {
        
        viewModel.polyLine.observe(this, Observer {
            val decoded = PolyUtil.decode(it)
            map.addPolyline(PolylineOptions().addAll(decoded)
                    .width(5F))
        })

        viewModel.direction.observe(this, Observer { direction ->
            direction!!.forEach { point ->
                LatLng(point.lat, point.lng).let {
                    map.addMarker(MarkerOptions().position(it))
                }
            }
        })

        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1F, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 17F).let {
                    map.animateCamera(it)
                    viewModel.compareLocation(location.latitude, location.longitude)
                }
            }
        }
    }
}
```

MVVM 패턴에서는 데이터 바인딩을 이용하여 xml에서 데이터를 바로 바인딩 하는 방법을 권장합니다. 지도에서 데이터를 받아오는 어쩔 수 없는 경우는 **Activity**나 **Fragment**에서 처리하지만, 그런 특별한 경우가 아니라면 xml에서 처리할 수 있도록 합니다.

- Activity, 즉 `View`에서는 `ViewModelProviders`를 이용하여 생명주기에 안전한 `ViewModel` 을 불러옵니다. 
- `DataBindingActivity<ActivityNavigationBinding>()`은 데이터바인딩을 편하게 하기 위해 만든`AppCompatActivity`을 상속받은 추상 클래스입니다. `getLayoutId()` 에서 레이아웃 아이디를 받고, 제네릭에서 받은 클래스를 이용하여 `binding`이라는 이름을 가진 객체를 생성합니다. `binding` 객체에 viewModel을 바인딩하여 xml에서 ViewModel의 데이터에 접근할 수 있게끔 합니다.
- **View**에서는 `travelFinishEvent`, `direction`, `polyline`을 관측하며 데이터의 변화가 감지하면 아래와 같은 로직을 처리합니다.

#### travelFinishEvent

- 여행이 끝났을 때 호출되며, `ArriveActivity`라는 새로운 액티비티를 띄워줍니다. 
- 액티비티를 전환하기 위해서는 Navigator 인터페이스를 정의하여 **ViewModel**에서 처리하거나  `SingleLiveEvent`를 이용하여 액티비티에서 처리하는 방법이 있는데, 후자의 방법이 간결하기 때문에 후자의 방법을 선택하였습니다.

#### polyLine / direction
- 서버에서 데이터를 받아오면 `polyLine`과 `direction`에 데이터를 넣어주는데, **VIew**에서 이 `LiveData`를 Observe 하며 데이터가 들어왔을 때 맵에 폴리라인과 마커를 그려줍니다.
- `travelFinishEvent`와 동일하게 `SingleLiveEvent`를 이용하여 구현하여도 무관하지만, `LiveData`의 Observe를 이해하기 위해서 다음과 같은 방법으로 로직을 구성하였습니다.

#### viewModel.compoareLocation

- 3초마다 위치 정보를 갱신하며 `viewModel.compareLocation` 을 호출하며 현재 위치와 목적지를 비교합니다. 
- 위치 정보 갱신같은 경우는  데이터 바인딩으로 처리하기엔 무리가 있어서 이런 경우에는 View(Activity)에서 처리를 하였습니다.

---

위 방법이 옳은 방법으로 MVVM 패턴을 구현한 것이라고 확신할 수는 없습니다.  하지만 위 코드를 리뷰하며 MVVM 패턴에 대한 갈피를 잡으셨으면 하는 바램입니다! 감사합니다.