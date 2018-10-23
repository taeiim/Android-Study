# Kotlin Anko
# Kotlin-Anko
Kotlin의 라이브러리 Anko를 좀 더 자세히 공부합니다.

## Anko란?
* Kotlin으로 작성된 DSL ( Domain-specific-Language) 입니다.
* 기존에 Android에서는 View를 디자인 할때 XML Layout을 통해서 표현하였습니다. 
* Anko의 공식적으로 발표된 자료에는 Android 개발을 좀더 쉽고 빠르게 개발할수 있는 라이브러리라고 소개되어 있습니다. 
* 기존의 XML 을 사용하게 되면 Java코드로 변환하는 작업을 하게 되면서 CPU나 배터리 소모를 하고 코드의 재활용이 불편하다는 단점이 있습니다. 
* Anko는 이한 단점을 라이브러리로 가독성은 높이고 XML 파싱에 따른 오버헤드를 없앨 수 있습니다.

## Anko의 종류
* Anko Commons
* Anko Loayouts
* Anko SQLite
* Anko Coroutines

## Build 추가
* 우선 Anko 를 사용하려면 Gradle추가 작업이 필요합니다. 2018 - 05 - 17 에서는 '0.10.5'버전까지 사용할수 있습니다.
~~~kotlin
dependencies {
    implementation "org.jetbrains.anko:anko:$anko_version"
}
~~~

# Anko Commons
## 의존성
* Anko Commons은 안드로이드 애플리케이션을 작성할 때 일반적으로 자주 구현하는 기능을 간편하게 추가할 수 있는 유틸리티 함수를 제공합니다. 
* Anko Commons을 사용하여면 이를 사용할 모듈의 빌드스크립트에 의존성을 추가하면 됩니다.
~~~gradle
// build.gradle

android {
    ...
}

dependencies {

    // Anko Commons 라이브러리를 추가합니다.
    implementation "org.jetbrains.anko:anko-commons:0.10.2"

    ...
}
~~~
* 서포트 라이브러리에 포함된 클래스를 사용하는 경우, 필요에 따라 anko-appcompat-v7-commons 혹은 anko=support-v4-commons을 빌드스크립트 내 의존성을 추가하면 됩니다.
~~~gradle
// build.gradle

android {
    ...
}

dependencies {

    // Anko Commons 라이브러리를 추가합니다.
    implementation "org.jetbrains.anko:anko-commons:0.10.2"

    // appcompat-v7용 Anko Commons 라이브러리를 추가합니다.
    implementation "org.jetbrains.anko:anko-appcompat-v7-commons:0.10.2"

    // support-v4용 Anko Commons 라이브러리를 추가합니다.
    implementation "org.jetbrains.anko:anko-support-v4-commons:0.10.2"
}
~~~
## 토스트 표시하기
* toast() 및 toastLong() 함수를 사용하면 토스트 메시지를 간편하게 표시할 수 있습니다.
* 토스트를 표시하려면 Context 클래스의 인스턴스가 필요하므로, 이 클래스 혹은 이를 상속하는 클래스(액티비티, 프래그먼트)내부에서만 사용할 수 있습니다.
~~~kotlin
// 다음 코드와 동일한 역할을 합니다.
// Toast.makeText(Context, "Hello, Kotlin!", Toast.LENGTH_SHORT).show()
toast("Hello, Kotlin!")

// 다음 코드와 동일한 역할을 합니다.
// Toast.makeText(Context, R.string.hello, Toast.LENGTH_SHORT).show()
toast(R.string.hello)

