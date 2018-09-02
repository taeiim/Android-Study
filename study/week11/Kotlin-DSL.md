# Android DSL
> Present Time: 2018-8-24 FRI
>
> Present Time: 2018-8-24 FRI


## 1. DSL(Domain-Specific Language)이란?
DSL이란 **특정 도메인(서비스)에 특화**되어있는 언어를 말한다.  
자연어처럼 만들어 기존 코드보다 더 표현적이고 가독성이 좋게 만들어 사용한다.  
SQL도 DSL 언어중 하나다.

### 1-1. 내부 DSL, 외부 DSL
DSL은 내부 DSL과, 외부 DSL로 나누어진다.  
그중, 이번에 알아볼 DSL은 내부 DSL이다.
  
#### 내부 DSL
- 호스트 언어(기존 언어)의 틀을 벗어나지 않으며 구성하는것을 의미한다.
- 기존 언어의 새로운 구문으로 도입되어 언어 확장을 할 수 있다.
- 인라인 코드 형태로 표현이 가능하다.

#### 외부 DSL
- 기존 언어와 다른 언어를 통해 생성된 DSL의 의미한다.
- XML, CSS 등

#### 코틀린에서?
- 코틀린에서 `Koin`, `Anko`등 코틀린 전용 라이브러리에서도 DSL을 사용하는 것을 볼 수 있다.

```kotlin
val values = ContentValues()
values.put("id", 5)
values.put("name", "John Smith")
values.put("email", "user@domain.org")
db.insert("User", null, values)
```
##### `Anko Sqlite` 라이브러리를 사용하지 않았을 때
```kotlin
db.insert("User", 
    "id" to 42,
    "name" to "John",
    "email" to "user@domain.org"
)
```
##### `Anko Sqlite` 라이브러리를 사용하였을 때  
  
DSL을 적용했을 경우, 가독성이 더 뛰어나다.  

```kotlin
inline fun Activity.verticalLayout(theme: Int = 0, init: (@AnkoViewDslMarker _LinearLayout).() -> Unit)
```
위 함수는 `Anko Layout`의 DSL인데, 보다시피 확장함수와 고차 함수를 사용하고 있다.  
물론, 이 예시 이외에도 DSL은 고차 함수나 확장 함수로 구현되어있는 경우가 많다.  
그런 의미에서 실제로 한번 DSL을 구현해보기 전에, **Higher-order function**과 **Extension Fucntion**이 무엇인지 알아보자.

## 2. Extension Function이란?

코틀린은 이미 만들어진 객체에 새로운 함수를 만드는 기능인 확장 함수를 지원한다.  

### 2-1. 확장 함수 맛보기
예를 들어, 문자열의 마지막 문자만 뽑아내려면 어떻게 해야할까?  
호출할 때 마다 str[str.length — 1] 처럼 긴 코드가 필요하다.  
확장 함수를 이용해 조금 더 편하게 메소드를 만들어보자.

```kotlin
fun String.lastIndex() = this[this.length - 1]
```

확장 함수 기능을 이용해서 String 객체의 함수를 만들었고, 이제는 str.lastIndex() 로 마지막 문자를 뽑아낼 수 있다.  

- 확장 함수에서 this는 메소드를 호출한 객체를 가리킨다.
- 만약 확장 함수의 이름과 원래 멤버 함수의 이름이 같다면, 무조건 멤버 함수가 호출된다.


## 3. Higher-order function(고차함수)
고차 함수를 충족하려면 다음 조건 중 하나를 만족해야한다.
- 함수를 함수의 인자로 받는다
- 함수의 반환값이 함수다.

코틀린에서 자주 사용하는 `let`, `run`, `apply`, `with`도 모두 고차 함수다.

