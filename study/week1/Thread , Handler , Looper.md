# Thread , Handler , Looper

> Android의 Thread, Handler, Looper 에 대하여

> 작성자 : 박태임
>
> Present Time : 2018–06-03-SUN
>
> Last Updated : 2018-

----------------------------

</br>

## 0. 들어가기전,, 

#### 0.1 용어정리

##### 메인 스레드 (UI 스레드)

프로세스에서 제일 처음 실행되는 스레드를 말한다. 메인 스레드는 응용프로그램의 시작과 함께 시작되고 해당 응용프로그램의 프로세스와 같은 수명을 가진다. 메인 스레드는 UI를 변경할 수 있는 유일한 스레드이기 때문에 UI스레드라고 부르기도 한다. UI스레드는 안드로이드 구성요소를 실행하고 화면 UI요소를 변경하는 데 사용하는 응용프로그램의 메인 스레드이다. 

##### 작업 스레드, 서브 스레드

메인 스레드에서 파생되어 실행되는 스레드

</br>

##### 단일 스레드 모델

다양한 종류의 스레드로 동시에 호출할 수 없는 메소드를 **단일 스레드 모델**이라 한다. 시스템 내에서 해당 작업 또는 해당 기능을 사용하는 시점에는 반드시 하나의 스레드만 실행되어야 한다. **스레드로부터 안전하지 못한 (Thread-Unsafe) 메소드** 라고 부르기도 한다.  

**뷰나 뷰그룹에서 제공하는 메소드** 는 단일 스레드 모델이다. 

</br>

##### 동기화(Synchronized)

하나의 자원을 여러 스레드가 사용할 때 한 시점에서 하나의 스레드만 접근을 허용하는 기능이다. 

Ex) 프린터 : 문서를 출력하고 있는 동안 다른 문서를 동시에 프린터로 출력할 수 없다.



</br>

</br>

## 1. Thread

#### 1.1 안드로이드에서의 스레드

스레드란 쉽게 말해 여러작업을 같이 하기 위한 기능이라고 생각하면 된다. 예를 들어 음악을 들으면서 SNS를 할 수 있는 것처럼 말이다. 

어플리케이션은 성능향상을 위해 멀티 스레드를 많이 사용하지만, UI를 업데이트 할 때는 단일 스레드 모델이 적용된다. 멀티 스레드로 UI를 업데이트 하면 동일한 UI자원을 사용할 때 *교착 상태,  *경합 상태 등 여러 문제가 발생할 수 있다. 따라서 UI업데이트는 메인스레드에서만 허용한다. 

###### </br>

#### 1.2 Thread, 왜 사용해요?

메인스레드 만으로 구현하게 된다면, 사용자는 해당 작업이 끝날 때까지 멈춰있는 화면을 보고만 있어야한다. 오랜 시간동안 UI관련 작업이 처리되지 못하면 결국 *ANR 에러가 발생한다. 그러면 어플리케이션은 정지된다.

안드로이드 3.0 버전부터 통신 클래스 내 전송이나 수신과 관련된 메소드는 메인 스레드에서 사용하지 못하도록 의도적으로 막아버렸다.  EX) 메인 스레드에서 소켓클래스의 conntect() 메소드를 호출하면 'NetworkOnMainThreadException' 이 발생한다. 

위와같은  네트워크 통신기능이나 데이터 검색, 빈번하게 사용하는 파일의 로드과 같이 시간이 걸리는 작업들은 사용자가 기다리지 않도록 하기 위하여 여분의 스레드를 사용해야 한다. (멀티스레드)

안드로이드 앱을 제대로 개발하기 위해서는 멀티 스레드를 이용하는 것은 필수적이라 할 수 있다. 

</br>

#### 1.3 Thread, 직접 만들어보자

스레드를 구현하는 방법은 2가지가 있다. 하나는 **Thread**를 이용하는 것이고, 나머지 하나는 **Runnable**을 이용하는 것이다.

##### 1.3.1 Thread를 상속받아 구현하기

```java
public class TaeiimThread extends Thread {
    @Override
    public void run() {
        super.run();
        // Do something..
    }
}
```

```java
// 스레드 실행
TaeiimThread thread = new TaeiimThread();
thread.start();
```

</br>

##### 1.3.2.  Runnable 인터페이스 구현

Runnable 인터페이스를 상속받은 후, run() 메소드에 원하는 작업을 하도록 구현하면 된다.

완성된 클래스를 생성한 후, Thread 클래스의 생성자에 인수로 전달한다.

```java
public class TaeiimRunnable implements Runnable {
    @Override
    public void run() {
        // Do something,, 
    }
}
```

```Java
// 사용하기 
TaeiimRunnable myRunnable = new TaeiimRunnable();
Thread thread = new Thread(myRunnable);
thread.start();
```

</br>

##### 1.3.3. Thread VS. Runnable

Thread와 Runnable 은 몇가지 차이점이 있다.

