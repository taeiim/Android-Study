# Service

발표자 : 송시은

발표일자 : 

발표주제 : Service

------

[TOC]



## 0. 참고 문서

- 될 때까지 안드로이드(오준석 지음)
- https://developer.android.com/guide/components/services?hl=ko
- https://developer.android.com/guide/components/bound-services#Lifecycle



## 1. 개요

**서비스:** 백그라운드에서 오래 실행되는 작업을 수행할 수 있는 애플리케이션 구성 요소 

- 서비스는 이름 그대로 다른 컴포넌트에게 서비스를 제공하려는 목적으로 사용

- 오래 걸리는 작업을 백그라운드에서 실행하게 하고, 눈에 보이는 화면을 가지지 않는다.

  - ex) 파일 다운로드, 음악 재생

- https://github.com/taeiim/Android-Study/tree/master/study/week14/Service/BadThreadExam
- 서비스 컴포넌트를 사용하면 위 예제와 같은 문제를 해결할 수 있다!



## 2. 서비스 사용하기

서비스는 스타트 서비스와 바인드 서비스, 두 가지 방식을 제공

두 가지 중 선택하여 사용할 수도 있고, 둘 다 사용할 수도 있다.

- **스타트 서비스** 
  - 시작해 두면 완전히 독립적으로 수행되는 작업을 한다.
  - 다른 컴포넌트가 중간에 종료시킬 수 있다.
- **바인드 서비스** 
  - 서비스와 다른 컴포넌트와 연결고리를 만든다. 
  - 따라서 연결된 컴포넌트의 상태에 따라 서비스의 동작을 제어할 수 있다. 
  - 독립적이지 않고 다른 컴포넌트에 의존한다.



### 2.1. 스타트 서비스

스타트 서비스는 파일 다운로드, 음악 재생과 같은 기능을 구현할 때 사용(물론 도중에 종료 가능)

앱을 종료했다가 다시 시작하더라도 서비스는 계속 동작하여 다운로드 작업을 멈추지 말아야 한다.

https://github.com/taeiim/Android-Study/tree/master/study/week14/Service/ServiceExam 

=> 서비스는 액티비티와 독립적으로 동작하므로 앱을 종료하고 다시 실행해도 서비스의 생명주기에는 아무 영향이 없다.



##### Q. 서비스와 스레드 중 어느 것을 사용해야 할까?

A. 서비스는 필요할 때만 생성하는 게 좋다. 

- 백그라운드에서 수행하지만 사용자가 앱을 사용 중일 때만 동작해야 하는 상황에는 서비스 대신 스레드! 
- 서비스를 사용할 때 주의할 점으로는 모든 컴포넌트는 메인 스레드에서 동작하므로 서비스가 수행하는 작업이 메인 스레드를 점유하는 일이라면 UI를 차단하지 않도록 서비스 내에서 스레드를 생성하야 한다는 점



##### 서비스의 생존을 결정하는 법

안드로이드 시스템은 자원이 부족하면 백그라운드에서 실행되는 프로세스를 하나씩 강제로 종료하므로 서비스 역시 강제 종료 대상이 될 수 있다!

onStartCommand() 메서드가 반환하는 상수가 그런 역할을 한다. 강제 종료된 서비스를 살릴 수 있는 다양한 옵션

| 상수                   | 의미                                                         |
| ---------------------- | ------------------------------------------------------------ |
| START_NOT_STICKY       | 전달할 인텐트가 있을 때를 제외하고, 서비스가 중단되면 재생성하지 않는다. 가장 일반적인 옵션 |
| START_STICKY           | 서비스가 중단되면 서비스를 재시작하지만, 인텐트는 다시 전달하지 않는다. 미디어 플레이어와 같이 서비스가 무기한으로 동작해야 할 때 적합 |
| START_REDELIVER_INTENT | START_STICKY와 다르게,  인텐트와 함께 서비스를 재시작한다. 파일 다운로드와 같이 무엇을 다운로드 했었는지 알아야 할 때 적합 |



##### 서비스에서 스레드를 사용하는 이유