// 다음 코드와 동일한 역할을 합니다.
// Toast.makeText(Context, "Hello, Kotlin!", Toast.LENGTH_LONG).show()
longToast("Hello, Kotlin!)
~~~
## 다이얼로그 생성 및 표시하기
* alert() 함수를 사용하면 AlertDialog를 생성할 수 있습니다. 
* 토스트와 마찬가지로 Context 클래스 혹은 이를 상속하는 클래스(액티비티, 프래그먼트) 내부에서만 사용할 수 있습니다.
~~~kotlin
// 다이얼로그의 제목과 본문을 지정합니다.
alert(title = "Message", message = "Let's learn Kotlin!") {

    // AlertDialog.Builder.setPositiveButton()에 대응합니다.
    positiveButton("Yes") {
        // 버튼을 클릭했을 때 수행할 동작을 구현합니다.
        toast("Yay!")
    }

    // AlertDialog.Builder.setNegativeButton()에 대응합니다.
    negativeButton("No") {
        // 버튼을 클릭했을 때 수행할 동작을 구현합니다.
        longToast("No way...")
    }
}.show()
~~~
* 프레임워크에서 제공하는 다이얼로그가 아닌 서포트 라이브러리에서 제공하는 다이얼로그(android.support.v7.app.AlertDialog)를 생성하려면,
* anko-appcompat-v7-commons을 의존성에 추가한 후 다음과 같이 Appcompat을 함께 인자에 추가하면 됩니다.
~~~kotlin
// import 문이 추가됩니다.
import org.jetbrains.anko.appcompat.v7.Appcompat

// Appcompat을 인자에 추가합니다.
alert(Appcompat, title = "Message", message = "Let's learn Kotlin!") {
    ...
}.show()
~~~
* 여러 항목 중 하나를 선택하도록 할 떄 사용하는 리스트 다이얼로그는 selector() 함수를 사용하여 생성할 수 있습니다.
~~~kotlin
// 다이얼로그에 표시할 목록을 생성합니다.
val cities = listOf("Seoul", "Tokyo", "Mountain View", "Singapore")

// 리스트 다이얼로그를 생성하고 표시합니다.
selector(title = "Select City", items = cities) { dig, selection -> 
    
    // 항목을 선택했을 때 수행할 동작을 구현합니다.
    toast("You selected ${cities[selection]}!")
}
~~~
* 작업의 진행 상태를 표시할 때 사용하는 프로그레스 다이얼로그는progressDialog()와 indeterminateProgressDialog() 함수를 사용하여 생성할 수 있습니다.
* progressDialog() 함수는 파일 다운로드 상태와 같이 진행률을 표시해야 하는 다이얼로그를 생성할 때 사용합니다. 
* indeterminateProgressDialog() 함수는 진행률을 표시하지 않는 다이얼로그를 생성할 때 사용합니다.
~~~kotlin
// 진행률을 표시하는 다이얼로그를 생성합니다.
val pd = progressDialog(title = "File Download", message = "Downloading...")

// 다이얼로그를 표시합니다.
pd.show()

// 진행률을 50으로 조정합니다.
pd.progress = 50

// 진행률을 표시하지 않는 다이얼로그를 생성하고 표시합니다.
indenterminateProgressDialog(message = "Please wait...").show()
~~~
## 인텐트 생성 및 사용하기
* 인텐트는 컴포넌트 간에 데이터를 전달할 때에도 사용하지만 주로 액티비티나 서비스를 실행하는 용도로 사용합니다.
* 다른 컴포넌트를 실행하기 위해 인텐트를 사용하는 경우, 이 인텐트는 대상 컴포넌트에 대한 정보와 기타 부가 정보를 포함합니다.
~~~kotlin
// DetailActivity 액티비티를 대상 컴포넌트로 지정하는 인텐트
val intent = Intent(this, DetailActivity::class.java)

// DetailActivity를 실행합니다.
startActivity(intent)
~~~
* 이 인텐트에 부가 정보를 추가하거나 플래그를 설정하는 경우 인텐트를 생성하는 코드는 다음과 같습니다.
~~~kotlin
val intent = Intent(this, DetailActivity::class.java)

// 인텐트에 부가정보를 추가합니다.
intent.putExtra("id", 150L)
intent.putExtra("title", "Awesome item")

// 인텐트에 플래그를 생성합니다.
intent.setFlag(Intent.FLAG_ACTIVITY_NO_HISTORY)
~~~
* intentFor() 함수를 사용하면 훨씬 간소한 현태로 동일한 역할을 하는 인텐트를 생성할 수 있습니다.
~~~kotlin
val intent = intentFor<DetailActivity>(
    // 부가 정보를 Pair 형태로 추가합니다.
    "id" to 150L, "title" to "Awesome item")

    // 인텐트 플래그를 설정합니다.
    .noHistory()
~~~
* 인텐트에 플래그를 지정하지 않는다면, startActivity() 함수나 startService() 함수를 사용하여 인텐트 생성과 컴포넌트 호출을 동시에 수행할 수 있습니다.
* 이들 함수는 모두 Context 클래스를 필요로 하므로, 이 클래스 혹은 이를 상속하는 클래스(액티비티, 프래그먼트) 내부에서만 사용할 수 있습니다.
~~~kotlin
// 부가정보 없이 DetailActivity를 실행합니다.
startActivity<DetailActivity>()

// 부가정보를 포함하여 DetailActivity를 실행합니다.
startActivity<DetailActivity>("id" to 150L, "title" to "Awesome item")

// 부가정보 없이 DataSyncService를 실행합니다.
startService<DataSyncService>()

// 부가정보를 포함하여 DataSyncService를 실행합니다.
startService<DataSyncService>("id" to 1000L)
~~~
* 이 외에도, 자주 사용하는 특정 작업을 바로 수행할 수 있는 함수들을 제공합니다.
~~~kotlin
// 전화를 거는 인텐트를 실행합니다.
makeCall(number = "01012345678")

// 문자메시지를 발송하는 인텐트를 실행합니다.
sendSMS(number = "01012345678", text = "Hello, Kotlin!")

// 웹 페이지를 여는 인텐트를 실행합니다.
browse(url = "https://google.com")

// 이메일을 발송하는 인텐트를 실행합니다.
email(email = "jyte82@gmail.com", subject = "Hello, Taeho Kim", text = "How are you?")
~~~
## 로그 메시지 기록하기
* 안드로이드 애플리케이션에서 로그메시지를 기록하려면 android.util.Log 클래스에서 제공하는 메서드를 사용해야 합니다. 
* 하지만 로그를 기록하는 함수를 호출할 때마다 매번 태그를 함께 입력해야 하므로 다소 불편합니다.
* Anko 라이브러리에서 제공하는 AnkoLogger를 사용하면 훨씬 편리하게 로그 메시지를 기록할 수 있습니다.
* AnkoLogger에서는 다음과 같이 android.util.Log 클래스의 로그 기록 메서드에 대응하는 함수를 제공합니다.

|android.util.Log|AnkoLogger|
|----------------|----------|
|v()|verbose()|
|d()|debug()|
|i()|info()|
|w()|warn()|
|e()|error()|
|wtf()|wtf()|
* AnkoLogger를 사용하려면 이를 사용할 클래스에서 AnkoLogger 인터페이스를 구현하면 됩니다. 
* AnkoLogger 인터페이스를 구현한 액티비티에서의 사용 예는 다음 코드에서 확인할 수 있습니다.
* 출력할 메시지의 타입으로 String만 허용하는 android.util.Log 클래스와 달리 모든 타입을 허용하는 모습을 확인할 수 있습니다.
~~~kotlin
// AnkoLogger 인터패이스를 구현합니다.
class MainActivity: AppCompatActivity(), AnkoLogger {

    fun doSomething() {
        // Log.INFO 레벨로 로그 메시지를 기록합니다.
        info("doSomething() called")
    }

    fun doSomethingWithParameter(number: Int) {
        // Log.DEBUG 레벨로 로그 메시지를 기록합니다.
        // String 타입이 아닌 인자는 해당 인자의 toString() 함수 변환값을 기록합니다.
        debug(number)
    }
    ...
}
~~~
* AnkoLogger에서 제공하는 함수를 사용하여 로그 메시지를 기록하는 경우, 로그 태그로 해당 함수가 호출되는 클래스의 이름을 사용합니다.
* 따라서 앞의 예제에서는 "MainActivity"를 로그 태그로 사용합니다.
* 로그 태그를 바꾸고 싶다면 loggerTag 프로퍼티를 오버라이드하면 됩니다.
~~~kotlin
class MainActivity: AppCompatActivity(), AnkoLogger {
    // 이 클래스 내에서 출력되는 로그 태그를 "Main"으로 지정합니다.
    override val loggerTag: String
        get() = "Main"

    ...
}
~~~
## 단위 변환하기
* 안드로이드는 다양한 기기를 지원하기 위해 픽셀(px) 단위 대신 dip(혹은 dp; device independent pixels)나 sp(scale independent pixels)를 사용합니다.
* dp나 sp 단위는 각 단말기의 화면 크기나 밀도에 따라 화면에 표시되는 크기를 일정 비율로 조정하므로, 다양한 화면 크기나 밀도를 가진 단말기에 대응하는 UI를 작성할 때 유용합니다.
* 커스텀 뷰 내뷰와 같이 뷰에 표시되는 요소의 크기를 픽셀 단위로 다루는 경우 dp나 sp단위를 픽셀 단위로 변환하기 위해 복잡한 과정을 거쳐야 합니다.
~~~kotlin
class MainActivity : AppCompatActivity() {
    fun doSomething() {
        // 100dp를 픽셀 단위로 변환합니다.
        val dpInPixel = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics)

        // 16sp를 픽셀 단위로 변환합니다.
        val spInPixel = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics)
    }
    ...
}
~~~
* Anko에서 제공하는 dip() 및 sp() 함수를 사용하면 이러한 단위를 매우 간단히 변환할 수 있습니다.
* 단위를 변환하기 위해 단말기의 화면 정보를 담고 있는 DisplayMetrics 객체가 필요하므로, 이 함수들은 단말기 화면 정보에 접근할 수 있는 클래스인 Context를 상속한 클래스 혹은 커스텀 뷰 클래스 내에서 사용할 수 있습니다.
* dip() 함수 및 sp() 함수를 사용하여 앞의 코드를 간단하게 표현한 모습입니다.
* TypedValue.applyDimension() 메서드는 Float 형 인자만 지원했지만, dip() 및 sp() 함수는 Int 형 인자도 지원합니다.
~~~kotlin
// 100dp를 픽셀 단위로 변환합니다.
val dpInPixel = dip(100)

