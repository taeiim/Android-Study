# Data Binding

> 발표주제 : Data Binding
>
> 발표일자 : 2018-07-30 (월)
>
> 발표자 : 송시은



## 0. 참고문서

https://developer.android.com/topic/libraries/data-binding/?hl=ko

https://brunch.co.kr/@oemilk/107



## 1. 정의

데이터 바인딩 라이브러리는 유연성과 폭넓은 호환성을 모두 제공하는 지원 라이브러리이다.

Android 2.1(API 레벨 7 이상)까지 Android 플랫폼의 모든 이전 버전에서 사용할 수 있다.

Android Plugin for Gradle 1.5.0-alpha1 이상이 필요하다.  

Android Studio 1.3 이상에서는 데이터 바인딩 코드를 위한 다양한 코드 편집 기능을 지원한다.

- 구문 강조표시
- 식 언어 구문 오류의 플래그 지정
- XML 코드 완성
- (선언 탐색 등의) 탐색과 빠른 문서화를 포함한 참조



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

바인딩 클래스는 레이아웃 파일의 이름을 기준으로 자동 생성된다.

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











