# Data Binding

> 발표주제 : Data Binding
>
> 발표일자 : 2018-07-30 (월)
>
> 발표자 : 송시은

[TOC]



## 0. 참고문서

[Android Developer - ko]: https://developer.android.com/topic/libraries/data-binding/?hl=ko
[Android Developer - en]: https://developer.android.com/topic/libraries/data-binding/?hl=en#java
[Android Data Binding]: https://brunch.co.kr/@oemilk/107



## 1. 정의

**데이터 바인딩 라이브러리** 

> 레이아웃의 UI 구성 요소를 프로그래밍 방식이 아닌 선언 형식을 사용하여 앱의 데이터 소스에 바인딩 할 수 있는 지원 라이브러리
>



### 1.1. 특징

- 액티비티에서 많은 UI 프레임 워크 호출을 제거 할 수 있으므로 유지 관리가 더 쉽고 간단해진다.
- 앱의 성능을 향상시키고 메모리 누수와 널 포인터 예외를 방지 할 수 있다. 
- 데이터 바인딩 라이브러리는 [Android Jetpack](https://developer.android.com/jetpack/) 아키텍처 구성 요소이다. 
- Android 2.1(API 레벨 7 이상)까지 Android 플랫폼의 모든 이전 버전에서 사용할 수 있다.
- Android Plugin for Gradle 1.5.0-alpha1 이상이 필요하다.  
- Android Studio 1.3 이상에서는 데이터 바인딩 코드를 위한 다양한 코드 편집 기능을 지원한다.
  - 구문 강조표시
  - 식 언어 구문 오류의 플래그 지정
  - XML 코드 완성
  - (선언 탐색 등의) 탐색과 빠른 문서화를 포함한 참조
- 데이터 바인딩 라이브러리는 변경 사항에 대한 데이터를 쉽게 관찰 할 수있는 클래스와 메서드를 제공한다.



### 1.2. 클래스 바인딩을 위한 새로운 데이터 바인딩 컴파일러

Android Gradle 플러그인 버전 3.1.0-alpha06 에 바인딩 클래스를 생성하는 새로운 데이터 바인딩 컴파일러가 포함되어 있다. 새로운 컴파일러는 바인딩 클래스를 점진적으로 생성한다.

- 대부분의 경우 빌드 프로세스의 속도가 빨라진다.

- 이전 버전의 데이터 바인딩 컴파일러는 코드가 컴파일되는 동일한 단계에서 바인딩 클래스를 생성했다. 따라서 코드가 컴파일되지 않으면 바인딩 클래스를 찾을 수 없다는 오류가 표시될 수 있다. 새로운 데이터 바인딩 컴파일러는 관리 컴파일러가 앱을 빌드하기 전에 바인딩 클래스를 생성하여 오류 방지한다.

- 새로운 데이터 바인딩 컴파일러를 사용하려면 gradle.properties 파일에 다음 옵션을 추가하면 된다.

  ```groovy
  android.databiding.enableV2=true
  -Pandroid.databinding.enableV2=true
  ```

  ![https://github.com/taeiim/Android-Study/blob/master/study/week8/DataBinding/gradle_properties.PNG](https://github.com/taeiim/Android-Study/blob/master/study/week8/DataBinding/gradle_properties.PNG)

- 안드로이드 플러그인 버전 3.1의 새로운 데이터 바인딩 컴파일러는 이전 버전과 호환되지 않는다. 증분 컴파일을 활용하려면 이 기능을 사용하는 모든 바인딩 클래스를 생성해야한다. 

- 안드로이드 플러그인 버전 3.2의 새로운 컴파일러는 이전 버전에서 생성된 바인딩 클래스와 호환된다. 버전 3.2의 새로운 컴파일러는 기본적으로 활성화 되어있다.



## 2. 실습

### 2.1. 빌드환경

앱 모듈의 `build.gradle` 파일에 `dataBinding` 요소를 추가

```groovy
android {
    ...
    dataBinding {
        enable = true
    }
}
```



### 2.2. Data Object - POJO(plain-old java object)

```java
public class User {
    private final String firstName;
    private final String lastName;
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
}
```



### 2.3. 데이터 바인딩 레이아웃 파일

데이터 바인딩 레이아웃 파일은 layout 의 루트 태그로 시작하고 그 뒤에 data 요소와 view 루트 요소가 나온다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout 
   xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView 
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
       <TextView 
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"/>
   </LinearLayout>
</layout>
```



### 2.4. 데이터 바인딩

데이터 바인딩 라이브러리는 레이아웃의 뷰를 데이터 객체와 바인딩하는 데 필요한 클래스를 자동으로 생성

> 파스칼 표기법(합성어의 첫 글자를 대문자로 표기)으로 변환하고 뒤에 "Binding" 접미사를 추가
>
> ex) R.layout.activity_main -> ActivityMainBinding.class

```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        User user = new User("Test", "User");
        User user1 = new User("asdf", "qwer");
        binding.setUser(user1);
    }
```



## 3. 응용

### 3.1. 이벤트 처리

표현식 언어를 사용하면 뷰에서 전달한 이벤트를 처리하는 표현식을 작성할 수 있다. 이벤트 특성 이름은 몇 가지 예외를 제외하면 리스너 메서드의 이름에 따라 결정된다.

> ex) `View.OnLongClickListener`에는 메서드 `onLongClick()`이 있으므로, 이 이벤트에 대한 특성은 `android:onLongClick`이다.

다음 메커니즘을 사용하여 이벤트를 처리 할 수 있다.

#### 3.1.1. 메소드 참조

Activity에 있는 메서드에 `android:onClick`을 할당하는 것과 비슷한 방법으로 이벤트를 핸들러 메서드에 직접 바인딩 할 수 있다. onClick 속성과 비교할 때 가장 큰 이점은 표현식이 컴파일 시점에 처리된다는 것이다. 따라서 메소드가 존재하지 않거나 서명이 올바르지 않으면 컴파일 시 오류가 발생한다.

- 핸들러에 이벤트를 지정하려면 호출 할 메소드 이름인 값으로 일반 바인딩 표현식을 사용한다.

```java
public class MyHandlers {
    public void onClickFriend(View view) { ... }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="handlers" type="com.example.MyHandlers"/>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView 
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"
           android:onClick="@{handlers::onClickFriend}"/>
   </LinearLayout>
</layout>
```

바인딩 식이 View에 대한 클릭 리스터를 할당 할 수 있다. 식에서 메서드의 서명은 Listener 객체에 있는 메서드의 서명과 정확히 일치해야 한다.



#### 3.1.2. 리스너 바인딩

리스너 바인딩은 이벤트 발생 시 실행되는 바인딩 식이다. 메서드 참조와 비슷하지만, 리스너 바인딩을 사용하면 임의의 데이터 바인딩 식을 실행할 수 있다(Android Gradle Plugin for Gradle 버전 2.0 이상에서만 사용 가능).

메서드 참조에서는 메서드의 매개변수가 이벤트 리스너의 매개변수와 일치해야한다. 리스너 바인딩에서는 반환 값은 리스너의 예상 반환 값과 일치해야 한다.

```java
public class Presenter {
    public void onSaveClick(Task task){}
}
```

```xml
<?xml version="1.0" encoding="utf-8"?><layout xmlns:android="http://schemas.android.com/apk/res/android">
<data>
    <variable
        name="task"
        type="com.android.example.Task" />
    <variable
        name="presenter"
        type="com.android.example.Presenter" />
</data>
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:onClick="@{() -> presenter.onSaveClick(task)}" />
</LinearLayout>
</layout>
```

다음과 같이 click 이벤트를 메서드에 바인딩 할 수 있다. 리스너틑 식의 루트 요소로만 허용되는 람다 식으로 표현된다. 표현식에서 콜백이 사용될 때 데이터 바인딩이 필요한 리스너를 자동으로 생성하고 이벤트에 등록한다. 뷰가 이벤트를 발생시키면 데이터 바인딩이 주어진 식을 계산한다. 정규 바인딩 식에서처럼, 이 리스너 식이 평가되는 동안 여전히 데이터 바인딩의 null 및 스레드 안전이 보장된다.

위의 예시에서는 `onClick(android.view.View)`로 전달되는 `view` 매개변수를 정의하지 않았다. 리스너 바인딩에서는 리스너 매개변수로 두 가지 중에서 선택할 수 있는데, 메서드에 대한 모든 매개변수를 무시하거나 모든 매개변수의 이름을 지정하는 것이다. 매개변수의 이름을 지정하기로 선택하면 식에 매개변수를 사용할 수 있다.



- 표현식에 매개변수를 사용할 경우에는 아래와 같이 쓸 수 있다.

```xml
android:onClick="@{(view) -> presenter.onSaveClick(task)}"
```



- 두 개 이상의 매개변수를 포함한 람다 식을 사용할 수 있다.

```java
public class Presenter {
    public void onSaveClick(View view, Task task){}
}
```

```xml
android:onClick="@{(theView) -> presenter.onSaveClick(theView, task)}"
```



- 수신 중인 이벤트가 void 형식이 아닌 값을 반환하는 경우, 식도 그와 동일한 형식의 값을 반환해야 한다. 예를 들어, LongClick 이벤트를 수신하려는 경우에는 식에서 boolean을 반환해야 한다.

```java
public class Presenter {
    public void onCompletedChanged(Task task, boolean completed){}
}
```

```xml
<CheckBox 
	android:layout_width="wrap_content" 
	android:layout_height="wrap_content"
	android:onCheckedChanged="@{(cb, isChecked) -> presenter.completeChanged(task, isChecked)}" />
```



### 3.2. 레이아웃 세부정보

data 요소 내에서는 import 요소가 전혀 사용되지 않거나 한 개 이상 사용될 수 있다. java에서와 마찬가지로, 이러한 요소를 사용하여 레이아웃 파일 내에 있는 클래스를 쉽게 참조할 수 있다.

```xml
<data>
    <import type="android.view.View"/>
</data>

<TextView
   android:text="@{user.lastName}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:visibility="@{user.isAdult ? View.VISIBLE : View.GONE}"/>
```

클래스 이름 간에 충돌이 발생할 때 해당 클래스 중 하나의 이름을 다음과 같이 바꿀 수 있다.

```xml
<import type="android.view.View"/>
<import type="com.example.real.estate.View"
        alias="Vista"/>
```



더 많은 정보들은 아래 클릭 ↓

https://developer.android.com/topic/libraries/data-binding/expressions



### 3.3. 데이터 객체(Observable)

데이터 바인딩에 임의의 POJO를 사용할 수 있지만, POJO를 수정하더라도 UI가 업데이트 되지는 않는다. 데이터가 변경될 때 이를 알려주는 기능을 데이터 객체에 부여하면 데이터 바인딩의 진정한 강점을 활용할 수 있다.



#### 3.3.1. Observable 객체

`android.databinding.Observable` 인터페이스를 구현하는 클래스를 사용하면 바인딩이 바인딩된 객체에 단일 리스너를 연결하여 그 객체에 대한 모든 속성의 변경 사항을 수신할 수 있다. 직접 개발할 수도 있지만, Observable 인터페이스를 구현한 BaseObservable 클래스를 이용하면 더 쉽게 개발할 수 있다. getter에는 `@Bindable` 주석을 할당하고 setter에는 `nofityPropertyChanged(int fieldId)` 를 통해 속성 변경을 알릴 수 있다.

```java
private static class User extends BaseObservable {
   private String firstName;
   private String lastName;
    
   @Bindable
   public String getFirstName() {
       return this.firstName;
   }
   @Bindable
   public String getLastName() {
       return this.lastName;
   }
   public void setFirstName(String firstName) {
       this.firstName = firstName;
       notifyPropertyChanged(BR.firstName);
   }
   public void setLastName(String lastName) {
       this.lastName = lastName;
       notifyPropertyChanged(BR.lastName);
   }
}
```

`@Bindable` 주석은 컴파일 중에 BR 클래스 파일에 항목을 생성한다. BR 클래스 파일은 모듈 패키지에 생성된다.



#### 3.3.2. ObservableField

`android.databinding.Observable` 클래스를 생성하려면 약간의 작업이 필요하다. 하지만 아래와 같은 속성이 있으면 더 쉽게 개발할 수 있다.

- `android.databinding.ObservableField ` 와 형제들인
- `android.databinding.ObservableBoolean`
- `android.databinding.ObservableByte`
- `android.databinding.ObservableChar`
- `android.databinding.ObservableShort`
- `android.databinding.ObservableInt`
- `android.databinding.ObservableLong`
- `android.databinding.ObservableFloat`
- `android.databinding.ObservableDouble`
- `android.databinding.ObservableParcelable`을 사용할 수 있다.  

`ObservableFields`는 단일 필드를 가진, 자체 포함 방식의 Observable 객체이다. 원시 버전은 액세스 작업 중에 boxing과 unboxing을 방지한다. 따라서 이를 사용하려면 다음과 같이 데이터 클래스에 public final 필드를 생성해야한다. 

```java
private static class User {
   public final ObservableField<String> firstName =
       new ObservableField<>();
   public final ObservableField<String> lastName =
       new ObservableField<>();
   public final ObservableInt age = new ObservableInt();
}
```

값에 엑세스하려면 set, get 접근자 메서드를 사용한다.

```java
user.firstName.set("Google");
int age = user.age.get();
```



#### 3.3.3. Observable 컬렉션

일부 애플리케이션에서는 데이터 유지를 위해 더욱 동적인 구조체를 사용한다. Observable 컬렉션을 사용하면 이러한 데이터 객체에 키 입력 방식으로 액세스할 수 있다. 

- 키가 String과 같은 참조 형식일 때는  `android.databinding.ObservableArrayMap`이 유용하다. 

```java
ObservableArrayMap<String, Object> user = new ObservableArrayMap<>();
user.put("firstName", "Google");
user.put("lastName", "Inc.");
user.put("age", 17);
```



- 레이아웃에서 다음과 같이 String 키를 통해 맵에 엑세스 할 수도 있다.

```xml
<data>
    <import type="android.databinding.ObservableMap"/>
    <variable name="user" type="ObservableMap&lt;String, Object&gt;"/>
</data>
…
<TextView
   android:text='@{user["lastName"]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
<TextView
   android:text='@{String.valueOf(1 + (Integer)user["age"])}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```



- 키가 정수일 때는 `android.databinding.ObservableArrayList`가 유용하다. 

```java
ObservableArrayList<Object> user = new ObservableArrayList<>();
user.add("Google");
user.add("Inc.");
user.add(17);
```



- 레이아웃에서는 인덱스를 통해 리스트에 엑세스할 수 있다.

```xml
<data>
    <import type="android.databinding.ObservableList"/>
    <import type="com.example.my.app.Fields"/>
    <variable name="user" type="ObservableList&lt;Object&gt;"/>
</data>
…
<TextView
   android:text='@{user[Fields.LAST_NAME]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
<TextView
   android:text='@{String.valueOf(1 + (Integer)user[Fields.AGE])}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