// 16sp를 픽셀 단위로 변환합니다.
val spInPixel = sp(16)
~~~
* 반대로, 픽셀 단위를 dp나 sp 단위로 변환하는 함수도 제공합니다. 각각 px2dip(), px2sp() 함수를 사용합니다.
~~~kotlin
// 300px를 dp 단위로 변환합니다.
val pxInDip = px2dip(300)

// 80px를 sp 단위로 변환합니다.
val pxInSp = px2sp(80)
~~~
## 기타
* 여러 단말기 환경을 지원하는 애플리케이션은, 단말기 환경에 따라 다른 형태의 UI를 보여주도록 구현하는 경우가 많습니다.
* 이러한 경우, configuration() 함수를 사용하면 특정 단말기 환경일 때만 실행할 코드를 간단하게 구현할 수 있습니다.

|매개변수 이름|단말기 환경 종류|
|---------|----------|
|density|화면 밀도|
|language|시스템 언어|
|long|화면 길이|
|nightMode|야간모드 여부|
|orientation|화면 방향|
|rightToLeft|RTL(Right-to-Left)레이아웃 여부
|screenSize|화면 크기|
|smallestWidth|화면의 가장 작은 변의 길이|
|uiMode|UI 모드(일반, TV, 차량, 시계, VR 등)|
* configuration() 또한 단말기 환경에 접근해야 하므로 이 정보에 접근할 수 있는 Context 클래스 혹은 이를 상속한 클래스(액티비티, 프래그먼트)에서만 사용할 수 있습니다.
~~~kotlin
class MainActivity : AppCompatActivity() {
    fun doSomething() {
        configuration(orientation = Orientation.PORTRAIT) {
            // 단말기가 세로 방량일 때 수행할 코드를 작성합니다.
            ...
        }
        configuration(orientation = Orientation.LANDSCAPE, language = "ko") {
            // 단말기가 가로 방향이면서 시스템 언어가 한국어로 설정되어 있을 때
            // 수행할 코드를 작성합니다.
            ...
        }
    }
    ...
}
~~~
* 단순히 단말기의 OS 버전에 따라 분기를 수행하는 경우 doFromSdk()와 doIfSdk()를 사용할 수 있습니다.
~~~kotlin
doFromSdk(Build.VERSION_CODES.0) {
    // 안드로이드 8.0 이상 기기에서 수행할 코드를 작성합니다.
    ...
}

