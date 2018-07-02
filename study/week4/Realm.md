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

