## android Image Caching 

>  Android Image Caching 대하여
>
>  Present Time: 2018-06-09-Fri
>
>  Last Updated: 2018-06-14-Thur

-----------------

## 0. 공식 문서

### 0-1. 가이드

- [Developers Caching Bitmaps](https://developer.android.com/topic/performance/graphics/cache-bitmap)
- [Developers Caching Bitmaps(한글 문서)](http://blog.naver.com/huewu/110142842611)
- [android Image caching](http://javacan.tistory.com/entry/android-image-cache-implementation)
- [Image caching library](https://d2.naver.com/helloworld/429368)

### 1.캐싱이란?

데이터에 대한 향후 요청에 신속하게 대응 할 수 있도록 데이터를 로컬에 저장하는 프로세스

#### 비트맵 캐시

이미지를 비트맵으로 바꾸어 메모리에 저장하는 것이다.

#### 메모리 캐시

메모리 캐시는 원본의 압축된 형식으로 저장된다. 이미지는 화면에 표시되기 전에  이 캐시에서 디코드 되어 가져온다

이 캐시는 앱이 백그라운드로 가면 지워진다.

#### 디스크 캐시

디스크 캐시는 데이터가 지속적으로 남아있고 용량도 메모리 캐시 보다  더 많이 쓸 수 있다.하지만 메모리 캐시보다 느리다. 

이 캐시는 앱이 백그라운드 상태거나, 종료,장치가 꺼져 있을 때 없어지지 않는다.  사용자는 안드로이드 설정메뉴에서 디스크 캐시를 없앨 수 있다.

### 2.Image Caching을 하는 이유

- 특정 URL 이미지를 리스트로 보여줄 때, 동일 한 URL 이미지를 매번 다운 받아 출력하면 비효율
- 사용자 응답속도 느려짐

###3.코드

#### 비트맵 캐시 코드

```
private LruCache<String, Bitmap> mMemoryCache;

@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.
    //메모리 사용량 알기위해서
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;

    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than
            // number of items.
            return bitmap.getByteCount() / 1024;
        }
    };
    ...
}

public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
    if (getBitmapFromMemCache(key) == null) {
        mMemoryCache.put(key, bitmap);//메모리 캐시에 저장
    }
}
//캐쉬된 이미지 가져오기
public Bitmap getBitmapFromMemCache(String key) {
    return mMemoryCache.get(key);
}
```

##### LruCache 사용시 maxSize 및 sizeOf()사용 의미

-LruCache를 생성할 때 정해주는 maxSize는 이미지 갯수도 될 수 있고 가용 메모리도 될 수 있다.

- sizeOf()메소드를 Override 하지 않은 경우

  ```
  int maxSize = 10;

  LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(maxSize);
  ```

  이렇게 구현하면 캐시에 저장된느 이미지 갯수가 10개라는 뜻이다.

- sizeOf()메소드를 Override 했을 경우 

  ```
  int memoryClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
  int maxSize = 1024 * 1024 * memoryClass / 4;

  LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(maxSize){

      @Override

      protected int sizeOf(String key, Bitmap bitmap){

      	return bitmap.getByteCount();

     }

  };
  ```

  이렇게 구현하면 캐시의 가용 메모리가 어플리케이션이 사용할 수 있는  메모리의 1/4만큼 캐시에 쓸 수 있게 하겠다라는 의미이다.

#### 디스크캐싱

```
private DiskLruCache mDiskLruCache;
private final Object mDiskCacheLock = new Object();
private boolean mDiskCacheStarting = true;
private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
private static final String DISK_CACHE_SUBDIR = "thumbnails";

@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    // Initialize memory cache
    ...
    // Initialize disk cache on background thread
    //백그라운드에 디스크 캐시 초기화
    File cacheDir = getDiskCacheDir(this, DISK_CACHE_SUBDIR);
    new InitDiskCacheTask().execute(cacheDir);
    ...
}
//비동기로 백그라운드에서 디스크 캐시를 실행한다.
class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
    @Override
    protected Void doInBackground(File... params) {
        synchronized (mDiskCacheLock) {
            File cacheDir = params[0];//디스크 캐시가 저장된 디렉토리
            mDiskLruCache = DiskLruCache.open(cacheDir, DISK_CACHE_SIZE);
            mDiskCacheStarting = false; //초기화 완료
            mDiskCacheLock.notifyAll(); //작업으로 연결된 쓰레드에게 알린다.
        }
        return null;
    }
}

class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    ...
    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
    
        final String imageKey = String.valueOf(params[0]);

        // Check disk cache in background thread
        백그라운드 스레드에서 필요한 비트맵을 가져온다.
        Bitmap bitmap = getBitmapFromDiskCache(imageKey);

        if (bitmap == null) { // Not found in disk cache
            // Process as normal
            //일반적으로 디코딩하는 방법
            final Bitmap bitmap = decodeSampledBitmapFromResource(
                    getResources(), params[0], 100, 100));
        }

		//디코딩된 비트맵은 해당 키와 함께 캐시 처리 된다.
        addBitmapToCache(imageKey, bitmap);

        return bitmap;
    }
    ...
}

public void addBitmapToCache(String key, Bitmap bitmap) {
    // Add to memory cache as before
    메모리캐시에 저장한다
    if (getBitmapFromMemCache(key) == null) {
        mMemoryCache.put(key, bitmap);
    }

    // Also add to disk cache
    //디스크 캐시에도 저장한다.
    synchronized (mDiskCacheLock) {//백그라운드에서 디스크 캐시가 시행되었을 때를 기다린다
    
        if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
            mDiskLruCache.put(key, bitmap);
        }
    }
}

public Bitmap getBitmapFromDiskCache(String key) {
    synchronized (mDiskCacheLock) {
        // Wait while disk cache is started from background thread
        //백그라운드에서 디스크 캐시가 시행되길 기다린다.
        while (mDiskCacheStarting) {
            try {
                mDiskCacheLock.wait();
            } catch (InterruptedException e) {}
        }
        if (mDiskLruCache != null) {
            return mDiskLruCache.get(key);
        }
    }
    return null;
}


public static File getDiskCacheDir(Context context, String uniqueName) {
       // 사용을 해보고 외부저장소의 캐시가 작동하지 않는다면 내부 저장소를 사용하라는 내용이의 코드인데 잘 이해가 안되용ㅇ

    final String cachePath =          Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                    !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                            context.getCacheDir().getPath();

    return new File(cachePath + File.separator + uniqueName);
}
```

AsyncTask<Params, Progress,Result>

Params-파라미터 타입은 작업 실행 시에 송신

Progress-dolnBackground* 작업 시 진행 단위의 타입

Result-dolnBackground 리턴값

#### 용어정리

가용메모리: 실제  메모리 사용가능 여유

AsyncTask: 스레드 간의 동기화를 고려해서 소프트웨어를 설계햐아하고 핸들러 사용으로 복잡하고 번거로운 작업을 좀 더 쉽게 만들어주는 추상클래스이다. 

donlnBackground: 기존의 Thread에서의 run() 메소드라고 생각하면 된다.

 synchronized: 동기화. 둘 이상의 쓰레드가 파일이나 메모리를 공유하는 경우, 순서를 잘 맞추어 다른 쓰레드가 자원을 사용하고 있는 동안 한 쓰레드가 절대 자원를 변경할 수 없도록 해야한다 .  이런 상황을 처리할 수 있는 방법 이다.



#### 

​

### 4.Image Caching 라이브러리

여러 오픈 소스 이미지 로딩 라이브러리들은 캐시,동시 실행,취소 등을 간편하게 처리할 수 있도록 도와준다. 



#### Android Universal Image Loader

AUIL는 화면 크기를 기준으로 캐시용량을 제한하는 등 다양한 캐시 정책을 지원한다.

ImageLoader 객체를 초기화한 후에, 각종 옵션을 설정하고 사용 할 수 있다.

```
DisplayImageOptions options = new DisplayImageOptions.Builder()  
                              .cacheInMemory()
                              .cacheOnDisc()
                              ...
                              .build();
ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
```

*extend Application 한 클래스만 사용 가능



#### AQuery

AQuery는 xml파싱과 권한 관리 등 다양한 기능을 가진 라이브러리리나 이미지 로딩과 캐시를 제공한다. 

````
AQuery aq = new AQuery(context);  
...
String imageUrl ="http://image.naver.com/goldrush.jpg";  
aq.id(R.id.imageView).image(imageUrl, true, true, 200,0);  
````



#### Libs for Android Image Loader

getSystem.Service()메소드를 오버라이드하여 System 서비스로 ImageLoader 객체를 참조 할 수 있게 해야 한다. 그런 다음 ImageLoader.get() 메서드를 호출해서 이미지 로딩해야 한다

```
Callback callback = new Callback() {  
    public void onImageLoaded(ImageView view, String url) { ... }
    public void onImageError(ImageView view, String url, Throwable error) { ... }
};
ImageLoader.get(this).bind(view, imageUrl, callback);
```

*extend Application 한 클래스만 사용 가능

#### Volley Image Loader 

네트워크 통신과 캐시, 디코딩 등을 알아서 해주는 networkImageView 라는ImageView를 사용한 클래스도 제공한다. 기본 사용법은  requestQueue에 요청을 추가하고, 결과를 처리할 때 콜백 개체를 넘긴다.

```
imageLoader = new ImageLoader(Volley.newRequestQueue(this),new LruBitmapCache());  
imageLoader.get(imageUrl,  
                imageLoader.getImageListener(
                    imageView, R.drawable.defaultIcon, R.drawable.errorIcon
                )
);

```

*SDK매니저를 통한 다운로드 제공 x

  git에서 복사해서 사용해야함



#### Picasso

테스트와 디버그에 유용해서 개발 편의성을 많이 배려 했다.

```
Picasso.with(context).load(imageUrl).resize(30, 30).into(imageView);  
```



####라이브러리별 비교 

<img src='https://raw.githubusercontent.com/taeiim/Android-Study/master/study/week2/Image_Caching_png/Image_Caching_library.PNG'>

#### 총평

AUIL는 다른 라이브러리에 비해 거의 모든 면에서 압도적이다. 기존 이미지 로딩 문제의 대부분을 잘 해결하고 있고, 여러 종류의 캐시나 다운로더, 디코더 등이 이미 구현되어 있어 사용자가 채워줘야 할 부분이 별로 없다. 



AQuery는 직관적인 사용이 어렵다. 메모리 캐시의 크기 제한을 절정할 때 메소드 호출이 이리송한 점이 존재한다.

Libs for Androidsms deprecate된 프로젝트인만틈 사용하지 않는 편이 좋다.

droid4me 다른 라이브러리보다 기본 구현 수준이 부족하여 디스크 캐시나 디코딩 부분을 사용자가 직접 구현해야한다. 



Volley ImageLoader는 메모리 캐시는 구현체를 직접 만들어줘야 하고 디코딩 기능 리사이즈 밖에 지원하지 않는 등 사용성 측면에서 다소 부족하다.



Picasso는 다른 라이브러리보다 간단하다.  이미지 로딩 문제 해결에 필요한 기능들도 충분하고 디버깅을 도와주는 기능도 독창적이다.