doIfSdk(Build.VERSION_CODES.N) {
    // 안드로이드 7.0 기기에서만 수행할 코드를 작성합니다.
    ...
}
~~~

# Anko Layouts
* 안드로이드 애플리케이션을 작성할 때, 대부분 XML 레이아웃을 사용하여 화면을 구성합니다.
* 소스 코드(Java 혹은 Kotlin)를 사용하여 화면을 구성하는 것도 가능하지만 XML 레이아웃에 비해 복잡하고 까다라로워 대다수의 사람들이 선호하지 않습니다.
* 하지만 XML로 작성된 레이아웃을 사용하려면 이 파일에 정의된 뷰를 파싱하는 작업을 먼저 수행해야 합니다.
* 때문에 소스 코드를 사용하여 화면을 구성한 경우에 비해 애플리케이션의 성능이 저하되고, 파싱 과정에서 자원이 더 필요한 만큼 배터리도 더 많이 소모합니다.
* Anko Layouts는 소스 코드로 화면을 구성할 때 유용하게 사용할 수 있는 여러 함수들을 제공하며, 아룰 사용하면 XML 레이아웃을 작성하는 것처럼 편리하게 소스코드로도 화면을 구성할 수 있습니다.
* Anko Layouts을 사용하려면 이를 사용할 모듈의 빌드스크립트에 의존성을 추가하면 되며, 애플리케이션의 minSdkVersion에 따라 사용하는 라이브러리가 달라집니다.
* 애플리케이션의 minSdkVersion에 대응하는 Anko Layouts 라이브러리는 다음과 같습니다.

|minSdkVersion|Anko Layouts 라이브러리|
|---------|----------|
|15이상 19미만|anko-sdk15|
|19이상 21미만|anko-sdk19|
|21이상 23미만|anko-sdk21|
|23이상 25미만|anko-sdk23|
|25이상|anko-sdk25|
~~~gradle
android {
    defaultConfig {
        // minSdkVersion이 15로 설정되어 있습니다.
        minSdkVersion 15
        targetSdkVersion 27
        ...
    }
    ...
}

dependencies {
    // minSdkVersion에 맞추어 Anko Layouts 라이브러리를 추가합니다.
    compile "org.jetbrains.anko:anko-sdk15:0.10.2"
}
~~~
* 서프트 라이브러리에 포함된 뷰를 사용하는 경우,  사용하는 뷰가 포함된 라이브러리에 대응하는 Anko Layouts 라이브러리를 의존성에 추가하면 됩니다.
* 각 서프트 라이브러리에 대응하는 Anko Layouts 라이브러리는 다음과 같습니다.
|서포트 라이브러리|Anko Layouts 라이브러리|
|---------|----------|
|appcompat-v7|anko-appcompat-v7|
|cardview|anko-cardview-v7|
|design|anko-design|
|gridlayout|anko-gridlayout-v7|
|recyclerview-v7|anko-recyclerview-v7|
|support-v4|anko-support-v4|
* 다음은 서포트 라이브러리와 이에 대응하는 Anko Layouts 라이브러리를 의존성으로 추가한 예입니다.
~~~gradle
android {
    ...
}

