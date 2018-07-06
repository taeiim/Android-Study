## Realm

### Realm이란 무엇인가?

-Realm테이터베이스는 오픈 소스로 모바일 사용에 촤적화된 내장 데이터베이스 라이브러리

### Realm을 사용하는 이유

- 기존 데이터베이스보다 속도가 빠르다.
- ORM*과 같은 뛰어난 사용성
- 오픈소스라서 사용하기 쉬움
- 코드가  간단함

#### 용어정리

+ ORM(Object-relational mapping): 단순하게 표현하면 객체와 객체 간에 관계의 설정

  ORM에서 말하는 객체의 의미는 우리가 흔히 알고 있는 OOP(Object Oriented Programming)의 객체를 의미한다. OOP는 객체지향 프로그램을 말하며 여기에서의 객체는 저장공간에서 할당되어 값을 가지거나 식별자에 의해 참조되는 공간을 의미 한다.

#### 참고

http://cocomo.tistory.com/409 sqlLite 사용하는 방법

### Realm list

Realm list는 보바일 데이터베이스 Api중 하나이다. list클래스는 객체간의 일대다 관계를 위해 사용한다.

####realm 과 다른 데이터베이스 비교

![sql](https://github.com/taeiim/Android-Study/blob/master/study/week4/Realm_image/sql.PNG?raw=true)

위에 사진과 같이 복잡한 관계를 갖고 있는 데이터베이스라면 몇 개의 기능만 추가하더라도 더 얽히게 된다.

두명의 사용자가 잇는 경우 그들의 정보가 알고 싶다면, user와 repositories 내의 모든 레코드를 링크해서 찾아 내야 한다 . 게다가 이를 가져오고 싶을 때 마다 실제로 가져올 두개의 레코드를 매번 찾아내는 작업을 해야한다. 한눈에 봐도 그렇게 효율적인 방법이 아니다. 그래서 join를 사용하여 권한을 따로 부여하기도 한다.

하지만 Realm모바일 데이터베이스는 테이블을 join하지 않으면 sql 쿼리도 사용하지 않는다. Realm 모바일 데이터베이스는 메모리에 있는 다른 객체와 거의 동일하다. 한 객체가 다른 객체를 가리킬 때 부모는 일반적으로 자식의 데이터를 복사하지 않고 포인터만 유지한다.

![realm](https://github.com/taeiim/Android-Study/blob/master/study/week4/Realm_image/realm.PNG)

List는 일대다 관계를 정말 효율적으로 만들어 준다. Array와  거의 비슷하게 작동하며, 객체가 추가된 순서를 유지하고, 객체를 특정 인덱스에 추가,제거하거나 이동하는 메소드도 제공한다. List에 저장되어있는 객체 자체는 다른 객체에 대한 직접적인 인덱스 목로만 저장한다.

### Realm 객체생성

Realm의 객체를 생성하기 위해서는 ```realm.createObject```메서드를 사용합니다. 이 메서드는 *Proxy객체로 만드는 개념이다.

Realm 객체가 호출되면 내부적으로 테이블 객체를 가져와서  비어있는 row를 만들고 전달하는 과정을 거친다. realm에서 사용하는 Schemam는 일종의 *메타데이터이자 몰델과 연관된 reamlObjectSchema와 table을 관리한다. 테이블 객체를 직접 호출 해서 메서드로 데이터를 만들 수 있지만 직접 호출하지 않고 Poxy객체를 만든다.

![객체](https://raw.githubusercontent.com/taeiim/Android-Study/master/study/week4/Realm_image/Realm_java.PNG)

Realm java 객체는 Proxy 객체를 거치고 JNI를 호출해서 마지막으로 Table.cpp라는 C++로 만들어진 코어 객체 안에 객체를 호출하게 된다.

```
public long addColumn(RealmFieldType type, String name, boolean isNullable) {
  verifyColumnName(name);
  return nativeAddColumn(nativePtr, type.getNativeValue(), name, isNullable);
}

```

Table.java의 실제코드를 보면 addCoumn이라는 메서드 내에서 JNI코드를 호출하는 nativeaddColumn 메서드를 호출한다. 이를 호출하면 C++로 된 JNI가 호출되고, 실제로 Table.cpp를 호출해서 add_column메소드가 호출된다.



#### 용어

Proxy: 실제로 액션을 취하는 객체를 대신해서 대리자 역할를 해준다. 프록시 패턴을 사용하면 필요에 따라 객체를 생성시키거나 사용하기 때문에 메모리를 절약할 수 있는 이점이 생긴다. 

메타데이터: 다른 데이터를 기술하기 위해 사용하는 데이터라고 할 수 있다.

### android realm 예제

![gradle_android](https://github.com/taeiim/Android-Study/blob/master/study/week4/Realm_image/gradle_android.PNG?raw=true)



먼저 위의 사진에 아래의 코드를 추가한다.

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "io.realm:realm-gradle-plugin:5.3.1"
    }
}
```

![gradle_app](https://github.com/taeiim/Android-Study/blob/master/study/week4/Realm_image/gradle_app.PNG?raw=true)

추가한 다음 build.gradle(app)에 가서 다음 코드를 맨 아래에 추가한다.

```
apply plugin: 'realm-android'
```

모델 클래스 정의

```
public class Dog extends RealmObject {
    private String name;
    private int age;
	    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
```

Realm데이터 쓸때 꼭 해줘야하는 것

```
//realm 초기화
Realm.init(context);

//이 스레드에 대한 Realm 인스턴스 가져오기
Realm realm = Realm.getDefaultInstance();
```

insert

```
Realm mRealm;
// 트랜잭션을 통해 데이터를 영속화합니다
mRealm.beginTransaction();
Dog dog=mRealm.createObject(Dog.class);
dog.setName("멍멍이");
dog.setAge(3);
mRealm.commitTransaction();
```

Select 

```
Realm mRealm;

//select All
RealmResults<Dog> result=mRealm.where(Dog.class).findAll();
for(int i=0;i<result.size();i++){
  Dog dog=result.get(i);
  dog.getName();
  dog.getAge();
}
//db에 저장되어 있는 첫번째꺼 select
Dog dog=mRealm.where(Dog.class).findFirst();
dog.getName();
dog.getAge();
//db에 저장되어 있는 것 중에 자신이 원하는 거 출력
Dog dog=mRealm.where(Dog.class).equalTo(name,"멍멍이").findFirst();
dog.getName();
dog.getAge();
```

Delete

```
 Realm();
 mRealm.beginTransaction();
 RealmResults<DB> results=mRealm.where(DB.class).findAll();
 results.deleteAllFromRealm();//전부 삭제
 mRealm.commitTransaction();
```

```
 Realm();
 mRealm.beginTransaction();
 RealmResults<DB> results=mRealm.where(DB.class).findAll();
 results.deleteFromRealm(0); 0번째 인덱스가 삭제
 mRealm.commitTransaction();
```

*가장 나중에 들어온 데이터가 0번째임

Between

```
Realm();
RealmResults<DB> results=mRealm.where(DB.class).between("age",start,finish).findAll();
 for(int i=0;i<results.size();i++){
    DB db=results.get(i);
   Toast.makeText(getApplication(),"이름:"+db.getName()+" 
   나이:"+db.getAge()+"개수:"+String.valueOf(i+1),Toast.LENGTH_LONG).show();

                }
```

출처:

https://academy.realm.io/kr/posts/realm-api-optimized-for-performance-and-low-memory-use/ (realm 리스트)

https://academy.realm.io/kr/posts/realm-api-optimized-for-performance-and-low-memory-use/ (realm object 설명)

https://realm.io/kr/docs/java/latest/ (realm 예제)