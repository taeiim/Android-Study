# Design Pattern

> Builder, Adapter, Observer에 대하여.
>
> ​       
>
> Present time : 2018-06-27-WED
>
> Last updated : 2018-07-05-THURS



----------



## 0. 공식 문서 

### 0-1. 가이드

* [Design Pattern] (http://chuumong.github.io/android/2017/01/16/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4)
* [Builder] (http://jdm.kr/blog/217)
* [Adapter] (http://jusungpark.tistory.com/22)
* [Observer] (http://flowarc.tistory.com/entry/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%98%B5%EC%A0%80%EB%B2%84-%ED%8C%A8%ED%84%B4Observer-Pattern)


----------



## 1. Design Pattern

### 1-1. 정의

> 많은 실무 프로그래머들이 인정한 효율적인 코딩 방법 또는 구조.



### 1-2. 필요한 이유

1. 명확하고 단순한 코딩

2. class나 function 등은 한 가지 기능만 하도록 작게 세분화 시킬 수 있음

3. 높은 재사용성

4. 쉬운 유지보수

5. 리소스의 낭비 제거

   ​

→  만약, (개인 프로젝트 + 변하지 않는 요구사항) 이라면 디자인 패턴은 필요가 없다고 말할 수도 있겠다.



### 1-3. 다양한 Design Pattern

### 1-3-1. 개념

1. 생성

   > 객체 생성에 대해서 다루고 상황에 적절한 객체를 만드는 것이다.
   >
   > 생성에 관련된 패턴을 사용하면 쉽고 간단하게 객체 생성을 할 수 있다.

2. 구조

   >더 큰 구조를 형성하기 위해 어떻게 클래스와 객체를 합성하는지와 관련된 패턴이다.

3. 행위

   > - 객체의 행위를 조직, 관리, 조합하는데 사용하는 패턴
   > - 객체들이 다른 객체와 상호작용하는 방식을 규정
   > - 각각 다른 객체들과 통신하는 방법과 객체의 책임을 규정하여 복잡한 행위들을 관리 할 수 있도록 함
   > - 두 객체 간의 관계에서부터 앱의 전체 아키텍처에까지 영향을 미침




### 1-3-2. 종류

-  생성

  - Builder
  - Dependency Injection
  - Singleton

- 구조

  - Adapter
  - Facade

- 행위

  - Command

  - Observer

  - Model View Controller

  - Model View ViewModel

    ​

----------



## 2. Builder

### 2-1. 개념

> 객체를 생성할 때 흔하게 사용하는 패턴.
>
> 복잡한 인스턴스를 조립하여 만드는 구조.



> 생성자에 파라미터가 많은 클래스인 경우 빌더 패턴을 사용하여 가독성을 높일 수 있음.
>
> Android에서는 `NotificationCompat.Builder`와  `AlertDialog.Builder` 같은 클래스를 사용 할 때 `Builder` 패턴이 나타남

```java
Notification notification =new NotificationCompat.Builder(this)
                                      .setSmallIcon(R.drawable.ic_notification)
                                      .setContentIntent(pendingIntent)
                                      .setTicker(message)
                                      .build();
```

```java
new AlertDialog.Builder(this)
    .setTitle("Builder Dialog")
    .setMessage("Builder Dialog Message")
    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
        }
    })
    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
        }
    })
    .show();
```



→ **생성자 인자가 많을 때**는 Builder 패턴 적용을 고려하라



### 2-2. 예제

### 2-2-1. 기본

```
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // Required parameters(필수 인자)
        private final int servingSize;
        private final int servings;

        // Optional parameters - initialized to default values(선택적 인자는 기본값으로 초기화)
        private int calories      = 0;
        private int fat           = 0;
        private int carbohydrate  = 0;
        private int sodium        = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;    // 이렇게 하면 . 으로 체인을 이어갈 수 있다.
        }
        public Builder fat(int val) {
            fat = val;
            return this;
        }
        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }
        public Builder sodium(int val) {
            sodium = val;
            return this;
        }
        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize  = builder.servingSize;
        servings     = builder.servings;
        calories     = builder.calories;
        fat          = builder.fat;
        sodium       = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }
}
```

위와 같이 하면 다음과 같이 객체를 생성할 수 있다.

```java
NutritionFacts.Builder builder = new NutritionFacts.Builder(240, 8);
builder.calories(100);
builder.sodium(35);
builder.carbohydrate(27);
NutritionFacts cocaCola = builder.build();
```

또는 다음과 같이 사용할 수도 있다.

```
// 각 줄마다 builder를 타이핑하지 않아도 되어 편리하다.
NutritionFacts cocaCola = new NutritionFacts
    .Builder(240, 8)    // 필수값 입력
    .calories(100)
    .sodium(35)
    .carbohydrate(27)
    .build();           // build() 가 객체를 생성해 돌려준다.
```



### 2-2-2. Lombok @Builder

#### 사용방법

2-2-1의 빌더 패턴이라면 롬복의 `@Builder` Annotation으로 쉽게 사용할 수 있다.

```
@Builder
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;
}
```

사용은 다음과 같이 할 수 있다.

```java
NutritionFacts.NutritionFactsBuilder builder = NutritionFacts.builder();
builder.calories(230);
builder.fat(10);
NutritionFacts facts = builder.build();
```

물론 `.`체인도 된다.

```
NutritionFacts facts = NutritionFacts.builder()
    .calories(230)
    .fat(10)
    .build();
```



#### Setting

1. Eclipse + Window

2. Android Studio + Window

   ​



### 2-2-3. 장점

1. 각 인자가 어떤 의미인지 알기 쉽다.

2. `setter` 메소드가 없으므로 변경 불가능 객체를 만들 수 있다.

3. 한 번에 객체를 생성하므로 객체 일관성이 깨지지 않는다.

4. `build()` 함수가 잘못된 값이 입력되었는지 검증하게 할 수도 있다.

   ​


------------------------




## 3. Adapter

### 3-1. 개념

> 기존시스템 업체에서 제공한 클래스
>
> 기존시스템 **어댑터** 업체에서 제공한 클래스



> Android에서 대표적으로 Adapter패턴을 보이는 클래스는 `RecyclerView.Adapter`
>
> `RecyclerView.Adapter`클래스는 비지니스 로직(Model)과 RecyclerView를 연결하는 역할
>
> `RecyclerView.Adapter`은 데이터 아이템에 접근하고 해당 데이터를 이용하여 아이템에 View를 그리는 역할을 수행

```java
public class SampleAdapter extends RecyclerView.Adapter<SampleViewHolder> {
    private List<Test> tests;

    public SampleAdapter() {
        tests = new ArrayList();
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // Create View Holder
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder) {
        // View Bind
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public void add(Test item) {
        tests.add(item);
    }
}
```

`RecyclerVIew`는 `Test`에 대해서 무엇인지 모름.

`SampleAdapter`가 `Test`와 `RecyclerView`을 연결하기 때문에 `RecyclerVIew`은 `Test`에 대해서 알 필요가 없음.



### 3-2. 예제

http://jusungpark.tistory.com/22 참고



### 3-3. 장점

- 호환되지 않는 인터페이스를 사용하는 클라이언트를 그대로 활용할 수 있음

- 클라이언트와 구현된 인터페이스를 분리시킬수 있으며, 향후 인터페이스가 바뀌더라도 그 변경 내역

  은 어댑터에 캡슐화 되기 때문에 클라이언트는 바뀔 필요가 없어짐.



-----------------------------------------



## 4. Observer

### 4-1. 개념

> observer : 관찰자

객체지향 설계를 하다보면 객체들 사이에서 다양한 처리를 할 경우가 많다.



상태를 가지고 있는 주체 객체와 상태의 변경을 알아야 하는 관찰 객체(Observer Object)가 존재하며 이들의 관계는 1:1이 될 수도 있고 1:N이 될 수가 있다.

서로의 정보를 넘기고 받는 과정에서 정보의 단위가 클수록, 객체들의 규모가 클수록, 각 객체들의 관계가 복잡할수록 점점 구현하기 어려워지고 복잡성이 매우 증가할 것이다. 이러한 기능을 할 수 있도록 가이드라인을 제시해주는 것이 바로 Observer Pattern이다.  



객체와의 관계를 맺고 끊으며 객체의 상태가 변경되면 그 정보를 Observer(Subscribe, 구독자)에게 알려주는 방법을 알아보자.



### 4-2. 예제

```java
apiService.getData(someData)
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe (/* an Observer */);
```

- 값(이벤트)를 방출 할 Observable 객체를 정의
- Observable들은 한 번 또는 연속적으로 스트림, 값, 이벤트를 방출함
- Subscriber는 이러한 값을 수신하고 도작한 대로 응답
- 예를들어, API 호출을 작성하고 서버에서 응답을 처리할 Subscriber를 지정 할 수 있음

### 4-2-1. 어떤 곳에 쓰일까?

Java의 Swing이나 Android의 View나 Button 등의 위젯에 각종 이벤트를 받을 때 쓰인다. 

#### Android - Button

```java
Button button = (Button) findViewById(R.id.button);
button.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(...) {
        // ACTION
    }
})
```

Button은 항상 Click이벤트가 있으며, 이 이벤트는 OnClickListener 라는 인터페이스로 구성되어있다. 

즉 Button이라는 객체가 Publisher가 되고, OnClickListener가 Observer가 된다고 볼 수 있다. Button에서 상태가 변경(클릭 될 경우)된다면 OnClickListener로 알려준다.

위의 예제와 같이 Button에 OnClickListener라는 Observer를 등록하는 경우가 대표적인 Observer Pattern을 적용한 것이다. 



### 4-3. 장점

- 느슨한 결합성 유지