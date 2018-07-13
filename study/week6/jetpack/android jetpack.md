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



###3.Animation 

#### 개요

애니매이션은 사용자에게 앱의 진행 상황을 알리는 시각적 단서를 추가 할 수  있음

UI가 상태를 바꿀때 유용함(예 새 내용로드 또는 새 작업을 할 때)

더 높은 품질의 보여지는 것 처럼 느낌을 제공함

#### Animate bitmaps

아이콘이나 일러스트레이션과 같은 비트 맵 그래픽을 애니메이션으로 만들려면 drawable animation API를 사용해야 한다.

일반적으로 이러한 애니메이션은 drawable resource로 정적으로 정의 됨 

runtime에 애니메이션에 대한 행동을 정의 할 수 있다. 

예를 들어 재생 버튼을 탭하며 일시 중지 버튼으로 변환되어 두 작업이 관련되어 있다는 것을 사용자에게 알리는 좋은 방법이면 다른 하나를 누리면 다른 버튼이 표시됩

#####AnimationDrawable 사용

애니메이션을 만드는 한 가지 방법은 Drawables폴더의 사진을 Drawble  resouce에 차래로 로드하여 애니매이션을 만드는 것이다. 이것은 필름 롤 처럼 순서대로 재생되는 점에서 전통적인 애니매이션이다.  AmimationDrawble 클래스는 Drawable 애니메이션의 기초이다.

AnimationDrawable 클래스 API를 사용하여 코드에서 애니메이션 프레임을 정의 할 수는 있지만 애니메이션을 구성하는 프레임을 나열하는 xml파일로 간단하게 사용할 수 있습니다.

xml파일은 ```<animation-list>```루트 노드의 요소와 ```<item>``` 각 프레임을 정의하는 일련의 하위 노드로 구성됨

```
<animation-list xmlns:android="http://schemas.android.com/apk/res/android"
    android:oneshot="true">
    <item android:drawable="@drawable/rocket_thrust1" android:duration="200" />
    <item android:drawable="@drawable/rocket_thrust2" android:duration="200" />
    <item android:drawable="@drawable/rocket_thrust3" android:duration="200" />
</animation-list>
```

```android:oneshot``` 의 속성을 true로 설정하면 프레임이 한 번만 순환 한 후 마지막 프레임을 중지하고 유지 합니다. false로 설정하면 애니메이션이 반복됩니다.

```
AnimationDrawable rocketAnimation;

public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.main);

  ImageView rocketImage = (ImageView) findViewById(R.id.rocket_image);
  rocketImage.setBackgroundResource(R.drawable.rocket_thrust);
  rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
//이벤트 처리 리스너 메소드를 통해서 특정 이벤트 발생시 start() 메소드로 애니메이션을 실행하는 것이 가능하다.
  rocketImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        rocketAnimation.start();
      }
  });
}
```

```rocketAnimation.start()```을 하면 애니메이션이 실행이 됩니다.

주의사항

            - ```start()``` 메소드는 ```onCreate()``` 에서는 실행이 불가능하기 때문에 처음부터 애니메이션이 실행되는 것을
                원한다면 ```onWindowFocusChanged()``` 메소드를 오버라이딩해서 실행해주는 것이 가능하다.

