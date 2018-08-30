# JSON

> 발표주제 : JSON
>
> 발표일자 : 2018-08-06 (월)
>
> 발표자 : 송시은



[TOC]



## 0. 참고문서

될 때까지 안드로이드[오준석 지음] : 22장 네트워크 통신



## 1. HTTP 통신

자바에서의 네트워크 통신은 HttpURLConnection 객체를 통한 방법이 일반적이다. 



### 1.1. 실습

- AndroidManifest.xml 파일에 INTERNET 권한을 추가한다.

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```



- 소스를 읽어오는 클래스 작성

안드로이드에서는 자바와 다르게 네트워크 통신은 반드시 작업 스레드에서 수행해야 하는 제약이 있다. 따라서 비동기 네트워크 통신을 하는 AsycnTask 를 작성해야 한다. 

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 소스를 확인하고 싶은 사이트 주소
        new HttpAsyncTask().execute("http://[주소]");
    }

    private static class HttpAsyncTask extends AsyncTask<String, void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String strUrl = params[0];
            try {
                // URL 객체 생성
                URL url = new URL(strUrl);
                // URL 을 연결한 객체 생성
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");     // GET 방식 통신
                connection.setDoOutput(true);           // 쓰기 모드
                connection.setDoInput(true);            // 읽기 모드
                connection.setUseCaches(false);         // 캐시 사용
                connection.setDefaultUseCaches(false);
                // 입력 스트림 열기
                InputStream inputStream = connection.getInputStream();
                // 문자열 저장 객체
                StringBuilder builder = new StringBuilder();
                // UTF-8 방식으로 입력받은 스트림을 읽어들이는 버퍼 객체
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                // 한 줄씩 문자열을 읽어들이기
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                // 결과
                result = builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Log.d("HttpAsyncTask", s);
            }
        }
    }
}
```

HttpURLConnection을 사용하여 GET 방식으로 소스를 읽어오는 기본적인 코드이다. GET 방식으로 웹사이트의 코드를 읽어들여 로그로 출력하는 간단한 코드지만, 꽤 복잡해 보인다. 실제로 자바의 네트워크 API는 사용하기가 그렇게 편하지는 않다.



## 2. OkHttp 라이브러리

Http/1.1 통신의 문제점 중 하나는 연결당 한 번에 하나의 요청과 응답만 허용한다는 점이다. 여러 요청을 병렬로 처리하려면 브라우저나 다른 클라이언트에서는 여러 개의 소켓을 열어야 하는데, 이것은 클라이언트에서는 큰 문제가 아니지만, 서버 관점에서는 심각한 문제이다. 이 문제를 해결하기 위해서 구글이 HTTP 프로토콜 개선 작업을 시작하였고, SPDY(스피디)라 불리는 새로운 프로토콜을 구현하였다. SPDY는 HTTP/1.1에 비해 상당한 성능 향상과 효율성을 보여주었고, 2015년 표준이 된 HTTP/2의 기반이 된다. OkHttp는 Square사에서 개발한 Http/2와 SPDY를 지원하는 라이브러리이다.

### 2.1. 실습

- build.gradle파일에 라이브러리 의존성 추가

```groovy
dependencies {
	...
    implementation 'com.squareup.okhttp3:okhttp:3.8.1'
}
```

- OKHttp 라이브러리 적용

```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 소스를 확인하고 싶은 사이트 주소
        new HttpAsyncTask().execute("http://[주소]");
    }
    private static class HttpAsyncTask extends AsyncTask<String, void, String> {
        // OkHttp 클라이언트
        OkHttpClient client = new OkHttpClient();
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String strUrl = params[0];
            try {
                // 요청
                Request request = new Request.Builder()
                        .url(strUrl)
                        .build();
                // 응답
                Response response = client.newCall(request).execute();
                Log.d(TAG, "onCreate: " + response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if (s != null){
                Log.d("HttpAsyncTask", s);
            }
        }
    }
}
```

- 로그캣에 소스가 표시되는 것을 확인한다.

OKHttp의 동작 방식은 okHttpClient 객체를 생성한 후 newCall() 메서드에 요청 정보를 Request 객체로 정의하여 전달한다. execute() 메서드는 동기 방식으로 네트워크에 접속하고 결과를 Response 객체로 응답받는다. 응답에는 Header와 Body 등의 정보가 담겨 있다.

이처럼 OKHttp는 간단하게 GET/POST 방식으로 HTTP 통신을 수행할 수 있게 해준다. 동기/비동기 방식을 모두 지원하며, 코드도 간결해진다. 안드로이드 2.3 이상에서 사용할 수 있으며, 자바 SDK 7 이상에서 동작한다.