서비스를 포함한 안드로이드 4대 컴포넌트는 메인 스레드에서 실행되므로 오래 걸리는 처리를 하게 되면 화면이 버벅대면서 ANR이 발생할 수 있다. ANR 발생을 막으려면 서비스 내에서 스레드를 생성해야 한다.



##### 서비스의 종료

서비스의 종료는 액티비티와 같은 컴포넌트에서 stopService() 메서드를 사용하였는데, 서비스 내에서 stopSelf() 메서드를 사용해도 된다.



### 2.2 인텐트 서비스

대부분의 서비스는 여러 요청을 동시에 처리하지 않아도 되기 때문에, 일반적으로 Service를 확장한 IntentService 클래스를 사용한다. 이 클래스는 기본 Service 클래스와 비교하여 다음과 같은 작업을 수행한다.

- onStartCommand() 메서드에 전달된 인텐트마다 별도의 스레드를 자동으로 생성해 준다.
- 한 번에 하나의 인텐트를 처리하는 onHandleIntent() 메서드를 제공한다.
- 모든 수행이 끝나면 자동으로 stopSelf() 메서드를 호출하여 종료된다.

가장 큰 특징은 인텐트 서비스는 기본적으로 작업 스레드에서 동작하므로 일반 서비스보다 코드를 간결하게 작성!!



#### 2.2.1. 같이 실습~

1. MyIntentService 클래스 생성

2. 생성자, onHandleIntent() 메서드만 남기고 나머지 코드 제거(인텐트 서비스 기본적인 사용방법을 알기 위해)

3. onHandleIntent() 메서드(작업 스레드로 동작하므로)에 오래 걸리는 처리를 작성

   ```java
   @Override
       protected void onHandleIntent(Intent intent) {
           for (int i = 0; i < 5; i++) {
               try {
                   // 1초마다 쉬기
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
               }
               // 1초마다 로그 남기기
               Log.d("MyIntentService", "인텐트 서비스 동작 중 " + i);
           }
       }
   ```

4. xml에 인텐트 서비스 시작 버튼 작성

5. 메인 액티비티에 인텐트 서비스를 시작하는 onStartIntentService() 메서드 작성

6. 로그캣 확인(인텐트 서비스 시작 여러번 클릭)

위에 말했다 시피 엔텐트 서비스는 차례대로 실행됨을 확인할 수 있다!!!!!

인텐트 서비스는 순서대로 일을 처리하고 스스로 stopSelf() 를 호출하므로 도중에 stopService() 메서드 호출 X



### 2.3. 포그라운드 서비스

- 서비스는 기본적으로 백그라운드에서 동작하는 것이 기본!이지만~ 포그라운드에서도 실행되게 할 수 있음!
- 포그라운드 서비스는 메모리가 부족할 때도 안드로이드 시스템이 강제로 종료할 수 없고, 서비스를 중단하거나 알림에서 제거되어야만 종료시킬 수 있다.
- 포그라운드 서비스를 만들려면
  - 상태 표시줄에 표시되는 알림(Notification)을 제공해야 한다.
  - 알림의 id가 0이 아니어야 한다.
  - 서비스 내부에서 startForeground() 메서드를 호출하여 포그라운드 서비스 시작



#### 2.3.1. 같이 실습~~

1. MyService에 포그라운드 서비스 만들기

```java
... 생략 ...
	private void startForegroundService() {
        // default 채널 ID로 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행 중");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        // 오레오에서는 알림 채널을 매니저에 생성해야 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) 
                getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", 
                NotificationManager.IMPORTANCE_DEFAULT));
        }
        // 포그라운드로 시작
        startForeground(1, builder.build());
    }
}
```

- 오레오부터는 알림에 채널 ID를 지정해 줘야 한다.
- 여기서는 default라는 채널 ID를 지정, 채널 ID는 카테고리라고 생각하면 된다.
- NotificationManager에 채널 생성을 요청해야 채널이 생성 됨(채널 ID, 채널 이름, 중요도)
- 채널을 생성하는 createNotificationChannel() 메서드는 이전 버전에 없기 때문에 버전 분기 처리함
- 주의할 점은 startForeground(int, Notification)의 첫 번째 파라미터인 알림 ID는 0이면 안됨!
- 알림의 필수 요소인 아이콘, 제목, 내용은 꼭 있어야함(하나라도 없으면 알림 등록 X)

