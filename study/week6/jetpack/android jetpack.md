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

#####- AnimationDrawable 사용

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

- ```start()``` 메소드는 ```onCreate()``` 에서는 실행이 불가능하기 때문에 처음부터 애니메이션이 실행되는 것을 원한다면 ```onWindowFocusChanged()``` 메소드를 오버라이딩해서 실행해주는 것이 가능하다.

     ​

#### Physics-based motion

일반적인 물리 기반 애니메이션은 다음과 같다.

+ Spring animation

  + Lifecycle of a spring animation(스프링 애니메이션 생명주기)

    + 스프링 기반 애니메이션에서 SpringForce 클래스를 사용하면 스프링의 강성, 감쇠 비율 및 최종 위치를 사용자 정의 할 수 있음
    + 애니메이션이 시작 되 자마자 스프링 력은 각 프레임의 애니메이션 값과 속도를 업데이트합니다.
    + 애니메이션은 스프링 력이 평형에 도달 할 때까지 계속됩니다.
    + 예를 들어 화면에서 앱 아이콘을 드래그 한 다음 아이콘에서 손가락을 떼어 놓으면 나중에 보이지 않지만 익숙한 방식으로 원래 위치로 돌아갑니다.

  + Build a spring animation(스프링 애니메이션 만들기)

    + 스프링 애니메이션 클래스를 사용하려면 프로젝트에 지원 라이브러리를 추가해야합니다.

      ```
      dependencies {
            implementation 'com.android.support:support-dynamic-animation:27.1.1'
        }
      ```

      ​

    + 기본 단계는 SpringAnimation 클래스의 인스턴스를 만들고 모션 동작 매개 변수를 설정한다

      ```
      final View img = findViewById(R.id.imageView);
      // 뷰의 translationY 속성에 애니메이션을 적용하는 스프링 애니메이션 설정
      // 0에서 스프링 위치
      final SpringAnimation springAnim = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 0);
      ```

      스프링 기반 애니메이션은 뷰 객체의 실제 속성을 변경하여 화면에서 뷰의 애니메이션을 만들 수 있습니다. 시스템에서 다음보기를 사용할 수 있습니다.

      + `TRANSLATION_X`, `TRANSLATION_Y`: 이 속성은 레이아웃 컨테이너에서 설정 한 왼쪽 좌표, 위쪽 좌표 및 고도에서 델타로 뷰가있는 위치를 제어합니다.

    + Demping  ratio(감쇠 비율)

       감쇠 비율을 사용하여 진동이 한 바운스에서 다음 바운스로 얼마나 빨리 감소하는지 정의 할 수 있습니다

      + getSpring () 메서드를 호출하여 감쇠 비율을 추가 할 스프링을 검색한다 
      + setDampingRatio () 메서드를 호출하고 스프링에 추가 할 감쇠 비율을 전달한다. 메서드는 감쇠 비율이 설정된 스프링 력 객체를 반환합니다.
        + 1.`DAMPING_RATIO_HIGH_BOUNCY`
        + 2.`DAMPING_RATIO_MEDIUM_BOUNCY`
        + 3.`DAMPING_RATIO_LOW_BOUNCY`
        + 4.`DAMPING_RATIO_NO_BOUNCY`



<img src="https://github.com/taeiim/Android-Study/blob/master/study/week6/jetpack/image/high_bounce.gif?raw=true" width="200" ><img src="https://github.com/taeiim/Android-Study/blob/master/study/week6/jetpack/image/medium_bounce.gif?raw=true" width="200" ><img src="https://github.com/taeiim/Android-Study/blob/master/study/week6/jetpack/image/low_bounce.gif?raw=true" width="200" ><img src="https://github.com/taeiim/Android-Study/blob/master/study/week6/jetpack/image/no_bounce.gif?raw=true" width="200" >



기본 감쇠 비율은 ``` DAMPING_RATIO_MEDIUM_BOUNCY```설정됩니다.

```
final View img = findViewById(R.id.imageView);
final SpringAnimation anim = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y);
…
//Setting the damping ratio to create a low bouncing effect.
anim.getSpring().setDampingRatio(DAMPING_RATIO_MEDIUM_BOUNCY);
```





