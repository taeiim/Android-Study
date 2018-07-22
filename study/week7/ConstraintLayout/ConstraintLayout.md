# ConstraintLayout



## 1. ConstraintLayout, 왜 사용해야 하죠?

### 뷰를 그리는 과정

3가지 단계를 거쳐 view가 화면에 나타난다.

1. 측정 (Measurement)
2. 레이아웃 (Layout)
3. 그리기 (Draw)

액티비티가 포커스를 갖게되면, 시스템은 액티비티의 root node를 요청한다. 

``Measure 단계`` 에서는 view의 크기를 결정한다. 측정은 root노드에서 시작해 반복적으로 호출되며, 각각의 호출은 부모로부터 전달된 인자들과 함께 발생한다. 이 인자들은 widthMeasureSpec, heightMeasureSpec이라는 이름으로 전달된다.

``Layout 단계`` 에서는 각각의 view 크기를 기준으로 view의 위치를 결정한다. root노드에서 시작해 leaf노드까지 반복적으로 호출된다.

``Draw 단계 ``는 최종적으로 뷰를 캔버스위에 실제 그리는 과정이다. 루트 노드가 자신을 화면에 그리라는 요청을 받으면, 자식들에게 자신들을 측정하라는 메시지가 전달되며 모든 view가 측정될 때까지 이러한 과정이 반복된다.

따라서, 중첩된 레이아웃이 많을 수록 측정하는데 걸리는 시간도 늘어나게 된다.



### ConstraintLayout을 사용하면?

[안드로이드 성능개선](study/week5/android%20performance/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C%20%EC%84%B1%EB%8A%A5%EA%B0%9C%EC%84%A0.md) 포스팅의 **레이아웃 최적화** 부분과 위의 뷰를 그리는 과정을 참고하면

레이아웃을 좁고 깊게 만들기보다, 얇고 넓게 만들어 레이아웃을 중첩을 줄이면 성능을 향상시킬 수 있다는 것을 알 수 있다. 

![스크린샷 2018-07-22 오후 1.56.21](/Users/parktaeim/Desktop/스크린샷 2018-07-22 오후 1.56.21.png)

 ###### [그림1] ConstraintLayout VS. 중첩된 여러 레이아웃

ConstraintLayout을 사용하면 위 그림과 같이 평평한(flattened) 계층 구조를 유지하면서도 복잡한 레이아웃을 작성할 수 있다. 



### ConstraintLayout의 장점

- 단순한 계층 구조로 만들 수 있음
- 언번들 형태로 제공 (번들되지 않는 라이브러리)
  - 개발자가 완전히 통제할 수 있음
  - 번들되지 않기 때문에, 어떤 기기에도 사용이 가능
- 맞는 버전을 사용할 수 있음
  - 다음 날 다음 버전이 나와도 내 코드에는 영향을 미치지 않음
- `RelativeLayout` 보다 풍부한 표현력을 가짐







</br></br>


## 2. 동작 원리

### 제약조건 (Constraint)



### 방정식 (Equation)



### 해법 (Solver)



</br></br>

## 3. 사용해보기





</br></br>

## 4.







## 마무리

### 소감





### 출처

#### 도서



#### 웹사이트

- https://academy.realm.io/kr/posts/constraintlayout-it-can-do-what-now/
- https://academy.realm.io/kr/posts/cool-constraintlayout-droidcon-boston-2017/
- https://academy.realm.io/kr/posts/exploring-new-android-layouts/ (이미지1 출처)