2. 인텐트에 액션을 설정하고 해당 액션을 처리할 수 있도록 onStartCommand() 메서드 수정

   ```java
   @Override
       public int onStartCommand(Intent intent, int flags, int startId) {
           if ("startForeground".equals(intent.getAction())) {
               // 포그라운드 서비스 시작
               startForegroundService();
           } else if (mThread == null) {
               ... 생략 ...
           }
           return START_STICKY;
       }
   ```

3. xml 파일에 포그라운드 서비스 시작 버튼 추가

4. 메인액티비티에 포그라운드 서비스 시작 버튼의 동작 정의(여기서 인텐트에 startForeground 액션 추가)

   ```java
   public void onStartForegroundService(View view) {
           Intent intent = new Intent(this, MyService.class);
           intent.setAction("startForeground");
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               startForegroundService(intent);
           } else {
               startService(intent);
           }
       }
   ```

5. 버전 분기 수행(오레오에서는 포그라운드 서비스를 시작하기 위해 startForegroundService() 메서드 호출 후 5초 이내 서비스에서 startForeground() 메서드를 호출해야 함)



##### 포그라운드 서비스 종료하기

- stopForeground() 메서드 사용하기
- stopForeground() 메서드가 서비스를 종료시키는 게 아니라, 서비스가 시스템에 의해 강제 종료 대상이 되는 것



##### 안드로이드 오레오부터는 다르다

- 안드로이드 오레오에서는 startService() 대신에 startForegroundService() 메서드를 사용해야 하며, 5초 이내에 startForeground() 메서드를 서비스에서 실행해야 한다.
- 만약 5초 이내에 startForeground() 메서드를 호출하지 않으면 시스템은 서비스가 응답하지 않는다고 판단하여 ANR 에러를 발생시킨다.
- 알림 채널이 필수여서 채널을 생성하지 알림 표시가 되지 않는다.
- 이러한 변경사항을 이전 버전과 새로운 버전에 모두 적용하려면 실습에서처럼 버전 분기 처리를 해야한다.



##### 인텐트 서비스와 포그라운드 서비스 정리

- 안드로이드는 백그라운드에서 작동하는 모든 서비스도 언젠가는 강제로 종료할 수 있다. 포그라운드 서비스를 사용하면 죽지 않는 서비스로 사용할 수 있다.
- 단, 항상 알림을 표시해야 하고 사용자가 알림을 제거하면 서비스도 중지될 수 있다.
- Service는 모든 서비스의 기본 클래스이며, 이 클래스를 상속할 경우 모든 작업은 Thread를 생성하여 작업해야 한다. 서비스는 메인 스레드에서 동작하므로 액티비티의 성능에 영향을 줄 수 있기 때문이다.
- IntentService는 Service의 서브클래스이며, 모든 작업을 작업 스레드에서 수행하며 한 번에 하나씩만 수행한다. 서비스가 여러 개의 작업을 동시에 할 필요가 없을 때 최선의 선택!! onHandleIntent() 만 구현하면 됨.



### 2.3. 바인드 서비스

서비스와 다른 구성요소를 서로 연결하려면 다른 구성요소가 서비스에 바인드해야 한다. 

지금까지는 카운팅 정보를 로그캣에서 확인했는데, 여기서는 액티비티를 서비스에 연결(바인드)하여 서비스로부터 카운팅 정보를 제공받아 토스트 메시지로 표시해 보겠다!



#### 2.3.1. 바인드 서비스를 작성하기 전에..