1. **Thread는 상속(Extends)을 받는 것이며 Runnable은 인터페이스**로서 구현하는 것이 큰 차이점이다.

   자바는 다중 상속이 불가능하기 때문에 Thread를 상속받게 되은 클래스는 다른 클래스를 상속 받을 수가 없지만 Runnable은 인터페이스이기 때문에 implements만 하면 되고 다른 필요한 클래스를 상속 받을 수 있다.

2. **Thread는 재사용 불가능, Runnable 가능**

   스레드는 일회용이다. 따라서 한 번 사용한 스레드는 재사용할 수 없다. 재사용시 'IllegalThreadStateException' 이 발생한다. 즉, 하나의 스레드에 대해 start()가 한 번만 호출될 수 있다. 하지만, Runnable로 구현한 경우 재사용 가능하다.

</br>

**용어정리**

###### *교착상태(dead lock): 두 개 이상의 작업이 서로 상대방의 작업이 끝나기 만을 기다리고 있기 때문에 결과적으로 아무것도 완료되지 못하는 상태

###### *경합상태(race condition) : 두 개 이상의 프로세스가 공통 자원을 병행적으로 읽거나 쓸 때, 공용데이터에 대한 접근이 어떤 순서에 따라 이루어졌는지에 따라 그 실행 결과가 달라지는 상황

###### *ANR : 안드로이드에서는 어떤 작업을 요청하고 5초간의 응답이 없을 때 강제종료 여부를 묻는 다이얼로그를 발생시킨다. 이 다이얼로그를 **ANR**(Application not responding) 메시지 라고 한다.

 

</br>

</br>

## 2. Handler

#### 2.1 Handler란?

Handler는 Looper로부터 받은 Message를 실행, 처리하거나 다른 스레드로부터 메시지를 받아서 Message Queue에 넣는 역할을 하는 스레드 간의 통신 장치이다.

![thread_img](/Users/parktaeim/Desktop/thread_img.png)



핸들러는 두 종류의 객체를 **메시지 큐(Message Queue)**를 통해 특정 스레드로 전달한다.

1. 문자와 필드로 구성된 메시지 객체
2. Runnable 객체

메시지큐는 하나의 프로세스 내 서브스레드가 다른 프로세스 내 존재하는 스레드에 메시지를 전달하는 기능을 한다.



핸들러는 반드시 메시지큐가 제공되는 스레드에서 생성해야한다. 메인 스레드는 기본으로 메시지큐를 제공한다. 

</br>



#### 2.2 사용목적

핸들러는 일반적으로 UI갱신을 위해 사용된다.

1. 메소드가 단일 스레드 모델일 때 , **Thread-Safe**로 만들기 위해

   아래의 코드를 실행시키면 'CalledFromWrongThreadException' 이라는 예외가 발생한다. 이것은 뷰 클래스에서 제공하는 메소드(여기서는 setText()) 를 메인스레드(UI 스레드) 가 아닌 서브 스레드에서 실행 시켰기 때문이다. 

   0-2에서 언급했듯이, 뷰나 뷰그룹에서 제공하는 메소드는 단일 스레드 모델(Thread-Unsafe)이기 때문에 메인 스레드에서 호출해야 한다.  

   ```java
   public class TaeiimThread extends Thread {
       @Override
       public void run() {
           super.run();
           textView.setText("박태임 짱");  // Error!!
       }
   }
   ```

   이러한 문제를 해결하기 위해 **자바는 동기화(Synchronized)**라는 기능을 제공하지만, **안드로이드는 핸들러**를 제공한다. 

   스레드에서 네트워크 등의 작업들을 하는 도중에 UI를 업데이트 할 때 핸들러를 사용한다. 

   </br>

2. 하나의 프로세스에서 다른 프로세스의 핸들러에 메시지를 전송하여 작업을 요청할 때


   독립적으로 실행되는 스레드 사이에 정보를 주고받는 수단으로 메시지큐와 핸들러를 채택했다. 만약 일반 스레드사이에서 필요한 정보를 주고 받으려면 루퍼를 사용해야 한다. 

</br>

#### 2.3 Handler, 직접 사용해보자!

다음은 Handler를 이용해 1초씩 증가하는 타이머를 만든 예제이다. 

```java
package com.example.parktaeim.threadex;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    TextView timeTv;
    Button startBtn;

    Runnable timeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTv = (TextView) findViewById(R.id.timeTv);
        startBtn = (Button) findViewById(R.id.startBtn);

        startBtn.setOnClickListener(v->{
            // Runnable 객체를 매개변수로 Thread 생성
            Thread timeThread = new Thread(timeRunnable);   
            timeThread.start();  // Thread Start
        });

        timeRunnable = new Runnable() {
            @Override
            public void run() {
                int time = 0;
                while(true){
                    Message msg = new Message();
                    msg.what = 0;
                    msg.arg1 = time;
                    handler.sendMessage(msg);    // Handler에 Message 보냄 

                    try{
                        Thread.sleep(1000);
                        time++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {    
            super.handleMessage(msg);
            if(msg.what == 0){   
                timeTv.setText(String.valueOf(msg.arg1));
            }
        }
    };
}

```



