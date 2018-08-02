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
       <variable 
           	name="user" 
            type="com.example.User"/>
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



### 2.4. 데이터바인딩

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

데이터 바인딩을 사용하여 뷰에서 발송되는 이벤트를 처리하는 식을 작성 할 수 있다.