dependencies {
    // appcompat-v7 서포트 라이브러리 추가
    implementation "com.android.support:appcompat-v7:27.0.1"

    // appcompat-v7용 Anko Layouts 라이브러리를 추가합니다.
    implementation "org.jetbrains.anko:anko-appcompat-v7:0.10.2"
}
~~~
## DSL로 화면 구성하기
* Anko Layouts을 사용하면 소스 코드에서 화면을 DSL(Domain Specific Language) 형태로 정의할 수 있습니다.
* 다음은 DSL을 사용하여 화면을 구성하는 간단한 예를 보여줍니다. XML 레이아웃으로 정의할 때보다 더 간단하게 화면을 구성할 수 있는 것을 확인할 수 있습니다.
~~~kotlin
verticalLayout {
    padding = dip(12)

    textView("Enter Login Credentials")

    editText {
        hint = "E-mail"
    }

    editText {
        hint = "Password"
    }

    button("Submit")
}
~~~
* 앞의 코드에서 사용한 verticalLayout(), textView(), editText(), button()은 Anko Layout에서 제공하는 함수로, 뷰 혹은 다른 뷰를 포함할 수 있는 레이아웃을 생성하는 역할을 합니다.
* 다음은 여기에서 제공하는 함수 중 자주 사용하는 함수 몇 개의 목록입니다.

|함수|생성하는 뷰|비고|
|---|--------|----|
|button()|android.widget.Button||
|checkBox()|android.widget.CheckBox||
|editText()|android-widget.EditText||
|frameLayout()|android-widget.FrameLayout||
|imageView()|android-widget-ImageView||
|linearLayout()|android.widget.LinearLayout||
|radioButton()|android.widget.RadioButton||
|relativeLayout()|android-widget.RelativeLayout||
|switch()|android-widget.Switch|서포트 라이브러리에서 제공하는 뷰는 switchCompat() 사용|
|verticalLayout()|android-widget-LinearLayout|orientation 값으로 LinearLayout.VERTICAL을 갖는 LinearLayout|
|webView()|android-webkit.WebView||
* XML 레이아웃 파일에 XML로 구성한 레이아웃을 저장하듯이, DSL로 구성한 뷰는 AnkoComponent 클래스를 컨테이너로 사용합니다.
* AnkoComponent에는 정의되어 있는 화면을 표시할 대상 컴포넌트의 정보를 포함합니다.
* 다음은 MainActivity 액티비티에 표시할 뷰의 정보를 가지는 AnkoComponent의 코드 예시를 보여줍니다.
~~~kotlin
class MainActivityUI : AnkoComponent<MainActivity> {

    override fun createView(ui: AnkoContext<MainActivity>) = ui.apply {
        vertivalLayout {
            // LinearLayout의 padding을 12dp로 설정합니다.
            padding = dip(12)

            // TextView를 추가합니다.
            textView("Enter Login Credentials")

            // EditText를 추가하고, 힌트 문자열을 설정합니다.
            editText {
                hint = "E-mail"
            }

            editText {
                hint = "password"
            }

            // 버튼을 추가합니다.
            button("Submit")
        }
    }.view
}
~~~
* 추가로, 액티비티에서는 AnkoComponent 없이 직접 액티비티 내에서 DSL을 사용하여 화면을 구성할 수 있습니다.
* 다음은 앞의 코드와 동일한 레이아웃을 AnkoComponent 없이 구성하는 예입니다. 이 방식으로 화면을 구성하는 경우 setContentView()를 호출하지 않아도 됩니다.
~~~kotlin
class MainActivity: AppcompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView()가 없어도 됩니다.
        verticalLayout {
            padding = dip(12)

            textView("Enter Login Credentials")

            editText {
                hint = "E-mail"
            }

            editText {
                hint = "Password"
            }

            button("Submit")
        }
    }
}
~~~
## 프래그먼트에서 사용하기
* 프래그먼트에서 Anko Layouts을 사용하려면 프래그먼트를 위한 AnkoComponent를 만들고, onCreateView()에서 createView()를 직접 호출하여 프래그먼트의 화면으로 사용할 뷰를 반환하면 됩니다.
* createView()를 직접 호출하려면 AnkoContext 객체를 직접 만들어 인자로 전달하면 됩니다.
~~~kotlin
class MainFragment : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        // AnkoComponent.createView() 함수를 호출하여 뷰를 반환합니다.
        return  MainFragmentUI().createView(AnkoContext.create(context, this))
    }
}

