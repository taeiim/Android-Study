## android Image Caching 

>  Android Image Caching 대하여
>
>  Present Time: 2018-06-09-Fri
>
>  Last Updated: 2018-06-05-Tues

-----------------

## 0. 공식 문서

### 0-1. 가이드

- [Developers Caching Bitmaps](https://developer.android.com/topic/performance/graphics/cache-bitmap)
- [Developers Caching Bitmaps(한글 문서)](http://blog.naver.com/huewu/110142842611)
- [android Image caching](http://javacan.tistory.com/entry/android-image-cache-implementation)
- [Image caching library](https://d2.naver.com/helloworld/429368)

----

### 1.Image Caching을 하는 이유

- 특정 URL 이미지를 리스트로 보여줄 때, 동일 한 URL 이미지를 매번 다운 받아 출력하면 비효율
- 사용자 응답속도 느려짐

### 2.Image Caching 기능

- 메모리에 지정 개수 만큼 이미지 보관
- 이미지 메모리/파일의 2레벨 캐시 제공
  + 캐시에 이미지에 보관하면 멤리와 파일에 동시 보관
  + 메모리 캐시 보관할 수 있는 개수에 제한
  + 파일 캐시는 보관 할 수 있는 전체 크기 제한
  + 메모리 캐시에 없으면 파일 캐시로 부터 이미지 받아옴


### 3.클래스 구성

+ ImageCache 

  + 이미지 캐시를 위한 인터페이스 


  + MemoryImageCache
    + 메모리 기반의 이미지 캐시 구현
+ FileImageCache 
  + 파일 기반의 이미지 캐시 구현
+ ChainedImageCache
  + 캐시 체인 기능 
+ ImageCacheFactory
  + ImageCache의 생성 및 검색 기능 
#### ImageCache 인터페이스

ImageCache 인터페이스는 캐시 목적의 기능을 정의한다.

```
public interface ImageCache {



	public void addBitmap(String key, Bitmap bitmap);



	public void addBitmap(String key, File bitmapFile);



	public Bitmap getBitmap(String key);



	public void clear();



}
```

#### MemoryImageCache클래스

내부적으로 LruCache*를 사용

```
ackage com.toonburi.app.infra.imagecache;



import java.io.File;



import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.support.v4.util.LruCache;



public class MemoryImageCache implements ImageCache {



	private LruCache<String, Bitmap> lruCache;



	public MemoryImageCache(int maxCount) {

		lruCache = new LruCache<String, Bitmap>(maxCount);

	}



	@Override

	public void addBitmap(String key, Bitmap bitmap) {

		if (bitmap == null)

			return;

		lruCache.put(key, bitmap);

	}



	@Override

	public void addBitmap(String key, File bitmapFile) {

		if (bitmapFile == null)

			return;

		if (!bitmapFile.exists())

			return;



		Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());

		lruCache.put(key, bitmap);

	}



	@Override

	public Bitmap getBitmap(String key) {

		return lruCache.get(key);

	}



	@Override

	public void clear() {

		lruCache.evictAll();

	}



}
```



#### FileImageCache 클래스

```
public class FileImageCache implements ImageCache {

	private static final String TAG = "FileImageCache";



	private FileCache fileCache;



	public FileImageCache(String cacheName) {
		//이미지 캐시 생성 전에 초기화
		fileCache = FileCacheFactory.getInstance().get(cacheName);

	}



	@Override

	public void addBitmap(String key, final Bitmap bitmap) {

		try {

			fileCache.put(key, new ByteProvider() {

				@Override

				public void writeTo(OutputStream os) {

					bitmap.compress(CompressFormat.PNG, 100, os);

				}

			});

		} catch (IOException e) {

			Log.e(TAG, "fail to bitmap to fileCache", e);

		}

	}



	@Override

	public void addBitmap(String key, File bitmapFile) {

		try {

			fileCache.put(key, bitmapFile, true);

		} catch (IOException e) {

			Log.e(TAG, String.format("fail to bitmap file[%s] to fileCache",

					bitmapFile.getAbsolutePath()), e);

		}

	}



	@Override

	public Bitmap getBitmap(String key) {

		FileEntry cachedFile = fileCache.get(key);

		if (cachedFile == null) {

			return null;

		}

		return BitmapFactory.decodeFile(cachedFile.getFile().getAbsolutePath());

	}



	@Override

	public void clear() {

		fileCache.clear();

	}



}
```

#### ChainedImageCache 클래스

이미지 캐시와 파일 캐시를 1차/2차 캐시로 사용하기 위해 만듬

```
....
	private List<ImageCache> chain;
	public ChainedImageCache(List<ImageCache> chain) {

		this.chain = chain;

	}
	//모든 ImageCach를 차례대로 실행 시켜 준다.
	@Override

	public void addBitmap(String key, Bitmap bitmap) {

		for (ImageCache cache : chain) {

			cache.addBitmap(key, bitmap);

		}

	}

//모든 파일캐쉬를 실행시켜준다.
	@Override

	public void addBitmap(String key, File bitmapFile) {

		for (ImageCache cache : chain) {

			cache.addBitmap(key, bitmapFile);

		}

	}
	
@Override

	public final Bitmap getBitmap(String key) {

		Bitmap bitmap = null;

		List<ImageCache> previousCaches = new ArrayList<ImageCache>();
		//chian을 따라 Bitmap이 존재 할 떄까지 탐색
		for (ImageCache cache : chain) {

			bitmap = cache.getBitmap(key);

			if (bitmap != null) {

				break;

			}
//bitmap이 발련됬으면 해당 캐시 이전에 위치한 캐시들에 Bitmap 정보 추가
			previousCaches.add(cache);

		}

		if (bitmap == null)

			return null;


		//이후 동인한 키로 요청이 오면 체인의 앞에서 발견되도록 한다.
		if (!previousCaches.isEmpty()) {

			for (ImageCache cache : previousCaches) {

				cache.addBitmap(key, bitmap);

			}

		}

		return bitmap;

	}
	....
```

#### ImageCachFactory 클래스

캐시 생성 기능 제공

```
....	
//메모리만 사용하는 ImageCache 생성
	public ImageCache createMemoryCache(String cacheName, int imageMaxCounts) {

		synchronized (cacheMap) {

			checkAleadyExists(cacheName);

			ImageCache cache = new MemoryImageCache(imageMaxCounts);

			cacheMap.put(cacheName, cache);

			return cache;

		}

	}
	
	
//	1차 메모리/2차 파일 기반의 2레벨 캐시를 생성한다.
public ImageCache createTwoLevelCache(String cacheName, int imageMaxCounts) {

		synchronized (cacheMap) {

			checkAleadyExists(cacheName);

			List<ImageCache> chain = new ArrayList<ImageCache>();

			chain.add(new MemoryImageCache(imageMaxCounts));

			chain.add(new FileImageCache(cacheName));

			ChainedImageCache cache = new ChainedImageCache(chain);

			cacheMap.put(cacheName, cache);

			return cache;

		}

	}	
....
```

####이미지 캐시 사용

```
-- onCreate 등 초기화 부분


// 2레벨 캐시(이미지 파일 캐시)를 사용하려면 동일 이름의 파일 캐시를 생성해 주어야 한다.

FileCacheFactory.getInstance().create(cacheName, cacheSize);



// 이미지 캐시 초기화

ImageCacheFactory.getInstance().createTwoLevelCache(cacheName, memoryImageMaxCounts);



-- 이미지 캐시 사용 부분

ImageCache imageCache = ImageCacheFactory.getInstance().getCache(cacheName);

Bitmap bitmap = imageCache.getBitmap(key);

if (bitmap != null) {

	imageView.set.....

}



-- 이미지 캐시 추가 부분

imageCache.putBitmap(key, someBitmap);
```

#### 용어정리

+ LruCache

  -LinkedHashMap을 사용하여 최근에 사용된 object의 strong reference를 보관하고 있다가 정해진 사이즈를 넘어가게 되면 가장 최근에 사용되지 않은 놈부터 쪽아내는 LRU 알고리즘을 사용하는 메모리 캐시이다.

  -메모리 캐쉬를 구현하는 데 유용하다.

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