## 3. JSON 파싱

### 3.1. 정의

> Json(Javascript Object Notation) : javascript 언어에서 사용되기 시작한 경량 데이터 교환 형식

![https://github.com/taeiim/Android-Study/blob/master/study/week10/json의%20형태.PNG](https://github.com/taeiim/Android-Study/blob/master/study/week10/json의%20형태.PNG)

- 위와 같은 형태의 데이터를 Json이라고 한다. 
- Json 포맷에서 []는 배열을 나타내며, {}는 객체를 나타낸다.
- 자료구조의 Map과 같은 구조이다.
- 안드로이드에서 JSON 객체를 다루려면 다음과 같은 클래스를 사용한다.
  - **JSONObject**   JSON 데이터를 담을 수 있는 객체
  - **JSONArray**   JSONObject를 여러 개 담을 수 있는 리스트 객체



### 3.2. 특징

- Json은 XML과 더불어 데이터 형식으로 가장 많이 사용되고 있으며, XML보다 간결하고 이해하기 쉽다.
- Json은 실제 데이터에 집중하므로 전송되는 데이터의 용량이 적다.
- 데이터를 분석해서 사용하기 편한 파싱도 비교적 쉽다.



### 3.3. 실습

- 모델클래스 작성

```java
public class Weather {
    private String country;
    private String weather;
    private String temperature;

    public Weather(String country, String weather, String temperature){
        this.country = country;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Weather{");
        sb.append("country='").append(country).append('\'');
        sb.append(", weather=").append(weather).append('\'');
        sb.append(", temperature=").append(temperature).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
```



- JSONObject 대신 Weather 클래스를 이용하여 날씨 데이터 다루기

```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 소스를 확인하고 싶은 사이트 주소
        new HttpAsyncTask().execute("http://[주소]");
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, List<Weather>> {
        // OKHttp 클라이언트
        OKHttpClient client = new OKHttpClient();
        @Override
        protected List<Weather> doInBackground(String... params) {
            List<Weather> weatherList = new ArrayList<>();
            String strUrl = params[0];
            try {
                // 요청
                Request request = new Request.Builder()
                    .url(strUrl)
                    .build();
                // 응답
                Response response = client.newCall(request).execute();
                JSONArray jsonArray = new JSONArray(resonse.body().string());
                for(int i = 0; l < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String country = jsonObject.getString("country");
                    String weather = jsonObject.getString("weather");
                    String temperature = jsonObject.getString("temperature");
                    Weather w = new Weather(country, weather, temperature);
                    weatherList.add(w);
                }
                Log.d(TAG, "onCreate : " + weatherList.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } 
            return weatherList;
        }
        @Override
        protected void onPostExecute(List<Weather> weatherList) {
            super.onPostExecute(weatherList);
            if (weatherList != null) {
                Log.d("HttpAsyncTask", weatherList.toString());
            }
        }
    }
}
```



- Weather의 리스트를 담을 수 있는 어댑터를 작성하고, 이 어댑터를 리스트뷰에 전달한다.

```java
public class WeatherAdapter extends BaseAdapter {
    private final List<Weather> mList;

    public WeatherAdapter(List<Weather> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_weather, parent, false);
            holder = new ViewHolder();
            holder.country = (TextView) convertView.findViewById(R.id.country_text);
            holder.weather = (TextView) convertView.findViewById(R.id.weather_text);
            holder.temperature = (TextView) convertView.findViewById(R.id.temperature_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Weather weather = (Weather) getItem(position);
        holder.country.setText(weather.getCountry());
        holder.weather.setText(weather.getWeather());
        holder.temperature.setText(weather.gettemperature());
        return convertView;
    }

    // 뷰 홀더 패턴
    static class ViewHolder {
        TextView country;
        TextView weather;
        TextView temperature;
    }
}
```



- HttpAsyncTask의 onPostExecute() 메서드에서 리스트뷰를 조작하기 위해 코드 수정

```java
... 생략 ...
@Override
        protected void onPostExecute(List<Weather> weatherList){
            super.onPostExecute(weatherList);
            if (weatherList != null){
                Log.d("HttpAsyncTask", weaterList.toString());
                WeatherAdapter adapter = new WeatherAdapter(weatherList);
                mWeaterListView.setAdapter(adapter);
            }
        }
... 생략 ...
}
```



## 4. GSON

Gson은 구글에서 개발한 JSON 데이터와 자바 오브젝트를 상호 변환할 수 있는 라이브러리이다. 



### 4.1. 특징

- 자바에서 JSON을 다루는 라이브러리는 그 외에도 Jackson, JSONIC 등 다양하다. 

- Gson은 내부적으로 자바의 리플랙션이라는 기법을 사용하는데, 리플랙션은 실행시간 비용이 많이 드는 기법이므로 실행시간에 민감한 앱을 개발할 때는 적합하지 않다.



### 4.2. 실습

- 라이브러리 의존성 추가

```groovy
dependencies {
    ... 생략 ...
    implementation 'com.google.code.gson:gson:2.8.1'
}
```



- MainActivity.java 파일 내의 HttpAsyncTask 클래스에서 JSON 데이터를 파싱하는 부분을 Gson으로 수정

```java
private static class HttpAsyncTask extends AsyncTask<String, void, List<Weather>> {
        // OkHttp 클라이언트
        OkHttpClient client = new OkHttpClient();
        @Override
        protected String doInBackground(String... params) {
            // JSON 데이터가 변환 될 객체
            List<Weather> weatherList = new ArrayList<>();
            String strUrl = params[0];
            try {
                // 요청
                Request request = new Request.Builder()
                        .url(strUrl)
                        .build();
                // 응답
                Response response = client.newCall(request).execute();
                Gson gson = new Gson();
                // Gson 라이브러리에서 이러한 리스트 타입을 자동으로 자바 객체로 변환하려면
                // 타입 객체를 지정해 줘야 한다. Type 클래스는 여러 패키지에 있는 데 그 중
                // import java.lang.reflect.Type을 사용해야 한다.
                Type listType = new TypeToken<ArrayList<Weather>>() {}.getType();
                // Type을 fromJson() 메서드에 함께 전달하면 자동으로 변환된다.
                weatherList = gson.fromJson(response.body().string(), listType);
                Log.d(TAG, "onCreate: " + response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    ... 생략 ...
}
```



## 5. Retrofit

Retrofit 라이브러리는 내부적으로는 OKHttp를 사용하면서 Gson 라이브러리를 결합하여 모델 클래스나 List<모델> 형태의 결과를 얻을 수 있는 라이브러리이다.



### 5.1. 실습

- build.gradle 파일에 종속성 추가

```groovy
dependencies {
    ... 생략 ...
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
}
```



- 인터페이스 작성

```

```







--------------

### 3.4. 응용

이 클래스들은 페이스북과 같은 SNS에 올리는 글과 사용자, 댓글을 표현한다.

Post는 User와 Comment의 List를 가지고 있다. 본 예제는 소스를 간단하게 보이고자 getter와 setter를 생략하고 필드의 멤버 변수들을 public으로 선언하였다.

- 사용자 클래스

```java
public class User {
    public String email;
    public String fullname;
    
    public User(String email, String fullname){
        this.email = email;
        this.fullname = fullname;
    }
}
```



- 포스트 클래스

```java
public class Post {
    public String title;
    public String content;
    public User author;
    public List<Comment> comments;

    public Post(User author, String title, String content) {
        this.comments = new ArrayList<Comment>();
        this.author = author;
        this.title = title;
        this.content = content;
    }
}
```



- 댓글 클래스

```java
public class Comment {
    public String author;
    public String content;
    
    public Comment(String author, String content){
        this.author = author;
        this.content = content;
    }
}
```



- 모델 클래스를 JSON으로 변환

자바로부터 JSON으로 변환할 때 toJson() 메서드를 사용한다. 기본적으로 null인 필드는 JSON에 포함되지 않는다.

```java
public class ToJsonSample {
    public static void main(String[] args) {
        Gson gson = new Gson();
        User user1 = new User("bob@jmail.com", null);   // fullname에 null을 설정
        User user2 = new User("jeff@jmail.com", "jeff");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        // Java 객체로부터 JSON으로 변환
        System.out.println("사용자 : " + gson.toJson(user1));
        // Java 객체로부터 JSON으로 변환 : List
        System.out.println("사용자 목록 : " + gson.toJson(userList));
        Post newPost = new Post(userList.get(0), "PostTitle", "postContent");
        Comment comment = new Comment("comment_author", "comment_comment");
        newPost.comments.add(comment);
        newPost.comments.add(comment);
        // Java 객체로부터 JSON으로 변환 : 필드에 List를 포함하는 객체
        System.out.println("댓글을 포함한 새 글 : " + gson.toJson(newPost));
    }
}
```



- JSON을 모델 클래스로 변환

JSON을 자바 객체로 변환할 때는 fromJson() 메서드를 사용한다.

```java

```

