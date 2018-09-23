# Socket

> TCP/IP Socket과 Socket io 사용법

> 작성자 : 송진우

> Present Time : 2018-09-28

## 0. 참고 자료
* https://m.blog.naver.com/goldenkingll/70106915167
* https://d2.naver.com/helloworld/1336
* http://woowabros.github.io/woowabros/2017/09/12/realtime-service.html
* https://github.com/socketio/socket.io-client-java

## 1. Socket 이란

두 프로그램이 네트워크를 통해 서로 통신을 수행 할 수 있도록 양쪽에 생성되는 링크의 단자입니다.

- 데이터를 캡슐화하여 전달 가능
- UNIX에서의 입출력 메소드의 표준인 개방/읽기/쓰기/닫기 메커니즘

![Socket 이미지](http://2.bp.blogspot.com/-ztRG8ei0eq4/TfBlm38bldI/AAAAAAAAAKw/sFf8d03WzYs/s1600/scheme.jpg)

## 2. TCP/IP Socket
TCP 는 두 프로그램 간의 통신이 처음 시작될 때부터 끝날 때까지 계속 연결을 유지하는 연결지향(Connection oriented) 방식입니다.

- 스트림 소켓 방식
- 양쪽 어플리케이션 모두 데이터 주고 받기 가능
- 흐름제어등을 보장해 주며 송신된 순서에 따른 중복되지 않은 데이터를 수신 가능
- IP와 포트 번호로 소켓을 연결하면 통신 시작
- byte 자료형으로 데이터를 보냄


### 2.1 통신 절차

![통신 절차](http://www.a2big.com/_images/product/web/main/jay/2012-07-2616:21:00.JPG)

* TCP Client
 1) 소켓을 생성합니다.
 2) 서버로 connect() 합니다.
 3) 접속이 성공됐다면 read 및 write 함수를 통해 서버와 통신을 주고 받습니다.
 4) 사용을 마치면 close로 소켓을 닫습니다.
 
* TCP Server
 1) 듣기 소켓을 생선합니다.
 2) bind합니다. (내선 부여)
 3) listen합니다. (내선 연결)
 4) accept() 클라이언트가 connect할 경우 소켓을 생성 하고 연결합니다.
 5) read와 write 함수를 이용해 메시지를 주고 받습니다.
 6) 사용된 연결 소켓을 닫습니다.
 7) 사용을 마쳤을 경우 듣기 소켓을 닫습니다.

### 2.2 클라이언트 통신 과정 - JAVA
#### 1. 소켓을 생성합니다.
* Socket 객체를 생성
~~~java
 private static Socket socket;
 socket = new Socket();
~~~
#### 2. 서버로 connect() 합니다.
* 서버의 아이피와 포트번호를 적고 connect()로 연결합니다.
~~~java
 socket.connect(new InetSocketAddress("localhost", '포트번호'));
~~~
#### 3. 접속이 성공했다면 read 및 write 함수를 통해 서버와 통신을 주고 받습니다.
* 스트림 소켓으로 통신하기 때문에 InputStream과 OutputStream을 만듭니다.
* 또한 바이트 배열로 서버와 통신을 주고 받습니다.
* OutputStream에 write() 로 데이터를 적습니다.
* InputStream에 read() 로 서버의 데이터를 읽습니다.
* 데이터 송수신시 데이터를 Charset으로 UTF 등을 설정할 수 있습니다.
~~~java
private static InputStream is;
private static OutputStream os;

is = socket.getInputStream();
os = socket.getOutputStream();

byte[] byteArr = null;
String msg = "Hello Server";

byteArr = msg.getBytes("UTF-8");
os.write(byteArr);
os.flush();
System.out.println("Data Transmitted OK!");

byteArr = new byte[512];
int readByteCount = is.read();

if(readByteCount == -1)
    throw new IOException();

msg = new String(byteArr, 0, readByteCount, "UTF-8");
System.out.println("Data Received OK!");
System.out.println("Message : " + msg);

~~~
#### 4. 사용을 마치면 close로 소켓을 닫습니다.
* 통신이 끊나면 스트림 소켓과 소켓을 close() 함수로 닫습니다.
~~~java
is.close();
os.close();

socket.close();
~~~



### 2.3 (참고용) 서버 통신 과정 - JAVA

#### 1. 듣기 소켓을 생성합니다.
~~~java
private static ServerSocket serverSocket;
serverSocket = new ServerSocket();
~~~
#### 2. bind합니다. (내선 부여)
~~~java
serverSocket.bind(new InetSocketAddress(3880));
~~~
#### 3. listen합니다. (내선 연결)
클라이언트에서 요청이 올 때까지 기다린다!!!!
#### 4. accept() 클라이언트가 connect할 경우 소켓을 생성 하고 연결합니다.
~~~java
private static Socket socket;

socket = serverSocket.accept();
~~~

#### 5. read와 write 함수를 이용해 메시지를 주고 받습니다.
~~~java
InputStream is = socket.getInputStream();
OutputStream os = socket.getOutputStream();

byte[] byteArr = new byte[512];
String msg = null;

int readByteCount = is.read(byteArr);

if(readByteCount == -1)
    throw new IOException();

msg = new String(byteArr, 0, readByteCount, "UTF-8");
System.out.println("Data Received OK!");
System.out.println("Message : " + msg);