- 바인드 서비스를 이용하면 액티비티와 같은 다른 컴포넌트와 연결하여 서로 데이터를 주고받을 수 있다. 서비스는 별도의 프로세스로 동작하기 때문에 서비스끼리 데이터를 주고받으려면 '바인더(binder)'를 사용한다.
- 리눅스의 IPC(Inter Process Communication)과 RPC(Remote Procedure Call) 기술이 바인더에 적용되어 다른 프로세스임에도 메서드를 사용할 수 있다.
- 원래는 프로세스끼리 자원을 공유할 수 없는데 그것을 가능하게 해주는 것이 바인더라고 생각하면 쉽다.

------

- **IPC(Inter Process Communication)** 

리눅스에서모든 프로세스가 공유하는 메모리에 접근해서 데이터를 읽고 쓸 수 있게하는 개념 

- **RPC(Remote Procedure Call)**

다른 프로세스의 함수(메서드)를 호출할 수 있도록 하는 기술



#### 2.3.2. 같이 실습~~

1. MyService.java 파일 수정

   - 바인드 서비스를 제공하려면 Service 클래스의 onBind() 메서드에서 바인더 객체를 반환해야 한다.
   - 바인더 객체는 Binder를 상속하여 만들고 서비스의 레퍼런스를 전달할 수 있도록 getService() 를 만든다.

   ```java
   ... 생략 ...
   // MyService의 레퍼런스를 반환하는 Binder 객체
       private IBinder mBinder = new MyBinder();
   
       public class MyBinder extends Binder{
           public MyService getService(){
               return MyService.this;
           }
       }
   
       // 바인드된 컴포넌트에 카운팅 변숫값 제공
       public int getmCount() {
           return mCount;
       }
   ... 생략 ...
   ```

   - onBind() 콜백 메서드는 다른 구성요소에 서비스가 바인딩(연결)되면 호출되며, 이때 Binder를 반환한다.
   - 이 객체를 통해 액티비티와 서비스는 통신할 수 있게 된다.

   

2. ServiceConnection 객체 정의

   - 액티비티와 같은 컴포넌트를 서비스에 연결하려면 ServiceConnection 객체가 필요하다.
   - 이 객체는 두 가지 콜백 메서드 제공
     - 첫 번째는 서비스에 연결되었는지 알 수 있는 콜백
     - 두 번째는 서비스가 시스템에 의해 강제로 종료되었을 때 감지할 수 있는 콜백
   - 메인액티비티에 ServiceConnection 객체를 정의하고,
   - onStart() 메서드에서 bindService() 메서드로 서비스에 연결한다.
   - 액티비티가 포그라운드에서 동작 중이 아닐 때는 서비스와 연결을 해제해야 하는데, onStop() 메서드에서 unBindService() 메서드로 연결을 해제한다.

   ```java
   @Override
       protected void onStart() {
           super.onStart();
           // 서비스에 바인딩
           Intent intent = new Intent(this, MyService.class);
           bindService(intent, mConnection, BIND_AUTO_CREATE);
       }
   
       @Override
       protected void onStop() {
           super.onStop();
           // 서비스와 연결 해제
           if (mBound) {
               unbindService(mConnection);
               mBound = false;
           }
       }
   
       /**
        * bindService() 를 통해 서비스와 연결될 때의 콜백 정의
        */
       private ServiceConnection mConnection = new ServiceConnection() {
           @Override
           public void onServiceConnected(ComponentName name, IBinder service) {
               // MyBinder와 연결될 것이며 IBinder 타입으로 넘어오는 것을 캐스팅하여 사용
               MyService.MyBinder binder = (MyService.MyBinder) service;
               mService = binder.getService();
               mBound = true;
           }
           @Override
           public void onServiceDisconnected(ComponentName name) {
               // 예기치 않은 종료
           }
       };
   ```

   - 여기서 ServiceConnection 객체의 콜백 중에 onServiceDisconnected() 메서드는 서비스와 연결을 종료할 때 항상 호출되는 콜백이 아니라, 강제 종료와 같은 예기치 않은 종료에만 호출된다.
   - onStart() 메서드에서 bindService() 메서드의 세 번째 파라미터인 플래그로 BIND_AUTO_CREATE 가 설정되어 있다. 서비스에 연결하려면 서비스가 시작되어야 하는데 이 플래그를 설정하면 서비스를 따로 시작하지 않고도 바인드 서비스를 사용할 수 있다.
   - onServiceConnected() 메서드가 호출되면 서비스에 잘 연결된 것이며, 이때 서비스에서 Binder 객체가 넘어오니 여기서 getService() 메서드를 통해 연결된 서비스의 인스턴스를 참조할 수 있다.

   

