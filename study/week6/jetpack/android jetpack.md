## android jetpack

### 1.android jetpack이란?

- android앱을 만들기 위한 컴포너트, 도구 및 지침 세트

- 기존 지원 라이브러리와 아키텍처 컴포너트를 하나로 모아서, 다음과 같이 네가지 범주로 나타냄

  ![jetpack](https://raw.githubusercontent.com/taeiim/Android-Study/master/study/week6/jetpack/image/jetpack.PNG)

  ​

android jetpack은 기본 Android 플랫폼에 속하지 않은 "별도의" 라이브러리로서 제공된다. 

또한 android jetpack은 특정 버전에 관계 없이 기능을 제공하도록 빌드되고 이전 버전과의 호환성을 제공하기 때문에, 다양한 버전의 플랫폼에서 앱을 실행할 수 있다.

#### 새롭게 추가 된 것

+ WorkManager
+ Navigation
+ Paging
+ Slices
+ Android KTX

####용어정리

- 컴포넌트: 유저가 사용하는 시스템에 대한 조작장치

### 2. Permission

- Android 6.0 Marshmallow 이후, 선택적인 권한 부여 방식 채택
- 6.0미만 버전에서는 androidMainfest.xml에 권하는 설정하는 것만으로 영구적으로 앱의 권한을 선택할 수 있었지만, 6.0이상에서는 비동기, 콜뱅 형태로 앱 실행 시에 앱 권한을 언제든지 시스템 설정에서 취소할 수 있으므로, 매번 특정 권한이 필요한 기능을 실행하기 전에 반디시 권한 획득 여부를 확인해야 한다는 점이 있다.

#### 앱 권한 획득 순서

![앱 권한 획득 순서](https://raw.githubusercontent.com/taeiim/Android-Study/master/study/week6/jetpack/image/%EC%95%B1%EA%B6%8C%ED%95%9C%ED%9A%8D%EB%93%9D%EC%88%9C%EC%84%9C.PNG)

먼저 AndroidMainfest.xml에 권한을 설정한 후

```
<uses-permission android:name="android.permission.CALL_PHONE" />
```

사용자의 os 버전이 마시멜로우 이상인지 체크한다.

```
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { 

}
```



###3. 