### 3-1. apply( )
`apply()`를 예시로 들어보며 고차함수가 어떻게 진행되는지 알아보자.
```kotlin
public inline fun <T> T.apply(block: T.() -> Unit): T {
    block()
    return this
}
```
##### (Kotlin 1.2 버전부터는 apply 안에 contract 메소드가 추가되어있으나, 편한 설명을 위해 제외시켰습니다.)

```kotlin
val test = Test(3,5).apply {
        square()
}

data class Test(var x: Int, var y: Int){
    fun square(){
        x *= x
        y *= y
    }
}
```
`T.apply`는 함수를 호출한 객체를 블록의 리시버(this)에 전달하고, 객체를 반환하는 함수다.  
### 3-2. apply() 함수 진행도
1. `apply()`는 `block: T.() -> Unit` 라는 T의 확장 함수를 인자로 받는다.  
2. 고로 `Test(3, 5).apply{}`를 호출하며 T의 확장 함수인 익명 함수를 생성한다.
3. 익명 함수 블록 안에서 리시버의 함수나 멤버를 사용한다.
4. `apply()` 에서 방금 선언한 익명 함수인 `block()`을 호출하고, 객체를 반환한다.  

### 결론
다른 고차 함수도 `apply()` 처럼 인자로 받은 함수를 동작되게 하는 방식으로 수행된다.

## 4. 직접 만들어보기 (추후 수정 예정)
확장 함수와 고차 함수를 알았으니 이제 우리도 DSL을 만들 수 있다.  
    [이 글](https://proandroiddev.com/writing-dsls-in-kotlin-part-1-7f5d2193f277)을 참고하여 객체를 생성하는 DSL을 만들어보자!

```kotlin
data class Person(var name: String = "", var age: Int = 0)
```
객체는 다음과 같다.

### 4-1. 확장함수를 사용하지 않고 만들기
Person을 인자로 받는 함수를 인자로 받는 함수를 만든다.
```kotlin
fun person(block: (Person) -> Unit): Person {
    val person = Person()
    block(person)
    return person
}
```
1. person 객체를 함수 내부에서 생성한다.
2. `block(person)`을 호출해 `person` 객체를 수정한다
2. 수정된 `person` 객체를 리턴한다.

다음과 같은 함수는 이렇게 사용 할 수 있다.
```kotlin
val person = person { 
        it.age = 3
        it.name = "Person"
    }
```
그러면 객체를 이렇게 생성할 수 있는데, 멤버를 호출할 때 마다 `it`을 붙여야한다는게 너무 귀찮다. `it`을 없애보자!

### 4-2. block 함수를 Person의 확장 함수로 만들기
`it`을 없애려면 어떻게 해야할까? 정말 간단하다. `block`함수를 `Person`객체의 확장 함수로 만들면 된다!

```kotlin
fun person(block: Person.() -> Unit) = Person().apply(block)
```
`block`을 `Person` 객체의 확장 함수로 만들고, `Person` 객체를 생성한 뒤 `block` 함수를 호출하고, 그 함수를 반환하게 만들었다.

```kotlin
val person = person {
        age = 3
        name = "Person"
    }
```
이렇게 하면 `it`을 없애서 가독성 좋고 편한 코드를 작성할 수 있다!

## 5. 참고 문서
### 5-1. 공식 문서

- [Extension Function 공식 문서](https://kotlinlang.org/docs/reference/extensions.html) {: target="_blank" }
### 5-2. 참고 문서

- [DSL with Kotlin](https://proandroiddev.com/writing-dsls-in-kotlin-part-1-7f5d2193f277)
- [DSL이란?](http://www.mimul.com/pebble/default/2013/06/21/1371806174467.html)
- [고차 함수 위키백과](https://ko.wikipedia.org/wiki/%EA%B3%A0%EC%B0%A8_%ED%95%A8%EC%88%98)
- [Extension Function](https://medium.com/@PaperEd/kotlin-infix-%ED%99%95%EC%9E%A5-%ED%95%A8%EC%88%98%EC%97%90-%EB%8C%80%ED%95%B4-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90-b8ae855f0c4)