3. 바인딩 상태 확인

   - 앱을 실행하기 전에 액티비티와 서비스의 연결 상태를 모니터링하기 위해 로그 추가

     ```java
     @Override
         public IBinder onBind(Intent intent) {
             Log.d(TAG, "onBind: ");
             return mBinder;
         }
     
         @Override
         public boolean onUnbind(Intent intent) {
             Log.d(TAG, "onUnbind: ");
             return super.onUnbind(intent);
         }
     ```

   - onBind() 와 반대로 onUnBind() 는 연결이 끊어졌을 때 호출되는 메서드

4. xml 파일에 카운팅 값 출력 버튼 추가

5. 메인 액티비티에서 토스트 값이 나올 수 있도록 getCountValue() 메서드 수정

   ```java
   public void getCountValue(View view) {
           if (mBound){
               Toast.makeText(this, "카운팅 : " + mService.getCount(), 
                              Toast.LENGTH_SHORT).show();
           }
       }
   ```

   



##### 결과

- 앱을 실행하여 <카운팅 값 출력> 버튼을 누르면 0이 표시된다.
- <서비스 시작>을 누른 후 <카운팅 값 출력> 버튼을 누르면, 카운팅 값이 변하는 것을 확인할 수 있다. 
- 참고로 바인드 서비스가 바인드 중일 때는 바인드 해제될 때까지 stopService() 로 서비스를 종료할 수 없다.
- startService() 는 가능하다. 따라서 이제부터는 서비스 시작 후에 <서비스 중지>를 눌러도 서비스가 종료 X





## 3. 서비스의 생명주기

![](https://github.com/taeiim/Android-Study/blob/master/study/week14/Service/service_lifecycle.png)

```java
public class ExampleService extends Service {
    int mStartMode;			// START_NOT_STICKY 등의 플래그
    IBinder mBinder;		// 바인딩 객체
    boolean mAllowRebind;	// 재 바인딩을 허용할지 여부
    
    @Override
    public void onCreate() {
        // 서비스 생성
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // startService()에 의해 서비스 시작
        return mStartMode;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // bindService()에 의해 클라이언트와 서비스 연결
        return mBinder;
    }
    
    @Override
    public boolean onUnBind(Intent intent) {
        // unBindService()에 의해 클라이언트와 연결 해제
        return mAllowRebind;
    }
    
    @Override
    public void onRebind(Intent intent) {
        // bindService()에 의해 클라이언트와 서비스 연결 후
        // onUnbind() 호출 후 다시 바인딩
    }
    
    @Override
    public void onDestroy() {
        // 서비스 종료
    }
}
```

- 바인드 서비스는 바인딩이 해제되면 안드로이드 시스템이 서비스를 종료하므로 개발자가 생명주기를 신경 쓰지 않아도 된다. 
- 하지만 onStartCommand() 메서드로 시작된 스타트 서비스는 바인딩 여부와 관계없이, 개발자가 생명주기를 관리해야 한다.
- 스타트 서비스는 stopSelf()나 stopService()메서드를 호출하기 전까지 계속 실행되기 때문이다.
- 스타트 서비스가 바인딩을 허용할 때 다음에 바인딩 시 onRebind() 메서드를 호출받고 싶다면 onUnbind() 메서드에서 true를 반환하도록 할 수 있다.
- 이 때도 클라이언트에서는 onServiceConnected() 콜백에서 바인더를 얻게 되는 것은 같다. 서비스가 클라이언트로부터 항상 새로운 접속으로 취급하느냐, 아니면 접속과 재접속을 허용하느냐의 차이이다.

![](https://github.com/taeiim/Android-Study/blob/master/study/week14/Service/service_binding_tree_lifecycle.png)