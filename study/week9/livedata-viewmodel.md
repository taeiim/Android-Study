# LiveData와 ViewModel

>Present Time : 2018-08-07-WED
>
>Last Updated : 2018-08-07-WED

-----

<br/>

## 0. 공식 문서

### 0-1. 레퍼런스

- [ViewModel](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html)
- [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html)

<br/>

### 0-2. 가이드

- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html)

<br/>

### 0-3. 기타 도움을 받은 사이트

- https://github.com/seoHyun99/android-study-log/edit/master/study/week3/livedata-and-viewmodel.md
- http://muyu.tistory.com/entry/Android-lifecycleaware-components-ViewModel%EA%B3%BC-LiveData-%EC%82%AC%EC%9A%A9%ED%95%B4%EB%B3%B4%EA%B8%B0
- https://codelabs.developers.google.com/codelabs/android-lifecycles/#0

-----

<br/>

ViewModel을 사용하기 위해서 dependencies를 추가한다. (LiveData도 마찬가지다)

project/build.gradle

```groovy
allprojects { 
    repositories { 
        jcenter() 
        maven { url 'https://maven.google.com' } 
    } 
}
```

<br/>

app/build.gradle

```groovy
implementation 'android.arch.lifecycle:runtime:1.0.0-alpha1'
implementation 'android.arch.lifecycle:extensions:1.0.0-alpha1'
annotationProcessor 'android.arch.lifecycle:compiler:1.0.0-alpha1'
```

<br/>

알맞게 추가해줘야 ViewModelProviders 등이 제대로 import된다.

-----

<br/>

## 1. ViewModel

>ViewModel is a class that is responsible for preparing and managing the data for an `Activity` or a `Fragment`. It also handles the communication of the Activity / Fragment with the rest of the application 

ViewModel은 ui관련된 data를 저장하고 관리하는 클래스이다.  

사용하기 위해서는 ViewModel이 Abstract class라 별도의 클래스를 하나 만들고 ViewModel을 상속받는다. 

#### 사용시 이점

: ViewModel을 이용하면, UI와 내부 로직을 분리할 수 있고, 리소스 관리가 용이해져 메모리를 관리하는데 간편하다는 장점이 있다. UI 컨트롤러 로직에서 뷰 데이터 소유권을 분리하는 것이다.

### 1-1. ViewModel 객체 가져오기

ViewModel은 일반적으로 Object를 생성하는 **new** 키워드로 생성하지 않고 **ViewModelProvider** 를 통해서만 가능하다. 

**CustomViewModel**

```java
public class ExampleViewModel extends ViewModel {
    private Integer num;

    public Integer getNum() {
        return num;
    }
    public void setNum(int i) {
        this.num = i;
    }
}
```

#### 1-1-1. DefaultFactory를 이용해 가져오기

```java
// ExampleViewModel extends ViewModel
ExampleViewModel viewmodel = ViewModelProviders.of(this).get(ExampleViewModel.class);
```

#### 1-1-2. CustomFactory를 이용해 가져오기

```java
// CustomViewModelFactory
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final TempData viewModelData;
    public ViewModelFactory(TempData viewModelData) {
        this.viewModelData = viewModelData;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExampleViewModel.class)) {
            return (T) new CustomViewModel(viewModelData);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
```

-----

</br>

## 2. LiveData

>LiveData is a data holder class that can be observed within a given lifecycle. 

**사용시 이점**

1. Obserevers를 통해 데이터가 변경되는 경우 즉각적으로 수행할 수 있어 UI가 데이터 상태와 일치하는 것을 보장한다.
2. Observers는 Lifecycle Object에 바인딩 되어 있고, destroyed 되는 경우에 알아서 clean up하기 때문에 memory leaks를 피할 수 있다.
3. Observers는 lifecycle이 inactive인 경우 event를 수신하지 않기때문에 에러가 발생할 염려가 없다.
4. lifecycle이 inactive 상태였다 active 상태로 변한 경우 최신 데이터를 수신해 항상 최신의 데이터를 보장한다.

→ LifecycleOwner의 getLifeCycle() 메서드를 통해 현재의 lifecycle을 가져올 수 있어 상태 체크가 가능하다. 

### 2-1. LifeCycleOwner

현재의 Lifecycle을 반환하는 getLifecycle() 메서드를 포함한 인터페이스이다. 

>2018년 1월 22일부터 FragmentActivity와 AppCompatActivity에서 getLifeCycle() 메서드를 지원한다. aac 1.0에서 지원하던 LifecycleActivity 와 LifecycleFragment은 depreaced 되었다. 

```java
public interface LifecycleOwner {
    @NonNull
    Lifecycle getLifecycle();
}
```

Lifecycle의 내부는 public enum으로 Event와 State가 있으며, Event에는 ON_CREATE, ON_START, ...(resume, pause, stop), ON_DESTORYED와 모든 이벤트인 ON_ANY가 있다. STATE에는 DESTROYED, INITIALIZED, CREATED, STARTED, RESUMED의 다섯 가지 STATE가 있다. 

-----

</br>

LiveData는 observe 호출을 통해서 데이터 갱신 이벤트를 받을 수 있다.

```java
LiveData.get(getActivity()).observe(this, location -> { 
    // update UI 
});
```

-----

</br>

## 결론

Lifecycle-aware components인 LiveData, ViewModel을 사용하면 그 동안 data-view 연결하는 작업을 할 때, exception이 발생하지 않도록 lifecycle을 신경써서 구현해야 했던 작업들을 줄일 수 있고 앱의 성능을 높이는데 도움이 될 것이다.