msg = "Hello Client";
byteArr = msg.getBytes("UTF-8");
os.write(byteArr);
System.out.println("Data Transmitted OK!");
os.flush();
~~~

#### 6. 사용된 연결 소켓을 닫습니다.
~~~java
is.close();
os.close();
socket.close();
~~~

#### 7. 사용을 마쳤을 경우 듣기 소켓을 닫습니다.
~~~java
if(!serverSocket.isClosed()) {
    try {
        serverSocket.close();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
~~~

## 3. Socket.io

- 실시간 이벤트 서버를 개발 할 수 있는 오픈소스 라이브러리 입니다. 
- WebSocket을 기반으로 FlashSocket, AJAX Long Polling등 다양한 방식의 실시간 웹 기술들을 하나의 API로 추상화한 node.js 모듈(MIT 라이센스 오픈소스) 
- 멀티 디바이스(web, android, ios, windows)를 지원합니다.

![Socket.io](http://img1.daumcdn.net/thumb/R1920x0/?fname=http%3A%2F%2Fcfile26.uf.tistory.com%2Fimage%2F2406273E596D913E130B7B)

### 3.1 Socket.io 의 특징

#### 장점

- 통신 구현시 코드가 매우 감소합니다.
- Socket의 성격과 같이 실시간 통신 가능
- http를 통해 통신이 가능하고 JsonObject나 일반 Object로 통신이 가능합니다.
- 브라우저와 웹 서버의 종류와 버전을 파악하여 가장 적합한 기술을 선택하여 사용하는 방식이기 때문에 브라우저의 종류와 상관없이 실시간 웹 구현 가능

#### 단점

- Socket.io는 Javascript에 초점이 만들어졌습니다.
- 다양한 언어의 서버와 통신을 하기에는 제약이 있습니다.

### 3.2 Socket.io 클라이언트 - JAVA

#### 1. 라이브러리 추가
* Gradle에 socket.io를 추가합니다.
~~~java
dependencies {
  	...생략 
    implementation ('com.github.nkzawa:socket.io-client:1.0.0'){
        exclude group: 'org.json', module: 'json'
    }
}
~~~

#### 2. 소켓을 선언하고 초기화
* TCP/IP와 동일하게 소켓을 만듭니다.
* http로 연결이 가능하며, 서버의 주소와 포트 번호를 초기화 해줍니다.
* URI 초기화 과정은 예외처리를 해주어야 합니다.
~~~java
private Socket socket;
{
    try{
        socket = IO.socket("http://***.***.***.***:***");
    } catch (URISyntaxException ue) {
        ue.printStackTrace();
    }
}
~~~

#### 3. 소켓을 연결
* connect() 함수로 소켓을 연결합니다.
~~~java
socket.connect();
~~~

#### 4(1). 서버 측에 이벤트 송신
* 클라이언트는 어떤 이벤트가 발생하면 이벤트를 서버로 송신할 수 있습니다.
* emit() 함수를 통해 데이터 또는 메시지를 서버에 전달합니다.
* 서버는 이를 이벤트의 이름으로 구분하여 수신합니다.
~~~java
socket.emit("EVENT_NANE", DATA);
~~~

#### 4(2). 서버 측의 이벤트 수신
* 서버는 다른 외부 클라이언트의 요청이나 서버의 이벤트 발생 시 클라이언트에 이벤트를 송신할 수 있습니다.
* on() 함수를 통해 해당 이벤트 명을 이용해 구분하고 서버의 emit의 반응하는 리스너를 구현합니다.
* 리스너 안의 call 함수 안에는 이벤트 수신 후 실행할 내용을 담습니다.
~~~java
socket.on("EVENT_NAME", '리스너 익명구현 객체');
Emitter.Listener '리스너 익명구현 객체' = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 이벤트 수신 시 실행할 내용들
                }
            });
~~~

#### 5. 사용 후 연결 헤제
* 서버와의 통신이 더 이상 필요 없는 경우 disconnect() 함수를 이용해 connect를 끊습니다.
* 그리고 on 시켜 두었던 소켓도 off() 함수로 닫습니다.
~~~java
protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("EVENT_NAME", '리스너 익명구현 객체');
    }
~~~

### 3.2 (참고용) Socket.io 서버 - Node.js
~~~javascript
var app = require('express')();
var server = require('http').createServer(app);

var io = require('socket.io')(server);

io.on('connection', function(socket) {
  console.log("user connect");

  socket.on('받은 이벤트 명', function(){
    console.log("받은 이벤트 명");
    io.emit('보낼 이벤트 명', "메시지나 데이터");
  });

});

http.listen('포트 번호!', function(){
  console.log("server on 포트번호");
});
~~~

## 4. 소켓 통신시 주의할 점
* TCP/IP의 경우 데이터 타입을 byte배열로 해야하고 Charset을 해야 데이터가 꺠지지 않습니다.
* 소켓을 생성하고 연결하여 사용을 하고 나면 꼭 정상적으로 소켓의 연결을 끊고 소켓을 닫아줘야 합니다.
* 소켓도 통신이기 때문에 멀티 스레드를 활용하여 작업을 해줘야 성능 개선 및 로직이 엉키는 것을 방지할 수 있습니다.
* Socket.io를 사용한다면 이벤트 명이 헷갈리지 않도록 잘 설정해야 합니다. 이벤트가 엇갈릴 경우 일일이 확인해야하는 불상사가 발생합니다.

## 5. Kotlin에 응용해보기
