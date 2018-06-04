# android-architecture-pattern

> Android Architecture Pattern인 MVC, MVP, MVVM에 대하여.

>

> Present Time : 2018-06-03-SUN
>
> Presentation File : https://github.com/ParkSohyeon17/TIL/blob/master/Android/MVC%2C%20MVVM%2C%20MVP.pptx
>
> Last Updated : 2018-06-04-MON



----------------------------



## 0. 공식 문서

### 0-1. 레퍼런스

### 0-2. 가이드

* [MVC](https://medium.com/upday-devs/android-architecture-patterns-part-1-model-view-controller-3baecef5f2b6)

* [MVP](https://upday.github.io/blog/model-view-presenter/)

* [MVVM](https://upday.github.io/blog/model-view-viewmodel/)

  ​

--------------------------------



예제 코드 출처 : [STEVE.JUNG](https://tosslab.github.io/android/2015/03/01/01.Android-mvc-mvvm-mvp)

```java
public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.btn_confirm);
        textView.setText("Non-Clicked");

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) findViewById(R.id.btn_confirm);
                textView.setText(getClickedText());
            }
        });
    }

    private String getClickedText() {
        return "Clicked!!!";
    }
}
```



## 1. MVC

> Both the Controller and the View depend on the Model: the Controller to update the data, the View to get the data.



#### 등장시기

- 그래픽 사용자 인터페이스의 초기 개발에 대한 통찰력 중 하나이다. 

- MVC는 책임과 관련하여 소프트웨어 구성을 설명하고 구현하는 첫 번째 방법 중 하나가 되었다. 

- Trygve Reenskaug는 1970 년대에 Xerox Palo Alto Research Center(PARC)를 방문하면서 Smalltalk -76에 MVC를 도입했다. 

  ​


#### 특징

![Model-View-Controller 클래스 구조](https://github.com/taeiim/Android-Study/blob/master/study/week1/aac_img.png/mvc_class_structure.png)

- Model : 비즈니스 계층 관리. 네트워크 또는 데이터베이스 API를 처리하는 **데이터 계층**

- View : 모델의 데이터를 시각화. 즉, UI Layer.

- Controller : Logic Layer은 사용자 행동을 통지 받고 필요에 따라 모델을 업데이트함.

  ​



#### 장점

- 코드의 테스트 가능성이 향상된다.

- 확장이 용이하여 새로운 기능을 쉽게 구현할 수 있다.

- Model 클래스는 Android 클래스에 대한 참조가 없으므로 단위 테스트에 가능하다. 마찬가지로 Controller의 단위테스트도 가능하다. 

  ​



#### 단점

- View가 Controller와 Model 모두에 의존한다는 점을 감안할 때, UI Logic의 변경은 여러 클래스의 업데이트가 필요할 수 있으므로 패턴의 유연성이 떨어진다.

  ​



#### 예제 코드

```java
public class MainActivity extends AppCompatActivity {
    private MainModel mainModel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainModel = new MainModel();

        textView = (TextView) findViewById(R.id.btn_confirm);
        textView.setText("Non-Clicked");

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mainModel.getClickedText();
                TextView textView = (TextView) findViewById(R.id.btn_confirm);
                textView.setText(mainModel.getClickedText());
            }
        });
    }

}
```

```java
public class MainModel {
    public String getClickedText() {
        return "Clicked!!!";
    }
}
```



## 1-1. Passive Model

![Passive Model Class Structure](C:\androidStudyLog\study\week4\img\mvc_passive_model.png)



### 1-1-1. 특징

- Controller는 Model을 조작하는 유일한 클래스.

- 사용자의 동작에 따라 Controller는 Model을 수정해야 한다.

- Model이 업데이트 된 후에 Controller는 View를 업데이트해야 함을 알린다. View는 Model로부터 데이터를 요청한다.

  ​



## 1-2. Active Model

![Active Model Class Structure](C:\androidStudyLog\study\week4\img\mvc_active_model_class_structure.png)

### 1-2-1. 특징

- Controller는 Model을 수정하는 유일한 클래스가 아닌 경우 모델은 **Observer Pattern**의 도움을 받아 View 및 기타 클래스에 업데이트를 알린다.

- Model에는 업데이트에 관심이 있는 Observer 컬렉션이 포함되어 있다.

- View는 Observer interface를 구현하고 Observer를 Model에 등록한다.

- Model이 업데이트 될 때마다 Observer 컬렉션을 반복하여 update메소드를 호출한다. View에서 이 메소드를 구현하면 Model의 최신 데이터 요청이 트리거된다.

  ​



![Active Model Behavior](C:\androidStudyLog\study\week4\img\mvc_active_model_behavior.png)

### 1-2-1. 특징

- ​





## 2. MVP

> Since the View and the Presenter work closely together, they need to have a reference to one another. To make the Presenter unit testable with JUnit, the View is abstracted and an interface for it used. The relationship between the Presenter and its corresponding View is defined in a `Contract` interface class, making the code more readable and the connection between the two easier to understand.



#### 등장시기

- MVC 패턴의 단점을 보완하기 위하여 등장한 패턴.

- MVC 패턴은 뷰가 모델에 강력하게 의존한다. MVP는 각 요소를 명확하게 분리하여 커플링을 낮추기 위해 등장했다.

  ​



#### 특징

![Model-View-Presenter Class Structure](C:\androidStudyLog\study\week4\img\mvp_class_structure.png)

- Model : 비즈니스 계층 관리. 네트워크 또는 데이터베이스 API를 처리하는 **데이터 계층**
- View : 모델의 데이터를 시각화. 즉, UI Layer.
- Presenter : Model에서 데이터를 검색. UI Logic 적용. View 상태 관리 및 표시 할 항목 결정. View에서 사용자 입력 통지에 반응.



- Presenter은 View에서 분리되어 있다. View를 interface를 통해 조작한다. 그래서 View를 Mock하여 테스트하기 쉽다. 

- Controller와 View가 모두 모델에 따라 다르다.

- Controller는 데이터를 업데이트한다.

- View는 데이터를 가져온다.

  ​



#### 장점

- 매우 훌륭한 분리를 제공한다.

  ​



#### 단점

- 작은 어플 or 프로토 타입 개발 시, 오버 헤드처럼 보일 수 있다. (사용되는 인터페이스가 많기 때문에)

- Presenter가 모든 것을 아는 클래스가 된다. 그러나 이는 코드를 더 많이 분해하고, 단위 테스트가 가능한 클래스를 만들면 된다.

  ​

#### 예제 코드

```java
public interface MainPresenter {
    void setView(MainPresenter.View view);

    void onConfirm();

    public interface View {
        void setConfirmText(String text);
    }
}
```

```java
public class MainActivity extends AppCompatActivity implements MainPresenter.View{
    private MainPresenter mainPresenter;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenterTmpl(MainActivity.this);
        mainPresenter.setView(this);

        confirmButton = (Button) findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.onConfirm();
            }
        });
    }

    @Override
    public void setButtonText(String text) {
        confirmButton.setText(text);
    }
}
```

```java
public class MainModel {
    public String getClickedText() {
        return "Clicked!!!";
    }
}
```

```java
public class MainPresenterTmpl implements MainPresenter {
    private Activity activity;
    private MainPresenter.View view;

    public MainPresenterTmpl(Activity activity) {
        this.activity = activity;
        this.mainModel = new MainModel();
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onConfirm() {
        if(view!=null) {
            view.setConfirmText(mainModel.getClickedText());
        }
    }
}
```



## 3. MVVM

>   The MVVM pattern was created to simplify the **event driven**programming of user interfaces.



#### 등장시기

: Activity(Fragment)가 View 와 Controller 두 가지의 특성을 모두 가지고 있기 때문에 View나Controller를 한 쪽으로 빼게 될 경우 View에 대한 바인딩이나 처리에서 중복 코드나 일관성을 잃어버리는 코드를 작성할 수 있다. 이를 개선하기 위해서 MVVM 이란 패턴이 등장했다.



#### 특징

![Model-View-View Model Class Structure](C:\androidStudyLog\study\week4\img\mvvm_class_structure.png)

- Model(=Data Model) : 데이터 소스를 추상화한다. ViewModel은 Data Model과 함께 작동하여 데이터를 가져오고 추상화한다. 
- View : 사용자의 행동에 대한 ViewModel을 알린다.
- ViewModel : View에 관련 데이터의 스트림을 노출한다.



- ViewModel에는 View에 대한 정보가 없다.

  ​



#### 장점

- 테스트 하기가 쉽다.
- UI 요구사항이 다시 변경되면 쉽게 바꿀 수 있다.





#### 단점

- Activity와 Fragment의 코드가 많아질수록 유지보수가 어렵다.

- 중첩된 콜백이 많으면 코드를 변경하거나 새로운 기능을 추가하기 어렵고 이해하기가 어렵다.

- Activity와 Fragment에 많은 로직들이 구현되어 있어 유닛 테스팅은 가능하지만 어렵다.

  ​



#### 예제 코드

```java
public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new MainViewModel(MainActivity.this);
    }

}
```

```java
public class MainModel {
    public String getClickedText() {
        return "Clicked!!!";
    }
}
```

```java
public class MainViewModel {
    private Activity activity;
    private MainModel mainModel;
    private TextView textView;


    public MainViewModel(Activity activity) {
        this.activity = activity;
        this.mainModel = new MainModel();
        initView(activity);
    }

    private void initView(Activity activity) {
        textView = (TextView) activity.findViewById(R.id.btn_confirm);
        textView.setText("Non-Clicked");

        activity.findViewById(R.id.btn_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = mainModel.getClickedText();
                        textView.setText(text);
                    }
                });
    }
}
```



## 4. 정리