</br>

</br>

## 3. Looper

#### 3.1 Looper란

 Looper는 무한히 루프를 돌며 자신히 속한 스레드의 Message Queue에서 Message나 Runnable 객체를 차례로 꺼내 Handler가 처리하도록 전달한다. 

메인 스레드에는 Looper가 기본적으로 생성되어 있지만, 새로 생성한 스레드는 Looper를 가지고 있지 않기 때문에 메시지를 받을 수 없다. 사용할 수 있는 메시지 큐가 없기 때문이다. 서브 스레드에서 메시지를 전달받기 위해서는 Looper를 생성해주어야 한다. 

</br>

#### 3.2 특징

1. 루퍼 클래스는 생성자를 사용하여 인스턴스를 만들어 사용하는 것이 아니라 **정적 메소드**를 사용한다.

   ```Java
   public final class Looper {
       ... ...
       //Looper 클래스의 prepare(), loop() 등의 메소드들은 정적 메소드로 되어있다. 
   	public static void prepare() {
    	   prepare(true);
   	}
   	public static void loop() {
   	 	......       
   	}
     
       ... ... 
   }
   ```

   ```java
   // 그래서 객체를 생성하지 않아도 바로 사용할 수 있다. 
   Looper.prepare();
   Looper.loop();
   ```

   </br>

2. 루퍼는 핸들러를 사용할 수 있도록 **메시지큐**와 연결시켜 준다.

   Looper.prepare();  했을 때 실행되는 순서

   ```java
   public static void prepare() {
           prepare(true);
       }

   private static void prepare(boolean quitAllowed) {
           if (sThreadLocal.get() != null) {
               throw new RuntimeException("Only one Looper may be created per thread");
           }
           sThreadLocal.set(new Looper(quitAllowed));
       }
   ```

   ```java
   // prepare(boolean quitAllowed) 메소드에서 new Looper()
   private Looper(boolean quitAllowed) {
           mQueue = new MessageQueue(quitAllowed);   // MessageQueue 여기서 생성
           mThread = Thread.currentThread();   
       }
   ```

   </br>


#### 3.3 사용하기

루퍼는 클래스를 인스턴스화하고 객체 내 메소드를 호출하는 일반적인 자바 프로그램과 다르게 

**루퍼 초기화 -> 핸들러 생성 -> 루퍼 실행**  이라는 3단계로 작업을 수행한다. 

```java
public class LooperEx extends Thread {
    public Handler handler;

    @Override
    public void run() {
        Looper.prepare();   // Looper 객체를 생성하고 메시지큐를 초기화
        
        handler = new Handler(){    // 핸들러 객체 생성
            @Override
            public void handleMessage(Message msg) {
                // 메시지 처리 
            }
        };
      
        // 스레드 내 핸들러에 메시지를 전달. 스레드가 종료되지 않도록 내부적으로 메시지를 기다리는 기능.
        Looper.loop();   
    }
}
```



</br>

</br>

## 4. 마무리

#### HandlerThread

메인 스레드에서는 기본적으로 Looper가 제공되지만 서브스레드에서는 Looper를 기본적으로 가지지 않아 직접 생성해주어야 하는 불편함이 있다. 이와 같은 불편함을 개선하기 위해 **생성할 때 Looper를 자동으로 보유한 클래스**를 제공하는데 그것이 바로 HandlerThread이다.  



#### AsyncTask



#### ThreadPoolExecutor

</br>

#### UI 업데이트 다른 방법들

안드로이드는 스레드로부터 안전하지 못한 메소드를 호출할 때는 **핸들러**를 사용하는 방법 이외에도 다음과 같은 메소드를 사용할 수 있다. 

- Activity.runOnUiThread(Runnable)
- View.post(Runnable) , View.postDelayed(Runnable, long)

위와 같은 메소드를 사용하여 UI를 업데이트 할 수 있다. 두 메소드는 엄밀하게 이야기하여 핸들러에서 제공하는 메소드이며, 모두 Runnable 객체를 매개 변수로 사용한다.

</br>

#### 출처

##### 도서

- 객체지향 원리로 배우는 안드로이드 프로그래밍 (박현재 지음)  - 13장. 스레드와 핸들러
- 안드로이드 프로그래밍 Next Step (노재춘 지음) - 2장. 메인스레드와 Handler
- 완벽한 안드로이드 앱을 만드는 실무 노하우 139 (가나 마나부, 오카준 외 6명 지음, 장독대 옮김) 
- Efficient Android Threading, 안드로이드 멀티스레딩 (안데르스 에란손 지음, 한대희 옮김) 



##### 웹사이트

- 안드로이드 백그라운드 잘 다루기 Thread, Looper, Handler

  (https://academy.realm.io/kr/posts/android-thread-looper-handler/)

  ​