// 프래그먼트를 위한 AnkoComponent를 만듭니다.
class MainFragmentUI : AnkoComponent<MainFragment>  {
    override fun createView(ui: AnkoContext<MainFragment>)  = ui.apply  {
        verticalLayout {

            textView("Enter Login Credentials")

            editText {
                hint = "E-mail"
            }

            editText {
                hint = "Password"
            }

            button("Submit")
        }
    }.view
}
~~~
## Anko Support Plugin
* Anko Support Plugin은 Anko와 같이 사용할 수 있는 부가 기능을 제공하는 IDE 플러그인입니다. 
* 플러그인을 설치하려면 코틀린 IDE 플러그인을 설치하는 과정과 동일하게 진행하면 되며, 플러그인  검색 다이얼로그에서 다음과 같이 'Anko Support'를 선택하여 설치하면 됩니다.

* Anko Support Plugin에서는 AnkoComponent로 작성한 화면이 어떻게 표시되는지 미리 확인할 수 있는 레이아웃 프리뷰 기능을 제공합니다.
* 레이아웃 프리뷰를 사용하려면, 먼저 프리뷰 기능으로 확인하고 싶은 AnkoComponent가 구혀되어 있는 파일을 연 후 AnkoComponent의 구현부 내부 아무곳에 커서를 둡니다.
* 그 다음, [View > Tools Windows > Anko Layout Preview]를  선택하여 레이이웃 프리뷰 창을 띄웁니다.
* 레이아웃 프리뷰 창은 XML 레이아웃 프리뷰 창과 거의 유사한 형태로 구성되어 있습니다.
* 앞에서 선택한 AnkoComponent의 레이아웃 프리뷰를 보여주며, 화면이 표시되지 않거나 바뀐 내용이 반영되지 않았다면 프로젝트를 다시 빌드하면 됩니다.

# Anko SQLite
* Android 커서를 사용하여 SQLite 쿼리 결과를 파싱하는 것은 힘듭니다. 
* 쿼리의 결과 행을 구문 분석하기 위해 많은 상용구 코드를 작성하고 이를 열거 된 모든 리소스를 적절하게 닫으려면 셀 수없는 try..finally 블록으로 묶어야합니다.
* Anko는 SQLite databases와 함께 간단하게 작동할 수 있도록 많은 기능들을 제공합니다.

