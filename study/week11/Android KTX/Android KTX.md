# Android Kotiln Extensions 와 Anko

## 1. Android KTX

요번 Google I/O 2018 에서 발표한 Android Jetpack 에 Android KTX 가 포함 되었다.  
Android KTX 의 목적은 Kotlin의 간결함과 기능을 활용하여 Android 개발을 보다 간결하고 쾌적하고 관용적으로 만드는 것이다.  

### 1-1. 시작하기
Android KTX 를 프로젝트에 사용하기 위해서는 build.gradle 파일에 아래처럼 추가 한다.
```Gradle
repositories {
    google()
}
```

build.gradle(Project: 프로젝트명) 부분

```Gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.0.0-alpha1'
}
```
build.gradle(Module: app) 부분

```Kotlin
import kotlinx.android.synthetic.main.<layout>.*
```
위에 처럼 import 해서 사용하자

### 1-2. KTX 를 사용시 생기는 마법들

자, 그렇다면 언제 어디서 어떻게 KTX 를 유용하게 쓸 수 있을까?

대표적인 예로 기존에 우리가 자주 사용했던 findViewById() 메서드에서 나타난다.

findViewById() 는 액티비티나 프래그먼트 등 레이아웃 파일에 선언된 여러개의 뷰(view)로 구성된 화면에서 특정 뷰의 인스턴스를 얻기위해 사용한다.  

그러나 문제점은 만약 이 메서드에서 반환한 뷰 객체를 잘못된 타입의 뷰로 캐스팅하거나 다른 레이아웃에 선언된 ID를 잘못 사용하면 널 값을 반환하게 되는데,

이는 실수로 버그를 발생시키기 매우 쉬워 우리들에게 번거롭게 한다.

뿐만 아니라 가독성도 장난 아니게 떨어뜨린다.  
왜냐하면 복잡한 구조로 구성된 화면에서 일일이 findViewByID() 를 뷰 인스턴스를 받아야 하기 때문에 코드만 몇십 줄을 차지하게 되기 때문이다. (답도 없다)

여기서 왜 findViewByID() 대한 나쁜 얘기를 꺼낸 이유는 무엇일까? 

당연히 Adroid KTX 는 이런 부분들을 깔끔하게 해결해 주기 때문이다.

Android KTX 는 뷰의 ID 를 사용하여 해당 뷰의 인스턴스에 바로 접근 할 수 있게 해준다.  
다시말해, findViewByID() 때문에 생긴 몇십 줄의 코드를 비약적으로 줄일 수 있다는 것이다.


```Kotlin
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // findViewById() 을 사용안해도 뷰의 ID 에 접근해 사용가능하다.
        text.text = "hi"
        text.setOnClickListener{
            Toast.makeText(applicationContext, "hi", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### 1-3. 더 나아가기
