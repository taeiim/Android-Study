# MVVM, 실제로 적용해보자!

> MVVM을 실제 프로젝트에도 적용해봅시당~~

> 작성자 : 이종현
>
> Present Time : 2018–10-03-WED

</br>

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
위와 같이 ViewModel을 짜줍니다.
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
* 각 Button에 onClick이 실행될 시 ViewModel의 calc 메소드를 호출하도록 해 두었습니다.
1. 그러면 ViewModel에 이벤트가 들어오면 ViewModel은 이벤트를 처리하고, ViewModel의 데이터가 변경될 것입니다.
2. View는 ViewModel의 데이터를 관측하고 데이터가 바뀐다면 View에 반영합니다.

## 2-0. Android Architecture Component
근데 이게 화면을 돌리면 죽어요  
그래서 구글은 생명주기에 최적화된 ViweModel과 LiveData를 내놓습니다.

### ViewModel
AAC의 ViewModel은 생명주기에 최적화되어 있습니다.  
아까 만들었던 ViewModel은 View가 죽으면 ViewModel도 같이 죽는 반면, AAC의 ViewModel은 Activity가 끝날 때 까지 사라지지 않고, View의 생명주기와 별개로 흘러갑니다.
```kotlin
class MainViewModel : ViewModel() {

}
```
1. AAC의 뷰모델을 사용하려면 ViewModel을 상속받습니다.
2. View에서 ViewModel을 불러올 때에는 `ViewModelProviders`를 이용하여 아래와 같은 방법으로 받아옵니다.
```kotlin
val viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
```
결론은 AAC의 ViewModel을 사용하면 생명주기에 영향을 받지 않고, 언제나 유지되는 ViewModel을 만들 수 있습니다.

#### tl;dr
ViewModel은 ViewModelProviders.of()의 인자로 들어가는 LifeCycleOwner에 따라 ViewModel 인스턴스를 반환해주는데, Fragment에서 `getActivity()`로 LifeCycleOwner를 가져오면 같은 액티비티 아래에 있는 두개의 프래그먼트가 같은 ViewModel을 공유할 수 있습니다.

### LiveData
LiveData는 생명주기와 데이터의 변경을 인지할 수 있는 컴포넌트입니다.    
`observe` 메소드를 이용해서 LiveData를 관측할 수 있고, 값이 변화되면 `observe`가 호출됩니다.

#### LiveData와 
Databinding과 혼용하려면
보통의 Databinding을 사용할 때에는 데이터바인딩에서 지원하는 `Observable` 과 같은 클래스들을 사용해야했지만, Databinding 객체에 lifecycleOwner를 할당시켜주면 `Observable` 대신 LiveData를 사용할 수 있습니다. 할당시키는 법은 다음과 같습니다.
```kotlin
val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
binding.setLifecycleOwner(this)
```
## 2-1. AAC로 바꿔보자
방금 짰던 코드에 AAC를 적용해봅시다!  
생각보다 간단하게, ObservableField들을 모두 LiveData로 바꾸고, 데이터 바인딩 객체의 `setLifeCycleOwner`를 호출하면 됩니다.

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
xml은 따로 건드릴 필요 없이 똑같이 두어도 상관이 없습니다. 이렇게 간단하게 AAC를 적용시킬 수 있습니다. 이제 생명주기에도 안전하네요. 와!

## 3-1. 실제로 적용하면 어떨까요? (추후 추가 예정)

### JustGo
```kotlin
class NavigationActivity : DataBindingActivity<ActivityNavigationBinding>(), OnMapReadyCallback {
    override fun getLayoutId(): Int = R.layout.activity_navigation

    val viewModel by lazy { ViewModelProviders.of(this)[NavigationViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        viewModel.getNavigation(transType.toString(), lat, lng, desLat, desLng)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.main_startTravel_mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
 
        viewModel.travelFinishEvent.observe(this, Observer {
            toast("Travel Is Finish!")
            val arriveIntent = Intent(this, ArriveActivity::class.java)
            arriveIntent.putExtra("placeid", placeId)
            startActivity(arriveIntent)
            finish()
        })
    }

    override fun onMapReady(map: GoogleMap) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        viewModel.polyLineEvent.observe(this, Observer {
            val d = PolyUtil.decode(viewModel.polyLine)
            map.addPolyline(PolylineOptions().addAll(d)
                    .width(5F))
            viewModel.direction.forEach {
                LatLng(it.lat, it.lng).let {
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
        })
}
```

```kotlin
class NavigationViewModel : ViewModel() {
    var direction = ArrayList<DirectionModel.Point>()
    var index = 0
    val changeTextLiveEvent = SingleLiveEvent<Any>()
    val travelFinishEvent = SingleLiveEvent<Any>()
    val transit = MutableLiveData<String>()
    val type = MutableLiveData<String>()
    val polyLineEvent = SingleLiveEvent<String>()
    var polyLine = ""

    fun getNavigation(transport: String, lat: Double, lng: Double, desLat: Double, desLng: Double) {
        getDirection(transport, lat, lng, desLat, desLng) {
            onSuccess = {
                direction = body()!!.points
                polyLine = body()!!.polyline.replace("\\", """\""")
                polyLineEvent.call()
                transit.value = direction[index].instruction
                type.value = direction[index + 1].let { " With ${it.mode}" }
            }
        }

    }

    fun compareLocation(lat: Double, lng: Double) {
        if (index < direction.size - 1) {
            val direction = direction[index + 1]
            direction.lat = String.format("%.5f", direction.lat).toDouble()
            direction.lng = String.format("%.5f", direction.lng).toDouble()

            if (direction.lat - 0.001 < lat.toFive() && lat.toFive() < direction.lat + 0.001) {
                if (direction.lng - 0.001 < lng.toFive() && lng.toFive() < direction.lng + 0.001) {
                    index++
                    transit.value = this.direction[index].instruction
                    type.value = this.direction[index + 1].let { " With ${it.mode}" }
                }
            }
        } else if (direction.size != 0) {
            travelFinishEvent.call()
        }
    }

    fun Double.toFive() = String.format("%.5f", this).toDouble()
}
```