## Using Anko SQLite in your project
* build.gradle의 dependency에 anko-sqlite를 추가합니다.
~~~gradle
dependencies {
    implementation "org.jetbrains.anko:anko-sqlite:$anko_version"
}
~~~
## Accessing the database
* SQLiteOpenHelper를 사용하는 경우 일반적으로 getReadableDatabase () 또는 getWritableDatabase ()를 호출합니다. 
* 결과는 실제로 프로덕션 코드에서 동일하지만 수신 된 SQLiteDatabase에서 close () 메서드를 호출해야합니다. 
* 또한 어딘가에 도우미 클래스를 캐시해야하며 여러 스레드에서이 클래스를 사용하는 경우 동시 액세스를 인식하고 있어야합니다. 
* 이 모든 것은 꽤 힘듭니다. 그래서 안드로이드 개발자는 디폴트 SQLite API에 열중하지 않고 대신 ORM과 같은 값 비싼 래퍼를 선호합니다.
* Anko는 기본 클래스를 완벽하게 대체하는 ManagedSQLiteOpenHelper 클래스를 제공합니다. 사용 방법은 다음과 같습니다.
~~~kotlin
class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("Customer", true, 
                    "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                    "name" to TEXT,
                    "photo" to BLOB)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("User", true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(getApplicationContext())
~~~
* try 블록에 코드를 포함하는 대신 이제 다음과 같이 작성할 수 있습니다.
~~~kotlin
database.use {
    // 'this' is a SQLiteDatabase instance
}
~~~
* {} 안에 모든 코드를 실행 한 후에 데이터베이스가 완전히 닫힙니다.

* 비동기 호출 예제 : 
~~~kotlin
class SomeActivity : Activity() {
    private fun loadAsync() {
        async(UI) {
            val result = bg { 
                database.use { ... }
            }
            loadComplete(result)
        }
    }
}
~~~
* 아래 언급된 메서드들과 모든 메서드를은 SQLiteException에 throw 할 수 있습니다.
* Anko가 오류가 발생하지 않는 것처럼 가장하는 것은 무리일 수 있으므로 직접 처리해야합니다.
## Creating and dropping tables
* Anko와 함께라면 쉽게 새로운 테이블을 만들고 삭제할 수 있습니다. 구문은 간단합니다.
~~~kotlin
database.use {
    createTable("Customer", true,
    "id" to INTEGER + PRIMARY_KEY + UNIQUE, 
    "name" to TEXT,
    "photo" to BLOB)
}
~~~
* SQLite에서는 NULL, INTEGER, REAL, TEXT, BLOB 총 5개의 메인 타입들이 있습니다.
* 하지만 각 열에는 PRIMATY KEY 또는 UNIQUE와 같은 수정자가 있을 수 있습니다. 이러한 수정자는 기본 유형 이름에 "추가"와 함께 추가 할 수 있습니다.
* 테이블을 지울 때에는 dropTable 함수를 사용합니다.
~~~kotlin
dropTable("User", true)
~~~
## Inserting data
* 일반적으로 테이블에 행을 삽입하려면 ContentValues ​​인스턴스가 필요합니다. 다음은 그 예입니다.
~~~kotlin
val values = ContentValues()
values.put("id", 5)
values.put("name", "John Smith")
values.put("email", "user@domain.org")
db.insert("User", null, values)
~~~
* Anko는 insert() 함수의 인자로 값을 직접 전달하여 이러한 코드의 거치래을 제거 할 수 있습니다.
~~~kotlin
// Where db is an SQLiteDatabase
// eg: val db = database.writeableDatabase
db.insert("User", 
    "id" to 42,
    "name" to "John",
    "email" to "user@domain.org"
)
~~~
* database.use를 사용하면
~~~kotlin
database.use {
    insert("User",
        "id" to 42,
        "name" to "John",
        "email" to "user@domain.org"
    )
}
~~~
## Querying data
* Anko는 편리한 쿼리 빌더를 제공합니다. db.select (tableName, vararg columns)를 사용하여 만들 수 있습니다. db는 SQLiteDatabase의 인스턴스입니다.

함수                                   | 기능
--------------------------------------|---------- 
`column(String)`                      | 검색어 선택하는 열 추가하기
`distinct(Boolean)`                   | 고유한 쿼리
`whereArgs(String)`                   | 원시 문자열 `where` 쿼리를 지정하기
`whereArgs(String, args)` (중요)      | 인자를 사용하여`where` 쿼리를 지정하기
`whereSimple(String, args)`           | `? '마크 인자를 가진`where` 쿼리를 지정하기
`orderBy(String, [ASC/DESC])`         | 이 열의 순서
`groupBy(String)`                     | 이 열의 그룹화하기
`limit(count: Int)`                   | 쿼리 결과 행 수 제한하기
`limit(offset: Int, count: Int)`      | 오프셋이있는 쿼리 결과 행 수 제한하기
`having(String)`                      | 원시 'having'표현식 지정하기
`having(String, args)` (중요)         | 인자로 `having' 표현식을 지정하기
* (중요) 로 표시된 함수는 특별한 방법으로 인자를 구문 분석합니다. 어떤 순서로든 값을 제공하고 원활한 escaping을 지원합니다.
~~~kotlin
db.select("User", "name")
    .whereArgs("(_id >  {userId}) and (name = {userName})",
        "userName" to "John", 
        "userId" to 42)
~~~
* 여기에서 {userId} 부분은 42, {userName}은 'John'으로 바뀝니다. 형식이 숫자가 아닌 경우 (Int, Float 등) 또는 Boolean 값이면 escaped됩니다. 다른 형식의 경우 toString() 표현이 사용됩니다.
* whereSimple 함수는 String 자료형을 허용합니다. 이것은 SQLiteDatavase의 query() 와 같은 일을 합니다. (질문 표시 ?는 자료형의 실제 값과 대체됩니다.)
* 이 쿼리를 어떻게 실행할까요? exec() 함수를 씁니다. Cursor. () -> T 형식의 확장 기능을 허용합니다. 수신한 확장 기능을 시작한 다음 Cursor를 닫아 스스로 수행할 필요가 없도록합니다.
~~~kotlin
db.select("User", "email").exec {
    // Doing some stuff with emails
}
~~~
## Parsing query results
* 그래서 우리는 Cursor를 가지고 있고, 그것을 정규 클래스로 어떻게 파싱할 수 있을까요? 
* Anko는 parseSingle, parseOpt 및 parseList 함수를 제공하여 훨씬 쉽게 처리 할 수 ​​있습니다.

| Method | Description |
|--------|-------------|
| parseSingle(rowParser): T | Parse exactly one row|
| parseOpt(rowParser): T? | Parse zero or one row |
| parseList(rowParser): List<T> | Parse zero or more rows |

* 수신 된 Cursor가 둘 이상의 행을 포함하면 parseSingle () 및 parseOpt ()가 예외를 throw합니다.
* rowParser 란 무엇일까요? 각 함수는 RowParser와 MapRowParser인 두 가지 유형의 파서를 지원합니다.
~~~kotlin
interface RowParser<T> {
    fun parseRow(columns: Array<Any>): T
}

interface MapRowParser<T> {
    fun parseRow(columns: Map<String, Any>): T
}
~~~
* 매우 효율적인 방법으로 쿼리를 작성하려면 RowParser를 사용합니다 (하지만 각 열의 인덱스를 알아야합니다). 
* parseRow는 Any의 타입을 받아들입니다 (Any 형은 Long, Double, String 또는 ByteArray 이외의 것일 수 있음). 
* 반면에 MapRowParser를 사용하면 열 이름을 사용하여 행 값을 가져올 수 있습니다.
* Anko는 이미 간단한 단일 열 행에 대한 파서를 보유하고 있습니다.

    * ShortParser
    * IntParser
    * LongParser
    * FloatParser
    * DoubleParser
    * StringParser
    * BlobParser
* 또한 클래스 생성자에서 행 파서를 만들 수 있습니다.
클래스가 있다고 가정합니다.
~~~kotlin
class Person(val firstName: String, val lastName: String, val age: Int)
~~~
* 파서는 간단해집니다.
~~~kotlin
val rowParser = classParser<Person>()
~~~
* 현재로서는 기본 생성자에 선택적 매개 변수가있는 경우 Anko는 이러한 파서 작성을 지원하지 않습니다.
* 또한 생성자는 Java Reflection을 사용하여 호출되므로 커다란 데이터 세트의 경우 사용자 정의 RowParser를 작성하는 것이 더 합리적입니다.
* Anko db.select () 빌더를 사용하는 경우에는 parseSingle, parseOpt 또는 parseList를 직접 호출하고 적절한 파서를 전달할 수 있습니다.
## Custom row parsers
* 예를 들어, 열 (Int, String, String)에 대해 새 파서를 만들어 봅시다. 가장 일반적인 방법은 다음과 같습니다.
~~~kotlin
class MyRowParser : RowParser<Triple<Int, String, String>> {
    override fun parseRow(columns: Array<Any>): Triple<Int, String, String> {
        return Triple(columns[0] as Int, columns[1] as String, columns[2] as String)
    }
}
~~~
* 자, 이제 코드에 3가지 명시적 캐스트가 있습니다. rowParser 함수를 사용하여 제거해보겠습니다.
~~~kotlin
val parser = rowParser { id: Int, name: String, email: String ->
    Triple(id, name, email)
}
~~~
* 이게 다 입니다. rowParser는 모든 캐스트를 생성하고 원하는대로 람다 매개 변수의 이름을 지정할 수 있습니다.
## Cursor streams
* Anko는 SQLite Cursor에 기능적으로 접근하는 방법을 제공합니다. 
*  cursor.asSequence () 또는 cursor.asMapSequence () 확장 함수를 호출하여 일련의 행을 가져옵니다. 커서를 닫는 것을 잊지 마세요 :)
## Updating values
* 사용자 중 한 명에게 새로운 이름을 줍니다.
~~~kotlin
update("User", "name" to "Alice")
    .where("_id = {userId}", "userId" to 42)
    .exec()
