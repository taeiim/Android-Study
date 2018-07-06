# Retrofit

> Present time : 2018-07-06 Fri
>
> 
>
> Last Updated : 



-----

## 0. 공식 문서

### 0-1. 공식 문서

* [Retrofit] (http://devflow.github.io/retrofit-kr/)



### 0-2. 가이드

* ​




-----

## 1. 소개

* Retrofit은 **HTTP API**를 자바 인터페이스 형태로 사용할 수 있습니다.

#### HTTP 요청은 어노테이션을 사용하여 명시합니다.

* URL 파라미터 치환과 쿼리 파라미터가 지원됩니다.
* 객체를 요청 body로 변환합니다. (예: JSON, protocol buffers)
* 멀티파트 요청 body와 파일 업로드가 가능합니다.




-----

## 2. API 정의 

인터페이스의 어노테이션과 메소드 매개변수들은 요청을 어떻게 다룰지 지시합니다.

### 2-1. 요청 메소드

모든 메소드들은 반드시 상대 URL과 요청 메소드를 명시하는 어노테이션을 가지고 있어야합니다. 기본으로 제공하는 요청 메소드 어노테이션은 다음과 같이 5개가 있습니다 : `GET`, `POST`, `PUT`, `DELETE`, `HEAD`.



정적 쿼리 인자를 URL에 명시할 수도 있습니다.

```java
@GET("/users/list?sort=desc")
```



### 2-2. URL 다루기

요청 URL은 동적으로 부분 치환 가능하며, 이는 메소드 매개변수로 변경이 가능합니다. 부분 치환은 영문/숫자로 이루어진 문자열을 `{` 와 `}` 로 감싸 정의해줍니다. 반드시 이에 대응하는 `@Path` 를 메소드 매개변수에 명시해줘야합니다.

 ```java
@GET("/group/{id}/users")
Call<List<User>> groupList(@Path("id") int groupId);
 ```



이외에도 쿼리 매개변수도 명시 가능합니다. @Query와 @QueryMap을 이용하면 됩니다.



### 2-3. 요청 본문

HTTP 요청 본문에 **객체**를 `@Body` 어노테이션을 통해 명시가 가능합니다.

```java
@POST("/users/new")
Call<User> createUser(@Body User user);
```

이러한 객체들은 `Retrofit` 인스턴스에 추가된 컨버터에 따라 변환됩니다. 만약 해당 타입에 맞는 컨버터가 추가되어있지 않다면, `RequestBody` 만 사용하실 수 있습니다.



#### FORM-ENCODED과 MULTIPART

메소드는 form-encoded 데이터와 multipart 데이터 방식으로 정의 가능합니다.



`@FormUrlEncoded` 어노테이션을 메소드에 명시하면 form-encoded 데이터로 전송 됩니다. 각 key-value pair의 key는 어노테이션 값에, value는 객체를 지시하는 `@Field` 어노테이션으로 매개변수에 명시하시면 됩니다. 

```java
@FormUrlEncoded
@POST("/user/edit")
Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);
```



Multipart 요청은 `@Multipart` 어노테이션을 메소드에 명시하시면 됩니다. 각 파트들은 `@Part` 어노테이션으로 명시합니다.

```java
@Multipart
@PUT("/user/photo")
Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);
```

Multipart의 part는 `Retrofit` 의 컨버터나, `RequestBody` 를 통하여 직렬화(serialization) 가능한 객체를 사용하실 수 있습니다.



### 2-4. 헤더 다루기