~~~
* 또한 전통적인 방식으로 쿼리를 제공하려는 경우 update에는 whereSimple () 메서드가 있습니다.
~~~kotlin
update("User", "name" to "Alice")
    .`whereSimple`("_id = ?", 42)
    .exec()
~~~
## Delete Data
* 행을 삭제 해 봅시다 (delete 메소드에는 whereSimple () 메소드가 없으며, 대신 인수에 직접 쿼리를 제공합니다).
~~~kotlin
val numRowsDeleted = delete("User", "_id = {userID}", "userID" to 37)
~~~
## Transaction
* transaction ()이라는 특별한 함수가 있는데, 여러 개의 데이터베이스 연산을 하나의 SQLite 트랜잭션으로 묶을 수 있습니다.
~~~kotlin
transaction {
    // Your transaction code
}
~~~
* {} 블록 내에 예외가 발생하지 않으면 트랜잭션은 성공으로 표시됩니다.
* 어떤 이유로 트랜잭션을 중단하려면 TransactionAbortException을 throw 하세요. 이 경우에는이 예외를 직접 처리 할 필요가 없습니다.

## 원문
* https://github.com/Kotlin/anko/wiki/Anko-SQLite

# Anko Coroutines
## Using Anko Coroutines in your project
* build.gradle의 dependency에 anko-coroutines를 추가합니다.
~~~kotlin
dependencies {
    implementation "org.jetbrains.anko:anko-coroutines:$anko_version"
}
~~~
## Listener helpers
## asReference()
* 비동기 API가 취소를 지원하지 않으면 코루틴이 무기한 정지 될 수 있습니다. 
* 코루틴은 캡처 된 객체에 대한 강력한 참조를 보유하므로 Activity 또는 Fragment 인스턴스의 인스턴스를 캡처하면 메모리 누수가 발생할 수 있습니다.
* 이러한 경우에는 직접 캡처 대신 asReference ()를 사용하면 됩니다.
~~~kotlin
suspend fun getData(): Data { ... }

class MyActivity : Activity() {
    fun loadAndShowData() {
		// Ref<T> uses the WeakReference under the hood
		val ref: Ref<MyActivity> = this.asReference()
	
		async(UI) {
		    val data = getData()
				
		    // Use ref() instead of this@MyActivity
		    ref().showData(data)
		}
    }

    fun showData(data: Data) { ... }
}
~~~
## bg()
* bg ()를 사용하여 백그라운드 스레드에서 코드를 쉽게 실행할 수 있습니다.
~~~kotlin
fun getData(): Data { ... }
fun showData(data: Data) { ... }

async(UI) {
    val data: Deferred<Data> = bg {
		// Runs in background
		getData()
    }

    // This code is executed on the UI thread
    showData(data.await())
}
~~~